package com.reznicek.jamie.liking;

import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/likings")
public class LikingController {

	@Autowired
	private LikingService likingService;
	@Autowired
	private Mapper mapper;

	@RequestMapping(method = RequestMethod.PUT)
	public Liking updateLiking(@RequestBody Liking liking) {
		LikingDBO likingDBO = mapper.map(liking, LikingDBO.class);
		likingDBO = likingService.updateLiking(likingDBO);
		return mapper.map(likingDBO, Liking.class);
	}
}
