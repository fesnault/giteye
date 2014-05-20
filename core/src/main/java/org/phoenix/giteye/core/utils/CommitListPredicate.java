package org.phoenix.giteye.core.utils;

import com.google.common.base.Predicate;
import org.phoenix.giteye.core.dto.Commit;
import org.phoenix.giteye.core.dto.Parent;

import java.util.List;
import java.util.Map;

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
    private Map<String, Commit> commitsById;

    public CommitListPredicate(Map<String, Commit> commitsById, int minPos, int maxPos) {
        this.minPos = minPos;
        this.maxPos = maxPos;
        this.commitsById = commitsById;
    }

    @Override
    public boolean apply(Object o) {
        Commit commit = (Commit)o;
        if (commit.isExtra()) {
            return true;
        }
        int position = commit.getPosition();
        boolean included = false;
        List<Parent> parents = commit.getParents();
        // if position is between min and max, then commit must be included.
        if (position >= minPos && position <maxPos) {
            included = true;
        }
        // if no parents, just return commit inclusion flag
        if (parents == null) {
            return included;
        }
        for (Parent parent : parents) {
            Commit parentCommit = commitsById.get(parent.getId());
            if (parentCommit == null) {
                continue;
            }
            int parentPosition = parentCommit.getPosition();
            // two possibilities :
            // - commit is included, then if its parent is not, it must be set extra
            // - commit is not included, then if its parent is included, the child must be set extra
            if (included) {
                // commit included
                if (parentPosition < minPos && parentPosition >maxPos) {
                    // parent out of range, but must be kept for graphing purposes.
                    // set it as extra
                    parentCommit.setExtra(true);
                }
            } else {
                // commit not included
                if (parentPosition >= minPos && parentPosition <maxPos) {
                    // parent is included. Child must be set extra for graphing purposes
                    commit.setExtra(true);
                }
            }
        }
        return included;
    }
}
