package com.ergo.poc.data.model;

/*
 * Created by mariano on 10/24/16.
 */
public class Notification {

    String name;
    int category;
    boolean state;

    public Notification() {

    }

    public Notification(String name, int category, boolean state) {
        this.name = name;
        this.category = category;
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }
}
