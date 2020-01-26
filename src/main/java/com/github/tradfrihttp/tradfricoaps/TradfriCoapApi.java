package com.github.tradfrihttp.tradfricoaps;

import com.github.tradfrihttp.model.LightGroup;

import java.util.List;

public interface TradfriCoapApi {

    List<LightGroup> getGroups();
    LightGroup getGroup(long groupId);

}
