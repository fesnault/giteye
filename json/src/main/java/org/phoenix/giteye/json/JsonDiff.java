package org.phoenix.giteye.json;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: phoenix
 * Date: 3/23/13
 * Time: 12:24 AM
 * To change this template use File | Settings | File Templates.
 */
public class JsonDiff {

    private String changeName;
    private int newMode;
    private String newPath;
    private String diff;

    public String getChangeName() {
        return changeName;
    }

    public void setChangeName(String changeName) {
        this.changeName = changeName;
    }

    public int getNewMode() {
        return newMode;
    }

    public void setNewMode(int newMode) {
        this.newMode = newMode;
    }

    public String getNewPath() {
        return newPath;
    }

    public void setNewPath(String newPath) {
        this.newPath = newPath;
    }

    public String getDiff() {
        return diff;
    }

    public void setDiff(String diff) {
        this.diff = diff;
    }
}
