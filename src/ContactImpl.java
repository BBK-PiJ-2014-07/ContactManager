
public class ContactImpl implements Contact {
	private int id;
	private String name;
	private String notes;
	
	public ContactImpl(int id, String name) {
		this.id = id;
		this.name = name;
		this.notes = null;
	}

	public int getId() {
		// TODO Auto-generated method stub
		return id;
	}

	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}

	
	public String getNotes() {
		// TODO Auto-generated method stub
		return null;
	}

	public void addNotes(String note) {
		// TODO Auto-generated method stub

	}

}
