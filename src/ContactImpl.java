
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
	public boolean equals(Object o){
		Contact c = (ContactImpl) o;
		if (c == null) {
			return false;
		} else if (c.getId() != this.getId()){
			return false; 
		} else {
			if (c.getName() != this.getName()) {
				return false;
			} else {
				if (c.getNotes() != this.getNotes()){
					return false;
				} else {
					return true;
				}
			}
		}
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
