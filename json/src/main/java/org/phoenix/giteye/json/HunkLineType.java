package org.phoenix.giteye.json;

/**
 * Created with IntelliJ IDEA.
 * User: phoenix
 * Date: 3/30/13
 * Time: 11:28 PM
 * To change this template use File | Settings | File Templates.
 */
public enum HunkLineType {
    COMMON(0),
    OLD(-1),
    NEW(1);
    private int value;

    private HunkLineType(int type) {
        this.value = type;
    }

    public int value() {
        return this.value;
    }
}
