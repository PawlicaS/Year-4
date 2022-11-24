package ie.szymon.services;

import ie.szymon.entities.Department;
import ie.szymon.entities.Office;

import java.util.List;

public interface OfficeService {
    List<Office> listAll();
    Office addOffice(int officeNo, int maxOccupancy, int currOccupancy, Department department);
}
