package org.phoenix.giteye.core.beans;

import org.eclipse.jgit.revwalk.RevCommit;

import java.util.List;

/**
 * Git repository java bean
 * @author phoenix
 */
public class RepositoryBean {
    private String head;
    private String shortHead;
    private String path;
    private String branch;
    private List<CommitBean> commits;

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getShortHead() {
        return shortHead;
    }

    public void setShortHead(String shortHead) {
        this.shortHead = shortHead;
    }

    public List<CommitBean> getCommits() {
        return commits;
    }

    public void setCommits(List<CommitBean> commits) {
        this.commits = commits;
    }

    public String getDisplayName() {
        return path+" [ "+branch+"]";
    }
}
