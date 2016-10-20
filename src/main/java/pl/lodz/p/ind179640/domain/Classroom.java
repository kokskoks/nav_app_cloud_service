package pl.lodz.p.ind179640.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Classroom.
 */
@Entity
@Table(name = "classroom")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Classroom implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "desription")
    private String desription;

    @Column(name = "floor")
    private Integer floor;

    @OneToMany(mappedBy = "classroom")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<UniversityClass> classes = new HashSet<>();

    @ManyToOne
    private Building building;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Classroom name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesription() {
        return desription;
    }

    public Classroom desription(String desription) {
        this.desription = desription;
        return this;
    }

    public void setDesription(String desription) {
        this.desription = desription;
    }

    public Integer getFloor() {
        return floor;
    }

    public Classroom floor(Integer floor) {
        this.floor = floor;
        return this;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }

    public Set<UniversityClass> getClasses() {
        return classes;
    }

    public Classroom classes(Set<UniversityClass> universityClasses) {
        this.classes = universityClasses;
        return this;
    }

    public Classroom addClasses(UniversityClass universityClass) {
        classes.add(universityClass);
        universityClass.setClassroom(this);
        return this;
    }

    public Classroom removeClasses(UniversityClass universityClass) {
        classes.remove(universityClass);
        universityClass.setClassroom(null);
        return this;
    }

    public void setClasses(Set<UniversityClass> universityClasses) {
        this.classes = universityClasses;
    }

    public Building getBuilding() {
        return building;
    }

    public Classroom building(Building building) {
        this.building = building;
        return this;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Classroom classroom = (Classroom) o;
        if(classroom.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, classroom.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Classroom{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", desription='" + desription + "'" +
            ", floor='" + floor + "'" +
            '}';
    }
}
