package org.phoenix.giteye.core.graph;

import org.phoenix.giteye.core.beans.json.JsonRepository;
import org.phoenix.giteye.core.graph.impl.LogGraphProcessorImpl;

/**
 * Created with IntelliJ IDEA.
 * User: phoenix
 * Date: 3/6/13
 * Time: 8:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class LogGraphProcessorFactory {

    private LogGraphProcessorFactory() {
        // Private emtpy constructor - Utility class
    }

    public static LogGraphProcessor getProcessor(JsonRepository repository) {
        //return new LogGraphProcessorImpl(repository);
        return null;
    }

    public static LogGraphProcessor getProcessor(JsonRepository repository, GraphState state) {
        //return new LogGraphProcessorImpl(repository, state);
        return null;
    }

}
