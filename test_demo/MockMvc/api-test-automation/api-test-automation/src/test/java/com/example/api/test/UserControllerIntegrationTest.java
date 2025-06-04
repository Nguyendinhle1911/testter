package com.example.api.test;

import com.example.api.test.model.User;
import com.example.api.test.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ObjectMapper objectMapper;

	private User testUser;

	@BeforeEach
	void setUp() {
		userRepository.deleteAll();
		testUser = new User("John Doe", "john.doe@example.com", 25);
		testUser.setAddress("123 Main St");
		testUser.setPhone("+84912345678");
		userRepository.save(testUser);
	}

	@Test
	void testGetAllUsers() throws Exception {
		mockMvc.perform(get("/api/users")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].name").value("John Doe"))
				.andExpect(jsonPath("$[0].email").value("john.doe@example.com"));
	}

	@Test
	void testGetUserById() throws Exception {
		mockMvc.perform(get("/api/users/" + testUser.getId())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("John Doe"))
				.andExpect(jsonPath("$.email").value("john.doe@example.com"));
	}

	@Test
	void testGetUserByIdNotFound() throws Exception {
		mockMvc.perform(get("/api/users/999")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	void testCreateUser() throws Exception {
		User newUser = new User("Jane Doe", "jane.doe@example.com", 30);
		newUser.setAddress("456 Oak St");
		newUser.setPhone("+84987654321");

		mockMvc.perform(post("/api/users")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(newUser)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.name").value("Jane Doe"))
				.andExpect(jsonPath("$.email").value("jane.doe@example.com"));
	}

	@Test
	void testCreateUserWithInvalidEmail() throws Exception {
		User invalidUser = new User("Jane Doe", "invalid-email", 30);

		mockMvc.perform(post("/api/users")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(invalidUser)))
				.andExpect(status().isBadRequest());
	}

	@Test
	void testUpdateUser() throws Exception {
		User updatedUser = new User("John Updated", "john.updated@example.com", 26);
		updatedUser.setAddress("789 Pine St");
		updatedUser.setPhone("+84911223344");

		mockMvc.perform(put("/api/users/" + testUser.getId())
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(updatedUser)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.name").value("John Updated"))
				.andExpect(jsonPath("$.email").value("john.updated@example.com"));
	}

	@Test
	void testDeleteUser() throws Exception {
		mockMvc.perform(delete("/api/users/" + testUser.getId())
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent());
	}

	@Test
	void testSearchUsersByName() throws Exception {
		mockMvc.perform(get("/api/users/search?keyword=John")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].name").value("John Doe"));
	}

	@Test
	void testGetUsersByAgeRange() throws Exception {
		mockMvc.perform(get("/api/users/age-range?minAge=20&maxAge=30")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].age").value(25));
	}

	@Test
	void testGetUsersWithPagination() throws Exception {
		mockMvc.perform(get("/api/users/paged?page=0&size=10&sortBy=id&sortDir=asc")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.content[0].name").value("John Doe"));
	}
}