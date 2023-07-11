package colony.webproj.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

class MemberServiceTest {



    @DisplayName("인코딩 테스트")
    @Test
    void validationPassword() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String originPassword = "abcdefg1!";
        String encodingPassword = encoder.encode(originPassword);
        Boolean value = encoder.matches(originPassword,encodingPassword);
        System.out.println(value);
    }
}