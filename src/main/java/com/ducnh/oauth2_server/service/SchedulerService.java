package com.ducnh.oauth2_server.service;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ducnh.oauth2_server.model.RegisterEvent;
import com.ducnh.oauth2_server.model.StravaEvent;

@Service
public class SchedulerService {
    @Autowired
    private ActivityService activityService;

    @Value("${strava.url.athlete.activities}")
	private String activitiesUrl;

	@Value("${strava.url.activities.lap}")
	private String lapUrl;

    @Autowired
    private EventService eventService;

    @Autowired
    private RegisterService registerService;

    public String getDataActivity() {
        StravaEvent event = eventService.findExactCurrentEvent().orElse(null);
        if (event == null) {
            return "No current event found.";   
        }
        Iterable<RegisterEvent> registerEvents = registerService.findAllByEventId(event.getId());
        AtomicInteger count = new AtomicInteger(0);
        for (RegisterEvent registered : registerEvents) {
            try {
                Long id = registered.getAthleteId();
                activityService.saveActivitiesFromStravaResponse(id);
                count.incrementAndGet();
            } catch (Exception e) {
                System.out.println("Error fetching activities for athlete ID: " + registered.getAthleteId() + " - " + e.getMessage());  
            }
        }
        return "Successfully fetched activities for " + count.get() + " athletes.";
    }
}
