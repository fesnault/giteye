package org.phoenix.giteye.core.hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * User: phoenix
 * Date: 1/13/13
 * Time: 1:07 PM
 */
@Entity
@Table(name="DUMMY")
public class DummyEntity extends HibernateEntity {
    private static final long serialVersionUID = 8963768781855107420L;
    private String name;
    private int age;

    @Column(nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(nullable = false)
    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
