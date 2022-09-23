package ie.szymon.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Component
public class Faculty {
    private String name="Engineering and Science";
    private String phone="021 433 5450";
}
