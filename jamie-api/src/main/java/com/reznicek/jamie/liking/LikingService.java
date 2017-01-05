package com.reznicek.jamie.liking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LikingService {

	@Autowired
	private LikingRepository likingRepository;

	public LikingDBO updateLiking(LikingDBO likingDBO) {
		LikingDBO existingLikingDBO = likingRepository.findOne(likingDBO.getId());
		existingLikingDBO.setValue(likingDBO.getValue());
		return likingRepository.save(existingLikingDBO);
	}
}
