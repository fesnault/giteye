package org.phoenix.giteye.core.hibernate;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
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

        /**
         *
         */
        public UUIDGenerator() {
            super();
        }

        /**
         * @see org.hibernate.id.Configurable#configure(org.hibernate.type.Type, java.util.Properties, org.hibernate.dialect.Dialect)
         */
        public void configure(Type type, Properties params, Dialect d) throws MappingException {
            separator = params.getProperty(KEY_SEPARATOR);
        }

        /**
         * @see org.hibernate.id.IdentifierGenerator#generate(org.hibernate.engine.SessionImplementor, Object)
         */
        public Serializable generate(SessionImplementor session, Object object) throws HibernateException {
            if (separator == null) {
                return UUID.randomUUID().toString();
            } else {
                return UUID.randomUUID().toString().replaceAll("-", separator);
            }
        }
    }
