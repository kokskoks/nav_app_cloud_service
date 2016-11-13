package pl.lodz.p.ind179640.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A UniversityGroup.
 */
@Entity
@Table(name = "university_group")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class UniversityGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "subject")
    private String subject;

    @Column(name = "code")
    private String code;

    @Column(name = "description")
    private String description;

    @Column(name = "semester")
    private Integer semester;

    @Column(name = "specialisation")
    private Boolean specialisation;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "university_group_classes",
               joinColumns = @JoinColumn(name="university_groups_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="classes_id", referencedColumnName="ID"))
    private Set<UniversityClass> classes = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public UniversityGroup subject(String subject) {
        this.subject = subject;
        return this;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getCode() {
        return code;
    }

    public UniversityGroup code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public UniversityGroup description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getSemester() {
        return semester;
    }

    public UniversityGroup semester(Integer semester) {
        this.semester = semester;
        return this;
    }

    public void setSemester(Integer semester) {
        this.semester = semester;
    }

    public Boolean isSpecialisation() {
        return specialisation;
    }

    public UniversityGroup specialisation(Boolean specialisation) {
        this.specialisation = specialisation;
        return this;
    }

    public void setSpecialisation(Boolean specialisation) {
        this.specialisation = specialisation;
    }

    public Set<UniversityClass> getClasses() {
        return classes;
    }

    public UniversityGroup classes(Set<UniversityClass> universityClasses) {
        this.classes = universityClasses;
        return this;
    }

    public UniversityGroup addClasses(UniversityClass universityClass) {
        classes.add(universityClass);
        universityClass.getGroups().add(this);
        return this;
    }

    public UniversityGroup removeClasses(UniversityClass universityClass) {
        classes.remove(universityClass);
        universityClass.getGroups().remove(this);
        return this;
    }

    public void setClasses(Set<UniversityClass> universityClasses) {
        this.classes = universityClasses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UniversityGroup universityGroup = (UniversityGroup) o;
        if(universityGroup.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, universityGroup.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "UniversityGroup{" +
            "id=" + id +
            ", subject='" + subject + "'" +
            ", code='" + code + "'" +
            ", description='" + description + "'" +
            ", semester='" + semester + "'" +
            ", specialisation='" + specialisation + "'" +
            '}';
    }
}
