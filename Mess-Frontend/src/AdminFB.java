public class AdminFB {
    private final int id;
    private final String student;
    private final String comment;

    public AdminFB(int id, String student, String comment) {
        this.id = id;
        this.student = student;
        this.comment = comment;
    }

    public int getId() {
        return id;
    }

    public String getStudent() {
        return student;
    }

    public String getComment() {
        return comment;
    }
}
