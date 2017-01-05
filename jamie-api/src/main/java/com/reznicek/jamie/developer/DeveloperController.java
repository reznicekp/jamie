package com.reznicek.jamie.developer;

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
@RequestMapping("/developers")
public class DeveloperController {

	@Autowired
	private DeveloperService developerService;
	@Autowired
	private Mapper mapper;

	@RequestMapping(method = RequestMethod.GET)
	public List<Developer> findAllDevelopers() {
		List<DeveloperDBO> developersDBO = developerService.findAllDevelopers();

		List<Developer> developers = new ArrayList<>();
		developersDBO.forEach(developerDBO -> developers.add(mapper.map(developerDBO, Developer.class)));
		return developers;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public Developer findDeveloper(@PathVariable Integer id) {
		DeveloperDBO developerDBO = developerService.findDeveloper(id);

		if (developerDBO != null) {
			return mapper.map(developerDBO, Developer.class);
		}
		return null;
	}

	@RequestMapping(method = RequestMethod.POST)
	public Developer createDeveloper(@RequestBody Developer developer) {
		DeveloperDBO developerDBO = mapper.map(developer, DeveloperDBO.class);
		developerDBO = developerService.createDeveloper(developerDBO);
		return mapper.map(developerDBO, Developer.class);
	}

	@RequestMapping(method = RequestMethod.PUT)
	public Developer updateDeveloper(@RequestBody Developer developer) {
		DeveloperDBO developerDBO = mapper.map(developer, DeveloperDBO.class);
		developerDBO = developerService.updateDeveloper(developerDBO);
		return mapper.map(developerDBO, Developer.class);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public void deleteDeveloper(@PathVariable Integer id) {
		developerService.deleteDeveloper(id);
	}
}
