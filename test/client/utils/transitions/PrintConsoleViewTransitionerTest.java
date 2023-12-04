package client.utils.transitions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

// output test for PrintConsoleViewTransitioner
public class PrintConsoleViewTransitionerTest {
    // ref: https://stackoverflow.com/questions/1119385/
    private ByteArrayOutputStream outContent;
    private PrintStream originalOut = System.out;

    @BeforeEach
    public void
    setUpStreams()
    {
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void
    restoreStreams()
    {
        System.setOut(originalOut);
    }

    @Test
    public void
    instance()
    {
        PrintConsoleViewTransitioner transitioner = new PrintConsoleViewTransitioner();
        assertInstanceOf(PrintConsoleViewTransitioner.class, transitioner);
    }

    @Test
    public void
    transitionToWithoutParam() throws UnsupportedEncodingException
    {
        PrintConsoleViewTransitioner transitioner = new PrintConsoleViewTransitioner();
        transitioner.transitionTo(MockClass.class, new Object[] {});

        assertEquals("[Transition] class client.utils.transitions.MockClass (0)",
            outContent.toString("UTF-8").trim());
    }

    @Test
    public void
    transitionToWithParam1() throws UnsupportedEncodingException
    {
        PrintConsoleViewTransitioner transitioner = new PrintConsoleViewTransitioner();
        transitioner.transitionTo(MockClass.class, new Object[] {1});

        assertEquals("[Transition] class client.utils.transitions.MockClass (1)",
            outContent.toString("UTF-8").trim());
    }

    @Test
    public void
    transitionToWithParam2() throws UnsupportedEncodingException
    {
        PrintConsoleViewTransitioner transitioner = new PrintConsoleViewTransitioner();
        transitioner.transitionTo(MockClass.class, new Object[] {1, "String"});

        assertEquals("[Transition] class client.utils.transitions.MockClass (2)",
            outContent.toString("UTF-8").trim());
    }

    @Test
    public void
    transitionToWithParam3() throws UnsupportedEncodingException
    {
        PrintConsoleViewTransitioner transitioner = new PrintConsoleViewTransitioner();
        Runnable r = () -> {};
        transitioner.transitionTo(MockClass.class, new Object[] {1, "String", r});

        String expected =
            String.format("[Transition] class client.utils.transitions.MockClass (3)", r);
        assertEquals(expected, outContent.toString("UTF-8").trim());
    }
}

class MockClass {}
