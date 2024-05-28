public class ManagerFPReq {
	private final String id;
	private final String meal;
	private final String status;
	
	public ManagerFPReq (String id, String meal, String status) {
		this.id = id;
		this.meal = meal;
		this.status = status;
	}
	
	public String getId() {
		return id;
	}
	
	public String getMeal() {
		return meal;
	}
	
	public String getStatus() {
		return status;
	}
}
