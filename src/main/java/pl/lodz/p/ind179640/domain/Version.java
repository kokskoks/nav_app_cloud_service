package pl.lodz.p.ind179640.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Version.
 */
@Entity
@Table(name = "version")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Version implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "ver")
    private Integer ver;

    @Column(name = "name")
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getVer() {
        return ver;
    }

    public Version ver(Integer ver) {
        this.ver = ver;
        return this;
    }

    public void setVer(Integer ver) {
        this.ver = ver;
    }

    public String getName() {
        return name;
    }

    public Version name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Version version = (Version) o;
        if(version.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, version.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Version{" +
            "id=" + id +
            ", ver='" + ver + "'" +
            ", name='" + name + "'" +
            '}';
    }
}
