package org.phoenix.giteye.json;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: phoenix
 * Date: 3/2/13
 * Time: 3:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class JsonCommitParent {
    private String id;
    private Date date;
    private int lane;

    public JsonCommitParent(String id, Date date, int lane) {
        this.id = id;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public int getLane() {
        return lane;
    }
}
