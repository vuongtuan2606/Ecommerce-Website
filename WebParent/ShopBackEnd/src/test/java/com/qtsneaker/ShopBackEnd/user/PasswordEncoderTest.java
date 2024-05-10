package com.qtsneaker.ShopBackEnd.user;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

public class PasswordEncoderTest {
    @Test
    public void testPasswordEncoder(){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String password = "tuan123";
        String encodedPassword = passwordEncoder.encode(password);

        System.out.println(encodedPassword);
        boolean matches = passwordEncoder.matches(password, encodedPassword);
        assertThat(matches).isTrue();
    }
}
