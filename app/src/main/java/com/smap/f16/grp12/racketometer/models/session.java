package com.smap.f16.grp12.racketometer.models;

import android.location.Location;

import org.joda.time.DateTime;

import java.io.Serializable;

/**
 * Holds data from a given performance Session
 */
public class Session implements Serializable {
    private long id;
    private final DateTime date;
    private final long userId;
    private final String description;
    private final int hits;
    private final double latitude;
    private final double longitude;
    private final Attributes attributes;

    public Session(long id, DateTime date, long userId, String description, int hits, double speed, double power, double agility, double latitude, double longitude) {
        this.id = id;
        this.date = date;
        this.userId = userId;
        this.description = description;
        this.hits = hits;
        this.latitude = latitude;
        this.longitude = longitude;
        this.attributes = new Attributes(agility, power, speed);
    }

    public Session(DateTime date, long userId, String description, int hits, double speed, double power, double agility, double latitude, double longitude) {
        this.date = date;
        this.userId = userId;
        this.description = description;
        this.hits = hits;
        this.latitude = latitude;
        this.longitude = longitude;
        this.attributes = new Attributes(agility, power, speed);
    }

    public long getId() {
        return id;
    }

    public DateTime getDate() {
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

    public double getSpeed() {
        return attributes.getSpeed();
    }

    public double getPower() {
        return attributes.getPower();
    }

    public double getAgility() {
        return attributes.getAgility();
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public String dump() {
        return "\n" +
                "ID:          " + getId() + "\n" +
                "Date:        " + getDate() + "\n" +
                "UserId:      " + getUserId() + "\n" +
                "Description: " + getDescription() + "\n" +
                "Hits:        " + getHits() + "\n" +
                "Speed:       " + getSpeed() + "\n" +
                "Power:       " + getPower() + "\n" +
                "Agility:     " + getAgility() + "\n" +
                "Location:    " + getLatitude() + " " + getLongitude();
    }
}
