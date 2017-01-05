package com.reznicek.jamie.dailypreference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.reznicek.jamie.liking.LikingDBO;
import com.reznicek.jamie.restaurant.Restaurant;

@RestController
@RequestMapping("/dailypreferences")
public class DailyPreferenceController {

	@Autowired
	private DailyPreferenceService dailyPreferenceService;
	@Autowired
	private Mapper mapper;

	@RequestMapping(method = RequestMethod.GET)
	public List<Restaurant> calculateDailyPreferences(@RequestParam String developers) {
		List<String> developersIds = Arrays.asList(developers.split(","));
		List<LikingDBO> calculatedDailyPreferences = dailyPreferenceService.calculateDailyPreferences(developersIds);

		List<Restaurant> restaurants = new ArrayList<>();
		for (LikingDBO likingDBO : calculatedDailyPreferences) {
			restaurants.add(mapper.map(likingDBO.getRestaurant(), Restaurant.class));
		}
		return restaurants;
	}
}
