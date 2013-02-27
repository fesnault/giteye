package org.phoenix.giteye.core.dao;


import org.phoenix.giteye.core.hibernate.GitRepository;

import java.util.List;

/**
 * Implementation of the GitRepository DAO interface.
 * User: phoenix
 * Date: 14/01/13
 * Time: 11:10
 */
public class GitRepositoryDAOImpl extends AbstractDAOImpl<String, GitRepository> implements GitRepositoryDAO {
    /**
     * {@inheritDoc}
     */
    public List<GitRepository> findByPath(String path) {
        return getHibernateTemplate().find("from GitRepository where path = ?", path);
    }
}
