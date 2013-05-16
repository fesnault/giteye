package org.phoenix.giteye.core.git.services.impl;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.LogCommand;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.revplot.PlotCommit;
import org.eclipse.jgit.revplot.PlotCommitList;
import org.eclipse.jgit.revplot.PlotLane;
import org.eclipse.jgit.revplot.PlotWalk;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.neo4j.graphdb.GraphDatabaseService;
import org.phoenix.giteye.core.beans.CommitBean;
import org.phoenix.giteye.core.beans.RepositoryBean;
import org.phoenix.giteye.core.beans.RepositoryConfig;
import org.phoenix.giteye.core.constants.GitConstants;
import org.phoenix.giteye.core.dao.GitRepositoryDAO;
import org.phoenix.giteye.core.exceptions.NoSuchRepositoryException;
import org.phoenix.giteye.core.exceptions.RepositoryPersistenceException;
import org.phoenix.giteye.core.git.services.RepositoryService;
import org.phoenix.giteye.core.git.services.dao.GitDAO;
import org.phoenix.giteye.core.hibernate.GitRepository;
import org.phoenix.giteye.core.neo4j.manager.GraphDatabaseManager;
import org.phoenix.giteye.json.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Repository Service implementation.
 * @author phoenix
 */
@Service
public class RepositoryServiceImpl implements RepositoryService {
    private final static Logger logger = LoggerFactory.getLogger(RepositoryServiceImpl.class);
    public static final String GIT_METAINF_SUFFIX = "/.git";

    @Autowired
    private GitRepositoryDAO repositoryDAO;
    @Autowired
    private GraphDatabaseManager graphDatabaseManager;
    @Autowired
    private GitDAO gitDAO;

    @Override
    public RepositoryBean getRepositoryInformation(String repositoryPath) {
        logger.info("Repository service was called for path : "+repositoryPath);
        RepositoryBean repo = new RepositoryBean();
        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        Repository repository = null;
        if (!repositoryPath.endsWith("/")) {
            repositoryPath+="/";
        }
        try {
            repository = builder.setGitDir(new File(repositoryPath + GitConstants.GIT_METAINF_SUFFIX))
              .readEnvironment() // scan environment GIT_* variables
              .findGitDir() // scan up the file system tree
              .build();
            repo.setPath(repository.getDirectory().getAbsolutePath());

            ObjectId headId = repository.resolve(Constants.HEAD);
            repo.setHead(headId.name());
            repo.setShortHead(headId.abbreviate(7).name());
            repo.setBranch(repository.getBranch());
            repository.close();
            logger.info("repo loaded :  " + repository);
        } catch (IOException e) {
            logger.error("Error while accessing repository.", e);
        } catch (Exception e) {
            logger.error("Error while resolving HEAD", e);
        }
        return repo;
    }

    @Override
    public void saveRepository(RepositoryConfig repository) throws RepositoryPersistenceException {
        GitRepository gitRepo = new GitRepository();
        gitRepo.setLocation(repository.getLocation());
        gitRepo.setName(repository.getName());
        try {
            repositoryDAO.saveOrUpdate(gitRepo);
        } catch (Exception e) {
            throw new RepositoryPersistenceException("Unable to persist repository.", e);
        }
    }

    @Override
    public RepositoryConfig getRepository(String name) throws NoSuchRepositoryException {
        GitRepository repo = repositoryDAO.findByUniqueKey("name", name);
        if (repo == null) {
            throw new NoSuchRepositoryException(name);
        }
        RepositoryConfig repository = new RepositoryConfig();
        repository.setName(repo.getName());
        repository.setLocation(repo.getLocation());
        return repository;
    }

    @Override
    public List<RepositoryConfig> getAllRepositories() {
        List<GitRepository> repositories = repositoryDAO.findAll();
        if (CollectionUtils.isEmpty(repositories)) {
            return Collections.<RepositoryConfig>emptyList();
        }
        List<RepositoryConfig> result = new ArrayList<RepositoryConfig>();
        for (GitRepository repo : repositories) {
            RepositoryConfig repository = new RepositoryConfig();
            repository.setName(repo.getName());
            repository.setLocation(repo.getLocation());
            result.add(repository);
        }
        return result;
    }

