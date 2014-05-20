package org.phoenix.giteye.core.graph;

import org.phoenix.giteye.core.beans.json.JsonCommit;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

/**
 * This bean holds the state of a log graph at the end of a page processing.
 * It keeps all tails and heads (hidden or not), active and blocked lanes, allowing to process previous or next page.
 * @author phoenix
 */
public class GraphState implements Serializable {
    private List<GraphHead> heads = new ArrayList<GraphHead>();
    private List<GraphTail> tails = new ArrayList<GraphTail>();
    private int activeLanes;
    private List<Integer> blockedLanes = new ArrayList<Integer>();

    public void addHead(JsonCommit commit) {
        heads.add(new GraphHead(commit.getLane(), commit.getId(), commit.isExtra()));
    }

    public void addTail(JsonCommit commit) {
        tails.add(new GraphTail(commit.getLane(), commit.getId(), commit.isExtra()));
    }

    public List<GraphHead> getHeads() {
        return heads;
    }

    public List<GraphTail> getTails() {
        return tails;
    }

    public int getActiveLanes() {
        return activeLanes;
    }

    public void setActiveLanes(int activeLanes) {
        this.activeLanes = activeLanes;
    }

    public List<Integer> getBlockedLanes() {
        return blockedLanes;
    }

    public void setBlockedLanes(BitSet lanesBitSet) {
        List<Integer> blockedLanes = new ArrayList<Integer>();
        for (int i=0; i<lanesBitSet.size(); i++) {
            if (lanesBitSet.get(i)) {
                blockedLanes.add(i);
            }
        }
        this.blockedLanes = blockedLanes;
    }
}
