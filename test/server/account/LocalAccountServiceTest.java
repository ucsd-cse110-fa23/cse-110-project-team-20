package server.account;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class LocalAccountServiceTest {
    @Test
    public void alwaysAllowLogin() {
        LocalAccountService service = new LocalAccountService();
        assertTrue(service.loginOrCreateAccount(null, null));
    }
}
