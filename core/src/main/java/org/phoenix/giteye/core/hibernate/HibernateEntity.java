package org.phoenix.giteye.core.hibernate;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.Date;

/**
 * Base class for all hibernate entities.
 * User: phoenix
 * Date: 14/01/13
 * Time: 10:46
 */
@MappedSuperclass
public abstract class HibernateEntity implements Serializable {
    public static final int UUID_LENGTH = 36;
    protected String id;
    protected Date created;
    protected Date modified;

    @Id
    @GeneratedValue(generator = "juuid")
    @Column(length = UUID_LENGTH)
    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * @see Object#hashCode()
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.getId()).toHashCode();
    }

    /**
     * @see Object#equals(Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof HibernateEntity)) {
            return false;
        }
        HibernateEntity cast = (HibernateEntity) obj;
        return new EqualsBuilder().append(this.getId(), cast.getId()).isEquals();
    }
}
