package org.phoenix.giteye.core.git.services.impl;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.LogCommand;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.phoenix.giteye.core.beans.CommitBean;
import org.phoenix.giteye.core.beans.RepositoryBean;
import org.phoenix.giteye.core.beans.RepositoryConfig;
import org.phoenix.giteye.core.constants.GitConstants;
import org.phoenix.giteye.core.dao.GitRepositoryDAO;
import org.phoenix.giteye.core.exceptions.NoSuchRepositoryException;
import org.phoenix.giteye.core.exceptions.RepositoryPersistenceException;
import org.phoenix.giteye.core.git.services.RepositoryService;
import org.phoenix.giteye.core.hibernate.GitRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
        gitRepo.setType(repository.getType());
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
        repository.setType(repo.getType());
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
            repository.setType(repo.getType());
            result.add(repository);
        }
        return result;
    }
}
