package org.phoenix.giteye.core.git.services.impl;

import com.google.common.collect.Collections2;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.RawTextComparator;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.revplot.PlotCommit;
import org.eclipse.jgit.revplot.PlotCommitList;
import org.eclipse.jgit.revplot.PlotLane;
import org.eclipse.jgit.revplot.PlotWalk;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevSort;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.phoenix.giteye.core.beans.BranchBean;
import org.phoenix.giteye.core.beans.GitLogRequest;
import org.phoenix.giteye.core.beans.RepositoryBean;
import org.phoenix.giteye.core.beans.json.*;
import org.phoenix.giteye.core.dto.Commit;
import org.phoenix.giteye.core.dto.Parent;
import org.phoenix.giteye.core.exceptions.NotInitializedRepositoryException;
import org.phoenix.giteye.core.git.services.GitService;
import org.phoenix.giteye.core.git.dao.GitDAO;
import org.phoenix.giteye.core.utils.CommitListPredicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.misc.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: phoenix
 * Date: 2/27/13
 * Time: 11:32 PM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class GitServiceimpl implements GitService {
    private final static Logger logger = LoggerFactory.getLogger(GitServiceimpl.class);
    private final static int DEFAULT_MAX_COMMITS = 300;
    public static final int CONTEXT_LINES = 3;
    private final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

    @Autowired
    private GitDAO gitDAO;

    @Override
    public List<BranchBean> getBranches(RepositoryBean repository) {

        List<BranchBean> branches= new ArrayList<BranchBean>();
        try {

            Repository repo = gitDAO.getRepository(repository.getPath());
            Iterable<Ref> iterable = gitDAO.getBranches(repo);

            for (Ref ref : iterable) {
                BranchBean branch = new BranchBean();
                String name = ref.getName();
                if (name.startsWith("refs/heads/")) {
                    name = name.replace("refs/heads/","");
                    branch.setName(name);
                    branch.setRemote(false);
                } else {
                    name = name.replace("refs/","");
                    branch.setName(name);
                    branch.setRemote(true);
                }
                branches.add(branch);
            }
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (NoHeadException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (GitAPIException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return branches;
    }


/*    public void getCommitDifferences(RepositoryBean repository, String commitId) {
        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        Repository repo = null;
        DiffFormatter df = null;
        RevWalk revWalk = null;


        JsonCommitDetails details = new JsonCommitDetails();
        try {
            repo = builder.setGitDir(new File(repository.getPath()))
                    .readEnvironment() // scan environment GIT_* variables
                    .findGitDir() // scan up the file system tree
                    .build();

            revWalk = new RevWalk(repo);
        } catch (IOException ioe) {

        } finally {
            df.release();
            revWalk.release();
        }
    }*/

    @Override
    public JsonDiff getCommitElementDifferences(RepositoryBean repository, String commitId, String parentId, String oldId, String newId) throws NotInitializedRepositoryException {
        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        Repository repo = null;
        DiffFormatter df = null;
        RevWalk revWalk = null;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        JsonDiff jdiff = null;

        try {
            repo = builder.setGitDir(new File(repository.getPath()))
                    .readEnvironment() // scan environment GIT_* variables
                    .findGitDir() // scan up the file system tree
                    .build();

            revWalk = new RevWalk(repo);

            RevCommit commit = revWalk.parseCommit(repo.resolve(commitId));
            RevCommit parent = revWalk.parseCommit(repo.resolve(parentId));
            if (commit == null || parent == null) {
                return null;
            }

            df = new DiffFormatter(out);
            df.setRepository(repo);
            df.setDiffComparator(RawTextComparator.DEFAULT);
            df.setDetectRenames(true);
            df.setContext(CONTEXT_LINES);

            RevTree commitTree =  commit.getTree();
            RevTree parentTree = parent.getTree();

            List<DiffEntry> diffs = df.scan(parentTree, commitTree);
            for (DiffEntry diff : diffs) {
                if ( (!diff.getOldId().name().equals(oldId)) && (!diff.getNewId().name().equals(newId)) ) {
                   continue;
                } else {
                    df.format(diff);
                    jdiff = new JsonDiff();
                    jdiff.setChangeName(diff.getChangeType().name());
                    jdiff.setNewMode(diff.getNewMode().getBits());
                    jdiff.setNewPath(diff.getNewPath());
                    jdiff.setOldMode(diff.getOldMode().getBits());
                    jdiff.setOldPath(diff.getOldPath());

                    String lines = out.toString("UTF-8");
                    StringTokenizer tokenizer = new StringTokenizer(lines,"\n");
                    JsonDiffChunk chunk = null;
                    int oldLineNumber = 0;
                    int newLineNumber = 0;
                    while (tokenizer.hasMoreTokens()) {
                        String line = tokenizer.nextToken();
                        if (line.startsWith("@@")) {
                            if (chunk != null) {
                                jdiff.addChunk(chunk);
                            }
                            chunk = new JsonDiffChunk();
                            line = line.replace("@@","").trim();
                            StringTokenizer spaceTokenizer = new StringTokenizer(line, " ");
                            while (spaceTokenizer.hasMoreTokens()) {
                                String token = spaceTokenizer.nextToken();
                                if (token.startsWith("-")) {
                                    token = token.substring(1);
                                    // old file diff part
                                    String[] elements = token.trim().split(",");
                                    chunk.setOldLineStart(Integer.parseInt(elements[0]));
                                    chunk.setOldLineRange(Integer.parseInt(elements[1]));
                                    oldLineNumber = chunk.getOldLineStart()-1;
                                } else {
                                    // new file diff part
                                    token = token.substring(1);
                                    String[] elements = token.trim().split(",");
                                    chunk.setNewLineStart(Integer.parseInt(elements[0]));
                                    chunk.setNewLineRange(Integer.parseInt(elements[1]));
                                    newLineNumber = chunk.getNewLineStart()-1;
                                }
                            }
                        } else if (chunk != null) {
                            JsonChunkLine chunkLine = new JsonChunkLine();
                            if (line.startsWith("-")) {
                                oldLineNumber++;
                                chunkLine.setOldLineNumber(oldLineNumber);
                                chunkLine.setNewLineNumber(0);
                                if (line.length() == 1) {
                                    line = " ";
                                } else {
                                    line = " "+line.substring(1);
                                }
                                chunkLine.setType(ChunkLineType.OLD);
                            } else if (line.startsWith("+")) {
                                newLineNumber++;
                                chunkLine.setNewLineNumber(newLineNumber);
                                chunkLine.setOldLineNumber(0);
                                if (line.length() == 1) {
                                    line = " ";
                                } else {
                                    line = " "+line.substring(1);
                                }
                                chunkLine.setType(ChunkLineType.NEW);
                            } else {
                                newLineNumber++;
                                oldLineNumber++;
                                chunkLine.setOldLineNumber(oldLineNumber);
                                chunkLine.setNewLineNumber(newLineNumber);
                                chunkLine.setType(ChunkLineType.COMMON);
                            }
                            chunkLine.setLine(line);
                            chunk.addLine(chunkLine);
                        }
                    }
                    if (chunk != null) {
                        jdiff.addChunk(chunk);
                    }
                    out.reset();
                    break;
                }
            }

        } catch (IOException ioe) {

        } finally {
            try {
                out.close();
            } catch (IOException ioe2) {
                logger.error("Oups ! Error while closing output stream...", ioe2);
            }
            df.release();
            revWalk.release();
        }
        return jdiff;
    }

    @Override
    public JsonCommitDetails getCommitDetails(RepositoryBean repository, String commitId) {
        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        Repository repo = null;
        DiffFormatter df = null;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        RevWalk revWalk = null;


        JsonCommitDetails details = new JsonCommitDetails();
        try {
            repo = builder.setGitDir(new File(repository.getPath()))
                    .readEnvironment() // scan environment GIT_* variables
                    .findGitDir() // scan up the file system tree
                    .build();

            revWalk = new RevWalk(repo);

            RevCommit commit = revWalk.parseCommit(repo.resolve(commitId));
            RevCommit parent = null;
            if (commit == null) {
                return null;
            }
            if (commit.getParentCount() == 0) {
                return null;
            }

            df = new DiffFormatter(out);
            df.setRepository(repo);
            df.setDiffComparator(RawTextComparator.DEFAULT);
            df.setDetectRenames(true);
            df.setContext(CONTEXT_LINES);

            List<JsonDiff> differences = new ArrayList<JsonDiff>();
            for (int i=0; i<commit.getParentCount(); i++) {
                parent = revWalk.parseCommit(commit.getParent(i).getId());

                RevTree commitTree =  commit.getTree();
                RevTree parentTree = parent.getTree();

                JsonDiffs diffSet = new JsonDiffs();
                diffSet.setParentCommitId(parent.getId().name());


                details.setId(commit.getId().name());
                details.setMessage(commit.getFullMessage());
                details.setCommitDate(dateFormatter.format(commit.getCommitterIdent().getWhen()));
                details.setAuthorDate(dateFormatter.format(commit.getAuthorIdent().getWhen()));
                details.setAuthorName(commit.getAuthorIdent().getName());
                details.setAuthorEmail(commit.getAuthorIdent().getEmailAddress());
                details.setCommitterName(commit.getCommitterIdent().getName());
                details.setCommitterEmail(commit.getCommitterIdent().getEmailAddress());

                List<DiffEntry> diffs = df.scan(parent.getTree(), commit.getTree());
                for (DiffEntry diff : diffs) {
                    df.format(diff);
                    JsonDiffId jdiff = new JsonDiffId();
                    jdiff.setName(diff.getChangeType().name().equals(DiffEntry.ChangeType.DELETE.name()) ? diff.getOldPath() : diff.getNewPath());
                    jdiff.setChangeName(diff.getChangeType().name());
                    jdiff.setOldId(diff.getOldId().name());
                    jdiff.setNewId(diff.getNewId().name());
                    jdiff.setParentId(parent.getId().name());

                    diffSet.addDifference(jdiff);
                }
                details.addDifferences(diffSet);
            }
        } catch (IOException ioe) {

        } finally {
            try {
                out.close();
            } catch (IOException ioe2) {
                logger.error("Oups ! Error while closing output stream...", ioe2);
            }
            df.release();
            revWalk.release();
        }
        return details;
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
    public JsonRepository getLogAsJson(RepositoryBean repository, GitLogRequest logRequest) throws NotInitializedRepositoryException {
        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        Repository repo = null;

        List<String> headsIds = logRequest.getHeads();
        if (headsIds ==  null) headsIds = Collections.<String>emptyList();
        logger.info("Processing log graph...");
        try {
            repo = gitDAO.getRepository(repository.getPath());

            JsonRepository jrep = new JsonRepository(repository.getDisplayName());

            // Retrieve refs
            addRepositoryRefs(jrep, repo, repository.getBranch());

            PlotWalk revWalk = new PlotWalk(repo);
            List<RevCommit> heads = null;
            boolean includeHeads = true;
            if (headsIds.size() > 0) {
                heads = getHeads(headsIds, repo, revWalk);
                includeHeads = false;
            } else {
                heads = getHeads(jrep, revWalk, repo);
            }
            revWalk.markStart(heads);

            // Process log graph
            //LogGraphProcessorFactory.getProcessor(jrep).process(repo, revWalk, heads, logRequest.getPageSize());

            revWalk.release();
            return jrep;
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return null;
    }

    private List<RevCommit> getHeads(List<String> headsIds, Repository repository, PlotWalk revWalk) {
        List<RevCommit> heads = new ArrayList<RevCommit>();
        for (String headId : headsIds) {
            RevCommit head = null;
            try {
                ObjectId headObjectId = repository.resolve(headId);
                if (headObjectId == null) {
                    logger.error("Could not resolve head commit with sha1 : "+headId);
                    continue;
                }
                head = revWalk.lookupCommit(headObjectId);
                heads.add(head);
            } catch (IOException ioe) {
                logger.error("Could not resolve head commit with sha1 : "+headId);
            }
        }
        return heads;
    }

    private List<RevCommit> getHeads(JsonRepository jrep, PlotWalk revWalk, Repository repo) throws IOException {
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
        return heads;
    }


    @Override
    public JsonRepository getLogAsJson(RepositoryBean repository, int pageSize, int page) {
        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        int position = 0;
        int max = page*pageSize;
        int firstCommitPosition = (page-1)*pageSize;
        JsonRepository jrep = null;
        List<Commit> commits = new LinkedList<Commit>();
        PlotWalk walk = null;
        Repository repo = null;
        Map<String, Commit> commitsById = null;

        /*try {
            repo = gitDAO.getRepository(repository.getPath());
            jrep = new JsonRepository(repository.getDisplayName());
            // Retrieve refs
            addRepositoryRefs(jrep, repo, repository.getBranch());
            // Retrieve objects
            PlotWalk revWalk = new PlotWalk(repo);
            // First create a list of heads targets for all non symbolic refs
            List<RevCommit> heads = getHeads(jrep, revWalk, repo);
            revWalk.markStart(heads);
            commits = new PlotCommitList<PlotLane>();
            commits.source(revWalk);
            commits.fillTo(Integer.MAX_VALUE);
            revWalk.release();
        } catch (Exception e) {
            logger.error("error : "+e.getMessage(), e);
        }*/
        try {
            repo = gitDAO.getRepository(repository.getPath());
            jrep = new JsonRepository(repository.getDisplayName());
            jrep.setCurrentPage(page);
            addRepositoryRefs(jrep, repo, repository.getBranch());
            walk = new PlotWalk(repo);
            List<RevCommit> heads = getHeads(jrep, walk, repo);
            walk.sort(RevSort.COMMIT_TIME_DESC, true);
            walk.markStart(heads);

            PlotCommitList<PlotLane> pcl = new PlotCommitList<PlotLane>();
            pcl.source(walk);
            pcl.fillTo(Integer.MAX_VALUE);
            Collections.reverse(pcl);
            int nbCommits = pcl.size();
            jrep.setMaxPage(nbCommits / pageSize);
            position = nbCommits - 1;
            commitsById = new HashMap<String, Commit>(nbCommits);
            logger.info("Preparing data...");
            for (int i = 0; i < nbCommits; i++) {
                PlotCommit<PlotLane> pc = pcl.get(i);
                Commit commit = new Commit();

                commit.setAuthor(pc.getCommitterIdent().getName());
                commit.setDate(dateFormatter.format(pc.getCommitterIdent().getWhen()));
                commit.setId(pc.getId().getName());
                commit.setEmail(pc.getAuthorIdent().getEmailAddress());
                commit.setMessage(pc.getFullMessage());
                if (pc.getLane() != null) {
                    commit.setLane(pc.getLane().getPosition());
                } else {
                    commit.setLane(0);
                }
                commit.setPosition(position);
                position--;

                for (RevCommit parentRC : pc.getParents()) {
                    Commit parentCommit = commitsById.get(parentRC.getId().getName());
                    if (parentCommit == null) {
                        logger.error("Commit not found  : "+parentRC.getId().getName());
                    } else {
                        parentCommit.addChild();
                        Parent parent = new Parent();
                        parent.setId(parentCommit.getId());
                        parent.setPosition(parentCommit.getPosition());
                        parent.setLane(parentCommit.getLane());
                        commit.addParent(parent);
                    }
                }

                commitsById.put(commit.getId(), commit);
                commits.add(commit);
            }

        } catch (Exception e) {
                logger.error("Error : "+e.getMessage(), e);
        } finally {
            walk.dispose();
            repo.close();
        }
        logger.info("Reversing...");
        Collections.reverse(commits);
        logger.info("Filtering...");
        Collection<Commit> result = Collections2.filter(commits, new CommitListPredicate(commitsById, firstCommitPosition, max));
        logger.info("Returning jrep (found "+result.size()+" commits.)");
        jrep.setCommits(result);
        return jrep;
    }

    /*public JsonRepository gedtLogAsJson(RepositoryBean repository, int pageSize, int page) throws NotInitializedRepositoryException {
        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        Repository repo = null;
        int position = 0;
        int max = page*pageSize;
        int firstCommitPosition = (page-1)*pageSize;

        try {
            repo = gitDAO.getRepository(repository.getPath());

            JsonRepository jrep = new JsonRepository(repository.getDisplayName());

            // Retrieve refs
            addRepositoryRefs(jrep, repo, repository.getBranch());
            // Retrieve objects
            PlotWalk revWalk = new PlotWalk(repo);
            // First create a list of heads targets for all non symbolic refs
            List<RevCommit> heads = getHeads(jrep, revWalk, repo);
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
                if (jrep.getCommitCount() >= max || jrep.getCommitCount() < firstCommitPosition) {
                    commit.setExtra(true);
                    commit.setDisposable(true);
                }
                if (revc.getLane() == null) {
                    commit.setLane(0);
                } else {
                    commit.setLane(revc.getLane().getPosition());
                }
                position++;
                jrep.addCommit(commit);
                if (revc.getParents() == null || revc.getParents().length == 0) {
                    continue;
                }
                for (RevCommit parent : revc.getParents()) {
                    commit.addParent(parent.getId().name());
                }
            }
            // set children
            for (JsonCommit commit : jrep.getCommits()) {
                if (!CollectionUtils.isEmpty(commit.getParents())) {
                    for (String parent : commit.getParents()) {
                        JsonCommit parentCommit = jrep.getCommit(parent);
                        if (parentCommit.isDisposable() && (!commit.isExtra())) {
                            parentCommit.setDisposable(false);
                            logger.info("Keeping extra commit "+parentCommit.getId());
                        } else if (commit.isDisposable() && (!parentCommit.isExtra())) {
                            commit.setDisposable(false);
                        }
                        if (parentCommit == null) {
                            logger.error("Could not get JsonCommit with id "+parent);
                        } else {
                            JsonCommitChild child = new JsonCommitChild(commit.getId());
                            child.setLane(commit.getLane());
                            child.setPosition(commit.getPosition());
                            parentCommit.addChild(child);
                        }
                    }
                }
                commit.resetParents();
            }
            revWalk.release();
            int nbPages = (int) Math.ceil(jrep.getCommitCount() / pageSize);
            jrep.setCurrentPage(page);
            jrep.setMaxPage(nbPages);
            logger.info("Before cleaning : Json Repository contains " + jrep.getCommitCount() + " commits ("+nbPages+").");
            jrep.tidy();
            logger.info("After cleaning : Json Repository contains "+jrep.getCommitCount()+" commits.");
            return jrep;

        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return null;
    }*/
}
