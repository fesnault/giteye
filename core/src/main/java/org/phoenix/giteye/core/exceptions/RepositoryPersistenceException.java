package org.phoenix.giteye.core.exceptions;

/**
 * Exception raised when an error occurs while persisting a repository.
 * @author phoenix
 */
public class RepositoryPersistenceException extends Exception {
    public RepositoryPersistenceException(String message) {
        super(message);
    }

    public RepositoryPersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
}
