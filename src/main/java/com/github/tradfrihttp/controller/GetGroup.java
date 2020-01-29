package com.github.tradfrihttp.controller;

import com.github.tradfrihttp.model.LightGroup;
import com.github.tradfrihttp.tradfricoaps.TradfriCoapsApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GetGroup {

    @Autowired
    private TradfriCoapsApi coapApi;

    @GetMapping("/groups/{id}")
    @ResponseBody
    public LightGroup group(@PathVariable Integer id) {
        return coapApi.getGroup(id);
    }

}
