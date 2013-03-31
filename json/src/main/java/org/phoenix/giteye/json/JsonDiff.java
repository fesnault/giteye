package org.phoenix.giteye.json;

import java.util.ArrayList;
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
    private int oldMode;
    private String oldPath;
    private List<JsonDiffHunk> hunks;

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


    public int getOldMode() {
        return oldMode;
    }

    public void setOldMode(int oldMode) {
        this.oldMode = oldMode;
    }

    public String getOldPath() {
        return oldPath;
    }

    public void setOldPath(String oldPath) {
        this.oldPath = oldPath;
    }

    public List<JsonDiffHunk> getHunks() {
        return hunks;
    }

    public void addHunk(JsonDiffHunk hunk) {
        if (this.hunks == null) {
            this.hunks = new ArrayList<JsonDiffHunk>();
        }
        this.hunks.add(hunk);
    }
}
