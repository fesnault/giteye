package org.phoenix.giteye.core.graph;

import java.io.Serializable;

/**
 * Represents a graph head commit.
 * This class gives the head id, its lane, and a boolean indicating if this head is an extra commit (meaning its not visible in the graph).
 * @author phoenix
 */
public class GraphHead implements Serializable {
    private int lane;
    private String id;
    private boolean extra;

    public GraphHead(int lane, String id, boolean extra) {
        this.lane = lane;
        this.id = id;
        this.extra = extra;
    }

    public GraphHead(int lane, String id) {
        this(lane, id, false);
    }
}
