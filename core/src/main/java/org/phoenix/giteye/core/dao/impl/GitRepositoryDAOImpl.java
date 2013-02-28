package org.phoenix.giteye.core.dao.impl;


import org.phoenix.giteye.core.dao.GitRepositoryDAO;
import org.phoenix.giteye.core.hibernate.GitRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Implementation of the GitRepository DAO interface.
 * User: phoenix
 * Date: 14/01/13
 * Time: 11:10
 */
@Repository
public class GitRepositoryDAOImpl extends AbstractDAOImpl<String, GitRepository> implements GitRepositoryDAO {
    /**
     * {@inheritDoc}
     */
    public List<GitRepository> findByPath(String path) {
        return getHibernateTemplate().find("from GitRepository where path = ?", path);
    }
}
