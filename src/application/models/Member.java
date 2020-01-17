package application.models;

import javafx.beans.property.SimpleStringProperty;

public class Member {
	
	private final SimpleStringProperty name;
	private final SimpleStringProperty phone;
	private final SimpleStringProperty id;
	private final SimpleStringProperty email;
	
	public Member(String name,String id,String phone,String email){
		this.name = new SimpleStringProperty(name);
		this.id   = new SimpleStringProperty(id);
		this.phone= new SimpleStringProperty(phone);
		this.email=new 	SimpleStringProperty(email);
	}

	public String getName() {
		return name.get();
	}

	public String getPhone() {
		return phone.get();
	}

	public String getId() {
		return id.get();
	}

	public String getEmail() {
		return email.get();
	}
	

}
