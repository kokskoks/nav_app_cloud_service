package pl.lodz.p.ind179640.service.parser.weeia;

import java.io.ByteArrayInputStream;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import pl.lodz.p.ind179640.domain.Building;
import pl.lodz.p.ind179640.domain.Classroom;
import pl.lodz.p.ind179640.domain.Lecturer;
import pl.lodz.p.ind179640.domain.UniversityClass;
import pl.lodz.p.ind179640.domain.UniversityGroup;
import pl.lodz.p.ind179640.domain.Week;
import pl.lodz.p.ind179640.domain.enumeration.ClassType;
import pl.lodz.p.ind179640.domain.enumeration.Weekday;
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


	private static final String COLUMN_DELIMETER = ";";
	

	private static final int SUBJECT = 0;
	private static final int SEMESTER = 1;
	private static final int GROUP = 2;
	private static final int MODULE = 3;
	private static final int MODULE_TYPE = 4;
	private static final int BUILDING = 5;
	private static final int ROOM = 6;
	private static final int DAY = 7;
	private static final int START_TIME = 8;
	private static final int END_TIME = 9;
	private static final int LECTURER_NAME = 10;
	private static final int WEEKS = 11;


	private static final String HOURS_DELIMETER = "-";


	private static final int START_HOUR_INDEX = 0;


	private static final int END_HOUR_INDEX = 1;

	
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
		
		try(Scanner planScanner = new Scanner(new ByteArrayInputStream(planBytes))){
		
			//ommit header line
			planScanner.nextLine();
			
			while(planScanner.hasNextLine()){
				String line = planScanner.nextLine();
				parseLine(line);
			}
		}
		
		
	}

	protected void parseLine(String line) {
		String[] columnValues = line.split(COLUMN_DELIMETER);
		
		String subject = columnValues[SUBJECT];
		String semester = columnValues[SEMESTER];
		String startHour = columnValues[START_TIME];
		String endHour = columnValues[END_TIME];
		String day = columnValues[DAY];
		String module = columnValues[MODULE];
		String moduleType = columnValues[MODULE_TYPE];
		String classroomCode = columnValues[ROOM];
		String lecturerName = columnValues[LECTURER_NAME];
		String weeks = columnValues[WEEKS];
		String uniGroup = columnValues[GROUP];
		String buildingCode = columnValues[BUILDING];
		
		UniversityClass uniClass = parseUniversityClass(subject, moduleType, startHour, endHour, day);
		

		
		if(classroomCode != null && !classroomCode.isEmpty()){
			Classroom classroom = parseClassRoom(classroomCode);
			uniClass.setClassroom(classroom);
		}
		
		if(buildingCode != null && !buildingCode.isEmpty()){
			Building building = parseBuilding(buildingCode);
			uniClass.getClassroom().setBuilding(building);
			building.addRooms(uniClass.getClassroom());
		}
		
		if(lecturerName != null && !lecturerName.isEmpty()){
			Lecturer lecturer = parseLecturer(lecturerName);
			uniClass.addLecturers(lecturer);
		}
		
		if(weeks != null && !weeks.isEmpty()){
			Set<Week> weeksSet = parseWeeks(weeks);
			uniClass.setWeeks(weeksSet);
		}
		
		if(uniGroup != null && !uniGroup.isEmpty()){
			UniversityGroup universityGroup = parseUniGroup(uniGroup);
			uniClass.addGroups(universityGroup);
		}
		
		
	}

	private Building parseBuilding(String buildingCode) {
		Building building = new Building();
		building.setName(buildingCode);
		
		Example<Building> buildingExample = Example.of(building);
		Building buldingResult = buildingRepo.findOne(buildingExample);
		
		if(buldingResult != null){
			building = buldingResult;
		} else {
			building = buildingRepo.save(building);
		}
		
		return building;

	}

	private Lecturer parseLecturer(String lecturerName) {
		int separationIndex = lecturerName.lastIndexOf(" ");
		
		String surname = null;
		String name = null;
		
		if(separationIndex == -1){
			surname = lecturerName;
		} else {
			name = lecturerName.substring(separationIndex + 1);
			surname = lecturerName.substring(0, separationIndex);
		}
		
		Lecturer lecturer = new Lecturer();
		lecturer.setFirstName(name);
		lecturer.setLastName(surname);
		
		Example<Lecturer> lecturerExample = Example.of(lecturer);
		Lecturer lecturerResult = lecturerRepo.findOne(lecturerExample);
		
		if(lecturerResult != null){
			lecturer = lecturerResult;
		} else {
			lecturer = lecturerRepo.save(lecturer);
		}
		
		return lecturer;
	}

	private Set<Week> parseWeeks(String weeks) {
		
		Set<Week> weeksSet = new HashSet<>();
		
		if (weeks.contains(",")){
			String[] weeksNumbers = weeks.split(",");
			
			for (String weekNumberOrWeeksInterval : weeksNumbers) {
				
				if(weekNumberOrWeeksInterval.contains("-")) {
					String[] weeksBounds = weekNumberOrWeeksInterval.split("-");
					int lowerBound = Integer.valueOf(weeksBounds[0]);
					int upperBound = Integer.valueOf(weeksBounds[1]);
					
					for(int weekNumber = lowerBound; weekNumber <= upperBound; weekNumber++){
						weeksSet.add(parseWeek(weekNumber));
					}
				} else {
					weeksSet.add(parseWeek(Integer.valueOf(weekNumberOrWeeksInterval)));
				}
					
			}
			
			
		} else if(weeks.contains("-")) {
			String[] weeksBounds = weeks.split("-");
			int upperBound = Integer.valueOf(weeksBounds[0]);
			int lowerBound = Integer.valueOf(weeksBounds[1]);
			
			for(int weekNumber = lowerBound; weekNumber <= upperBound; weekNumber++){
				weeksSet.add(parseWeek(weekNumber));
			}
			
		}
		
		return weeksSet;
	}
	
	private Week parseWeek(int number){
		
		Week week = new Week();
		week.setNumber(number);
		
		Example<Week> weekExample = Example.of(week);
		Week weekResult = weekRepo.findOne(weekExample);
		
		if(weekResult != null){
			week = weekResult;
		} else {
			week = weekRepo.save(week);
		}
		
		return week;
		
	}

	private UniversityGroup parseUniGroup(String uniGroup) {
		UniversityGroup universityGroup = new UniversityGroup();
		universityGroup.setName(uniGroup);
		
		Example<UniversityGroup> universityGroupExample = Example.of(universityGroup);
		UniversityGroup universityGroupResult = universityGroupRepo.findOne(universityGroupExample);
		
		if(universityGroupResult != null){
			universityGroup = universityGroupResult;
		} else {
			universityGroup = universityGroupRepo.save(universityGroup);
		}
		
		return universityGroup;
	}

	private Classroom parseClassRoom(String classroomName) {
		
		Classroom classroom = new Classroom();
		classroom.setName(classroomName);
		
		Example<Classroom> classroomExample = Example.of(classroom);
		Classroom classroomResult = classroomRepo.findOne(classroomExample);
		
		if(classroomResult != null){
			classroom = classroomResult;
		} else {
			classroom = classroomRepo.save(classroom);
		}
		
		return classroom;
	}

	private UniversityClass parseUniversityClass(String subject, String moduleType, String startHour, String endHour, String day) {
		
		UniversityClass uniClass = new UniversityClass();
		uniClass.setName(subject);
		
		Example<UniversityClass> uniClassExample = Example.of(uniClass);
		UniversityClass universityClass = universityClassRepo.findOne(uniClassExample);
		
		if(universityClass == null) {
			
			if(startHour != null && endHour != null){
				
				parseTime(uniClass, startHour + HOURS_DELIMETER + endHour );
			}
			if(day != null){
				parseDay(uniClass, day);
			}
			if(moduleType != null){
				parseClassType(uniClass, moduleType);
			}
			
			uniClass = universityClassRepo.save(uniClass);

		} else {
			uniClass = universityClass;
		}
		
		
		return uniClass;
	}

	private void parseClassType(UniversityClass uniClass, String classType) {
		switch(classType.toLowerCase()){
		case "laboratorium":
			uniClass.setType(ClassType.LABORATORY);
			break;
		case "wyk³ad":
			uniClass.setType(ClassType.LECTURE);
			break;
		case "seminarium":
			uniClass.setType(ClassType.SEMINARY);
			break;
		case "æwiczenia":
			uniClass.setType(ClassType.EXERCISE);
			break;
		case "sport":
			uniClass.setType(ClassType.SPORT);
			break;
		case "lektorat":
			uniClass.setType(ClassType.LANGUAGE);
			break;
		default:
			uniClass.setType(ClassType.OTHER);
		}
		
	}

	private void parseDay(UniversityClass uniClass, String day) {
		switch(day.toLowerCase()){
		case "poniedzia³ek":
			uniClass.setWeekday(Weekday.MONDAY);
			break;
		case "wtorek":
			uniClass.setWeekday(Weekday.TUESDAY);
			break;
		case "œroda":
			uniClass.setWeekday(Weekday.WEDNESDAY);
			break;
		case "czwartek":
			uniClass.setWeekday(Weekday.THURSDAY);
			break;
		case "pi¹tek":
			uniClass.setWeekday(Weekday.FRIDAY);
			break;
		}
	}

	private void parseTime(UniversityClass uniClass, String hours) {
		String[] startEndHours = hours.split(HOURS_DELIMETER);
		
		String startHour = startEndHours[START_HOUR_INDEX];
		String endHour = startEndHours[END_HOUR_INDEX];
		
		startHour = startHour.replace(":", "");
		endHour = endHour.replace(":", "");
		
		uniClass.setStartHour(Integer.valueOf(startHour));
		uniClass.setEndHour(Integer.valueOf(endHour));
	}

}
