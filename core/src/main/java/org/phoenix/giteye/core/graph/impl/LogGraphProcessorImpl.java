package org.phoenix.giteye.core.graph.impl;

import org.phoenix.giteye.core.graph.LogGraphProcessor;
import org.phoenix.giteye.json.JsonCommit;
import org.phoenix.giteye.json.JsonCommitChild;
import org.phoenix.giteye.json.JsonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: phoenix
 * Date: 3/6/13
 * Time: 8:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class LogGraphProcessorImpl implements LogGraphProcessor{
    private final static Logger logger = LoggerFactory.getLogger(LogGraphProcessorImpl.class);
    private int minLane = 0;
    private int maxLane = 0;


    public JsonRepository process(JsonRepository repository) {
        List<JsonCommit> commits = repository.getCommits();
        int x = 0;

        for (JsonCommit commit : commits) {
            processCommit(repository, commit, x);
        }
        return repository;
    }

    private void processCommit(JsonRepository repository, JsonCommit commit, int x) {
        logger.info("Commit  ["+commit.getPosition()+"] -> "+commit.getId());

    }

}
