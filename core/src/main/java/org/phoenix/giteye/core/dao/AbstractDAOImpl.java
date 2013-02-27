package org.phoenix.giteye.core.dao;

import org.phoenix.giteye.core.dao.impl.PersistentDAOImpl;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.Serializable;

/**
 * Implementation of the Abstract DAO interface.
 * User: phoenix
 * Date: 14/01/13
 * Time: 11:50
 */
public class AbstractDAOImpl<K extends Serializable, T extends Serializable> extends PersistentDAOImpl<K, T> implements AbstractDAO<K, T> {
    protected JdbcTemplate jdbcTemplate;

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public JdbcTemplate getJdbcTemplate() {
        return this.jdbcTemplate;
    }
}
