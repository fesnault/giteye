package org.phoenix.giteye.json;

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
    private Date commitDate;
    private String authorName;
    private String authorEmail;
    private List<JsonDiff> differences;

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

    public Date getCommitDate() {
        return commitDate;
    }

    public void setCommitDate(Date commitDate) {
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

    public List<JsonDiff> getDifferences() {
        return differences;
    }

    public void setDifferences(List<JsonDiff> differences) {
        this.differences = differences;
    }
}
