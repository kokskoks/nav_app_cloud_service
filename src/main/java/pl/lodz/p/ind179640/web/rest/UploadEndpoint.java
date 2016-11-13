package pl.lodz.p.ind179640.web.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import pl.lodz.p.ind179640.service.PlanParserService;
import pl.lodz.p.ind179640.service.parser.ParserNotFoundException;
import pl.lodz.p.ind179640.service.parser.ParsersDispatcher;

@RestController
public class UploadEndpoint {
	
	 private final Logger log = LoggerFactory.getLogger(UploadEndpoint.class);
	
	private final ParsersDispatcher planParsersDispatcher;
	
	@Autowired
	public UploadEndpoint(ParsersDispatcher planParsersDispatcher) {
		this.planParsersDispatcher = planParsersDispatcher;
	}
	
	
	@RequestMapping(path="/upload/plan/{dept}",method=RequestMethod.POST)
	public void uploadPlan(@RequestParam MultipartFile plan, @PathVariable String dept) throws IOException, ParserNotFoundException{
		log.debug("Plan file uploaded!");
		byte[] bytes = plan.getBytes();
		planParsersDispatcher.parse(bytes, dept);
		
	}
	

	
	



}
