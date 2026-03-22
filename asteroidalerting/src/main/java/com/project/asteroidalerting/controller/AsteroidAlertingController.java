package com.project.asteroidalerting.controller;

import com.project.asteroidalerting.service.AsteroidAlertingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

// In AsteroidAlertingController.java
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/asteroid-alerting")
public class AsteroidAlertingController {
    private final AsteroidAlertingService asteroidAlertingService;

    @Autowired

    public AsteroidAlertingController(AsteroidAlertingService asteroidAlertingService) {
        this.asteroidAlertingService = asteroidAlertingService;
    }



    @PostMapping("/alert")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void alert(){
        asteroidAlertingService.alert();

    }
}
