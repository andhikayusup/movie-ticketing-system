package com.andhikayusup.moviex.controller.v1;

import com.andhikayusup.moviex.enums.UserRole;
import com.andhikayusup.moviex.model.User;
import com.andhikayusup.moviex.service.UserService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@SpringBootTest(classes = {UserController.class})
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class UserControllerTests {
    
    private static final User USER_1 = new User(1L, "USER_1", "pwd", UserRole.ROLE_USER);
    private static final User USER_2 = new User(2L, "USER_2", "pwd", UserRole.ROLE_ADMIN);
    private static final List<User> USERS = Arrays.asList(USER_1, USER_2);
    
    final ObjectMapper objectMapper = new ObjectMapper();
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private UserController userController;
    
    @MockBean
    private UserService userService;
    
    @BeforeEach
    void initMockMock() {
        mockMvc = standaloneSetup(userController).build();
    }
    
    @Test
    void testGetUsers() throws Exception {
        when(userService.getUsers()).thenReturn(USERS);
        
        String rawResponse = mockMvc.perform(get("/api/v1/users/"))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();
        
        List<User> result = objectMapper.readValue(rawResponse, new TypeReference<List<User>>() {
        });
        assertThat(result).containsAll(USERS);
    }
    
    @Test
    void testGetUser() throws Exception {
        when(userService.getUser(USER_1.getUsername())).thenReturn(USER_1);
        
        String rawResponse = mockMvc.perform(get("/api/v1/users/{username}", "USER_1"))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();
        
        User result = objectMapper.readValue(rawResponse, User.class);
        assertThat(result).isEqualTo(USER_1);
    }
    
    @Test
    void testSaveUser() throws Exception {
        mockMvc.perform(post("/api/v1/users/")
                .content(objectMapper.writeValueAsString(USER_1))
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
        
        verify(userService).saveUser(USER_1);
    }
}
