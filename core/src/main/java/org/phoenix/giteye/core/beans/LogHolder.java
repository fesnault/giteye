package org.phoenix.giteye.core.beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: phoenix
 * Date: 2/28/13
 * Time: 10:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class LogHolder {
    private Map<String, CommitBean> commitsMap;
    private List<CommitBean> commits;

    public void add(CommitBean commit) {
        if (commits == null) {
            commits = new ArrayList<CommitBean>();
            commitsMap = new HashMap<String, CommitBean>();
        }
        commits.add(commit);
        commitsMap.put(commit.getId(), commit);
    }

    public List<CommitBean> getCommits() {
        return commits;
    }

    public void setCommits(List<CommitBean> commits) {
        this.commits = commits;
    }

    public CommitBean getCommit(String id) {
        return commitsMap.get(id);
    }
}
