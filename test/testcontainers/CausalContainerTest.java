package testcontainers;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import se.umu.cs._5dv147_proj.middleware.message.container.CausalContainer;

import java.util.HashMap;
import java.util.UUID;

import static junit.framework.TestCase.assertEquals;

public class CausalContainerTest {
    private HashMap<UUID, Integer> referenceVector;
    private HashMap<UUID, Integer> messageVector;
    private UUID sender;
    private UUID receiver;
    private UUID person3;
    private UUID person4;
    private UUID person5;
    private CausalContainer cc;


    @Before
    public void setUp() throws Exception {

        this.referenceVector = new HashMap<>();
        this.messageVector = new HashMap<>();
        this.sender = UUID.randomUUID();
        this.receiver = UUID.randomUUID();
        this.person3 = UUID.randomUUID();
        this.person4 = UUID.randomUUID();
        this.person5 = UUID.randomUUID();
    }

    @After
    public void tearDown() throws Exception {
        this.referenceVector = null;
        this.messageVector = null;
        this.sender = null;
        this.receiver = null;
        this.cc = null;
    }



    @Test
    public void initialTest() throws Exception {
        this.referenceVector.put(sender, 1);
        this.referenceVector.put(receiver,2);


        this.messageVector.put(sender, 1);
        this.messageVector.put(receiver, 0);
        this.cc = new CausalContainer(null, this.messageVector, sender);

        assertEquals(true, cc.isDeliverable(referenceVector, sender));

    }


    @Test
    public void testMismatchingVectors() throws Exception {
        this.referenceVector.put(sender, 1);
        this.referenceVector.put(receiver,0);


        this.messageVector.put(sender, 1);
        this.messageVector.put(receiver, 0);
        this.messageVector.put(person3,1);
        this.messageVector.put(person4,2);
        this.messageVector.put(person5,3);
        this.cc = new CausalContainer(null, this.messageVector, sender);

        assertEquals(false, cc.isDeliverable(referenceVector, sender));
        assertEquals(false, cc.isRepeat(referenceVector,sender));
    }
}