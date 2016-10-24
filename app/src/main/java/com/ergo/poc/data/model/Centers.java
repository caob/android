package com.ergo.poc.data.model;

import android.location.Location;
import android.support.annotation.NonNull;

import com.ergo.poc.util.LocationUpdate;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Locale;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/*
 * Created by mariano on 10/21/16.
 */
public class Centers extends RealmObject implements Serializable, Comparable<Centers> {

    @PrimaryKey
    @SerializedName("_id")
    String id;

    @SerializedName("tipo")
    String type;

    @SerializedName("nombre")
    String name;

    @SerializedName("direccion")
    String address;

    @SerializedName("localidad")
    String city;

    @SerializedName("provincia")
    String state;

    @SerializedName("capacidad1")
    String capacityOne;

    @SerializedName("capacidad2")
    String capacityTwo;

    @SerializedName("capacidad3")
    String capacityThree;

    @SerializedName("capacidad4")
    String capacityFour;

    @SerializedName("capacidad5")
    String capacityFive;

    @SerializedName("latitud")
    String latitude;

    @SerializedName("longitud")
    String longitude;

    @SerializedName("atencion")
    String attention;

    @SerializedName("maplink")
    String mapLink;

    @SerializedName("maplink2")
    String mapLinkTwo;

    @SerializedName("direccion_completa")
    String fullAddress;

    @SerializedName("FIELD18")
    String field;

    @SerializedName("id")
    int idNumber;

    @Ignore
    String distance;

    public Centers() {

    }

    public Centers(String id, String type, String name, String address, String city, String state
            , String capacityOne, String capacityTwo, String capacityThree, String capacityFour
            , String capacityFive, String latitude, String longitude, String attention, String mapLink
            , String mapLinkTwo, String fullAddress, String field, int idNumber) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.address = address;
        this.city = city;
        this.state = state;
        this.capacityOne = capacityOne;
        this.capacityTwo = capacityTwo;
        this.capacityThree = capacityThree;
        this.capacityFour = capacityFour;
        this.capacityFive = capacityFive;
        this.latitude = latitude;
        this.longitude = longitude;
        this.attention = attention;
        this.mapLink = mapLink;
        this.mapLinkTwo = mapLinkTwo;
        this.fullAddress = fullAddress;
        this.field = field;
        this.idNumber = idNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCapacityOne() {
        return capacityOne;
    }

    public void setCapacityOne(String capacityOne) {
        this.capacityOne = capacityOne;
    }

    public String getCapacityTwo() {
        return capacityTwo;
    }

    public void setCapacityTwo(String capacityTwo) {
        this.capacityTwo = capacityTwo;
    }

    public String getCapacityThree() {
        return capacityThree;
    }

    public void setCapacityThree(String capacityThree) {
        this.capacityThree = capacityThree;
    }

    public String getCapacityFour() {
        return capacityFour;
    }

    public void setCapacityFour(String capacityFour) {
        this.capacityFour = capacityFour;
    }

    public String getCapacityFive() {
        return capacityFive;
    }

    public void setCapacityFive(String capacityFive) {
        this.capacityFive = capacityFive;
    }

    public String getLatitude() {
        return latitude;
    }

    public Double getLatitudeDouble() {
        return latitude != null ? Double.parseDouble(latitude) : 0d;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public Double getLongitudeDouble() {
        return longitude != null ? Double.parseDouble(longitude) : 0d;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getAttention() {
        return attention;
    }

    public void setAttention(String attention) {
        this.attention = attention;
    }

    public String getMapLink() {
        return mapLink;
    }

    public void setMapLink(String mapLink) {
        this.mapLink = mapLink;
    }

    public String getMapLinkTwo() {
        return mapLinkTwo;
    }

    public void setMapLinkTwo(String mapLinkTwo) {
        this.mapLinkTwo = mapLinkTwo;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public int getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(int idNumber) {
        this.idNumber = idNumber;
    }

    public void setDistanceUpdated(String distance) {
        this.distance = distance;
    }

    public String getDistanceUpdated() {
        return distance;
    }

    public String getDistanceParsed() {
        Location location = LocationUpdate.getInstance().getLocation();
        Location centersLocation = new android.location.Location("");
        centersLocation.setLatitude(Double.parseDouble(getLatitude() != null ? getLatitude() : "0"));
        centersLocation.setLongitude(Double.parseDouble(getLongitude() != null ? getLongitude() : "0"));
        return location != null ? String.format(Locale.getDefault(), "%.2f", centersLocation.distanceTo(location) / 1000) + " Km" : "";
    }

    public Float getDistance() {
        Location location = LocationUpdate.getInstance().getLocation();
        Location centersLocation = new Location("");
        centersLocation.setLatitude(Double.parseDouble(getLatitude() != null ? getLatitude() : "0"));
        centersLocation.setLongitude(Double.parseDouble(getLongitude() != null ? getLongitude() : "0"));
        return centersLocation.distanceTo(location);
    }

    @Override
    public int compareTo(@NonNull Centers centers) {

        if (getDistance() > centers.getDistance()) {
            return 1;
        }
        else if (getDistance() <  centers.getDistance()) {
            return -1;
        }
        else {
            return 0;
        }
    }
}
