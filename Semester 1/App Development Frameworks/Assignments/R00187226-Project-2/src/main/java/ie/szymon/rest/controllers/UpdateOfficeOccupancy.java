package ie.szymon.rest.controllers;

import javax.validation.constraints.NotNull;

public record UpdateOfficeOccupancy(@NotNull int currOccupancy) {
}
