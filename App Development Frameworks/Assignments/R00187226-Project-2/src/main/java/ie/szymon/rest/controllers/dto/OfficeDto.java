package ie.szymon.rest.controllers.dto;

import ie.szymon.entities.Department;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Relation(collectionRelation = "offices", itemRelation = "office")
public class OfficeDto extends RepresentationModel<OfficeDto> {
    private int officeNo, maxOccupancy, currOccupancy;
    private Department department;
}
