package pl.lodz.p.ind179640.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the Classroom entity.
 */
public class ClassroomDTO implements Serializable {

    private Long id;

    private String name;

    private String desription;

    private Integer floor;


    private Long buildingId;
    
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
    public String getDesription() {
        return desription;
    }

    public void setDesription(String desription) {
        this.desription = desription;
    }
    public Integer getFloor() {
        return floor;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }

    public Long getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(Long buildingId) {
        this.buildingId = buildingId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ClassroomDTO classroomDTO = (ClassroomDTO) o;

        if ( ! Objects.equals(id, classroomDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ClassroomDTO{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", desription='" + desription + "'" +
            ", floor='" + floor + "'" +
            '}';
    }
}
