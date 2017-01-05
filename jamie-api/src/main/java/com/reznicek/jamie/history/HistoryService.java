package com.reznicek.jamie.history;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HistoryService {

	@Autowired
	private HistoryRepository historyRepository;

	public HistoryDBO findHistory(Integer id) {
		return historyRepository.findOne(id);
	}

	public HistoryDBO findHistoryByDate(LocalDate date) {
		return historyRepository.findOneByDate(date);
	}

	public HistoryDBO createHistory(HistoryDBO historyDBO) {
		historyDBO.setId(null);
		if (historyDBO.getDate() == null) {
			historyDBO.setDate(LocalDate.now());
		}
		return historyRepository.save(historyDBO);
	}

	public HistoryDBO updateHistory(HistoryDBO historyDBO) {
		if (historyDBO.getDate() == null) {
			historyDBO.setDate(LocalDate.now());
		}
		return historyRepository.save(historyDBO);
	}
}
