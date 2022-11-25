package ie.szymon.rest.controllers;

import javax.validation.constraints.NotNull;

public record NewDepartment (@NotNull String title,
                             @NotNull String email) {
}
