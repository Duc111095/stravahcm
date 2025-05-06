package com.ducnh.oauth2_server.controller;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ducnh.oauth2_server.dto.DetailsResultDTO;
import com.ducnh.oauth2_server.dto.SummaryEventDTO;
import com.ducnh.oauth2_server.model.StravaEvent;
import com.ducnh.oauth2_server.service.ActivityService;
import com.ducnh.oauth2_server.service.EventService;

@Controller
@RequestMapping("")
public class SummaryController {
	
	@Autowired
	private ActivityService activityService;

	@Autowired
	private EventService eventService;

	@GetMapping("/summary")
	public String getTeamSummary(Model model) {
		Iterable<StravaEvent> events = eventService.findAll();
		ZoneId zoneId = ZoneId.of("Asia/Ho_Chi_Minh");
		LocalDateTime currentDate = LocalDateTime.now(zoneId);
        String formattedDate = currentDate.format(DateTimeFormatter.ofPattern("dd/MM"));
        model.addAttribute("currentDate", formattedDate);
		model.addAttribute("events", events);
		getEventSummary(events.iterator().next().getId());
		return "summary";
	}
	
	@GetMapping("/summary/team")
	public String initSummary(Model model, @Param("teamId") int teamId, @Param("eventId") String eventId) {
		List<DetailsResultDTO> detailsSummary = new ArrayList<>();
		StravaEvent event = eventService.findById(eventId).orElse(null);
		if (event == null) {
			model.addAttribute("message", "Không có sự kiện nào với id: " + eventId);
			return "detailsSummary";
		}
		List<Object[]> results = activityService.getSummaryEvent(eventId, teamId, 1);
		
		if (results.isEmpty() || results == null || results.size() == 0) {
			model.addAttribute("message", "Không có dữ liệu cho đội này");
			return "detailsSummary";
		}
		for (Object[] result : results) {
			Long athleteId = ((Number) result[0]).longValue();
			String athleteName = (String) result[2];
			int team_id = ((Number) result[1]).intValue();
			String teamName = "Đội " + team_id;
			double totalDistance = ((Number) result[3]).doubleValue();
			double totalCurrentDistance = ((Number) result[4]).doubleValue();
			int stt = ((Number) result[5]).intValue();
			double distanceAll = ((Number) result[7]).doubleValue();
			int activeDay = ((Number) result[8]).intValue();
			int exPoint = ((Number) result[9]).intValue();
			DetailsResultDTO detailResult = new DetailsResultDTO();
			
			detailResult.setAthleteId(athleteId);
			detailResult.setAthleteName(athleteName);
			detailResult.setTeamId(teamId);
			detailResult.setTotalDistance(totalDistance);
			detailResult.setTotalCurrentDistance(totalCurrentDistance);
			detailResult.setStt(stt);
			detailResult.setTeamName(teamName);
			double totalDistanceEx = totalDistance * event.getExRate();
			detailResult.setTotalDistanceEx(totalDistanceEx);
			detailResult.setDistanceAll(distanceAll);
			detailResult.setActiveDay(activeDay);
			detailResult.setExPoint(exPoint);
			detailsSummary.add(detailResult);
		}
		
		ZoneId zoneId = ZoneId.of("Asia/Ho_Chi_Minh");
		LocalDateTime currentDate = LocalDateTime.now(zoneId);
		String formattedDate = currentDate.format(DateTimeFormatter.ofPattern("dd/MM"));
        model.addAttribute("teamId", teamId);
		model.addAttribute("currentDate", formattedDate);
		model.addAttribute("detailsSummary", detailsSummary);
		model.addAttribute("teamId", teamId);
		return "detailsSummary";
	}

	@GetMapping("/summary/event")
	@ResponseBody
	public ResponseEntity<List<?>> getActivitySummary(Model model, @Param("eventId") String eventId) {
		try {
			List<SummaryEventDTO> summaryEvents = getEventSummary(eventId);
			return ResponseEntity.ok(summaryEvents);
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(null);
		}
	}

	private List<SummaryEventDTO> getEventSummary(String eventId) throws RuntimeException{
		if (eventId == null || !eventService.existsById(eventId)) {
			throw new RuntimeException("No event found with id: " + eventId);
		}
		List<SummaryEventDTO> summaryEvents = new ArrayList<>();
		StravaEvent event = eventService.findById(eventId).orElse(null);
		if (event == null) {
			throw new RuntimeException("No event found with id: " + eventId);
		}
		List<Object[]> results = activityService.getSummaryEvent(eventId, 0, 0);
		for (Object[] result : results) {
			int teamId = ((Number) result[0]).intValue();
			int totalAthlete = ((Number) result[1]).intValue();
			double totalDistance = ((Number) result[2]).doubleValue();
			double totalCurrentDistance = ((Number) result[3]).doubleValue();
			int stt = ((Number) result[4]).intValue();
			double distanceAll = ((Number) result[7]).doubleValue();
			int activeDay = ((Number) result[8]).intValue();
			int exPoint = ((Number) result[9]).intValue();
			double totalDistanceEx = totalDistance * event.getExRate();

			SummaryEventDTO summaryEvent = new SummaryEventDTO();
			summaryEvent.setEventId(eventId);
			summaryEvent.setTeamName("Đội " + teamId);
			summaryEvent.setTeamId(teamId);
			summaryEvent.setTeamCount(totalAthlete);
			summaryEvent.setTotalDistance(totalDistance);
			summaryEvent.setTotalCurrentDistance(totalCurrentDistance);
			summaryEvent.setSTT(stt);
			summaryEvent.setTotalDistanceEx(totalDistanceEx);
			summaryEvent.setDistanceAll(distanceAll);
			summaryEvent.setActiveDay(activeDay);
			summaryEvent.setExPoint(exPoint);
			summaryEvents.add(summaryEvent);
		}
		return summaryEvents;
	}
}
