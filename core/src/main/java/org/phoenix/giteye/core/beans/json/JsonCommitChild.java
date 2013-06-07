package org.phoenix.giteye.core.beans.json;

/**
 * Created with IntelliJ IDEA.
 * User: phoenix
 * Date: 3/2/13
 * Time: 3:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class JsonCommitChild {
    private String id;
    private int lane;
    private int position;
    private int parentCount;

    public JsonCommitChild(String id) {
        this.id = id;
        this.lane = -1;
        this.parentCount = 0;
    }

    public String getId() {
        return id;
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

    public void addParent() {
        this.parentCount++;
    }

    public int getParentCount() {
        return parentCount;
    }
}
