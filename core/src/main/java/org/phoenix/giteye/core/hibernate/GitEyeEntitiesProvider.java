package org.phoenix.giteye.core.hibernate;

/**
 * GitEye entities provider.
 * User: phoenix
 * Date: 14/01/13
 * Time: 12:20
 */
public class GitEyeEntitiesProvider extends EntitiesProvider {

    public GitEyeEntitiesProvider() {
        add(GitRepository.class);
    }
}
