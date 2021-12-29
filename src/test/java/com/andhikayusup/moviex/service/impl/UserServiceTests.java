package com.andhikayusup.moviex.service.impl;

import com.andhikayusup.moviex.enums.UserRole;
import com.andhikayusup.moviex.model.User;
import com.andhikayusup.moviex.repository.UserRepository;
import com.andhikayusup.moviex.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebMvcTest({UserService.class, UserDetailsService.class})
@ActiveProfiles("test")
public class UserServiceTests {
    
    private static final User USER_1 = new User(1L, "USER_1", "pwd", UserRole.ROLE_USER);
    
    @MockBean
    UserRepository userRepository;
    
    @MockBean
    PasswordEncoder passwordEncoder;
    
    @Autowired
    UserService userService;
    
    @Autowired
    UserDetailsService userDetailsService;
    
    @Captor
    ArgumentCaptor<User> captor;
    
    @Test
    void testSaveUser() {
        final User user = new User(1L, "USER_1", "pwd", UserRole.ROLE_USER);
        userService.saveUser(user);
        
        verify(userRepository).save(captor.capture());
        assertThat(captor.getValue().getPassword()).isEqualTo(passwordEncoder.encode(user.getPassword()));
        assertThat(captor.getValue()).isEqualTo(user);
    }
    
    @Test
    void testLoadUserByUsername() {
        when(userRepository.findByUsername(USER_1.getUsername())).thenReturn(USER_1);
        
        UserDetails userDetails = userDetailsService.loadUserByUsername("USER_1");
        assertThat(userDetails.getUsername()).isEqualTo(USER_1.getUsername());
        assertThat(userDetails.getPassword()).isEqualTo(USER_1.getPassword());
        assertThat(userDetails.getAuthorities().toArray()).contains(new SimpleGrantedAuthority(USER_1.getRole().name()));
    }
    
    @Test
    void testLoadUserByUsername_UsernameNotFound_ThrowUsernameNotFoundException() {
        when(userRepository.findByUsername("USER_1")).thenReturn(null);
        
        assertThatThrownBy(() -> userDetailsService.loadUserByUsername(""))
            .isInstanceOf(UsernameNotFoundException.class);
    }
    
    @Test
    void testGetUser() {
        when(userRepository.findByUsername(USER_1.getUsername())).thenReturn(USER_1);
        
        User user = userService.getUser(USER_1.getUsername());
        assertThat(user).isEqualTo(USER_1);
    }
    
    @Test
    void testGetUsers() {
        when(userRepository.findAll()).thenReturn(Collections.singletonList(USER_1));
        
        List<User> user = userService.getUsers();
        assertThat(user).containsAll(Collections.singletonList(USER_1));
    }
}
