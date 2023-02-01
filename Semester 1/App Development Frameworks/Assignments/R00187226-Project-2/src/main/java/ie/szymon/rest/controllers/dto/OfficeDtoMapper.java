package ie.szymon.rest.controllers.dto;

import ie.szymon.rest.entities.Office;
import ie.szymon.rest.controllers.WebService;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class OfficeDtoMapper extends RepresentationModelAssemblerSupport<Office, OfficeDto> {
    public OfficeDtoMapper() {
        super(WebService.class, OfficeDto.class);
    }

    @Override
    public OfficeDto toModel(Office entity) {
        OfficeDto officeDto = new OfficeDto(entity.getOfficeNo(), entity.getMaxOccupancy(), entity.getCurrOccupancy(), entity.getDepartment());
        officeDto.add(WebMvcLinkBuilder.linkTo(methodOn(WebService.class).getAOfficeByOfficeNo(entity.getOfficeNo())).withSelfRel());
        officeDto.add(WebMvcLinkBuilder.linkTo(methodOn(WebService.class).moveOffice(entity.getOfficeNo(), null, null)).withRel("move"));
        officeDto.add(WebMvcLinkBuilder.linkTo(methodOn(WebService.class).updateOfficeOccupancy(entity.getOfficeNo(), null, null)).withRel("occupancy"));
        officeDto.add(WebMvcLinkBuilder.linkTo(methodOn(WebService.class).getADepartmentByTitle(entity.getDepartment().getTitle())).withRel("department"));
        return officeDto;
    }

    @Override
    public CollectionModel<OfficeDto> toCollectionModel(Iterable<? extends Office> entities) {
        return super.toCollectionModel(entities);
    }
}
