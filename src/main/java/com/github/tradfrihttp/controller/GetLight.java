package com.github.tradfrihttp.controller;

import com.github.tradfrihttp.model.LightBulb;
import com.github.tradfrihttp.tradfricoaps.TradfriCoapsApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GetLight {

    @Autowired
    private TradfriCoapsApi coapApi;

    @GetMapping("/lights/{id}")
    @ResponseBody
    public LightBulb light(@PathVariable Integer id) {
        return coapApi.getLight(id);
    }

}