package org.phoenix.giteye.core.hibernate;

import org.springframework.beans.factory.config.AbstractFactoryBean;

import java.util.HashSet;
import java.util.ServiceLoader;
import java.util.Set;

/**
 * Uses {@link java.util.ServiceLoader} to locate all concrete subclasses of {@link GitEyeEntitiesProvider} and merge their collections of classes into
 * one {@link java.util.Set} of classes (deduplicating the entities in the process).
 * 
 * @author bruyeron
 * @version $Id: EntitiesFactoryBean.java 83718 2010-09-18 14:07:05Z bazoud $
 */
public class GitEyeEntitiesFactoryBean extends AbstractFactoryBean<Set<Class<?>>> {

    @Override
    protected Set<Class<?>> createInstance() throws Exception {
        HashSet<Class<?>> entities = new HashSet<Class<?>>();
        ServiceLoader<GitEyeEntitiesProvider> loader = ServiceLoader.load(GitEyeEntitiesProvider.class);
        for (GitEyeEntitiesProvider ep : loader) {
            logger.info("Located entities provider " + ep.getClass());
            if (logger.isDebugEnabled()) {
                logger.debug("providing entities " + ep.getEntities());
            }
            entities.addAll(ep.getEntities());
        }
        logger.info(entities.size() + " entities found: " + entities);
        return entities;
    }

    @Override
    public Class<?> getObjectType() {
        return Set.class;
    }

}