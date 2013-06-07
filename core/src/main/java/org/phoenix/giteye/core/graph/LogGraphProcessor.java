package org.phoenix.giteye.core.graph;

import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revplot.PlotWalk;
import org.eclipse.jgit.revwalk.RevCommit;
import org.phoenix.giteye.core.beans.GitLogRequest;
import org.phoenix.giteye.json.JsonRepository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: phoenix
 * Date: 3/6/13
 * Time: 8:16 PM
 * To change this template use File | Settings | File Templates.
 */
public interface LogGraphProcessor {
    //void process(Repository gitRepository, PlotWalk walk, List<RevCommit> heads, int max);
}
