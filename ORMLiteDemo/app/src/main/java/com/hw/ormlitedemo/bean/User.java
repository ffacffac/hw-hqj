package com.hw.ormlitedemo.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by huangqj on 2019-07-25.
 */
@DatabaseTable(tableName = "user")
public class User {

    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String PASSWORD = "password";
    public static final String ADDRESS = "address";
    public static final String AGE = "age";

    @DatabaseField(columnName = ID, canBeNull = false, generatedId = true)
    private long id;

    @DatabaseField(columnName = NAME)
    private String name;

    @DatabaseField(columnName = PASSWORD)
    private String password;

    @DatabaseField(columnName = ADDRESS)
    private String address;

    @DatabaseField(columnName = AGE)
    private int age;

    public User() {
    }

    public User(String name, String password, String address, int age) {
        this.name = name;
        this.password = password;
        this.address = address;
        this.age = age;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", name='" + name + '\'' + ", password='" + password + '\'' + ", address='"
                + address + '\'' + ", age=" + age + '}';
    }
}
