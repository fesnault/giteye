package org.phoenix.giteye.core.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: phoenix
 * Date: 5/31/13
 * Time: 10:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class Commit implements Serializable {
    private String id;
    private String date;
    private String author;
    private String email;
    private String message;
    private int lane;
    private int position;
    private List<Parent> parents;
    private int parentCount = 0;
    private int childCount = 0;
    private boolean extra = false;

    public List<Parent> getParents() {
        return parents;
    }

    public void addParent(Parent parent) {
        if (parents == null) {
            parents = new ArrayList<Parent>();
        }
        parents.add(parent);
        parentCount++;
    }

    public void addChild() {
        childCount++;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
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

    public int getParentCount() {
        return parentCount;
    }

    public void setParentCount(int parentCount) {
        this.parentCount = parentCount;
    }

    public int getChildCount() {
        return childCount;
    }

    public void setChildCount(int childCount) {
        this.childCount = childCount;
    }
}
