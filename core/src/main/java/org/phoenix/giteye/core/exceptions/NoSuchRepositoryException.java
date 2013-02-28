package org.phoenix.giteye.core.exceptions;

/**
 * No such repository in persistence store.
 * @author phoenix
 */
public class NoSuchRepositoryException extends Exception {

    public NoSuchRepositoryException(String message) {
            super(message);
        }

    public NoSuchRepositoryException(String message, Throwable cause) {
        super(message, cause);
    }
}
