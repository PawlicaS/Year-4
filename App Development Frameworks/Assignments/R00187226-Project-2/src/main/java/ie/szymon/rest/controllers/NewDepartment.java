package ie.szymon.rest.controllers;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public record NewDepartment (@NotNull @NotEmpty String title,
                             @NotNull @NotEmpty String email) {
}
