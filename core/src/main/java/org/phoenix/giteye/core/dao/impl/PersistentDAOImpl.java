package org.phoenix.giteye.core.dao.impl;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.phoenix.giteye.core.dao.PersistentDAO;
import org.springframework.orm.hibernate5.HibernateCallback;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: phoenix
 * Date: 27/02/13
 * Time: 18:30
 * To change this template use File | Settings | File Templates.
 */
public abstract class PersistentDAOImpl<K extends Serializable, T extends Serializable>
        extends HibernateDaoSupport implements PersistentDAO<K, T> {

    private Class<T> persistentClass;

    /**
     * Default constructor
     */
    protected PersistentDAOImpl() {
        this.persistentClass = getPersistentClass();
    }


    /**
     * Get the persistent class this DAO is handling. By default, persistent class is the second generic type.
     *
     * @return The persistent class
     */
    protected Class<T> getPersistentClass() {
        Class clazz = getClass();
        do {
            Type type = clazz.getGenericSuperclass();
            if (type instanceof ParameterizedType) {
                //noinspection unchecked
                return (Class<T>)((ParameterizedType) type).getActualTypeArguments()[1];
            }
            clazz = clazz.getSuperclass();
        } while (clazz != null);
        return null;
    }


    /**
     * Get a persistent object from its id
     *
     * @param id the persistent object's id
     * @return a Persistent object (may be null)
     */
    @SuppressWarnings("unchecked")
    @Override
    public T findById(K id) {
        //noinspection unchecked
        return (T)getHibernateTemplate().get(persistentClass, id);
    }


    /**
     * Get a persistent object from one of its unique keys
     *
     * @param property the unique property's name
     * @param value the unique property's value
     * @return a Persistent object (may be null)
     */
    @SuppressWarnings("unchecked")
    @Override
    public T findByUniqueKey(final String property, final Object value) {
        return (T)getHibernateTemplate().execute(new HibernateCallback() {
            @Override
            public Object doInHibernate(Session session) throws HibernateException {
                return session.createCriteria(persistentClass).add(Restrictions.eq(property, value)).uniqueResult();
            }
        });
    }


    /**
     * Get all persistent objects of a class
     *
     * @return a List of Persistent objects
     */
    @Override
    public List<T> findAll() {
        return (List<T>)getHibernateTemplate().find("from "+persistentClass.getSimpleName());
    }


    /**
     * Save or update an object
     *
     * @param object the object to save or update
     */
    @Transactional
    @Override
    public void saveOrUpdate(T object) {
        getHibernateTemplate().saveOrUpdate(object);
    }


    /**
     * Delete an object
     *
     * @param object the object to delete
     */
    @Transactional
    @Override
    public void delete(T object) {
        getHibernateTemplate().delete(object);
    }

    /**
     * Merge an object
     *
     * @param object the object to merge
     */
    @Transactional
    @Override
    public T merge(T object) {
        return getHibernateTemplate().merge(object);
    }

    /**
     * Count the elements
     *
     * @param where the criteria
     * @return the number of elements matching the criteria
     */
    @Override
    public long count(final Criterion[] where) {
        return (Long)getHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException {
                Criteria c = session.createCriteria(persistentClass);
                if (where != null && where.length > 0) {
                    for (Criterion aWhere : where) {
                        c.add(aWhere);
                    }
                }
                c.setProjection(Projections.rowCount());
                return c.uniqueResult();
            }
        });
    }
}

