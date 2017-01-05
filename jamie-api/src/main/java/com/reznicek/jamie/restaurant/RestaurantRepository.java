package com.reznicek.jamie.restaurant;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface RestaurantRepository extends CrudRepository<RestaurantDBO, Integer> {

	List<RestaurantDBO> findAllByOrderByNameAscIdAsc();
}
