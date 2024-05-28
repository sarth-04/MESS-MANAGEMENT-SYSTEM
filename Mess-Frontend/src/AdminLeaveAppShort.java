import javafx.scene.control.Button;

public class AdminLeaveAppShort {
    private final String student;
    private final String dates;
    private final Button approveButton;

    public AdminLeaveAppShort(String student, String dates, Button button) {
        this.student = student;
        this.dates = dates;
        this.approveButton = button;
    }

    public String getStudent() {
        return student;
    }

    public String getDates() {
        return dates;
    }

    public Button getApproveButton() {
        return approveButton;
    }
}
