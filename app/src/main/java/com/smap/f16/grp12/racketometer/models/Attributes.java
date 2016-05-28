package com.smap.f16.grp12.racketometer.models;

import java.io.Serializable;

/**
 * Attributes model to hold a performance attribute set.
 */
public class Attributes implements Serializable {
    private final double agility;
    private final double power;
    private final double speed;

    public Attributes(double agility, double power, double speed) {
        this.agility = agility;
        this.power = power;
        this.speed = speed;
    }

    public double getAgility() {
        return agility;
    }

    public double getPower() {
        return power;
    }

    public double getSpeed() {
        return speed;
    }
}
