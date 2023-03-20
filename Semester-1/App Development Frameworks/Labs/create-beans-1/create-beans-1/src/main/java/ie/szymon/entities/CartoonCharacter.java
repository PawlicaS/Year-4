package ie.szymon.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartoonCharacter {
    private String name;
    private int yearFirstAppeared;
    private boolean film, tv;
    private Studio studio;
}
