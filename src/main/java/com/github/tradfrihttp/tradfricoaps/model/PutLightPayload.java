package com.github.tradfrihttp.tradfricoaps.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class PutLightPayload {

    @JsonProperty("3311")
    private List<DeviceValuesIkea> deviceValuesIkea;

    public PutLightPayload() {

    }

    public PutLightPayload(boolean powerOn, int dimmer) {
        deviceValuesIkea = new ArrayList<>();
        DeviceValuesIkea values = new DeviceValuesIkea();
        values.setPowerState(powerOn ? 1 : 0);
        values.setDimmer(dimmer);
        deviceValuesIkea.add(values);
    }

    public List<DeviceValuesIkea> getDeviceValuesIkea() {
        return deviceValuesIkea;
    }

    public void setDeviceValuesIkea(List<DeviceValuesIkea> deviceValuesIkea) {
        this.deviceValuesIkea = deviceValuesIkea;
    }
}
