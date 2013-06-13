package org.phoenix.giteye.core.beans.json;

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
    private List<JsonDiffId> differences;

    public String getParentCommitId() {
        return parentCommitId;
    }

    public void setParentCommitId(String parentCommitId) {
        this.parentCommitId = parentCommitId;
    }

    public List<JsonDiffId> getDifferences() {
        return differences;
    }

    public void addDifference(JsonDiffId difference) {
        if (this.differences == null) {
            this.differences = new ArrayList<JsonDiffId>();
        }
        this.differences.add(difference);
    }
}
