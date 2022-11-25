package ie.szymon.rest.services;

import ie.szymon.rest.entities.Department;
import ie.szymon.rest.entities.Office;

import java.util.List;

public interface OfficeService {
    List<Office> listAll();
    List<Office> listAllOfficesInDepartment(String title);
    Office findOffice(int officeNo);

    List<Office> listAllEmptyOffices();

    List<Office> listAllNotFullOffices();
    Office addOffice(int officeNo, int maxOccupancy, int currOccupancy, String departmentTitle);

    boolean deleteOffice(int officeNo);

    void moveOfficeDepartment(String title, int officeNo);

    void updateOfficeCurrOccupancy(int currOccupancy, int officeNo);
}
