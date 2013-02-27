package org.phoenix.giteye.core.hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * User: phoenix
 * Date: 1/13/13
 * Time: 1:07 PM
 */
@Entity
@Table(name="REPOSITORIES")
public class GitRepository extends HibernateEntity {
    private static final long serialVersionUID = 8963768781855107420L;
    private String path;

    @Column(nullable = false)
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
