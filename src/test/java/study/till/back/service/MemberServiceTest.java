package study.till.back.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import study.till.back.util.valid.SignupValidUtil;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Test
    public void checkPassword() {
        String password1 = "Gusals13@";
        boolean bool1 = SignupValidUtil.isValidPassword(password1);
        assertEquals(false, bool1);

        String password2 = "Gusals1016@";
        boolean bool2 = SignupValidUtil.isValidPassword(password2);
        assertEquals(true, bool2);

        String password3 = "Gusals1016:D";
        boolean bool3 = SignupValidUtil.isValidPassword(password3);
        assertEquals(true, bool3);

        String password4 = "test12345:D";
        boolean bool4 = SignupValidUtil.isValidPassword(password4);
        assertEquals(true, bool4);

    }

}