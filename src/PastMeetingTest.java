import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Set;

import org.junit.*;

import static org.junit.Assert.*;

public class PastMeetingTest {
	private PastMeeting pm;
	private Set<Contact> contactSet;

	@Before
	public void buildUp(){
		Contact alan = new ContactImpl(1, "Alan");
		Contact sarah = new ContactImpl(2, "Sarah");
		contactSet = new HashSet<Contact>();
		contactSet.add(alan);
		contactSet.add(sarah);
		Calendar cal = new GregorianCalendar(2011,11,01);
		pm = new PastMeetingImpl(cal, "Meeting notes");
	}
	
	@Test
	public void testGetId(){
		assertEquals(pm.getId(), 1);
	}
	
	@SuppressWarnings("static-access")
	@Test
	public void testGetDate(){
		Calendar newCal = pm.getDate();
		assertEquals(newCal.get(newCal.YEAR),2011);
	}

	@Test
	public void testGetContacts(){
		Set<Contact> newSet = pm.getContacts();
		assertTrue(newSet.containsAll(contactSet));
	}
}
