package org.phoenix.giteye.core.dao;

import org.phoenix.giteye.core.hibernate.GitRepository;

import java.util.List;

/**
 * Interface of the Repository DAO.
 * User: phoenix
 * Date: 14/01/13
 * Time: 11:01
 */
public interface GitRepositoryDAO extends PersistentDAO<String, GitRepository> {
    /**
     * Find a repository from its path
     * @param path the path parameter
     * @return the list of matching repositories.
     */
    List<GitRepository> findByPath(String path);
}
