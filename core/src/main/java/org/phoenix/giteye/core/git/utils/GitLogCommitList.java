package org.phoenix.giteye.core.git.utils;

import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.errors.RevWalkException;
import org.eclipse.jgit.revplot.PlotCommit;
import org.eclipse.jgit.revplot.PlotCommitList;
import org.eclipse.jgit.revplot.PlotLane;
import org.eclipse.jgit.revplot.PlotWalk;
import org.eclipse.jgit.revwalk.DepthWalk;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.phoenix.giteye.core.beans.GitLogRequest;
import org.phoenix.giteye.json.JsonCommit;
import org.phoenix.giteye.json.JsonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: phoenix
 * Date: 5/23/13
 * Time: 10:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class GitLogCommitList<T extends PlotLane> extends PlotCommitList<T> {
    private final static Logger logger = LoggerFactory.getLogger(GitLogCommitList.class);
    private Map<Integer, PlotCommit> laneTails = new HashMap<Integer, PlotCommit>();
    private JsonRepository jrep;
    private int position = 0;
    private boolean tailsClosed = false;
    private PlotWalk walker;

    public GitLogCommitList(JsonRepository jrep) {
        this.jrep = jrep;
    }

    public void source(RevWalk source) {
        this.walker = (PlotWalk)source;
        super.source(source);
    }

    public void fillTo(int commitCount, List<RevCommit> heads) throws MissingObjectException,
            IncorrectObjectTypeException, IOException{
        fillTo(commitCount);
        tailsClosed = true;
        addHiddenCommits();
    }

    private void addHiddenCommits() {
        for (Map.Entry<Integer, PlotCommit> entry : laneTails.entrySet()) {
            logger.info("Tail of lane "+entry.getKey()+ "has sha1 : "+entry.getValue().getId().getName());
            PlotCommit commit = entry.getValue();
            if (commit.getParentCount() > 0) {
                for (RevCommit parent : commit.getParents()) {
                    if (jrep.getCommit(parent.getId().getName()) == null) {
                        // jrep does not contain commit parent. Let's add it.
                        add((PlotCommit)parent);
                        enter(position+1, (PlotCommit)parent);
                        logger.info("Added parent "+parent.getId().getName()+ " at position "+position+" and lane "+((PlotCommit) parent).getLane().getPosition()+ " to commit at lane "+entry.getKey());
                    }
                }
            }
        }
    }

    @Override
    protected void enter(final int index, final PlotCommit<T> currCommit) {
        super.enter(index, currCommit);
        PlotLane plotLane  = currCommit.getLane();
        int lane = plotLane == null ? 0 : plotLane.getPosition();
        if (!tailsClosed) {
            laneTails.put(lane, currCommit);
        }
        //addCommitToRepository(currCommit);
    }



    public JsonRepository getRepository() {
        return jrep;
    }

}
