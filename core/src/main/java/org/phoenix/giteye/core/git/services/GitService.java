package org.phoenix.giteye.core.git.services;

import org.eclipse.jgit.revplot.PlotCommit;
import org.eclipse.jgit.revplot.PlotCommitList;
import org.eclipse.jgit.revplot.PlotLane;
import org.phoenix.giteye.core.beans.BranchBean;
import org.phoenix.giteye.core.beans.GitLogRequest;
import org.phoenix.giteye.core.beans.RepositoryBean;
import org.phoenix.giteye.core.beans.json.JsonCommitDetails;
import org.phoenix.giteye.core.beans.json.JsonRepository;
import org.phoenix.giteye.core.dto.Commit;
import org.phoenix.giteye.core.exceptions.NotInitializedRepositoryException;

import java.util.Collection;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: phoenix
 * Date: 2/27/13
 * Time: 11:31 PM
 * To change this template use File | Settings | File Templates.
 */
public interface GitService {

    List<BranchBean> getBranches(RepositoryBean repository);

    JsonRepository getLogAsJson(RepositoryBean repository, int pageSize, int page);

    //JsonRepository getLogAsJson(RepositoryBean repository, int pageSize, int page) throws NotInitializedRepositoryException;

    JsonRepository getLogAsJson(RepositoryBean repository, GitLogRequest logRequest) throws NotInitializedRepositoryException;

    JsonCommitDetails getCommitDetails(RepositoryBean repository, String commitId) throws NotInitializedRepositoryException;
}
