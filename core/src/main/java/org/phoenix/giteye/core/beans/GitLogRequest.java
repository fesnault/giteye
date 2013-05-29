package org.phoenix.giteye.core.beans;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: phoenix
 * Date: 5/22/13
 * Time: 10:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class GitLogRequest {
    private int pageSize = 100;
    private List<String> heads;

    public void setHeads(List<String> heads) {
        this.heads = heads;
    }

    public List<String> getHeads() {
        return heads;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