    private void addRepositoryRefs(JsonRepository jrep, Repository repo, String currentBranch) throws IOException{
        Map<String, Ref> refs = gitDAO.getAllRefs(repo);
        for (Map.Entry<String, Ref> refEntry : refs.entrySet()) {
            String refName = refEntry.getKey();
            Ref ref = refEntry.getValue();
            String name = refName;
            if (name.contains("/") && !name.contains("remotes") && !name.contains("tags")) {
                name = name.substring(name.lastIndexOf("/")+1);
            } else if (name.contains("tags")) {
                name = name.substring(name.lastIndexOf("/")+1);
                JsonTag tag = new JsonTag();
                tag.setName(name);
                tag.setTarget(ref.getTarget().getObjectId().name());
                tag.setRef(ref.getObjectId().name());
                jrep.addTag(tag);
                continue;
            } else if (name.contains("/")) {
                name = name.replace("refs/", "");
                name = name.replace("remotes/", "");
            }
            JsonBranch branch = new JsonBranch(name);
            branch.setRef(ref.getName());
            branch.setTarget(ref.getObjectId().name());
            if (currentBranch.equals(name)) {
                branch.setCurrent(true);
            }
            if (ref instanceof SymbolicRef) {
                branch.setSymbolic(true);
            }
            jrep.addBranch(branch);
        }

    }

    @Override
    public void createRepository(RepositoryConfig repository) {
        RepositoryBean bean = getRepositoryInformation(repository.getLocation());
        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        Repository repo = null;
        int position = 0;
        String repositoryName = repository.getName();
        GraphDatabaseService graphDb = graphDatabaseManager.getDatabase(repositoryName);
        try {
            repo = gitDAO.getRepository(bean.getPath());

            JsonRepository jrep = new JsonRepository(repositoryName);

            // Retrieve refs
            addRepositoryRefs(jrep, repo, bean.getBranch());
            // Retrieve objects
            PlotWalk revWalk = new PlotWalk(repo);
            // First create a list of heads targets for all non symbolic refs
            List<RevCommit> heads = new ArrayList<RevCommit>();
            for (JsonBranch branch : jrep.getBranches()) {
                if (branch.isSymbolic()) {
                    continue;
                }
                String headId = branch.getTarget();
                RevCommit head = revWalk.lookupCommit(repo.resolve(headId));
                if (head == null) {
                    continue;
                }
                heads.add(head);
            }
            revWalk.markStart(heads);
            PlotCommitList<PlotLane> commits = new PlotCommitList<PlotLane>();
            commits.source(revWalk);
            commits.fillTo(Integer.MAX_VALUE);
            Iterator<PlotCommit<PlotLane>> iterator = commits.iterator();
            while (iterator.hasNext()) {
                PlotCommit revc = iterator.next();
                JsonCommit commit = new JsonCommit(revc.getId().name());
                commit.setShortMessage(revc.getShortMessage());
                commit.setMessage(revc.getFullMessage());
                commit.setDate(new Date(revc.getCommitTime() * 1000L));
                commit.setAuthorName(revc.getAuthorIdent().getName());
                commit.setAuthorEmail(revc.getAuthorIdent().getEmailAddress());
                commit.setCommitterName(revc.getCommitterIdent().getName());
                commit.setCommitterEmail(revc.getCommitterIdent().getEmailAddress());
                commit.setPosition(position);
                if (revc.getLane() == null) {
                    commit.setLane(0);
                } else {
                    commit.setLane(revc.getLane().getPosition());
                }
                position++;
                jrep.addCommit(commit);
                graphDatabaseManager.addCommit(graphDb, commit);
                if (revc.getParents() == null || revc.getParents().length == 0) {
                    continue;
                }
                for (RevCommit parent : revc.getParents()) {
                    commit.addParent(parent.getId().name());
                }
            }
            revWalk.release();
            // set children
            for (JsonCommit commit : jrep.getCommits()) {
                graphDatabaseManager.setParents(graphDb, commit.getId(), commit.getParents());
            }
            for (JsonBranch branch : jrep.getBranches()) {
                graphDatabaseManager.addBranch(graphDb, branch);
            }
            if (jrep.getTags() != null) {
                for (JsonTag tag : jrep.getTags()) {
                    graphDatabaseManager.addTag(graphDb, tag);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            graphDatabaseManager.shutdown(graphDb);
        }
    }
}
