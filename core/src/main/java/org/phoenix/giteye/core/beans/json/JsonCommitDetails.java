package org.phoenix.giteye.core.beans.json;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: phoenix
 * Date: 3/23/13
 * Time: 12:28 AM
 * To change this template use File | Settings | File Templates.
 */
public class JsonCommitDetails {
    private String id;
    private String message;
    private String commitDate;
    private String authorName;
    private String authorEmail;
    private String authorDate;
    private String committerName;
    private String committerEmail;
    private List<JsonDiffs> differences;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCommitDate() {
        return commitDate;
    }

    public void setCommitDate(String commitDate) {
        this.commitDate = commitDate;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorEmail() {
        return authorEmail;
    }

    public void setAuthorEmail(String authorEmail) {
        this.authorEmail = authorEmail;
    }

    public List<JsonDiffs> getDifferences() {
        return differences;
    }

    public void addDifferences(JsonDiffs differenceSet) {
        if (this.differences == null) {
            this.differences = new ArrayList<JsonDiffs>();
        }
        this.differences.add(differenceSet);
    }

    public String getAuthorDate() {
        return authorDate;
    }

    public void setAuthorDate(String authorDate) {
        this.authorDate = authorDate;
    }

    public String getCommitterName() {
        return committerName;
    }

    public void setCommitterName(String committerName) {
        this.committerName = committerName;
    }

    public String getCommitterEmail() {
        return committerEmail;
    }

    public void setCommitterEmail(String committerEmail) {
        this.committerEmail = committerEmail;
    }
}
