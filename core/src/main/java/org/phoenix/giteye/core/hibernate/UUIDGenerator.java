package org.phoenix.giteye.core.hibernate;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.util.Properties;
import java.util.UUID;


/**
 * UUID generator.
 * User: phoenix
 * Date: 14/01/13
 * Time: 12:36
 */
public class UUIDGenerator implements IdentifierGenerator, Configurable {
    private final static String KEY_SEPARATOR = "separator";
    private String separator = null;

    public void configure(Type type, Properties properties, ServiceRegistry serviceRegistry) throws MappingException {
        separator = properties.getProperty(KEY_SEPARATOR);
    }


    public Serializable generate(SessionImplementor sessionImplementor, Object o) throws HibernateException {
        if (separator == null) {
            return UUID.randomUUID().toString();
        } else {
            return UUID.randomUUID().toString().replaceAll("-", separator);
        }
    }

}
