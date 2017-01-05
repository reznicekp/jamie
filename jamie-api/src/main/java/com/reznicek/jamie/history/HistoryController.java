package com.reznicek.jamie.history;

import java.time.LocalDate;
import java.util.ArrayList;

import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.reznicek.jamie.developer.Developer;
import com.reznicek.jamie.developer.DeveloperDBO;
import com.reznicek.jamie.restaurant.Restaurant;
import com.reznicek.jamie.restaurant.RestaurantDBO;

@RestController
@RequestMapping("/history")
public class HistoryController {

	@Autowired
	private HistoryService historyService;
	@Autowired
	private Mapper mapper;

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public History findHistory(@PathVariable Integer id) {
		HistoryDBO historyDBO = historyService.findHistory(id);

		if (historyDBO != null) {
			return mapper.map(historyDBO, History.class);
		}
		return null;
	}

	@RequestMapping(method = RequestMethod.GET)
	public History findHistoryByDate(@RequestParam @DateTimeFormat(iso = ISO.DATE) LocalDate date) {
		HistoryDBO historyDBO = historyService.findHistoryByDate(date);

		if (historyDBO != null) {
			// History history = mapper.map(historyDBO, History.class);
			// this can't be done by mapper because it can't initialize LocalDate
			History history = new History();
			history.setId(historyDBO.getId());
			history.setDate(historyDBO.getDate());
			history.setRestaurant(mapper.map(historyDBO.getRestaurant(), Restaurant.class));
			history.setDevelopers(new ArrayList<>());
			for (DeveloperDBO developerDBO : historyDBO.getDevelopers()) {
				history.getDevelopers().add(mapper.map(developerDBO, Developer.class));
			}
			return history;
		}
		return null;
	}

	@RequestMapping(method = RequestMethod.POST)
	public History createHistory(@RequestBody History history) {
		HistoryDBO historyDBO = new HistoryDBO();
		historyDBO.setDate(history.getDate());
		historyDBO.setRestaurant(mapper.map(history.getRestaurant(), RestaurantDBO.class));
		historyDBO.setDevelopers(new ArrayList<>());
		for (Developer developer : history.getDevelopers()) {
			historyDBO.getDevelopers().add(mapper.map(developer, DeveloperDBO.class));
		}

		historyDBO = historyService.createHistory(historyDBO);

		history.setId(historyDBO.getId());
		return history;
	}

	@RequestMapping(method = RequestMethod.PUT)
	public History updateHistory(@RequestBody History history) {
		HistoryDBO historyDBO = mapper.map(history, HistoryDBO.class);
		historyDBO = historyService.updateHistory(historyDBO);
		return history;
	}
}
