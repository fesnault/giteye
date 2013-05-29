package org.phoenix.giteye.core.graph.impl;

import org.eclipse.jgit.lib.AnyObjectId;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revplot.PlotCommit;
import org.eclipse.jgit.revplot.PlotWalk;
import org.eclipse.jgit.revwalk.RevCommit;
import org.phoenix.giteye.core.beans.GitLogRequest;
import org.phoenix.giteye.core.graph.LogGraphProcessor;
import org.phoenix.giteye.json.JsonCommit;
import org.phoenix.giteye.json.JsonCommitChild;
import org.phoenix.giteye.json.JsonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: phoenix
 * Date: 3/6/13
 * Time: 8:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class LogGraphProcessorImpl implements LogGraphProcessor{
    private final static Logger logger = LoggerFactory.getLogger(LogGraphProcessorImpl.class);
    private BitSet blockedLanes;
    private JsonRepository repository;
    private int activeLanes = 0;
    private Map<String, List<String>> childrenCount = new HashMap<String, List<String>>();
    private Map<Integer, JsonCommit> laneTails;
    private int position = 0;

    public LogGraphProcessorImpl(JsonRepository repository) {
        this.repository = repository;
        this.blockedLanes = new BitSet();
        this.laneTails = new HashMap<Integer, JsonCommit>();
    }

    @Override
    public void process(Repository gitRepository, PlotWalk walk, List<RevCommit> heads, int max) {
        try {
            PlotCommit commit = null;
            while ( (commit = (PlotCommit)walk.next()) != null) {
                addCommitToRepository(commit, position, false);
                position++;
                if (position == max) {
                    break;
                }
            }
            finishGraph(gitRepository, walk);

        } catch (Exception e) {
            logger.error("Exception occured during graph processing : "+e.getMessage(), e);
        }
    }

    private void addCommitToRepository(PlotCommit current, int position, boolean extra) {
        JsonCommit commit = new JsonCommit(current.getId().name());

        commit.setShortMessage(current.getShortMessage());
        commit.setMessage(current.getFullMessage());
        commit.setDate(new Date(current.getCommitTime() * 1000L));
        commit.setAuthorName(current.getAuthorIdent().getName());
        commit.setAuthorEmail(current.getAuthorIdent().getEmailAddress());
        commit.setCommitterName(current.getCommitterIdent().getName());
        commit.setCommitterEmail(current.getCommitterIdent().getEmailAddress());
        commit.setPosition(position);
        commit.setExtra(extra);
        if (current.getParents() != null && current.getParents().length > 0) {
            for (RevCommit parent : current.getParents()) {
                commit.addParent(parent.getId().name());
                String parentId = parent.getId().getName();
                List<String> parentChildrenIds = childrenCount.get(parentId);
                if (parentChildrenIds == null) {
                    parentChildrenIds = new ArrayList<String>();
                }
                parentChildrenIds.add(current.getId().getName());
                childrenCount.put(parentId, parentChildrenIds);
            }
        }
        setCommitChildren(commit, current);
        computeCommitLane(commit);

        repository.addCommit(commit);
        commit.resetParents();
    }

    private void setCommitChildren(JsonCommit commit, PlotCommit plotcommit) {
        List<String> childrenIds = childrenCount.get(commit.getId());
        if (childrenIds == null) {
            return;
        }
        int childCount = childrenIds.size();
        if (childCount > 0) {
            for (int i = 0; i < childCount; i++) {
                String childId = childrenIds.get(i);
                JsonCommit childCommit = repository.getCommit(childId);
                if (childCommit == null) {
                    logger.warn("Could not find commit child with id : "+childId);
                }
                childCommit.addParent(commit.getId());
                JsonCommitChild child = new JsonCommitChild(childCommit.getId());
                child.setPosition(childCommit.getPosition());
                child.setLane(childCommit.getLane());
                child.addParent();
                commit.addChild(child);
            }
        }
    }

    private void computeCommitLane(JsonCommit commit) {
        int childCount = commit.getChildCount();
        if (childCount == 0) {
            return;
        }
        if (childCount == 1) {
            JsonCommitChild child = commit.getChildren().get(0);
            if (child == null) {
                logger.error("Could not find child commit. Weird");
            } else {
                setCommitAndchildLane(commit, child);
            }
        } else {
            // more than 1 child : commit is a fork
            for (int childIndex=0; childIndex<commit.getChildCount(); childIndex++) {
                JsonCommitChild child = commit.getChildren().get(childIndex);
                if (child == null) {
                    logger.error("Could not find child commit. Weird");
                } else {
                    if (child.getLane() == -1) {
                        setCommitAndchildLane(commit, child);
                    } else if (commit.getLane() == -1) {
                        setCommitLane(commit, child.getLane());
                    }
                }
            }
        }

    }

    private void setCommitAndchildLane(JsonCommit commit, JsonCommitChild child) {
        if (child.getLane() == -1) {
            int lane = getNextAvailableLane();
            child.setLane(lane);
            setChildCommitLane(child.getId(), lane);
            if (child.getParentCount() < 2) {
                setCommitLane(commit, lane);
                blockedLanes.set(lane);
            } else  {
                lane = getNextAvailableLane();
                setCommitLane(commit, lane);
                blockedLanes.set(lane);
            }
        } else if (commit.getLane() == -1) {
            setCommitLane(commit,child.getLane());
        }
    }

    private void setChildCommitLane(String childId, int lane) {
        JsonCommit childCommit = repository.getCommit(childId);
        if (childCommit == null) {
            logger.error("Could not find child commit. This is weird...");
        } else {
            setCommitLane(childCommit, lane);
        }
    }

    private void setCommitLane(JsonCommit commit, int lane) {
        commit.setLane(lane);
        laneTails.put(lane, commit);
    }

    private int getNextAvailableLane() {
        int lane = 0;
        if (activeLanes > 0) {
        for (lane = 0; lane < activeLanes; lane++) {
            if (!blockedLanes.get(lane)) {
                blockedLanes.set(lane);
                return lane;
            }
        }
        lane++;
        activeLanes++;
        blockedLanes.set(lane);
        } else {
            activeLanes++;
            blockedLanes.set(lane);
        }
        return lane;
    }

    private void finishGraph(Repository gitRepository, PlotWalk walk) {
        for (Map.Entry<Integer, JsonCommit> entry : laneTails.entrySet()) {
            JsonCommit commit = entry.getValue();
            int lane = entry.getKey().intValue();
            try {
                ObjectId objectId = gitRepository.resolve(commit.getId());
                PlotCommit gitCommit = (PlotCommit)walk.lookupCommit(objectId);
                if (gitCommit == null) throw new Exception("Cannot retrieve commit");
                int parentCount = gitCommit.getParentCount();
                if (parentCount > 0) {
                    for (int i = 0; i < parentCount; i++) {
                        PlotCommit parent = (PlotCommit)gitCommit.getParent(i);
                        addCommitToRepository(parent, position, true);
                        position++;
                    }
                }
            } catch (Exception e) {
                logger.error("Cannot find commit already in graph. Weird !");
            }
            //RevCommit rCommit = walk.lookupCommit(AnyObjectId.)
        }
    }

}
