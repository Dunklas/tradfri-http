package com.github.tradfrihttp.tradfricoaps;

import com.github.tradfrihttp.model.LightGroup;

import java.util.List;

public interface TradfriCoapsApi {

    List<LightGroup> getGroups();
    LightGroup getGroup(int groupId);

}
