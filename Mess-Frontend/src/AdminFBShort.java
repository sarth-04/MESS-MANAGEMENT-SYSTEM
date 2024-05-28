public class AdminFBShort {
    private final int feedbackId;
    private final int studentId;
    private final String studentName;
    private final String message;
    private String remark;

    public AdminFBShort(int feedbackId, int studentId, String studentName, String message, String remark) {
        this.feedbackId = feedbackId;
        this.studentId = studentId;
        this.studentName = studentName;
        this.message = message;
        this.remark = remark;
    }

    public int getFeedbackId() {
        return feedbackId;
    }

    public int getStudentId() {
        return studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getMessage() {
        return message;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
