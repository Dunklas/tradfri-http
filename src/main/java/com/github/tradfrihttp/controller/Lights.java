package com.github.tradfrihttp.controller;

import com.github.tradfrihttp.model.LightBulb;
import com.github.tradfrihttp.model.PostLightRequest;
import com.github.tradfrihttp.tradfricoaps.TradfriCoapsApi;
import com.github.tradfrihttp.tradfricoaps.exceptions.TradfriCoapsApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class Lights {

    @Autowired
    private TradfriCoapsApi coapsApi;

    @GetMapping("/lights/{id}")
    @ResponseBody
    public ResponseEntity<LightBulb> light(@PathVariable Integer id) {
        try {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(coapsApi.getLight(id));
        } catch (TradfriCoapsApiException tcai) {
            return ResponseEntity
                    .status(tcai.getHttpStatus())
                    .build();
        }
    }

    @PostMapping("/lights/{id}")
    @ResponseBody
    public ResponseEntity<Void> light(@PathVariable Integer id, @RequestBody PostLightRequest request) {
        try {
            coapsApi.putLight(id, request.powerOn, request.dimmer);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .build();
        } catch (TradfriCoapsApiException tcai) {
            return ResponseEntity
                    .status(tcai.getHttpStatus())
                    .build();
        }
    }

}