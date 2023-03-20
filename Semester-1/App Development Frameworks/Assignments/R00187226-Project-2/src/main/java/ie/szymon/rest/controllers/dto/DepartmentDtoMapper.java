package ie.szymon.rest.controllers.dto;

import ie.szymon.rest.entities.Department;
import ie.szymon.rest.controllers.WebService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class DepartmentDtoMapper extends RepresentationModelAssemblerSupport<Department, DepartmentDto> {
    public DepartmentDtoMapper() {
        super(WebService.class, DepartmentDto.class);
    }

    @Override
    public DepartmentDto toModel(Department entity) {
        DepartmentDto departmentDto = new DepartmentDto(entity.getTitle(), entity.getEmail());
        departmentDto.add(WebMvcLinkBuilder.linkTo(methodOn(WebService.class).getADepartmentByTitle(entity.getTitle())).withSelfRel());
        departmentDto.add(WebMvcLinkBuilder.linkTo(methodOn(WebService.class).getOfficesInDepartment(entity.getTitle())).withRel("offices"));
        return departmentDto;
    }

    @Override
    public CollectionModel<DepartmentDto> toCollectionModel(Iterable<? extends Department> entities) {
        return super.toCollectionModel(entities);
    }
}
