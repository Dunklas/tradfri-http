package com.github.tradfrihttp.tradfricoaps;

import com.github.tradfrihttp.model.LightBulb;
import com.github.tradfrihttp.model.LightGroup;
import com.github.tradfrihttp.tradfricoaps.exceptions.TradfriCoapsApiException;

import java.util.List;

public interface TradfriCoapsApi {

    List<LightGroup> getGroups() throws TradfriCoapsApiException;
    LightGroup getGroup(int groupId) throws TradfriCoapsApiException;
    void setLightStateOfGroup(int groupId, boolean powerOn, int dimmer) throws TradfriCoapsApiException;
    LightBulb getLight(int lightId) throws TradfriCoapsApiException;
    void putLight(int lightId, boolean powerOn, int dimmer) throws TradfriCoapsApiException;
}
