package ie.szymon.services;

import ie.szymon.entities.Department;
import ie.szymon.entities.Office;
import ie.szymon.repos.OfficeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OfficeServiceImpl implements OfficeService {
    @Autowired
    OfficeRepo officeRepo;

    @Override
    public List<Office> listAll() {
        return null;
    }

    @Override
    public Office addOffice(int officeNo, int maxOccupancy, int currOccupancy, Department department) {
        return null;
    }
}
