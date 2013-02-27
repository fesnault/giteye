package org.phoenix.giteye.core.git.services.impl;

import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.phoenix.giteye.core.git.services.RepositoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

/**
 * Repository Service implementation.
 * @author phoenix
 */
@Service
public class RepositoryServiceImpl implements RepositoryService {
    private final static Logger logger = LoggerFactory.getLogger(RepositoryServiceImpl.class);

    public void getRepositoryInformation(String repositoryPath) {
        logger.info("Repository service was called for path : "+repositoryPath);
        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        Repository repository = null;
        try {
            repository = builder.setGitDir(new File(repositoryPath))
              .readEnvironment() // scan environment GIT_* variables
              .findGitDir() // scan up the file system tree
              .build();
            logger.info("repo loaded :  "+repository);
        } catch (IOException e) {
            logger.error("Error while accessing repository.", e);
        }
    }
}
