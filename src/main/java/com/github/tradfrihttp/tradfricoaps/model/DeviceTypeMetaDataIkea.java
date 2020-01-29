package com.github.tradfrihttp.tradfricoaps.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;

public class DeviceTypeMetaDataIkea {

    private String vendorName;
    private String deviceTypeName;
    private String deviceTypeId;
    private String unknownThing;
    private String otherUnknownThing;

    @JsonSetter("0")
    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    @JsonSetter("1")
    public void setDeviceTypeName(String deviceTypeName) {
        this.deviceTypeName = deviceTypeName;
    }

    @JsonSetter("3")
    public void setDeviceTypeId(String deviceTypeId) {
        this.deviceTypeId = deviceTypeId;
    }

    @JsonSetter("2")
    public void setUnknownThing(String unknownThing) {
        this.unknownThing = unknownThing;
    }

    @JsonSetter("6")
    public void setOtherUnknownThing(String otherUnknownThing) {
        this.otherUnknownThing = otherUnknownThing;
    }

    public String getVendorName() {
        return vendorName;
    }

    public String getDeviceTypeName() {
        return deviceTypeName;
    }

    public String getDeviceTypeId() {
        return deviceTypeId;
    }

    public String getUnknownThing() {
        return unknownThing;
    }

    public String getOtherUnknownThing() {
        return otherUnknownThing;
    }
}
