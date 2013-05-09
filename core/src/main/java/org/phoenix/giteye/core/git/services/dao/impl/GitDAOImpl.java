package org.phoenix.giteye.core.git.services.dao.impl;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.RefDatabase;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.phoenix.giteye.core.git.services.dao.GitDAO;

import javax.persistence.MapsId;
import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: phoenix
 * Date: 5/9/13
 * Time: 9:59 PM
 * To change this template use File | Settings | File Templates.
 */
@org.springframework.stereotype.Repository
public class GitDAOImpl implements GitDAO {
    @Override
    public Repository getRepository(String path) throws IOException {
        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        return builder.setGitDir(new File(path))
                .readEnvironment() // scan environment GIT_* variables
                .findGitDir() // scan up the file system tree
                .build();
    }

    @Override
    public Iterable<Ref> getBranches(Repository repository) throws GitAPIException, NoHeadException {
        Git git = new Git(repository);
        return git.branchList().setListMode(ListBranchCommand.ListMode.ALL).call();
    }

    @Override
    public Map<String,Ref> getAllRefs(Repository repository) throws IOException {
        RefDatabase refDb = repository.getRefDatabase();
        return refDb.getRefs("");
    }
}

