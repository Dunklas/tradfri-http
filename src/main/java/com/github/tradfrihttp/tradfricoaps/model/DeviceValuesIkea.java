package com.github.tradfrihttp.tradfricoaps.model;

import com.fasterxml.jackson.annotation.JsonSetter;

public class DeviceValuesIkea {

    private int somethingWithColor;
    private int powerState;
    private int dimmer;
    private int somethingElseWithColor;
    private int xColor;
    private int yColor;
    private String hexColor;
    private int unknownThing;
    private int otherUnknownThing;
    private int yetAnotherUnknownThing;

    public int getSomethingWithColor() {
        return somethingWithColor;
    }

    @JsonSetter("5708")
    public void setSomethingWithColor(int somethingWithColor) {
        this.somethingWithColor = somethingWithColor;
    }

    public int getPowerState() {
        return powerState;
    }

    @JsonSetter("5850")
    public void setPowerState(int powerState) {
        this.powerState = powerState;
    }

    public int getDimmer() {
        return dimmer;
    }

    @JsonSetter("5851")
    public void setDimmer(int dimmer) {
        this.dimmer = dimmer;
    }

    public int getSomethingElseWithColor() {
        return somethingElseWithColor;
    }

    @JsonSetter("5707")
    public void setSomethingElseWithColor(int somethingElseWithColor) {
        this.somethingElseWithColor = somethingElseWithColor;
    }

    public int getxColor() {
        return xColor;
    }

    @JsonSetter("5709")
    public void setxColor(int xColor) {
        this.xColor = xColor;
    }

    public int getyColor() {
        return yColor;
    }

    @JsonSetter("5710")
    public void setyColor(int yColor) {
        this.yColor = yColor;
    }

    public String getHexColor() {
        return hexColor;
    }

    @JsonSetter("5706")
    public void setHexColor(String hexColor) {
        this.hexColor = hexColor;
    }

    public int getUnknownThing() {
        return unknownThing;
    }

    @JsonSetter("9003")
    public void setUnknownThing(int unknownThing) {
        this.unknownThing = unknownThing;
    }

    public int getOtherUnknownThing() {
        return otherUnknownThing;
    }

    @JsonSetter("5717")
    public void setOtherUnknownThing(int otherUnknownThing) {
        this.otherUnknownThing = otherUnknownThing;
    }

    public int getYetAnotherUnknownThing() {
        return yetAnotherUnknownThing;
    }

    @JsonSetter("5711")
    public void setYetAnotherUnknownThing(int yetAnotherUnknownThing) {
        this.yetAnotherUnknownThing = yetAnotherUnknownThing;
    }

}
