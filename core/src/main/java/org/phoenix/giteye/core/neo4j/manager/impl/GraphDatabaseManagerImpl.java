package org.phoenix.giteye.core.neo4j.manager.impl;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.factory.GraphDatabaseSettings;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.ReadableIndex;
import org.neo4j.kernel.EmbeddedGraphDatabase;
import org.neo4j.kernel.EmbeddedReadOnlyGraphDatabase;
import org.phoenix.giteye.core.neo4j.manager.GitRelation;
import org.phoenix.giteye.core.neo4j.manager.GraphCallback;
import org.phoenix.giteye.core.neo4j.manager.GraphDatabaseManager;
import org.phoenix.giteye.core.neo4j.manager.GraphTemplate;
import org.phoenix.giteye.json.JsonBranch;
import org.phoenix.giteye.json.JsonCommit;
import org.phoenix.giteye.json.JsonRepository;
import org.phoenix.giteye.json.JsonTag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: phoenix
 * Date: 5/11/13
 * Time: 1:33 AM
 * To change this template use File | Settings | File Templates.
 */
public class GraphDatabaseManagerImpl implements GraphDatabaseManager{
    private String databasePath;
    private GraphTemplate graphTemplate;
    private final static Logger logger = LoggerFactory.getLogger(GraphDatabaseManagerImpl.class);


    public void setDatabasePath(String databasePath) {
        String finalDatabasePath = databasePath.endsWith("/") ? databasePath : databasePath+"/";
        this.databasePath = databasePath;

    }

    @Override
    public void shutdown(GraphDatabaseService graphDb) {
        logger.info("Closing database");
        graphDb.shutdown();
        logger.info("Database closed");
    }

    @Override
    public void addBranch(final GraphDatabaseService graphDb, final JsonBranch branch) {
        graphTemplate.doInGraph(graphDb, new GraphCallback() {
            public Node execute(GraphDatabaseService graphDb) {
                Node node = graphDb.createNode();
                node.setProperty("name", branch.getName());
                node.setProperty("ref", branch.getRef());
                node.setProperty("target", branch.getTarget());
                node.setProperty("symbolic", branch.isSymbolic());
                node.setProperty("type", "nt:branch");
                ReadableIndex<Node> autoIndex = graphDb.index().forNodes("commits");
                Node target = autoIndex.get("sha1",branch.getTarget()).getSingle();
                node.createRelationshipTo(target, GitRelation.POINTS_TO);
                logger.info("Added branch "+branch.getName());
                return node;
            }
        });
    }

    @Override
    public void addCommit(final GraphDatabaseService graphDb, final JsonCommit commit) {
        graphTemplate.doInGraph(graphDb, new GraphCallback() {
            public Node execute(GraphDatabaseService graphDb) {
                Node node = graphDb.createNode();
                node.setProperty("sha1", commit.getId());
                node.setProperty("authorName", commit.getAuthorName());
                node.setProperty("authorEmail", commit.getAuthorEmail());
                node.setProperty("committerName", commit.getCommitterName());
                node.setProperty("committerEmail", commit.getCommitterEmail());
                node.setProperty("date", commit.getDate().toString());
                node.setProperty("message", commit.getMessage());
                node.setProperty("shortMessage", commit.getShortMessage());
                node.setProperty("parentCount", commit.getParentCount());
                node.setProperty("lane", commit.getLane());
                node.setProperty("position", commit.getPosition());
                node.setProperty("type", "nt:commit");
                Index<Node> index = graphDb.index().forNodes("commits");
                index.add(node, "sha1", commit.getId());
                index.add(node, "authorName", commit.getAuthorName());
                index.add(node, "authorEmail", commit.getAuthorEmail());
                index.add(node, "message", commit.getMessage());
                logger.info("Added commit "+commit.getId());
                return node;
            }
        });
    }

    @Override
    public void setParents(final GraphDatabaseService graphDb, final String commitId, final List<String> parentIds) {
        graphTemplate.doInGraph(graphDb, new GraphCallback() {
            public Node execute(GraphDatabaseService graphDb) {
                ReadableIndex<Node> autoIndex = graphDb.index().forNodes("commits");
                Node child = autoIndex.get("sha1",commitId).getSingle();
                if (child == null) return null;
                if (!CollectionUtils.isEmpty(parentIds)) {
                    Iterator parentIterator = parentIds.iterator();
                    while (parentIterator.hasNext()) {
                        String parentId = (String)parentIterator.next();
                        Node parent = autoIndex.get("sha1",parentId).getSingle();
                        child.createRelationshipTo(parent, GitRelation.IS_CHILD_OF);
                        parent.createRelationshipTo(child, GitRelation.IS_PARENT_OF);
                    }
                }
                logger.info("Added parent(s) to commit "+commitId);
                return child;
            }
        });
    }

    @Override
    public void addRepository(GraphDatabaseService graphDb, final JsonRepository repository) {
        // Is it necessary ?
    }

    @Override
    public void addTag(GraphDatabaseService graphDb, final JsonTag tag) {
        graphTemplate.doInGraph(graphDb, new GraphCallback() {
            public Node execute(GraphDatabaseService graphDb) {
                Node node = graphDb.createNode();
                node.setProperty("name", tag.getName());
                node.setProperty("ref", tag.getRef());
                node.setProperty("target", tag.getTarget());
                node.setProperty("type", "nt:tag");
                ReadableIndex<Node> autoIndex = graphDb.index().forNodes("commits");
                Node target = autoIndex.get("sha1",tag.getTarget()).getSingle();
                if (target == null) {
                    logger.error("Could not find commit with sha1 "+tag.getTarget());
                    return null;
                }
                node.createRelationshipTo(target, GitRelation.POINTS_TO);
                return node;
            }
        });
    }

    @Override
    public void clearDatabase(GraphDatabaseService graphDb) {

    }

    @Override
    public GraphDatabaseService getDatabase(String name) {
        return getDatabase(name, false);
    }

    @Override
    public GraphDatabaseService getDatabase(String name, boolean readOnly) {
        Map<String, String> config = new HashMap<String, String>();
        config.put("read_only", Boolean.toString(readOnly));

        /*config.put(GraphDatabaseSettings.node_keys_indexable.toString(), "sha1,authorName,committerName,type,message,name");
        config.put(GraphDatabaseSettings.node_auto_indexing.toString(), "true");
        config.put(GraphDatabaseSettings.relationship_auto_indexing.toString(), "true");*/
        GraphDatabaseService graphDb = new GraphDatabaseFactory().newEmbeddedDatabaseBuilder( databasePath+name ).
                setConfig(config).
                newGraphDatabase();
        Map<String, String> indexConfig = new HashMap<String, String>();
        indexConfig.put("type", "fulltext");
        Index<Node> index = graphDb.index().forNodes("commits", indexConfig);
        logger.info("Opened database " + name + " with location : " + databasePath + name);
        registerShutdownHook( graphDb );
        return graphDb;
    }


    private static void registerShutdownHook( final GraphDatabaseService graphDb )
    {
        // Registers a shutdown hook for the Neo4j instance so that it
        // shuts down nicely when the VM exits
        Runtime.getRuntime().addShutdownHook( new Thread()
        {
            @Override
            public void run()
            {
                graphDb.shutdown();
                logger.info("Closed database.");
            }
        } );
    }

    public void setGraphTemplate(GraphTemplate graphTemplate) {
        this.graphTemplate = graphTemplate;
    }
}
