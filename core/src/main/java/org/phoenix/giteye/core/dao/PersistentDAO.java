package org.phoenix.giteye.core.dao;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Projection;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

/**
 *
 */
public interface PersistentDAO<K extends Serializable, T extends Serializable> {

    /**
     * Get a persistent object from its id
     *
     * @param id the persistent object's id
     * @return a Persistent object (may be null)
     */
    T findById(K id);


    /**
     * Get a persistent object from one of its unique keys
     *
     * @param property the unique property's name
     * @param value    the unique property's value
     * @return a Persistent object (may be null)
     */
    T findByUniqueKey(String property, final Object value);


    /**
     * Get all persistent objects of a class
     *
     * @return a List of Persistent objects
     */
    List<T> findAll();

    /**
     * Save or update an object
     *
     * @param object the object to save or update
     */
    @SuppressWarnings("unchecked")
    void saveOrUpdate(T object);


    /**
     * Delete an object
     *
     * @param object the object to delete
     */
    @SuppressWarnings("unchecked")
    void delete(T object);


    /**
     * Merge an object
     *
     * @param object the object to merge
     */
    T merge(T object);

    /**
     * Count the elements
     *
     * @param where the criteria
     * @return the number of elements matching the criteria
     */
    long count(Criterion[] where);
}
