package org.phoenix.giteye.core.neo4j.manager;

import org.neo4j.graphdb.GraphDatabaseService;

/**
 * Created with IntelliJ IDEA.
 * User: phoenix
 * Date: 5/12/13
 * Time: 11:36 AM
 * To change this template use File | Settings | File Templates.
 */
public interface GraphCallback {
    <T> T execute(GraphDatabaseService graphDb);
}
