package pl.lodz.p.ind179640.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the UniversityGroup entity.
 */
public class UniversityGroupDTO implements Serializable {

    private Long id;

    private String name;

    private String description;

    private Integer semester;

    private Boolean specialisation;


    private Set<UniversityClassDTO> classes = new HashSet<>();

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
    public Integer getSemester() {
        return semester;
    }

    public void setSemester(Integer semester) {
        this.semester = semester;
    }
    public Boolean getSpecialisation() {
        return specialisation;
    }

    public void setSpecialisation(Boolean specialisation) {
        this.specialisation = specialisation;
    }

    public Set<UniversityClassDTO> getClasses() {
        return classes;
    }

    public void setClasses(Set<UniversityClassDTO> universityClasses) {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UniversityGroupDTO universityGroupDTO = (UniversityGroupDTO) o;

        if ( ! Objects.equals(id, universityGroupDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "UniversityGroupDTO{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", description='" + description + "'" +
            ", semester='" + semester + "'" +
            ", specialisation='" + specialisation + "'" +
            '}';
    }
}
