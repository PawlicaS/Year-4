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
    public Department(String departmentTitle, String departmentEmail) {
        this.departmentTitle = departmentTitle;
        this.departmentEmail = departmentEmail;
    }

    @Id
    @Column(name = "departmentTitle", nullable = false)
    private String departmentTitle;

    @Column(name = "departmentEmail", nullable = false)
    private String departmentEmail;

    @OneToMany(mappedBy = "department", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @ToString.Exclude
    private List<Office> films = new ArrayList<>();
}
