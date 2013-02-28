package org.phoenix.giteye.core.beans;

/**
 * Created with IntelliJ IDEA.
 * User: phoenix
 * Date: 28/02/13
 * Time: 18:32
 * To change this template use File | Settings | File Templates.
 */
public class BranchBean {
    private String name;
    private boolean remote;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isRemote() {
        return remote;
    }

    public void setRemote(boolean remote) {
        this.remote = remote;
    }
}
