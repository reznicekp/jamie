package com.reznicek.jamie.restaurant;

import java.util.ArrayList;
import java.util.List;

import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/restaurants")
public class RestaurantController {

	@Autowired
	private RestaurantService restaurantService;
	@Autowired
	private Mapper mapper;

	@RequestMapping(method = RequestMethod.GET)
	public List<Restaurant> findAllRestaurants() {
		Iterable<RestaurantDBO> restaurantsDBO = restaurantService.findAllRestaurants();

		List<Restaurant> restaurants = new ArrayList<>();
		restaurantsDBO.forEach(restaurantDBO -> restaurants.add(mapper.map(restaurantDBO, Restaurant.class)));
		return restaurants;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public Restaurant findAllRestaurants(@PathVariable Integer id) {
		RestaurantDBO restaurantDBO = restaurantService.findRestaurant(id);

		if (restaurantDBO != null) {
			return mapper.map(restaurantDBO, Restaurant.class);
		}
		return null;
	}

	@RequestMapping(method = RequestMethod.POST)
	public Restaurant createRestaurant(@RequestBody Restaurant restaurant) {
		RestaurantDBO restaurantDBO = mapper.map(restaurant, RestaurantDBO.class);
		restaurantDBO = restaurantService.createRestaurant(restaurantDBO);
		return mapper.map(restaurantDBO, Restaurant.class);
	}

	@RequestMapping(method = RequestMethod.PUT)
	public Restaurant updateRestaurant(@RequestBody Restaurant restaurant) {
		RestaurantDBO restaurantDBO = mapper.map(restaurant, RestaurantDBO.class);
		restaurantDBO = restaurantService.updateRestaurant(restaurantDBO);
		return mapper.map(restaurantDBO, Restaurant.class);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void deleteRestaurant(@PathVariable Integer id) {
		restaurantService.deleteRestaurant(id);
	}
}
