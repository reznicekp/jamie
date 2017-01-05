package com.reznicek.jamie.developer;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.reznicek.jamie.liking.LikingDBO;
import com.reznicek.jamie.restaurant.RestaurantDBO;
import com.reznicek.jamie.restaurant.RestaurantService;

@Service
public class DeveloperService {

	@Autowired
	private DeveloperRepository developerRepository;
	@Autowired
	private RestaurantService restaurantService;

	public List<DeveloperDBO> findAllDevelopers() {
		return developerRepository.findAllByOrderByNameAscIdAsc();
	}

	public List<DeveloperDBO> findAllDevelopersById(Set<Integer> ids) {
		return developerRepository.findByIdIn(ids);
	}

	public DeveloperDBO findDeveloper(Integer id) {
		return developerRepository.findOne(id);
	}

	public DeveloperDBO createDeveloper(DeveloperDBO developerDBO) {
		developerDBO.setId(null);

		developerDBO.setLikings(new ArrayList<>());
		Iterable<RestaurantDBO> restaurantsDBO = restaurantService.findAllRestaurants();
		restaurantsDBO.forEach(restaurantDBO -> {
			developerDBO.getLikings().add(new LikingDBO(developerDBO, restaurantDBO, 0));
		});

		return developerRepository.save(developerDBO);
	}

	public DeveloperDBO updateDeveloper(DeveloperDBO developerDBO) {
		DeveloperDBO existingDeveloperDBO = developerRepository.findOne(developerDBO.getId());
		existingDeveloperDBO.setName(developerDBO.getName());
		existingDeveloperDBO.setUsuallyJoins(developerDBO.isUsuallyJoins());
		return developerRepository.save(existingDeveloperDBO);
	}

	public void deleteDeveloper(Integer id) {
		developerRepository.delete(id);
	}
}
