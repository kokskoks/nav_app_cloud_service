package pl.lodz.p.ind179640.service.parser.weeia;

import java.io.ByteArrayInputStream;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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

	private static final int START_HOUR_INDEX = 0;

	private static final String HOURS_DELIMETER = "-";

	private static final String COLUMN_DELIMETER = "\t";
	
	private static final int END_HOUR_INDEX = 1;

	private static final int DAY = 1;
	private static final int TIME = 2;
	private static final int WEEKS = 3;
	private static final int EVENT_CAT = 4;
	private static final int WEIGHTNING = 5;
	private static final int MODULE = 6;
	private static final int MOD_CODE = 7;
	private static final int ROOM = 8;
	private static final int SURNAME = 9;
	private static final int FORENAME = 10;
	private static final int STAFF_CODE = 11;
	private static final int GROUP = 12;
	private static final int STUDENT = 13;
	private static final int STUD_CODE = 14;
	private static final int PROTECTED = 15;
	private static final int GLOBAL = 16;
	private static final int DESCRIPTION = 17;
	private static final int DATE_CHANGED = 18;

	
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
		String hours = columnValues[TIME];
		String day = columnValues[DAY];
		String classType = columnValues[EVENT_CAT];
		String classroomName = columnValues[ROOM];
		String lecturerName = columnValues[SURNAME];
		String weeks = columnValues[WEEKS];
		String uniGroup = columnValues[GROUP];
		
		UniversityClass uniClass = parseUniversityClass(moduleCode, moduleName, hours, day, classType);
		
		if(classroomName != null && !classroomName.isEmpty()){
			Classroom classroom = parseClassRoom(classroomName);
			uniClass.setClassroom(classroom);
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

	private UniversityClass parseUniversityClass(String moduleCode, String moduleName, String hours, String day, String classType) {
		
		UniversityClass uniClass = new UniversityClass();
		uniClass.setModuleCode(moduleCode);
		
		Example<UniversityClass> uniClassExample = Example.of(uniClass);
		UniversityClass universityClass = universityClassRepo.findOne(uniClassExample);
		
		if(universityClass == null) {
			uniClass.setName(moduleName);
			
			if(hours != null){
				parseTime(uniClass, hours);
			}
			if(day != null){
				parseDay(uniClass, day);
			}
			if(classType != null){
				parseClassType(uniClass, classType);
			}
			
			uniClass = universityClassRepo.save(uniClass);

		} else {
			uniClass = universityClass;
		}
		
		
		return uniClass;
	}

	private void parseClassType(UniversityClass uniClass, String classType) {
		switch(classType.toLowerCase()){
		case "l":
			uniClass.setType(ClassType.LABORATORY);
			break;
		case "w":
			uniClass.setType(ClassType.LECTURE);
			break;
		case "sem":
			uniClass.setType(ClassType.SEMINARY);
			break;
		case "c":
			uniClass.setType(ClassType.EXERCISE);
			break;
		case "sport":
			uniClass.setType(ClassType.SPORT);
			break;
		case "lekt":
			uniClass.setType(ClassType.LANGUAGE);
			break;
		default:
			uniClass.setType(ClassType.OTHER);
		}
		
	}

	private void parseDay(UniversityClass uniClass, String day) {
		switch(day.toLowerCase()){
		case "pn":
			uniClass.setWeekday(Weekday.MONDAY);
			break;
		case "wt":
			uniClass.setWeekday(Weekday.TUESDAY);
			break;
		case "œr":
			uniClass.setWeekday(Weekday.WEDNESDAY);
			break;
		case "czw":
			uniClass.setWeekday(Weekday.THURSDAY);
			break;
		case "pt":
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
