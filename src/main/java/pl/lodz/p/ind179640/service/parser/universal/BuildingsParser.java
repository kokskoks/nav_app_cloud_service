package pl.lodz.p.ind179640.service.parser.universal;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import org.hibernate.metamodel.source.annotations.attribute.ColumnValues;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import pl.lodz.p.ind179640.domain.Building;
import pl.lodz.p.ind179640.domain.Sublocation;
import pl.lodz.p.ind179640.repository.BuildingRepository;
import pl.lodz.p.ind179640.repository.SublocationRepository;
import pl.lodz.p.ind179640.service.parser.Parser;

@Component(BuildingsParser.PARSER_NAME)
@Transactional
public class BuildingsParser implements Parser {
	
	private static final Logger log = LoggerFactory.getLogger(BuildingsParser.class);
	
	public static final String PARSER_NAME = "buildings";
	
	private final BuildingRepository buildingRepo;

	private final SublocationRepository sublocationRepo;
	
	private final static String COLUMN_DELIMETER = ";";
	
	private static final int OBJECT_NAME = 0;
	private static final int OBJECT_CODE = 1;
	private static final int OBJECT_ADDRESS = 2;
	private static final int OBJECT_LONGITUDE = 3;
	private static final int OBJECT_LATITUDE = 4;
	private static final int OBJECT_DESCRIPTION = 5;
	private static final int SUB_LOC_CODE = 6;
	
	
	
	@Autowired
	public BuildingsParser(BuildingRepository buildingRepo, SublocationRepository sublocationRepo) {
		super();
		this.buildingRepo = buildingRepo;
		this.sublocationRepo = sublocationRepo;
	}

	@Override
	public void parse(byte[] bytes) {
		
		try(Scanner buildingsScanner = new Scanner(new ByteArrayInputStream(bytes), StandardCharsets.UTF_8.name())){
			
			//ommit header line
			buildingsScanner.nextLine();
			
			while(buildingsScanner.hasNextLine()){
				String line = buildingsScanner.nextLine();
				parseLine(line);
			}
		}
		
	}

	private void parseLine(String line) {
		
		String[] columnValues = line.split(COLUMN_DELIMETER);
		
		String name = columnValues[OBJECT_NAME];
		
		String code = columnValues[OBJECT_CODE];
		
		String address = columnValues[OBJECT_ADDRESS];
		
		String longitude = columnValues[OBJECT_LONGITUDE];
		
		String latitude = columnValues[OBJECT_LATITUDE];
		
		String description = columnValues[OBJECT_DESCRIPTION];
		
		String subLocCode = columnValues[SUB_LOC_CODE];
		
		parseBuilding(name, code, address, longitude, latitude, description, subLocCode);
		
		
	}

	private void parseBuilding(String name, String code, String address, String longitude, String latitude,
			String description, String subLocCode) {
		
		Building building = new Building();
		building.setCode(code);
		
		Example<Building> buildingExample = Example.of(building);
		Building buildingResult = buildingRepo.findOne(buildingExample);
		
		if(buildingResult != null){
			building = buildingResult;
		} else {
			building = buildingRepo.save(building);
		}
		
		Double longitudeDouble = parseStringDouble(longitude);
		
		Double latitudeDouble = parseStringDouble(latitude);
		
		Sublocation sublocation = parseSublocation(name, subLocCode);
		
		building.addSublocations(sublocation);
		
		
		
		building.setLatitude(latitudeDouble);
		
		building.setLongitude(longitudeDouble);
		
		building.setStreet(address);
		
		building.setDescription(description);
	}

	private Sublocation parseSublocation(String name, String subLocCode) {
		Sublocation sublocation = new Sublocation();
		sublocation.setCode(Integer.valueOf(subLocCode));
		
		Example<Sublocation> sublocationExample = Example.of(sublocation);
		Sublocation sublocationResult = sublocationRepo.findOne(sublocationExample);
		
		if(sublocationResult != null){
			sublocation = sublocationResult;
		} else {
			sublocation = sublocationRepo.save(sublocation);
		}
		
		sublocation.setName(name);
		return sublocation;
	}

	private Double parseStringDouble(String doubleString) {
		
		Double result = 0.0;
		
		try {
			result = Double.valueOf(doubleString);
		} catch(NumberFormatException nfe){
			log.error("couldnt parse double value {} - inproper format {}", doubleString, nfe);
		}
		
		return result;
	}
}
