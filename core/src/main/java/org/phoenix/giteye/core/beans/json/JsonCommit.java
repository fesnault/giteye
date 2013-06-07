package org.phoenix.giteye.core.beans.json;

import java.util.ArrayList;
import java.util.Collections;
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
    private List<JsonCommitChild> children;
    private List<String> parents;
    private int parentCount = 0;
    private int lane = -1;
    private int position;
    private boolean extra = false;
    private transient boolean disposable = false;

    public JsonCommit(String id) {
        this.id = id;
    }

    public boolean hasParents() {
        return parents == null;
    }

    public boolean hasChildren() {
        return children == null;
    }

    public int getParentCount() {
        return parentCount;
    }

    public int getChildCount() {
        return children == null ? 0 : children.size();
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

    public List<JsonCommitChild> getChildren() {
        if (children == null) {
            return Collections.<JsonCommitChild>emptyList();
        }
        return children;
    }

    public void addChild(JsonCommitChild child) {
        if (children == null) {
            children = new ArrayList<JsonCommitChild>();
        }
        children.add(child);
    }

    public void addParent(String parentId) {
        if (parents == null) {
            parents = new ArrayList<String>();
        }
        parents.add(parentId);
        parentCount++;
    }

    public List<String> getParents() {
        if (parents == null) {
            return Collections.<String>emptyList();
        }
        return parents;
    }

    public void resetParents() {
        if (parents != null) {
            this.parents.clear();
        }
    }

    public int getLane() {
        return lane;
    }

    public void setLane(int lane) {
        this.lane = lane;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean isExtra() {
        return extra;
    }

    public void setExtra(boolean extra) {
        this.extra = extra;
    }

    public boolean isDisposable() {
        return disposable;
    }

    public void setDisposable(boolean disposable) {
        this.disposable = disposable;
    }
}
