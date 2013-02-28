package org.phoenix.giteye.core.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: phoenix
 * Date: 2/28/13
 * Time: 10:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class LogHolder {
    private List<CommitBean> commits;

    public void add(CommitBean commit) {
        if (commits == null) {
            commits = new ArrayList<CommitBean>();
        }
        commits.add(commit);
    }

    public List<CommitBean> getCommits() {
        return commits;
    }

    public void setCommits(List<CommitBean> commits) {
        this.commits = commits;
    }
}
