package org.phoenix.giteye.core.git.services;


import org.phoenix.giteye.core.beans.RepositoryBean;
import org.phoenix.giteye.core.beans.RepositoryConfig;
import org.phoenix.giteye.core.exceptions.NoSuchRepositoryException;
import org.phoenix.giteye.core.exceptions.RepositoryPersistenceException;
import org.springframework.dao.DataAccessException;

import java.io.IOException;
import java.util.List;

/**
 * Git Repository service.
 * @author phoenix
 */
public interface RepositoryService {
    RepositoryBean getRepositoryInformation(String repositoryPath);

    void saveRepository(RepositoryConfig repository) throws RepositoryPersistenceException;

    void createRepository(RepositoryConfig repository);

    RepositoryConfig getRepository(String name) throws NoSuchRepositoryException;

    List<RepositoryConfig> getAllRepositories();
}
