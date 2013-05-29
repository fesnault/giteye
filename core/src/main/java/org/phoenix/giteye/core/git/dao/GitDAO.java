package org.phoenix.giteye.core.git.dao;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;

import java.io.IOException;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: phoenix
 * Date: 5/9/13
 * Time: 9:53 PM
 * To change this template use File | Settings | File Templates.
 */

public interface GitDAO {
    Repository getRepository(String path) throws IOException;
    Iterable<Ref> getBranches(Repository repository) throws GitAPIException, NoHeadException;
    Map<String, Ref> getAllRefs(Repository repository) throws IOException;
}
