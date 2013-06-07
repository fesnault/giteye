package org.phoenix.giteye.core.beans.json;

/**
 * Created with IntelliJ IDEA.
 * User: phoenix
 * Date: 3/3/13
 * Time: 1:14 AM
 * To change this template use File | Settings | File Templates.
 */
public class JsonTag {
    private String name;
    private String ref;
    private String target;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }
}
