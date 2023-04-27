package com.minis.test.entity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class User {
    int id = 1;
    String name = "";
    Date birthday = new Date();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    @Override
    public String toString() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", birthday=" + simpleDateFormat.format(birthday) +
                '}';
    }
}
