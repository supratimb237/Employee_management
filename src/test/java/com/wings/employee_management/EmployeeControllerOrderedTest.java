package com.wings.employee_management;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.wings.employee_management.model.Authority;
import com.wings.employee_management.model.Employee;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
class EmployeeAuthCrudIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	private static String adminToken;
	private static String userToken;
	private static Long employeeId;

	// ---------------- LOGIN DTO ----------------
	static class LoginRequest {
		public String username;
		public String password;

		LoginRequest(String username, String password) {
			this.username = username;
			this.password = password;
		}
	}

	// ---------------- AUTH TESTS ----------------

	@Test
	@Order(1)
	void login_asAdmin_shouldReturnJwt() throws Exception {
		LoginRequest request = new LoginRequest("alice", "admin123");

		mockMvc.perform(post("/auth/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.token").exists())
				.andDo(result -> {
					String response = result.getResponse().getContentAsString();
					adminToken = "Bearer " + JsonPath.read(response, "$.token");
				});
	}

	@Test
	@Order(2)
	void login_asUser_shouldReturnJwt() throws Exception {
		LoginRequest request = new LoginRequest("bob", "user123");

		mockMvc.perform(post("/auth/login")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(request)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.token").exists())
				.andDo(result -> {
					String response = result.getResponse().getContentAsString();
					userToken = "Bearer " + JsonPath.read(response, "$.token");
				});
	}

	// ---------------- CREATE ----------------

	@Test
	@Order(3)
	void createEmployee_asAdmin_shouldSucceed() throws Exception {
		Employee employee = new Employee(
				"TestUser",
				"IT",
				"IT",
				Set.of(Authority.USER)
		);

		mockMvc.perform(post("/api/employees")
						.header("Authorization", adminToken)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(employee)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").exists())
				.andDo(result -> {
					String response = result.getResponse().getContentAsString();
					employeeId =
							objectMapper.readValue(response, Employee.class).getId();
				});
	}

	// ---------------- READ ----------------

	@Test
	@Order(4)
	void getEmployeeById_asUser_shouldSucceed() throws Exception {
		mockMvc.perform(get("/api/employees/{id}", employeeId)
						.header("Authorization", userToken))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.username").value("TestUser"));
	}

	@Test
	@Order(5)
	void getAllEmployees_asUser_shouldSucceed() throws Exception {
		mockMvc.perform(get("/api/employees")
						.header("Authorization", userToken))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").isArray());
	}

	// ---------------- UPDATE ----------------

	@Test
	@Order(6)
	void updateEmployee_asAdmin_shouldSucceed() throws Exception {
		String updateJson = """
            {
                "username": "UpdatedUser",
                "department": "HR",
            }
            """;

		mockMvc.perform(put("/api/employees/{id}", employeeId)
						.header("Authorization", adminToken)
						.contentType(MediaType.APPLICATION_JSON)
						.content(updateJson))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.username").value("UpdatedUser"));
	}

	// ---------------- DELETE ----------------

	@Test
	@Order(7)
	void deleteEmployee_asAdmin_shouldSucceed() throws Exception {
		mockMvc.perform(delete("/api/employees/{id}", employeeId)
						.header("Authorization", adminToken))
				.andExpect(status().isOk());
	}

	@Test
	@Order(8)
	void getDeletedEmployee_shouldReturnNotFound() throws Exception {
		mockMvc.perform(get("/api/employees/{id}", employeeId)
						.header("Authorization", adminToken))
				.andExpect(status().isNotFound());
	}

	// ---------------- SECURITY ----------------

	@Test
	@Order(9)
	void createEmployee_asUser_shouldBeForbidden() throws Exception {
		Employee employee = new Employee(
				"Unauthorized",
				"IT",
				"Accounts",
				Set.of(Authority.USER)
		);

		mockMvc.perform(post("/api/employees")
						.header("Authorization", userToken)
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(employee)))
				.andExpect(status().isForbidden());
	}

	@Test
	@Order(10)
	void accessWithoutToken_shouldReturnUnauthorized() throws Exception {
		mockMvc.perform(get("/api/employees"))
				.andExpect(status().isUnauthorized());
	}
}
