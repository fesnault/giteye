package org.phoenix.giteye.core.neo4j.manager;

import org.neo4j.graphdb.RelationshipType;


/**
 * Created with IntelliJ IDEA.
 * User: phoenix
 * Date: 5/12/13
 * Time: 2:21 PM
 * To change this template use File | Settings | File Templates.
 */
public enum GitRelation implements RelationshipType {
    HAS_BRANCH,
    IS_PARENT_OF,
    IS_CHILD_OF,
    POINTS_TO;

}
