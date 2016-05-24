package com.smap.f16.grp12.racketometer.models;

import android.location.Location;

import org.joda.time.DateTime;

/**
 * Holds data from a given performance Session
 */
public class Session {
    private long id;
    private final DateTime date;
    private final long userId;
    private final String description;
    private final int hits;
    private final double speed;
    private final double power;
    private final double agility;
    private final Location location;

    public Session(long id, DateTime date, long userId, String description, int hits, double speed, double power, double agility, double latitude, double longitude) {
        this.id = id;
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

    public Session(DateTime date, long userId, String description, int hits, double speed, double power, double agility, double latitude, double longitude) {
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
        return speed;
    }

    public double getPower() {
        return power;
    }

    public double getAgility() {
        return agility;
    }

    public Location getLocation() {
        return location;
    }

    public double getLatitude() {
        return location.getLatitude();
    }

    public double getLongitude() {
        return location.getLongitude();
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
                "Location:    " + getLocation().toString();
    }
}
