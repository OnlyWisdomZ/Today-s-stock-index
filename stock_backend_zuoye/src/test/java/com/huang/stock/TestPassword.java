package com.huang.stock;

import com.huang.SysUserApp;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest(classes = SysUserApp.class)
public class TestPassword {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void testPassword(){
        String password ="123456";
        String encodedpassword =passwordEncoder.encode(password);
        System.out.println(encodedpassword);
        boolean result = passwordEncoder.matches("$2a$10$NblfA3y4PK0TRfukapu.QeyLZ6GHLniMkjJso6w.gweNpoCw4003e", encodedpassword);
        System.out.println(result);
    }
}
