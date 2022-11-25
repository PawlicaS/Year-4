package ie.szymon.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import ie.szymon.rest.controllers.NewDepartment;
import lombok.SneakyThrows;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest("ie.szymon.rest")
@AutoConfigureMockMvc
class MainAppTests {
	@Autowired
	MockMvc mockMvc;
	@Test
	@SneakyThrows
	void getDepartments() {
		mockMvc.perform(MockMvcRequestBuilders.get("/departments"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("_embedded.departments", Matchers.hasSize(4)));
	}

	@Test
	@SneakyThrows
	void getADepartment() {
		mockMvc.perform(MockMvcRequestBuilders.get("/departments/{id}", "Test1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.departmentTitle").value("Test1"));
	}

	@Test
	@SneakyThrows
	void getADepartmentNoSuchID() {
		mockMvc.perform(MockMvcRequestBuilders.get("/departments/{id}", "Test6"))
				.andExpect(status().isNotFound());
	}

	@Test
	@SneakyThrows
	void getOffices() {
		mockMvc.perform(MockMvcRequestBuilders.get("/offices"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("_embedded.offices", Matchers.hasSize(4)));
	}

	@Test
	@SneakyThrows
	void getAOffice() {
		mockMvc.perform(MockMvcRequestBuilders.get("/offices/{id}", 121))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.officeNo").value(121));
	}

	@Test
	@SneakyThrows
	void getAOfficeNoSuchID() {
		mockMvc.perform(MockMvcRequestBuilders.get("/offices/{id}", 125))
				.andExpect(status().isNotFound());
	}

	@Test
	@SneakyThrows
	@WithMockUser(roles="HOS")
	void postNewDepartmentOkAndHOS() {
		String jsonString = new ObjectMapper().writeValueAsString(new NewDepartment("Test5", "test5@mtu.ie"));
		mockMvc.perform(MockMvcRequestBuilders.post("/departments/add")
						.content(jsonString)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$._links.self").exists());
	}

	@Test
	@SneakyThrows
	@WithMockUser(roles="HOD")
	void postNewDepartmentOkNotHOS() {
		String jsonString = new ObjectMapper().writeValueAsString(new NewDepartment("Test5", "test5@mtu.ie"));
		mockMvc.perform(MockMvcRequestBuilders.post("/departments/add")
						.content(jsonString)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isForbidden());
	}

	@Test
	@SneakyThrows
	void postNewDepartmentOkNoUser() {
		String jsonString = new ObjectMapper().writeValueAsString(new NewDepartment("Test5", "test5@mtu.ie"));
		mockMvc.perform(MockMvcRequestBuilders.post("/departments/add")
						.content(jsonString)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnauthorized());
	}

	@Test
	@SneakyThrows
	@WithMockUser(roles="HOS")
	void postNewDepartmentConflict() {
		String jsonString = new ObjectMapper().writeValueAsString(new NewDepartment("Test1", "test1@mtu.ie"));
		mockMvc.perform(MockMvcRequestBuilders.post("/departments/add")
						.content(jsonString)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isConflict());
	}

	@Test
	@SneakyThrows
	@WithMockUser(roles="HOS")
	void postNewDepartmentWrongJson() {
		String jsonString = new ObjectMapper().writeValueAsString(new NewDepartment("", "test5@mtu.ie"));
		mockMvc.perform(MockMvcRequestBuilders.post("/departments/add")
						.content(jsonString)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}

}
