package ie.szymon.rest.controllers.dto;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Relation(collectionRelation = "departments", itemRelation = "department")
public class DepartmentDto extends RepresentationModel<DepartmentDto> {
    private String departmentTitle, departmentEmail;
}
