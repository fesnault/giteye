package org.phoenix.giteye.core.neo4j.manager;

import org.neo4j.graphdb.GraphDatabaseService;
import org.phoenix.giteye.json.JsonBranch;
import org.phoenix.giteye.json.JsonCommit;
import org.phoenix.giteye.json.JsonRepository;
import org.phoenix.giteye.json.JsonTag;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: phoenix
 * Date: 5/11/13
 * Time: 1:33 AM
 * To change this template use File | Settings | File Templates.
 */
public interface GraphDatabaseManager {

    void addBranch(GraphDatabaseService graphDb, JsonBranch branch);

    void addCommit(GraphDatabaseService graphDb, JsonCommit commit);

    void setParents(GraphDatabaseService graphDb, String commitId, List<String> parentIds);

    void addRepository(GraphDatabaseService graphDb, JsonRepository repository);

    void addTag(GraphDatabaseService graphDb, JsonTag tag);

    void clearDatabase(GraphDatabaseService graphDb);

    void shutdown(GraphDatabaseService graphDb);

    GraphDatabaseService getDatabase(String name);

    GraphDatabaseService getDatabase(String name, boolean readOnly);
}
