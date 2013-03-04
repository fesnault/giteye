package org.phoenix.giteye.core.exceptions.json;

/**
 * No such repository in persistence store.
 * @author phoenix
 */
public class NotInitializedRepositoryException extends Exception {

    public NotInitializedRepositoryException(String message) {
            super(message);
        }

    public NotInitializedRepositoryException(String message, Throwable cause) {
        super(message, cause);
    }
}
