package org.phoenix.giteye.core.git.services.impl;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.phoenix.giteye.core.beans.CommitBean;
import org.phoenix.giteye.core.beans.RepositoryBean;
import org.phoenix.giteye.core.constants.GitConstants;
import org.phoenix.giteye.core.git.services.LogService;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: phoenix
 * Date: 2/27/13
 * Time: 11:32 PM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class LogServiceimpl implements LogService{
    @Override
    public List<CommitBean> getLog(RepositoryBean repository) {
        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        Repository repo = null;
        List<CommitBean> commits = new ArrayList<CommitBean>();
        try {
            repo = builder.setGitDir(new File(repository.getPath() + GitConstants.GIT_METAINF_SUFFIX))
                    .readEnvironment() // scan environment GIT_* variables
                    .findGitDir() // scan up the file system tree
                    .build();

            Git git = new Git(repo);
            Iterable<RevCommit> iterable = git.log().all().call();
            for (RevCommit revCommit : iterable) {
                CommitBean commit = new CommitBean();
                commit.setId(revCommit.getId().abbreviate(7).name());
                commit.setCommitterName(revCommit.getCommitterIdent().getName());
                commit.setMessage(revCommit.getShortMessage());
                commits.add(commit);
            }
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (NoHeadException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (GitAPIException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return commits;
    }
}
