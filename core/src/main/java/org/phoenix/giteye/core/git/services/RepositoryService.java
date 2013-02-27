package org.phoenix.giteye.core.git.services;


import org.phoenix.giteye.core.beans.RepositoryBean;

import java.io.IOException;

/**
 * Git Repository service.
 * @author phoenix
 */
public interface RepositoryService {
    RepositoryBean getRepositoryInformation(String repositoryPath);
}
