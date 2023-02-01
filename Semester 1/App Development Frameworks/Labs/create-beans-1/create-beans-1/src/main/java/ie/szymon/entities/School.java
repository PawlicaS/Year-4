package ie.szymon.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class School {
    private String school;
    private String email;
    @Autowired
    private Faculty faculty;
    public School(String school, String email) {
        this.school = school;
        this.email = email;
    }
}
