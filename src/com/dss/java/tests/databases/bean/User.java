package com.dss.java.tests.databases.bean;

import java.util.Date;

/**
 * FileName: User
 * Author: Chris
 * Date: 2018/10/25 11:21
 * Description: User bean
 */
public class User {
    private int mId;
    private String mName;
    private String mEmail;
    private Date mBirth;

    public User() {
    }

    public User(int id, String name, String email, Date birth) {
        mId = id;
        mName = name;
        mEmail = email;
        mBirth = birth;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public Date getBirth() {
        return mBirth;
    }

    public void setBirth(Date birth) {
        mBirth = birth;
    }

    @Override
    public String toString() {
        return "User{" +
                "mId=" + mId +
                ", mName='" + mName + '\'' +
                ", mEmail='" + mEmail + '\'' +
                ", mBirth=" + mBirth +
                '}';
    }
}
