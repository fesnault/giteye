package org.phoenix.giteye.core.neo4j.manager;

import org.apache.commons.collections.MapUtils;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.factory.GraphDatabaseSettings;
import org.neo4j.kernel.AbstractGraphDatabase;
import org.neo4j.kernel.EmbeddedGraphDatabase;
import org.neo4j.kernel.EmbeddedReadOnlyGraphDatabase;
import org.neo4j.kernel.configuration.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: phoenix
 * Date: 5/12/13
 * Time: 11:35 AM
 * To change this template use File | Settings | File Templates.
 */
public class GraphTemplate {
    private final static Logger logger = LoggerFactory.getLogger(GraphTemplate.class);

    public <T> T doInGraph(GraphDatabaseService graphDb, GraphCallback callback) {

        Transaction tx = graphDb.beginTx();
        T result = null;
        try {
            result =  callback.execute(graphDb);
            logger.info("Transaction successful.");
            tx.success();
        } catch (Exception e) {
            logger.error("Transaction failure "+e.getMessage(), e);
            tx.failure();
        } finally {
            logger.info("Transaction ended.");
            tx.finish();
            return result;
        }
    }

}
