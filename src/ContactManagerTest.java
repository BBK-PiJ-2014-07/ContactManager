import org.junit.*;

import static org.junit.Assert.*;

import java.util.GregorianCalendar;
import java.util.Set;
import java.util.HashSet;

public class ContactManagerTest {
	private ContactManager cm;
	private Set<Contact> contacts;
	private FutureMeeting fm;
	
	@Before
	public void buildUp(){
		cm = new ContactManagerImpl();
		contacts = new HashSet<Contact>();
		contacts.add(new ContactImpl(1, "Alan", "nice"));
		contacts.add(new ContactImpl(2, "Sarah", "horrible"));
		fm = new FutureMeetingImpl(contacts, new GregorianCalendar(2015,1,12));
		
	}
	
	@Test
	public void testAddFutureMeeting(){
		int newFutureMeetingId = cm.addFutureMeeting(contacts, new GregorianCalendar(2015,1,12));	//13 feb 2015
		assertEquals(fm.getId(), newFutureMeetingId);
	}
}
