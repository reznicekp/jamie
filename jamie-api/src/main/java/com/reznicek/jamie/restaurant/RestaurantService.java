package com.reznicek.jamie.restaurant;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reznicek.jamie.developer.DeveloperDBO;
import com.reznicek.jamie.developer.DeveloperService;
import com.reznicek.jamie.liking.LikingDBO;

@Service
public class RestaurantService {

	@Autowired
	private RestaurantRepository restaurantRepository;
	@Autowired
	private DeveloperService developerService;

	public List<RestaurantDBO> findAllRestaurants() {
		return restaurantRepository.findAllByOrderByNameAscIdAsc();
	}

	public RestaurantDBO findRestaurant(Integer id) {
		return restaurantRepository.findOne(id);
	}

	public RestaurantDBO createRestaurant(RestaurantDBO restaurantDBO) {
		restaurantDBO.setId(null);

		restaurantDBO.setLikings(new ArrayList<>());
		List<DeveloperDBO> developersDBO = developerService.findAllDevelopers();
		developersDBO.forEach(developerDBO -> {
			restaurantDBO.getLikings().add(new LikingDBO(developerDBO, restaurantDBO, 0));
		});

		return restaurantRepository.save(restaurantDBO);
	}

	public RestaurantDBO updateRestaurant(RestaurantDBO restaurantDBO) {
		return restaurantRepository.save(restaurantDBO);
	}

	public void deleteRestaurant(Integer id) {
		restaurantRepository.delete(id);
	}
}
