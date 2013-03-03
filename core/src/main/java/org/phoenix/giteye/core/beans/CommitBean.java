package org.phoenix.giteye.core.beans;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: phoenix
 * Date: 2/27/13
 * Time: 11:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class CommitBean {
    private String id;
    private String committerName;
    private String message;
    private List<CommitBean> parents = new ArrayList<CommitBean>();
    private Date date;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCommitterName() {
        return committerName;
    }

    public void setCommitterName(String committerName) {
        this.committerName = committerName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void addParent(CommitBean parent) {
        parents.add(parent);
    }

    public List<CommitBean> getParents() {
        return parents;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
