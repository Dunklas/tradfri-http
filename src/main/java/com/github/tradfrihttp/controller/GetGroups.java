package com.github.tradfrihttp.controller;

import com.github.tradfrihttp.model.LightGroup;
import com.github.tradfrihttp.tradfricoaps.TradfriCoapsApi;
import com.github.tradfrihttp.tradfricoaps.exceptions.TradfriCoapsApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class GetGroups {

    @Autowired
    private TradfriCoapsApi coapApi;

    @GetMapping("/groups")
    @ResponseBody
    public List<LightGroup> groups() throws TradfriCoapsApiException {
        return coapApi.getGroups();
    }

}
