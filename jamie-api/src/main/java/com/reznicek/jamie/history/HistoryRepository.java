package com.reznicek.jamie.history;

import java.time.LocalDate;

import org.springframework.data.repository.CrudRepository;

public interface HistoryRepository extends CrudRepository<HistoryDBO, Integer> {

	HistoryDBO findOneByDate(LocalDate date);
}
