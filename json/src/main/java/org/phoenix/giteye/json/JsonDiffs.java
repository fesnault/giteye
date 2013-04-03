package org.phoenix.giteye.json;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: phoenix
 * Date: 4/1/13
 * Time: 10:13 AM
 * To change this template use File | Settings | File Templates.
 */
public class JsonDiffs {
    private String parentCommitId;
    private List<JsonDiff> differences;

    public String getParentCommitId() {
        return parentCommitId;
    }

    public void setParentCommitId(String parentCommitId) {
        this.parentCommitId = parentCommitId;
    }

    public List<JsonDiff> getDifferences() {
        return differences;
    }

    public void addDifference(JsonDiff difference) {
        if (this.differences == null) {
            this.differences = new ArrayList<JsonDiff>();
        }
        this.differences.add(difference);
    }
}
