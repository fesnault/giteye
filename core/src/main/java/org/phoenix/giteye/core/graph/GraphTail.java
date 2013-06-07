package org.phoenix.giteye.core.graph;

import java.io.Serializable;

/**
 * Represents a graph tail commit.
 * This class gives the tail id, its lane, and a boolean indicating if this tail is an extra commit (meaning its not visible in the graph).
 * @author phoenix
 */
public class GraphTail implements Serializable {
    private int lane;
    private String id;
    private boolean extra;

    public GraphTail(int lane, String id, boolean extra) {
        this.lane = lane;
        this.id = id;
        this.extra = extra;
    }

    public GraphTail(int lane, String id) {
        this(lane, id, false);
    }
}