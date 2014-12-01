import org.junit.*;
import static org.junit.Assert.*;


public class ContactTest {
	private Contact person;
	
	@Before
	public void buildUp(){
		person = new ContactImpl (1, "Alan", "Likes cheese");
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
		assertEquals(person.getNotes(), "Likes cheese");
	}
	
	@Test
	public void testAddNotes(){
		person.addNotes("Speaks French");
		System.out.println(person.getNotes());
		assertEquals(person.getNotes(), "Likes cheese, Speaks French");
	}
	
	@After
	public void closeDown(){
		//close the file
	}
}
