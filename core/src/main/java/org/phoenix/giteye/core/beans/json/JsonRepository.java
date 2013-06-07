package org.phoenix.giteye.core.beans.json;

import org.phoenix.giteye.core.dto.Commit;
import org.phoenix.giteye.core.graph.GraphState;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: phoenix
 * Date: 3/2/13
 * Time: 2:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class JsonRepository {
    private String name;
    private List<JsonBranch> branches;
    private Collection<Commit> commits;
    private List<JsonTag> tags;
    private int commitCount = 0;
    private int currentPage = 0;
    private int maxPage = 0;
    private int maxLane = 0;
    private GraphState state;

    public JsonRepository(String name) {
        this.name = name;
    }

    public GraphState getState() {
        return state;
    }

    public void setState(GraphState state) {
        this.state = state;
    }

/*    public void tidy() {
        commitCount = 0;
        List<Commit> tempCommits = new ArrayList<Commit>();
        for (Commit commit : this.commits) {
            if (!commit.isDisposable()) {
                tempCommits.add(commit);
                commitCount++;
            }
        }
        this.commits = tempCommits;
        tempCommits = null;
    }*/

    public void removeCommit(Commit commit) {
        commits.remove(commit);
    }

    public void addTag(JsonTag tag) {
        if (tags == null) {
            tags = new ArrayList<JsonTag>();
        }
        tags.add(tag);
    }

    public void addBranch(JsonBranch branch) {
        if (branches == null) {
            branches = new ArrayList<JsonBranch>();
        }
        branches.add(branch);
    }

    public List<JsonTag> getTags() {
        return tags;
    }

    public Collection<Commit> getCommits() {
        return commits == null ? Collections.<Commit>emptyList() : commits;
    }

    public void setCommits(Collection<Commit> commits) {
        this.commits = commits;
    }

    /*public void addCommit(Commit commit) {
        if (commits == null) {
            commits = new ArrayList<Commit>();
        }
        commits.add(0, commit);
        commitCount++;
        if (commit.getLane() > maxLane) {
            maxLane = commit.getLane();
        }
    }*/

    public List<JsonBranch> getBranches() {
        return branches == null ? Collections.<JsonBranch>emptyList() : branches;
    }

    public Commit getCommit(String id) {
        if (commits == null) {
            return null;
        }
        for (Commit commit : commits) {
            if (commit.getId().equals(id)) {
                return commit;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public int getCommitCount() {
        return commitCount;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getMaxPage() {
        return maxPage;
    }

    public void setMaxPage(int maxPage) {
        this.maxPage = maxPage;
    }

    public int getMaxLane() {
        return maxLane;
    }
}
