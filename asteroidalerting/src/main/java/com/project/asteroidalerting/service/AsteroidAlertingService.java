package com.project.asteroidalerting.service;

import com.project.asteroidalerting.client.NasaClient;
import dto.Asteroid;
import event.AsteroidCollisionEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
public class AsteroidAlertingService {

    private NasaClient nasaClient;
    private final KafkaTemplate<Object, String> kafkaTemplate;


    @Autowired


    private ObjectMapper objectMapper;

    public AsteroidAlertingService (NasaClient nasaClient , KafkaTemplate<Object, String> kafkaTemplate){
        this.nasaClient = nasaClient;
        this.kafkaTemplate= kafkaTemplate;

    }

    public void alert(){
        log.info("Alerting service called");

        //From and to date
        final LocalDate fromDate= LocalDate.now();
        final LocalDate toDate = LocalDate.now().plusDays(7);

        //Call NASA API to get the asteroid data
        log.info("Getting asteroid list for dates: {}  to {} , fromDate, toDate");

        final List<Asteroid> asteroidList =  nasaClient.getNeoAsteroids(fromDate,toDate);

        log.info("Retrieved Asteroid List of size: {}" , asteroidList.size());

        final List<Asteroid> dangerousAsteroids = asteroidList.stream()
                .filter(Asteroid::isPotentiallyHazardous)
                .toList();
        log.info("Found {} hazardous asteroids", dangerousAsteroids.size());
        final List<AsteroidCollisionEvent> asteroidCollisionEventList =
                createEventLisOfDangerousAsteroids(dangerousAsteroids);

        log.info("Sending {} asteroid alerts to kafka", asteroidCollisionEventList.size() );
        asteroidCollisionEventList.forEach(event -> {
            try {
                String json = objectMapper.writeValueAsString(event);
                kafkaTemplate.send("asteroid-alert", json);  // KafkaTemplate<String, String>
                log.info("Asteroid alert sent to kafka topic: {}", event.getAsteroidName());
            } catch (Exception e) {
                log.error("Failed to send asteroid alert", e);
            }

    });
}




    private List<AsteroidCollisionEvent> createEventLisOfDangerousAsteroids(final List<Asteroid> dangerousAsteroids) {
        return dangerousAsteroids.stream()
                .map(asteroid ->{
                    if (asteroid.isPotentiallyHazardous()) {
                        return AsteroidCollisionEvent.builder()
                                .asteroidName(asteroid.getName())
                                .closeApproachDate(asteroid.getCloseApproachData().get(0).getCloseApproachDate().toString())
                                .missDistanceKilometers(asteroid.getCloseApproachData().get(0).getMissDistance().getKilometers())
                                .estimatedDiameterAvgMeters((asteroid.getEstimatedDiameter().getMeters().getMinDiameter() +
                                        asteroid.getEstimatedDiameter().getMeters().getMaxDiameter()) / 2)
                                .build();
                    }
                    return null;
                })
                .toList();
    }
}
