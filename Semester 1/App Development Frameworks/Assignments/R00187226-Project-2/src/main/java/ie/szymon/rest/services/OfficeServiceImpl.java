package ie.szymon.rest.services;

import ie.szymon.rest.entities.Department;
import ie.szymon.rest.entities.Office;
import ie.szymon.rest.repos.DepartmentRepo;
import ie.szymon.rest.repos.OfficeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OfficeServiceImpl implements OfficeService {
    @Autowired
    OfficeRepo officeRepo;

    @Autowired
    DepartmentRepo departmentRepo;

    @Override
    public List<Office> listAll() {
        if (officeRepo.findAll().size() == 0)
            return null;
        return officeRepo.findAll();
    }

    @Override
    public List<Office> listAllOfficesInDepartment(String title) {
        if (departmentRepo.existsById(title)) {
            if (officeRepo.findAllByDepartment_Title(title).size() == 0)
                return null;
            return officeRepo.findAllByDepartment_Title(title);
        }
        return null;
    }

    @Override
    public Office findOffice(int officeNo) {
        Optional<Office> officeOptional = officeRepo.findById(officeNo);
        return officeOptional.orElse(null);
    }

    @Override
    public List<Office> listAllEmptyOffices() {
        if (officeRepo.findAllEmptyOffices().size() == 0)
            return null;
        return officeRepo.findAllEmptyOffices();
    }

    @Override
    public List<Office> listAllNotFullOffices() {
        if (officeRepo.findAllNotFullOffices().size() == 0)
            return null;
        return officeRepo.findAllNotFullOffices();
    }

    @Override
    public Office addOffice(int officeNo, int maxOccupancy, int currOccupancy, String departmentTitle) {
        if (departmentRepo.existsById(departmentTitle)) {
            Office office = new Office(officeNo, maxOccupancy, currOccupancy, departmentRepo.findById(departmentTitle).get());
            if (officeRepo.existsById(officeNo))
                return null;
            return officeRepo.save(office);
        }
        return null;
    }

    @Override
    public boolean deleteOffice(int officeNo) {
        if (officeRepo.existsById(officeNo)) {
            officeRepo.deleteById(officeNo);
            return true;
        }
        return false;
    }

    @Override
    public void moveOfficeDepartment(String title, int officeNo) {
        if (officeRepo.existsById(officeNo))
            if (departmentRepo.existsById(title))
                officeRepo.updateOfficeDepartment(departmentRepo.findById(title).get(), officeNo);
    }

    @Override
    public void updateOfficeCurrOccupancy(int currOccupancy, int officeNo) {
        if (officeRepo.existsById(officeNo))
            if (officeRepo.findById(officeNo).get().getMaxOccupancy() >= currOccupancy)
                officeRepo.updateOfficeCurrOccupancy(currOccupancy, officeNo);
    }
}
