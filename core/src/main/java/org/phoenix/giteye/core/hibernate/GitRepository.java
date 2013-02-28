package org.phoenix.giteye.core.hibernate;

import org.phoenix.giteye.core.RepositoryType;

import javax.persistence.*;

/**
 * User: phoenix
 * Date: 1/13/13
 * Time: 1:07 PM
 */
@Entity
@Table(name="REPOSITORIES")
public class GitRepository extends HibernateEntity {
    private static final long serialVersionUID = 8963768781855107420L;
    private String location;
    private RepositoryType type;
    private String name;

    @Column(nullable = false)
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    public RepositoryType getType() {
        return type;
    }

    public void setType(RepositoryType type) {
        this.type = type;
    }

    @Column(nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
