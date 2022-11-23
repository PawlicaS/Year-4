package ie.szymon.repos;

import ie.szymon.entities.Office;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface OfficeRepo extends JpaRepository<Office, Integer> {
    List<Office> findAll();
    Optional<Office> findByOfficeNo(int officeNo);
    List<Office> findAllByDepartment_DepartmentTitle(String departmentTitle);
    @Query("select o from Office o where officeCurrOccupancy = 0")
    List<Office> findAllEmptyOffices();
    @Query("select o from Office o where officeCurrOccupancy < officeMaxOccupancy")
    List<Office> findAllNotFullOffices();
    int deleteByOfficeNo(int officeNo);

    @Modifying
    @Query(value = "update Office o set o.officeCurrOccupancy = :newOfficeCurrOccupancy where o.officeNo = :officeNo")
    @Transactional
    void updateOfficeCurrOccupancy(@Param("newOfficeCurrOccupancy") int newOfficeCurrOccupancy, @Param("officeNo") int officeNo);
}
