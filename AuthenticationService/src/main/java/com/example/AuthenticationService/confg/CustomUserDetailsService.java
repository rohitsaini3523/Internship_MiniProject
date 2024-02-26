package com.example.AuthenticationService.confg;
import com.example.AuthenticationService.model.UserLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
@Component
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserLogin user = this.restTemplate.getForObject("http://backend/user/" + username, UserLogin.class);
//        System.out.println(user.getUsername()+" " + user.getPassword());
        UserDetails userDetails = User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .build();
        return userDetails;
    }
}