package com.github.tradfrihttp.tradfricoaps;

import com.github.tradfrihttp.model.LightBulb;
import com.github.tradfrihttp.model.LightGroup;
import com.github.tradfrihttp.tradfricoaps.exceptions.TradfriCoapsApiException;

import java.util.List;

public interface TradfriCoapsApi {

    List<LightGroup> getGroups() throws TradfriCoapsApiException;
    LightGroup getGroup(int groupId) throws TradfriCoapsApiException;
    LightGroup setLightStateOfGroup(int groupId, boolean powerOn, int dimmer) throws TradfriCoapsApiException;
    LightBulb getLight(int lightId) throws TradfriCoapsApiException;
    LightBulb putLight(int lightId, boolean powerOn, int dimmer) throws TradfriCoapsApiException;
}
