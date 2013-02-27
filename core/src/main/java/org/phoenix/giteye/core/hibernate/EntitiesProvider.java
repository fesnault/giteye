package org.phoenix.giteye.core.hibernate;

import java.util.HashSet;
import java.util.Set;

public abstract class EntitiesProvider {
    private HashSet<Class<?>> entities = new HashSet<Class<?>>();

    public final Set<Class<?>> getEntities() {
        return entities;
    }

    /**
     * To be invoked from the constructor of concrete subclasses
     *
     * @param clazz the Hibernate entity to register
     */
    protected final void add(Class<?> clazz) {
        entities.add(clazz);
    }
}