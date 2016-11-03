package pl.lodz.p.ind179640.service.parser.weeia;

import java.io.ByteArrayInputStream;
import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import pl.lodz.p.ind179640.domain.UniversityClass;
import pl.lodz.p.ind179640.repository.BuildingRepository;
import pl.lodz.p.ind179640.repository.ClassroomRepository;
import pl.lodz.p.ind179640.repository.LecturerRepository;
import pl.lodz.p.ind179640.repository.UniversityClassRepository;
import pl.lodz.p.ind179640.repository.UniversityGroupRepository;
import pl.lodz.p.ind179640.repository.WeekRepository;
import pl.lodz.p.ind179640.service.parser.Parser;

@Component(value = "weeia")
@Transactional
public class WeeiaParser implements Parser {

	private static final String COLUMN_DELIMETER = "\t";
	
	private static final int EVENT_CAT = 0;
	private static final int WEIGHTNING = 1;
	private static final int MODULE = 2;
	private static final int MOD_CODE = 3;
	private static final int ROOM = 4;
	private static final int SURNAME = 5;
	private static final int FORENAME = 6;
	private static final int STAFF_CODE = 7;
	private static final int GROUP = 8;
	private static final int STUDENT = 9;
	private static final int STUD_CODE = 10;
	private static final int PROTECTED = 11;
	private static final int GLOBAL = 12;
	private static final int DESCRIPTION = 13;
	private static final int DATE_CHANGED = 14;
	
	private final UniversityClassRepository universityClassRepo;
	private final UniversityGroupRepository universityGroupRepo;
	private final ClassroomRepository classroomRepo;
	private final BuildingRepository buildingRepo;
	private final LecturerRepository lecturerRepo;
	private final WeekRepository weekRepo;
	
	

	@Autowired
	public WeeiaParser(UniversityClassRepository universityClassRepo, UniversityGroupRepository universityGroupRepo,
			ClassroomRepository classroomRepo, BuildingRepository buildingRepo, LecturerRepository lecturerRepo,
			WeekRepository weekRepo) {
		super();
		this.universityClassRepo = universityClassRepo;
		this.universityGroupRepo = universityGroupRepo;
		this.classroomRepo = classroomRepo;
		this.buildingRepo = buildingRepo;
		this.lecturerRepo = lecturerRepo;
		this.weekRepo = weekRepo;
	}

	@Override
	public void parse(byte[] planBytes) {
		
		Scanner planScanner = new Scanner(new ByteArrayInputStream(planBytes));
		
		//ommit header line
		planScanner.nextLine();
		
		while(planScanner.hasNextLine()){
			String line = planScanner.nextLine();
			parseLine(line);
		}
		
		
	}

	protected void parseLine(String line) {
		String[] columnValues = line.split(COLUMN_DELIMETER);
		
		String moduleCode = columnValues[MOD_CODE];
		String moduleName = columnValues[MODULE];
		
		UniversityClass uniClass = parseUniversityClass(moduleCode, moduleName);
		
	}

	private UniversityClass parseUniversityClass(String moduleCode, String moduleName) {

		return null;
	}

}
