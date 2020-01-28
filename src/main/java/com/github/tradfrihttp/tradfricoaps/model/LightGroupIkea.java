package com.github.tradfrihttp.tradfricoaps.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.github.tradfrihttp.model.LightGroup;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties({"5851", "5850", "9039", "9108"})
public class LightGroupIkea {

    private int groupId;
    private String groupName;
    private Long someTimestamp; // Might be 'createdDate', or 'connectedDate'
    private List<Integer> lightIds;

    @JsonSetter("9003")
    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    @JsonSetter("9001")
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    @JsonSetter("9002")
    public void setSomeTimestamp(Long someTimestamp) {
        this.someTimestamp = someTimestamp;
    }

    @JsonSetter("9018")
    public void setLightIds(Map<String, Object> parentValue) {
        Map<String, Object> nestedProperty = (Map<String, Object>) parentValue.get("15002");
        List<Integer> lightIds = (List<Integer>) nestedProperty.get("9003");
        this.lightIds = lightIds;
    }

    public LightGroup toLightGroup() {
        return new LightGroup.Builder()
                .setId(this.groupId)
                .setName(this.groupName)
                .setLightBulbs(this.lightIds)
                .build();
    }
}
