package org.phoenix.giteye.core.hibernate;
import java.util.HashSet;
import java.util.List;
import java.util.ServiceLoader;
import java.util.Set;

import org.springframework.beans.factory.config.AbstractFactoryBean;

public class PackagesFactoryBean extends AbstractFactoryBean<Set<String>> {
    /**
     * You can choose to specify additionnal packages
     */
    private List<String> annotatedPackages;

    @Override
    protected Set<String> createInstance() throws Exception {
        HashSet<String> entities = new HashSet<String>();

        // Processing Entities packages
        ServiceLoader<EntitiesProvider> loader = ServiceLoader.load(EntitiesProvider.class);
        for (EntitiesProvider ep : loader) {
            for (Class<?> entityClass : ep.getEntities()) {
                if (isAnnotatedPackage(entityClass.getPackage().getName())) {
                    entities.add(entityClass.getPackage().getName());
                }
            }
        }

        // Processing additionnal packages
        if (annotatedPackages != null) {
            for (String packageName : annotatedPackages) {
                if (isAnnotatedPackage(packageName)) {
                    entities.add(packageName);
                }
            }
        }

        logger.info("Packages found: " + entities);
        return entities;
    }

    private boolean isAnnotatedPackage(String packageName) {
        try {
            Class.forName(packageName + ".package-info");
            // Logger
            if (logger.isDebugEnabled()) {
                logger.debug("Package found: " + packageName);
            }
            return true;
        } catch (ClassNotFoundException e) {
            // Logger
            if (logger.isDebugEnabled()) {
                logger.debug("Package not found: " + packageName);
            }
            return false;
        }
    }

    public void setAnnotatedPackages(List<String> annotatedPackages) {
        this.annotatedPackages = annotatedPackages;
    }

    @Override
    public Class<?> getObjectType() {
        return Set.class;
    }
}