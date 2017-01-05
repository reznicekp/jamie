package com.reznicek.jamie.developer;

import java.util.List;
import java.util.Set;

import org.springframework.data.repository.CrudRepository;

public interface DeveloperRepository extends CrudRepository<DeveloperDBO, Integer> {

	List<DeveloperDBO> findAllByOrderByNameAscIdAsc();

	List<DeveloperDBO> findByIdIn(Set<Integer> ids);
}
