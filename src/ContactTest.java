import org.junit.*;
import static org.junit.Assert.*;


public class ContactTest {
	private Contact person;
	
	@Before
	public void buildUp(){
		person = new ContactImpl(1, "Alan");
		//open the file, create details
	}
	
	@Test
	public void testGetId(){
		//test ID getter
		assertEquals(person.getId(), 1);
	}
	
	@Test
	public void testGetName(){
		assertEquals(person.getName(), "Alan");
	}
	
	@Test
	public void testGetNotes(){
		assertEquals(person.getNotes(), "");
	}
	
	@After
	public void closeDown(){
		//close the file
	}
}
