package org.phoenix.giteye.core.utils;

import com.google.common.base.Predicate;
import com.sun.istack.internal.Nullable;
import org.phoenix.giteye.core.dto.Commit;
import org.phoenix.giteye.core.dto.Parent;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: phoenix
 * Date: 6/1/13
 * Time: 7:56 AM
 * To change this template use File | Settings | File Templates.
 */
public class CommitListPredicate implements Predicate {
    private int minPos;
    private int maxPos;

    public CommitListPredicate(int minPos, int maxPos) {
        this.minPos = minPos;
        this.maxPos = maxPos;
    }

    @Override
    public boolean apply(@Nullable Object o) {
        Commit commit = (Commit)o;
        int position = commit.getPosition();
        if (position >= minPos && position <maxPos) {
            return true;
        }
        List<Parent> parents = commit.getParents();
        if (parents == null) {
            return false;
        }
        for (Parent parent : parents) {
            int parentPosition = parent.getPosition();
            if (parentPosition >= minPos && parentPosition <maxPos) {
                return true;
            }
        }
        return false;
    }
}
