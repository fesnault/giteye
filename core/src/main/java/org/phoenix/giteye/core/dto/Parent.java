package org.phoenix.giteye.core.dto;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: phoenix
 * Date: 5/31/13
 * Time: 10:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class Parent implements Serializable {
    private String id;
    private int position;
    private int lane;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getLane() {
        return lane;
    }

    public void setLane(int lane) {
        this.lane = lane;
    }
}
