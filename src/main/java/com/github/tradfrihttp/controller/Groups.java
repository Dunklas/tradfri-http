package com.github.tradfrihttp.controller;

import com.github.tradfrihttp.model.LightGroup;
import com.github.tradfrihttp.tradfricoaps.TradfriCoapsApi;
import com.github.tradfrihttp.tradfricoaps.exceptions.TradfriCoapsApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class Groups {

    @Autowired
    private TradfriCoapsApi coapApi;

    @GetMapping("/groups")
    @ResponseBody
    public ResponseEntity<List<LightGroup>> groups() {
        try {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(coapApi.getGroups());
        } catch (TradfriCoapsApiException tcai) {
            return ResponseEntity
                    .status(tcai.getHttpStatus())
                    .build();
        }
    }

    @GetMapping("/groups/{id}")
    @ResponseBody
    public ResponseEntity<LightGroup> group(@PathVariable Integer id) {
        try {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(coapApi.getGroup(id));
        } catch (TradfriCoapsApiException tcai) {
            return ResponseEntity
                    .status(tcai.getHttpStatus())
                    .build();
        }
    }

}
