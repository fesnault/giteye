package org.phoenix.giteye.core.graph;

import org.phoenix.giteye.json.JsonRepository;

/**
 * Created with IntelliJ IDEA.
 * User: phoenix
 * Date: 3/6/13
 * Time: 8:16 PM
 * To change this template use File | Settings | File Templates.
 */
public interface LogGraphProcessor {
    JsonRepository process(JsonRepository repository);
}
