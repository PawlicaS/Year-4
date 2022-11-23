package ie.szymon.entities;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "offices")
public class Office {
    @Id
    @Column(name = "officeNo", nullable = false)
    private int officeNo;

    @Column(name = "officeMaxOccupancy", nullable = false)
    private int officeMaxOccupancy;

    @Column(name = "officeCurrOccupancy", nullable = false)
    private int officeCurrOccupancy;

    @ManyToOne
    @JoinColumn(name = "departmentTitle", nullable = false)
    private Department department;
}
