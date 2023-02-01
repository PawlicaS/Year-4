package ie.szymon.rest.controllers;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public record NewOffice (@NotNull @Min(0) int officeNo,
                         @NotNull @Min(0) int maxOccupancy,
                         @NotNull @Min(0) int currOccupancy,
                         @NotNull String departmentTitle) {
}
