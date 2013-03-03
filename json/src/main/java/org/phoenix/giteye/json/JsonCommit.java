package org.phoenix.giteye.json;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: phoenix
 * Date: 3/2/13
 * Time: 3:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class JsonCommit {
    private String id;
    private String authorName;
    private String authorEmail;
    private String committerName;
    private String committerEmail;
    private Date date;
    private String message;
    private String shortMessage;
    private List<JsonCommitParent> parents;
    private int lane;

    public JsonCommit(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getShortMessage() {
        return shortMessage;
    }

    public void setShortMessage(String shortMessage) {
        this.shortMessage = shortMessage;
    }

    public List<JsonCommitParent> getParents() {
        return parents;
    }

    public void addParent(JsonCommitParent parent) {
        if (parents == null) {
            parents = new ArrayList<JsonCommitParent>();
        }
        parents.add(parent);
    }

    public int getLane() {
        return lane;
    }

    public void setLane(int lane) {
        this.lane = lane;
    }
}
