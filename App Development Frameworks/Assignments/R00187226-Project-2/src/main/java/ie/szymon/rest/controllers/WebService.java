package ie.szymon.rest.controllers;

import ie.szymon.entities.Department;
import ie.szymon.entities.Office;
import ie.szymon.repos.DepartmentRepo;
import ie.szymon.repos.OfficeRepo;
import ie.szymon.rest.controllers.dto.DepartmentDto;
import ie.szymon.rest.controllers.dto.DepartmentDtoMapper;
import ie.szymon.rest.controllers.dto.OfficeDto;
import ie.szymon.rest.controllers.dto.OfficeDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
public class WebService {
    @Autowired
    private DepartmentRepo departmentRepo;

    @Autowired
    private OfficeRepo officeRepo;

    @Autowired
    private DepartmentDtoMapper departmentDtoMapper;

    @Autowired
    private OfficeDtoMapper officeDtoMapper;

    // http://localhost:8080/departments
    @GetMapping("/departments")
    public CollectionModel<DepartmentDto> getAllDepartments(){
        return departmentDtoMapper.toCollectionModel(departmentRepo.findAll());
    }

    // http://localhost:8080/offices
    @GetMapping("/offices")
    public CollectionModel<OfficeDto> getAllOffices(){
        return officeDtoMapper.toCollectionModel(officeRepo.findAll());
    }

    // http://localhost:8080/departments/CompSci
    @GetMapping("/departments/{title}")
    public DepartmentDto getADepartmentByTitle(@PathVariable("title") String title) {
        Optional<Department> departmentOptional = departmentRepo.findByTitle(title);
        if (departmentOptional.isPresent()) return departmentDtoMapper.toModel(departmentOptional.get());
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Department with title " + title + " could not be found.");
    }

    // http://localhost:8080/departments/CompSci/offices
    @GetMapping("/departments/{title}/offices")
    public CollectionModel<OfficeDto> getOfficesInDepartment(@PathVariable("title") String title) {
        if (departmentRepo.existsById(title))
            return officeDtoMapper.toCollectionModel(officeRepo.findAllByDepartment_Title(title));
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Department with title " + title + " could not be found.");
    }

    // http://localhost:8080/offices/120
    @GetMapping("/offices/{officeNo}")
    public OfficeDto getAOfficeByOfficeNo(@PathVariable("officeNo") int officeNo) {
        Optional<Office> officeOptional = officeRepo.findByOfficeNo(officeNo);
        if (officeOptional.isPresent()) return officeDtoMapper.toModel(officeOptional.get());
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Office with officeNo " + officeNo + " could not be found.");
    }

    // http://localhost:8080/offices/120 DELETE
    @DeleteMapping("/offices/{officeNo}")
    void deleteOfficeByOfficeNo(@PathVariable("officeNo") int officeNo) {
        try {
            officeRepo.deleteById(officeNo);
        } catch (EmptyResultDataAccessException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Office with officeNo " + officeNo + " could not be found.");
        }
    }
}
