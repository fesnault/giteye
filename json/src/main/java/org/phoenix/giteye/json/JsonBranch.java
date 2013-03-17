package org.phoenix.giteye.json;

/**
 * Created with IntelliJ IDEA.
 * User: phoenix
 * Date: 3/2/13
 * Time: 2:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class JsonBranch {
    private String name;
    private String ref;
    private String target;
    private boolean symbolic;
    private boolean current;

    public JsonBranch(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
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

    public boolean isSymbolic() {
        return symbolic;
    }

    public void setSymbolic(boolean symbolic) {
        this.symbolic = symbolic;
    }

    public boolean isCurrent() {
        return current;
    }

    public void setCurrent(boolean current) {
        this.current = current;
    }
}
