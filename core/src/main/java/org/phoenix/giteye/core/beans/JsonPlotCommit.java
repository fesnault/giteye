package org.phoenix.giteye.core.beans;

import org.eclipse.jgit.lib.AnyObjectId;
import org.eclipse.jgit.revplot.PlotCommit;
import org.eclipse.jgit.revplot.PlotLane;

/**
 * Created with IntelliJ IDEA.
 * User: phoenix
 * Date: 5/30/13
 * Time: 10:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class JsonPlotCommit extends PlotCommit<PlotLane> {
    /**
     * Create a new commit.
     *
     * @param id the identity of this commit.
     */
    protected JsonPlotCommit(AnyObjectId id) {
        super(id);
    }


}
