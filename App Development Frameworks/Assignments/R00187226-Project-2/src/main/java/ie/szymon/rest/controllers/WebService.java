package ie.szymon.rest.controllers;

import ie.szymon.rest.controllers.dto.*;
import ie.szymon.rest.entities.Department;
import ie.szymon.rest.entities.Office;
import ie.szymon.rest.repos.DepartmentRepo;
import ie.szymon.rest.repos.OfficeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
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

    @PostMapping({"/departments/add"})
    @ResponseStatus(HttpStatus.CREATED)
    public DepartmentDto addDepartment(@RequestBody @Valid NewDepartment payload, BindingResult bindingResult) {
        try {
            if (bindingResult.hasErrors())
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "JSON was malformed or there are errors.");
            Department newDepartment = new Department(payload.title(), payload.email());
            return departmentDtoMapper.toModel(departmentRepo.save(newDepartment));
        } catch (DataIntegrityViolationException ex) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Department with title " + payload.title() + " already exists.");
        }
    }

    @PostMapping("/offices/add")
    @ResponseStatus(HttpStatus.CREATED)
    public OfficeDto addOffice(@RequestBody @Valid NewOffice payload, BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "SON was malformed or there are errors.");
        Optional<Department> departmentOptional = departmentRepo.findById(payload.departmentTitle());
        if (departmentOptional.isPresent()) {
            Optional<Office> officeOptional = officeRepo.findById(payload.officeNo());
            if(officeOptional.isEmpty()) {
                Office newOffice = new Office(payload.officeNo(), payload.currOccupancy(), payload.maxOccupancy(), departmentOptional.get());
                return officeDtoMapper.toModel(officeRepo.save(newOffice));
            }
            else {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Office with officeNo " + payload.officeNo() + " already exists.");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Department with title " + payload.departmentTitle() + " could not be found.");
        }
    }

    @PatchMapping("/offices/{officeNo}/move")
    public OfficeDto moveOffice(@PathVariable("officeNo") int officeNo, @Valid @RequestBody MoveOffice payload, BindingResult bindingResult){
        if (bindingResult.hasErrors())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request is missing data or does not obey data requirements.");
        Optional<Department> departmentOptional = departmentRepo.findById(payload.departmentTitle());
        if (departmentOptional.isPresent()) {
            officeRepo.updateOfficeDepartment(departmentRepo.findById(payload.departmentTitle()).get(), officeNo);
            return officeDtoMapper.toModel(officeRepo.findById(officeNo).get());
        }
        else {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Department with title " + payload.departmentTitle() + " could not be found.");
        }
    }

    @PatchMapping("/offices/{officeNo}/occupancy")
    public OfficeDto updateOfficeOccupancy(@PathVariable("officeNo") int officeNo, @Valid @RequestBody UpdateOfficeOccupancy payload, BindingResult bindingResult){
        if (bindingResult.hasErrors())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Request is missing data or does not obey data requirements.");
        if (officeRepo.findById(officeNo).get().getMaxOccupancy() >= payload.currOccupancy()) {
            officeRepo.updateOfficeCurrOccupancy(payload.currOccupancy(), officeNo);
            return officeDtoMapper.toModel(officeRepo.findById(officeNo).get());
        }
        else {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Office with officeNo " + officeNo + " does not have enough maxOccupancy.");
        }
    }

    // http://localhost:8080/departments/CompSci
    @GetMapping("/departments/{title}")
    public DepartmentDto getADepartmentByTitle(@PathVariable("title") String title) {
        Optional<Department> departmentOptional = departmentRepo.findById(title);
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
        Optional<Office> officeOptional = officeRepo.findById(officeNo);
        if (officeOptional.isPresent()) return officeDtoMapper.toModel(officeOptional.get());
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Office with officeNo " + officeNo + " could not be found.");
    }

    @GetMapping("/offices/free")
    public CollectionModel<OfficeDto> getOfficesByEmpty() {
        return officeDtoMapper.toCollectionModel(officeRepo.findAllEmptyOffices());
    }

    @GetMapping("/offices/notfull")
    public CollectionModel<OfficeDto> getOfficesByNotFull() {
        return officeDtoMapper.toCollectionModel(officeRepo.findAllNotFullOffices());
    }

    @DeleteMapping("/departments/{title}/delete")
    void deleteDepartmentByTitle(@PathVariable("title") String title) {
        try {
            departmentRepo.deleteById(title);
        } catch (EmptyResultDataAccessException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Department with title " + title + " could not be found.");
        }
    }
    @DeleteMapping("/offices/{officeNo}/delete")
    void deleteOfficeByOfficeNo(@PathVariable("officeNo") int officeNo) {
        try {
            officeRepo.deleteById(officeNo);
        } catch (EmptyResultDataAccessException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Office with officeNo " + officeNo + " could not be found.");
        }
    }
}
