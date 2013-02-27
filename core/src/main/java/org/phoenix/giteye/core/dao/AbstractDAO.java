package org.phoenix.giteye.core.dao;

import org.springframework.jdbc.core.JdbcTemplate;

import java.io.Serializable;

/**
 * Interface of the Abstract DAO, extended by all DAOs.
 * User: phoenix
 * Date: 14/01/13
 * Time: 11:46
 */
public interface AbstractDAO <K extends Serializable, T extends Serializable> extends PersistentDAO<K, T> {
    void setJdbcTemplate(JdbcTemplate jdbcTemplate);
    JdbcTemplate getJdbcTemplate();
}
