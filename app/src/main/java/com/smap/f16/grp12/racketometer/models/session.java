package com.smap.f16.grp12.racketometer.models;

import android.location.Location;

import java.util.Date;

/**
 * Holds data from a given performance session
 */
public class Session {
    private Date date;
    private long userId;
    private String description;
    private int hits;
    private int speed;
    private int power;
    private int agility;
    private Location location;

    public Session(Date date, long userId, String description, int hits, int speed, int power, int agility, double latitude, double longitude) {
        this.date = date;
        this.userId = userId;
        this.description = description;
        this.hits = hits;
        this.speed = speed;
        this.power = power;
        this.agility = agility;
        this.location = new Location("api");
        this.location.setLongitude(longitude);
        this.location.setLatitude(latitude);
    }

    public Date getDate() {
        return date;
    }

    public long getUserId() {
        return userId;
    }

    public String getDescription() {
        return description;
    }

    public int getHits() {
        return hits;
    }

    public int getSpeed() {
        return speed;
    }

    public int getPower() {
        return power;
    }

    public int getAgility() {
        return agility;
    }

    public Location getLocation() {
        return location;
    }
}
