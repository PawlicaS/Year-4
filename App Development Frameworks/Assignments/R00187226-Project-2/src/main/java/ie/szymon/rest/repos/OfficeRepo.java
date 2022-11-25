package ie.szymon.rest.repos;

import ie.szymon.rest.entities.Department;
import ie.szymon.rest.entities.Office;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface OfficeRepo extends JpaRepository<Office, Integer> {
    List<Office> findAll();
    List<Office> findAllByDepartment_Title(String Title);
    @Query("select o from Office o where currOccupancy = 0")
    List<Office> findAllEmptyOffices();
    @Query("select o from Office o where currOccupancy < maxOccupancy")
    List<Office> findAllNotFullOffices();

    @Modifying
    @Query(value = "update Office o set o.currOccupancy = :newCurrOccupancy where o.officeNo = :officeNo")
    @Transactional
    void updateOfficeCurrOccupancy(@Param("newCurrOccupancy") int newCurrOccupancy, @Param("officeNo") int officeNo);

    @Modifying
    @Query(value = "update Office o set o.department = :newDepartment where o.officeNo = :officeNo")
    @Transactional
    void updateOfficeDepartment(@Param("newDepartment") Department newDepartment, @Param("officeNo") int officeNo);
}
