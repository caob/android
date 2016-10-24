package com.ergo.poc.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/*
 * Created by mariano on 10/21/16.
 */
public class Notifications extends RealmObject implements Serializable {

    @PrimaryKey
    private String id;

    private long timestamp;

    private int type;
    private int category;

    @SerializedName("inner_id")
    private int innerId;

    private String title;
    private String description;
    private String link;

    private boolean read;

    public Notifications() {

    }

    public Notifications(String id, long timestamp, int type, int category, int innerId, String title
            , String description, String link, boolean read) {
        this.id = id;
        this.timestamp = timestamp;
        this.type = type;
        this.category = category;
        this.innerId = innerId;
        this.title = title;
        this.description = description;
        this.link = link;
        this.read = read;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public int getInnerId() {
        return innerId;
    }

    public void setInnerId(int innerId) {
        this.innerId = innerId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public String getDateFormat() {
        return new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date(timestamp));
    }
}
