public class LeaveApplication {
    private final String dates;
    private final String status;

    public LeaveApplication(String dates, String status) {
        this.dates = dates;
        this.status = status;
    }

	public String getDates() {
        return dates;
    }

    public String getStatus() {
        return status;
    }
}