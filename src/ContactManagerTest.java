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
	private Contact sarah;
	
	@Before
	public void buildUp(){
		cm = new ContactManagerImpl();
		contacts = new HashSet<Contact>();
		alan = new ContactImpl(1, "Alan", "nice");
		contacts.add(alan);
		sarah = new ContactImpl(2, "Sarah", "horrible");
		contacts.add(sarah);
		fm = new FutureMeetingImpl(contacts, new GregorianCalendar(2015,1,12));
		cm.addNewContact("Alan", "nice");
		cm.addNewContact("Sarah", "horrible");
	}

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

	@Test
	public void testGetContactsWithSingleId(){
		Set<Contact> getCont = cm.getContacts(1);
		assertTrue(getCont.contains(alan));
	}

	@Test
	public void testGetContactsWithMultipleIds(){
		Set<Contact> getCont = cm.getContacts(1,2);
		assertTrue(getCont.containsAll(contacts));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetContactsWithIllegalId(){
		Set<Contact> getCont = cm.getContacts(12);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetContactsWithIllegalName(){
		Set<Contact> getCont = cm.getContacts("Frodo");
	}
	@Test
	public void testAddNewContact(){
		cm.addNewContact("Buffy", "vampire slayer");
		Set<Contact> getCont = cm.getContacts("Buffy");
		assertFalse(getCont.isEmpty());
	}
}
