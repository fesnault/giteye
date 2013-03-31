package org.phoenix.giteye.json;

/**
 * Created with IntelliJ IDEA.
 * User: phoenix
 * Date: 3/30/13
 * Time: 8:31 AM
 * To change this template use File | Settings | File Templates.
 */
public class JsonHunkLine {
    private String line;
    private int newLineNumber;
    private int oldLineNumber;
    private HunkLineType type;

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public int getNewLineNumber() {
        return newLineNumber;
    }

    public void setNewLineNumber(int newLineNumber) {
        this.newLineNumber = newLineNumber;
    }

    public int getOldLineNumber() {
        return oldLineNumber;
    }

    public void setOldLineNumber(int oldLineNumber) {
        this.oldLineNumber = oldLineNumber;
    }

    public void setType(HunkLineType type) {
        this.type = type;
    }

    public HunkLineType getType() {
        return this.type;
    }
}
