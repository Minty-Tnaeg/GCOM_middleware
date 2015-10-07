package testmessagemodule;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import se.umu.cs._5dv147_proj.message.container.ContainerType;
import se.umu.cs._5dv147_proj.message.module.MessageModule;

import static org.junit.Assert.*;

public class MessageModuleTest {
    private MessageModule mm;


    @Before
    public void setUp() throws Exception {
        this.mm = new MessageModule(null, ContainerType.Causal, "TEST");
    }

    @After
    public void tearDown() throws Exception { this.mm = null; }

    @Test
    public void testFetchTextMessage() throws Exception {

    }

    @Test
    public void testFetchSystemMessage() throws Exception {

    }

    @Test
    public void testQueueIncomingMessage() throws Exception {

    }

    @Test
    public void testSend() throws Exception {

    }

    @Test
    public void testSend1() throws Exception {

    }

    @Test
    public void testSend2() throws Exception {

    }
}