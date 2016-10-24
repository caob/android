package com.ergo.poc.data.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/*
 * Created by mariano on 10/22/16.
 */
public class User extends RealmObject implements Serializable {

    @PrimaryKey
    @SerializedName("_id")
    private String id;

    @SerializedName("nombre")
    private String name;

    @SerializedName("apellido")
    private String lastName;

    @SerializedName("sexo")
    private String sex;

    @SerializedName("fecha_nacimiento")
    private String birthday;

    @SerializedName("id_str")
    private String stringId;

    @SerializedName("phone_number")
    private String phoneNumber;

    @SerializedName("__v")
    private int version;

    @SerializedName("id")
    private long idCode;

    public User() {

    }

    public User(String id, String name, String lastName, String sex, String birthday, String stringId
            , String phoneNumber, int version, long idCode) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.sex = sex;
        this.birthday = birthday;
        this.stringId = stringId;
        this.phoneNumber = phoneNumber;
        this.version = version;
        this.idCode = idCode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getStringId() {
        return stringId;
    }

    public void setStringId(String stringId) {
        this.stringId = stringId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public long getIdCode() {
        return idCode;
    }

    public void setIdCode(long idCode) {
        this.idCode = idCode;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", lastName='" + lastName + '\'' +
                ", sex='" + sex + '\'' +
                ", birthday='" + birthday + '\'' +
                ", stringId='" + stringId + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", version=" + version +
                ", idCode=" + idCode +
                '}';
    }
}
