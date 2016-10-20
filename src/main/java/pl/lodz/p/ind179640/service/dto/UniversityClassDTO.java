package pl.lodz.p.ind179640.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import pl.lodz.p.ind179640.domain.enumeration.ClassType;
import pl.lodz.p.ind179640.domain.enumeration.Weekday;

/**
 * A DTO for the UniversityClass entity.
 */
public class UniversityClassDTO implements Serializable {

    private Long id;

    private String name;

    private String description;

    private ClassType type;

    private Integer startHour;

    private Integer endHour;

    private Weekday weekday;


    private Set<LecturerDTO> lecturers = new HashSet<>();

    private Long classroomId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public ClassType getType() {
        return type;
    }

    public void setType(ClassType type) {
        this.type = type;
    }
    public Integer getStartHour() {
        return startHour;
    }

    public void setStartHour(Integer startHour) {
        this.startHour = startHour;
    }
    public Integer getEndHour() {
        return endHour;
    }

    public void setEndHour(Integer endHour) {
        this.endHour = endHour;
    }
    public Weekday getWeekday() {
        return weekday;
    }

    public void setWeekday(Weekday weekday) {
        this.weekday = weekday;
    }

    public Set<LecturerDTO> getLecturers() {
        return lecturers;
    }

    public void setLecturers(Set<LecturerDTO> lecturers) {
        this.lecturers = lecturers;
    }

    public Long getClassroomId() {
        return classroomId;
    }

    public void setClassroomId(Long classroomId) {
        this.classroomId = classroomId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UniversityClassDTO universityClassDTO = (UniversityClassDTO) o;

        if ( ! Objects.equals(id, universityClassDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "UniversityClassDTO{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", description='" + description + "'" +
            ", type='" + type + "'" +
            ", startHour='" + startHour + "'" +
            ", endHour='" + endHour + "'" +
            ", weekday='" + weekday + "'" +
            '}';
    }
}
