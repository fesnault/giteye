package org.phoenix.giteye.core.beans.json;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: phoenix
 * Date: 3/27/13
 * Time: 10:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class JsonDiffChunk {
    private List<JsonChunkLine> lines;
    private int oldLineStart;
    private int oldLineRange;
    private int newLineStart;
    private int newLineRange;

    public List<JsonChunkLine> getLines() {
        return lines;
    }

    public void addLine(JsonChunkLine line) {
        if (this.lines == null) {
            this.lines = new ArrayList<JsonChunkLine>();
        }
        this.lines.add(line);
    }

    public int getOldLineStart() {
        return oldLineStart;
    }

    public void setOldLineStart(int oldLineStart) {
        this.oldLineStart = oldLineStart;
    }

    public int getOldLineRange() {
        return oldLineRange;
    }

    public void setOldLineRange(int oldLineRange) {
        this.oldLineRange = oldLineRange;
    }

    public int getNewLineStart() {
        return newLineStart;
    }

    public void setNewLineStart(int newLineStart) {
        this.newLineStart = newLineStart;
    }

    public int getNewLineRange() {
        return newLineRange;
    }

    public void setNewLineRange(int newLineRange) {
        this.newLineRange = newLineRange;
    }
}
