package ie.szymon.entities;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "departments")
public class Department {
    public Department(String title, String email) {
        this.title = title;
        this.email = email;
    }

    @Id
    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String email;

    @OneToMany(orphanRemoval = true, mappedBy = "department")
    @ToString.Exclude
    private List<Office> offices = new ArrayList<>();
}
