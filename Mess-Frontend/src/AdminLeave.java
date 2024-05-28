public class AdminLeave {
    private final int id;
    private final String student;
    private final String dates;
    private final String comment;

    public AdminLeave(int id, String student, String dates, String comment) {
        this.id = id;
        this.student = student;
        this.dates = dates;
        this.comment = comment;
    }

    public int getId() {
        return id;
    }

    public String getStudent() {
        return student;
    }

    public String getDates() {
        return dates;
    }

    public String getComment() {
        return comment;
    }
}
