package org.phoenix.giteye.core.beans;

import org.phoenix.giteye.core.RepositoryType;

/**
 * A repository configuration bean.
 * @author phoenix
 */
public class RepositoryConfig {
    private String location;
    private String name;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
