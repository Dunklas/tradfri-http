package com.github.tradfrihttp.tradfricoaps.model;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.github.tradfrihttp.model.LightBulb;

import java.util.List;

public class LightBulbIkea {

    private int lightId;
    private String lightName;
    private long someTimestamp;
    private long someOtherTimestamp;
    private int unknownThing;
    private int otherUnknownThing;
    private int applicationType;
    private DeviceTypeMetaDataIkea deviceTypeMetaData;
    private List<DeviceValuesIkea> deviceValues;

    @JsonSetter("9003")
    public void setLightId(int lightId) {
        this.lightId = lightId;
    }

    @JsonSetter("9001")
    public void setLightName(String lightName) {
        this.lightName = lightName;
    }

    @JsonSetter("9002")
    public void setSomeTimestamp(long someTimestamp) { this.someTimestamp = someTimestamp; }

    @JsonSetter("9020")
    public void setSomeOtherTimestamp(long someOtherTimestamp) { this.someOtherTimestamp = someOtherTimestamp; }

    @JsonSetter("9019")
    public void setUnknownThing(int unknownThing) { this.unknownThing = unknownThing; }

    @JsonSetter("9054")
    public void setOtherUnknownThing(int otherUnknownThing) { this.otherUnknownThing = otherUnknownThing; }

    @JsonSetter("5750")
    public void setApplicationType(int applicationType) { this.applicationType = applicationType; }

    @JsonSetter("3")
    public void setDeviceTypeMetaData(DeviceTypeMetaDataIkea deviceTypeMetaData) {
        this.deviceTypeMetaData = deviceTypeMetaData;
    }

    @JsonSetter("3311")
    public void setDeviceValues(List<DeviceValuesIkea> deviceValues) {
        this.deviceValues = deviceValues;
    }

    public LightBulb toLightBulb() {
        return new LightBulb.Builder()
                .setId(this.lightId)
                .setName(this.lightName)
                .setDeviceTypeId(this.deviceTypeMetaData.getDeviceTypeId())
                .setDeviceTypeName(this.deviceTypeMetaData.getDeviceTypeName())
                .setPowerOn(this.deviceValues.get(0).getPowerState() == 1 ? true : false)
                .setDimmer(this.deviceValues.get(0).getDimmer())
                .setxColor(this.deviceValues.get(0).getxColor())
                .setyColor(this.deviceValues.get(0).getyColor())
                .setHexColor(this.deviceValues.get(0).getHexColor())
                .build();
    }
}
