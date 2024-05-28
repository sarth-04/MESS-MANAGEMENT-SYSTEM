public class ManagerAOReq {
    private final String id;
    private final String name; // Add name field for student's name
    private final String meal;
    private final String addon; // Add addon field

    public ManagerAOReq(String id, String name, String meal, String addon) {
        this.id = id;
        this.name = name;
        this.meal = meal;
        this.addon = addon;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getMeal() {
        return meal;
    }

    public String getAddon() {
        return addon;
    }
}
