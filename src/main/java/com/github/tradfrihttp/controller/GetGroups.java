package com.github.tradfrihttp.controller;

import com.github.tradfrihttp.model.LightGroup;
import com.github.tradfrihttp.tradfricoaps.TradfriCoapApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GetGroups {

    @Autowired
    private TradfriCoapApi coapApi;

    @GetMapping("/groups")
    @ResponseBody
    public List<LightGroup> lights() {
        return coapApi.getGroups();
    }

}
