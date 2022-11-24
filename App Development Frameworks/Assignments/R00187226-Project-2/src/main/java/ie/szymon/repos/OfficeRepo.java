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
    List<Office> findAllByDepartment_Title(String Title);
    @Query("select o from Office o where currOccupancy = 0")
    List<Office> findAllEmptyOffices();
    @Query("select o from Office o where currOccupancy < maxOccupancy")
    List<Office> findAllNotFullOffices();
    int deleteByOfficeNo(int officeNo);

    @Modifying
    @Query(value = "update Office o set o.currOccupancy = :newCurrOccupancy where o.officeNo = :officeNo")
    @Transactional
    void updateOfficeCurrOccupancy(@Param("newCurrOccupancy") int newCurrOccupancy, @Param("officeNo") int officeNo);
}
