package ie.szymon.rest.entities;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @Column(nullable = false)
    private int officeNo;

    @Column(nullable = false)
    private int maxOccupancy;

    @Column(nullable = false)
    private int currOccupancy;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(nullable = false)
    private Department department;
}
