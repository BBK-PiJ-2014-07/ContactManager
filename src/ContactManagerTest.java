import org.junit.*;

import static org.junit.Assert.*;

import java.util.GregorianCalendar;
import java.util.Set;
import java.util.HashSet;

public class ContactManagerTest {
	private ContactManager cm;
	private Set<Contact> contacts;
	private FutureMeeting fm;
	private Contact alan;
	
	@Before
	public void buildUp(){
		cm = new ContactManagerImpl();
		contacts = new HashSet<Contact>();
		alan = new ContactImpl(1, "Alan", "nice");
		contacts.add(alan);
		//contacts.add(new ContactImpl(2, "Sarah", "horrible"));
		fm = new FutureMeetingImpl(contacts, new GregorianCalendar(2015,1,12));
		
	}
	/**
	 * While the IO hasn't been written for ContactManagerImpl, this test is failing.
	 * this method throws an IllegalArgumentException which technically it should, 
	 * because it is checking the set of contacts I'm passing in against an empty set
	 * of contacts. This test should pass if I instantiate contactList in ContactManagerImpl with the contacts above.
	 */
	@Test
	public void testAddFutureMeeting(){
		int newFutureMeetingId = cm.addFutureMeeting(contacts, new GregorianCalendar(2015,1,12));	//13 feb 2015
		assertEquals(fm.getId(), newFutureMeetingId);
	}

	@Test
	public void testGetContactsWithString(){
		Set<Contact> getCont = cm.getContacts("Alan");
		assertTrue(getCont.contains(alan));
	}
}
