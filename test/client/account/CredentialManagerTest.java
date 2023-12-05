package client.account;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.jupiter.api.*;

public class CredentialManagerTest {
    private static final String TEST_FILE_PATH = "test_credentials.json";
    @BeforeEach
    public void
    setup()
    {
        // Clean up the test file before each test
        resetTestFile();
    }

    @AfterEach
    public void
    tearDown()
    {
        // Clean up the test file after each test
        resetTestFile();
    }

    private void
    resetTestFile()
    {
        try {
            Files.write(Paths.get(TEST_FILE_PATH), "".getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void
    testSetAndGetCredentials()
    {
        CredentialManager credentialManager = new CredentialManager(TEST_FILE_PATH);

        credentialManager.setCredentials("john_doe", "securePassword");

        // Check if credentials are set correctly
        assertEquals("john_doe", credentialManager.getUsername());
        assertEquals("securePassword", credentialManager.getPassword());

        // Check if the file is updated
        String fileContent = readFileContent(TEST_FILE_PATH);
        assertTrue(fileContent.contains("john_doe"));
        assertTrue(fileContent.contains("securePassword"));
    }

    @Test
    public void
    testIsStayLoggedIn()
    {
        CredentialManager credentialManager = new CredentialManager(TEST_FILE_PATH);

        assertFalse(credentialManager.hasCredentials());

        // Set credentials
        credentialManager.setCredentials("john_doe", "securePassword");

        assertTrue(credentialManager.hasCredentials());
    }

    @Test
    public void
    credentialFileUpdated()
    {
        CredentialManager credentialManager = new CredentialManager(TEST_FILE_PATH);

        assertFalse(credentialManager.hasCredentials());

        // Set credentials
        credentialManager.setCredentials("john_doe", "securePassword");

        assertTrue(credentialManager.hasCredentials());

        CredentialManager c2 = new CredentialManager(TEST_FILE_PATH);
        assertEquals(c2.getUsername(), "john_doe");
        assertEquals(c2.getPassword(), "securePassword");
    }

    private String
    readFileContent(String filePath)
    {
        try {
            return new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
