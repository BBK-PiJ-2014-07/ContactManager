
public class ContactImpl implements Contact {
	private int id;
	private String name;
	private String notes;
	
	public ContactImpl(int id, String name, String notes) {
		this.name = name;
		this.notes = notes;
		this.id = id;
	}
	
	@Override
	public boolean equals(Object obj){
		if (obj.getClass() != this.getClass()) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			//DEBUG - System.out.println("Object is null");
			return false;
		}
		Contact otherContact = (ContactImpl) obj;
		if (otherContact.getId() != this.id){
			// DEBUG System.out.println("ID mismatch");
			return false;
		}
		if (!this.name.equals(otherContact.getName())) {
			// DEBUG System.out.println("Name mismatch");
				return false;
		}
		if (!this.notes.equals(otherContact.getNotes())){
			//DEBUG System.out.println("Notes mismatch");
			return false;
		}
		return true;
				
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	
	public String getNotes() {
		return notes;
	}

	public void addNotes(String note) {
		//concatenate old notes with new notes
		this.notes = notes + ", " +  note;
	}

}
