package ie.szymon.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Movie {
    private int movieId;
    private String title;
    private int releaseYear;
    private int takings;
    private int directorId;
}
