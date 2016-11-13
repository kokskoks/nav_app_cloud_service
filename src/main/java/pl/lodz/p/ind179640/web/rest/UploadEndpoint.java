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
import pl.lodz.p.ind179640.service.parser.universal.BuildingsParser;

@RestController
public class UploadEndpoint {
	
	 private final Logger log = LoggerFactory.getLogger(UploadEndpoint.class);
	
	private final ParsersDispatcher parsersDispatcher;
	
	@Autowired
	public UploadEndpoint(ParsersDispatcher parsersDispatcher) {
		this.parsersDispatcher = parsersDispatcher;
	}
	
	
	@RequestMapping(path="/upload/plan/{dept}",method=RequestMethod.POST)
	public void uploadPlan(@RequestParam MultipartFile plan, @PathVariable String dept) throws IOException, ParserNotFoundException{
		log.debug("Plan file uploaded!");
		byte[] bytes = plan.getBytes();
		parsersDispatcher.parse(bytes, dept);
		
	}
	
	@RequestMapping(path="/upload/buildings",method=RequestMethod.POST)
	public void uploadBuilding(@RequestParam MultipartFile buildings) throws IOException, ParserNotFoundException{
		log.debug("Building file uploaded!");
		byte[] bytes = buildings.getBytes();
		parsersDispatcher.parse(bytes, BuildingsParser.PARSER_NAME);
		
	}
	
	
	



}
