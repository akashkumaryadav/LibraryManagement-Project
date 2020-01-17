package application.models;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

public  class Books {
	
	private final SimpleStringProperty title;
	private final SimpleStringProperty author;
	private final SimpleStringProperty id;
	private final SimpleStringProperty publisher;
	private final SimpleBooleanProperty available;
		
	public Books(String title, String author, String id, String publisher, Boolean available) {
		this.title = new SimpleStringProperty(title);
		this.author = new SimpleStringProperty(author);
		this.id = new SimpleStringProperty(id);
		this.publisher = new SimpleStringProperty(publisher);
		this.available = new SimpleBooleanProperty(available);
	}
	public String getTitle() {
		return title.get();
	}
	public String getAuthor() {
		return author.get();
	}
	public String getId() {
		return id.get();
	}
	public String getPublisher() {
		return publisher.get();
	}
	public Boolean getAvailable() {
		return available.get();
	}
	

}
