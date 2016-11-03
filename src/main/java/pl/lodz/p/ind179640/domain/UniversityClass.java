package pl.lodz.p.ind179640.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import pl.lodz.p.ind179640.domain.enumeration.ClassType;

import pl.lodz.p.ind179640.domain.enumeration.Weekday;

/**
 * A UniversityClass.
 */
@Entity
@Table(name = "university_class")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class UniversityClass implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "module_code")
    private String moduleCode;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private ClassType type;

    @Column(name = "start_hour")
    private Integer startHour;

    @Column(name = "end_hour")
    private Integer endHour;

    @Enumerated(EnumType.STRING)
    @Column(name = "weekday")
    private Weekday weekday;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "university_class_weeks",
               joinColumns = @JoinColumn(name="university_classes_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="weeks_id", referencedColumnName="ID"))
    private Set<Week> weeks = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "university_class_lecturers",
               joinColumns = @JoinColumn(name="university_classes_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="lecturers_id", referencedColumnName="ID"))
    private Set<Lecturer> lecturers = new HashSet<>();

    @ManyToOne
    private Classroom classroom;

    @ManyToMany(mappedBy = "classes")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<UniversityGroup> groups = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public UniversityClass name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModuleCode() {
        return moduleCode;
    }

    public UniversityClass moduleCode(String moduleCode) {
        this.moduleCode = moduleCode;
        return this;
    }

    public void setModuleCode(String moduleCode) {
        this.moduleCode = moduleCode;
    }

    public String getDescription() {
        return description;
    }

    public UniversityClass description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ClassType getType() {
        return type;
    }

    public UniversityClass type(ClassType type) {
        this.type = type;
        return this;
    }

    public void setType(ClassType type) {
        this.type = type;
    }

    public Integer getStartHour() {
        return startHour;
    }

    public UniversityClass startHour(Integer startHour) {
        this.startHour = startHour;
        return this;
    }

    public void setStartHour(Integer startHour) {
        this.startHour = startHour;
    }

    public Integer getEndHour() {
        return endHour;
    }

    public UniversityClass endHour(Integer endHour) {
        this.endHour = endHour;
        return this;
    }

    public void setEndHour(Integer endHour) {
        this.endHour = endHour;
    }

    public Weekday getWeekday() {
        return weekday;
    }

    public UniversityClass weekday(Weekday weekday) {
        this.weekday = weekday;
        return this;
    }

    public void setWeekday(Weekday weekday) {
        this.weekday = weekday;
    }

    public Set<Week> getWeeks() {
        return weeks;
    }

    public UniversityClass weeks(Set<Week> weeks) {
        this.weeks = weeks;
        return this;
    }

    public UniversityClass addWeeks(Week week) {
        weeks.add(week);
        return this;
    }

    public UniversityClass removeWeeks(Week week) {
        weeks.remove(week);
        return this;
    }

    public void setWeeks(Set<Week> weeks) {
        this.weeks = weeks;
    }

    public Set<Lecturer> getLecturers() {
        return lecturers;
    }

    public UniversityClass lecturers(Set<Lecturer> lecturers) {
        this.lecturers = lecturers;
        return this;
    }

    public UniversityClass addLecturers(Lecturer lecturer) {
        lecturers.add(lecturer);
        lecturer.getClasses().add(this);
        return this;
    }

    public UniversityClass removeLecturers(Lecturer lecturer) {
        lecturers.remove(lecturer);
        lecturer.getClasses().remove(this);
        return this;
    }

    public void setLecturers(Set<Lecturer> lecturers) {
        this.lecturers = lecturers;
    }

    public Classroom getClassroom() {
        return classroom;
    }

    public UniversityClass classroom(Classroom classroom) {
        this.classroom = classroom;
        return this;
    }

    public void setClassroom(Classroom classroom) {
        this.classroom = classroom;
    }

    public Set<UniversityGroup> getGroups() {
        return groups;
    }

    public UniversityClass groups(Set<UniversityGroup> universityGroups) {
        this.groups = universityGroups;
        return this;
    }

    public UniversityClass addGroups(UniversityGroup universityGroup) {
        groups.add(universityGroup);
        universityGroup.getClasses().add(this);
        return this;
    }

    public UniversityClass removeGroups(UniversityGroup universityGroup) {
        groups.remove(universityGroup);
        universityGroup.getClasses().remove(this);
        return this;
    }

    public void setGroups(Set<UniversityGroup> universityGroups) {
        this.groups = universityGroups;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UniversityClass universityClass = (UniversityClass) o;
        if(universityClass.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, universityClass.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "UniversityClass{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", moduleCode='" + moduleCode + "'" +
            ", description='" + description + "'" +
            ", type='" + type + "'" +
            ", startHour='" + startHour + "'" +
            ", endHour='" + endHour + "'" +
            ", weekday='" + weekday + "'" +
            '}';
    }
}
