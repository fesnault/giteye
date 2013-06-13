package org.phoenix.giteye.core.beans.json;

/**
 * Created with IntelliJ IDEA.
 * User: phoenix
 * Date: 6/13/13
 * Time: 10:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class JsonDiffId {
    private String parentId;
    private String oldId;
    private String newId;
    private String changeName;
    private String name;

    public String getOldId() {
        return oldId;
    }

    public void setOldId(String oldId) {
        this.oldId = oldId;
    }

    public String getNewId() {
        return newId;
    }

    public void setNewId(String newId) {
        this.newId = newId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getChangeName() {
        return changeName;
    }

    public void setChangeName(String changeName) {
        this.changeName = changeName;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
}
