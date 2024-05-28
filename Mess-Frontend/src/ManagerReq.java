import javafx.scene.control.Button;

public class ManagerReq {
	private final String student;
	private final String id;
	private final String type;
	private final String item;
	private final Button statusButton;
	
	public ManagerReq (String student, String id, String type, String item, 
			Button button) {
		this.student = student;
		this.id = id;
		this.type = type;
		this.item = item;
		this.statusButton = button;
	}
	
	public String getStudent() {
		return student;
	}
	
	public String getId() {
		return id;
	}
	
	public String getType() {
		return type;
	}
	
	public String getItem() {
		return item;
	}
	
	public Button getStatusButton() {
		return statusButton;
	}
}
