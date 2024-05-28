import javafx.scene.control.Button;

public class MealEntry {
    private final String meal;
    private final String time;
    private final Button optButton;

    public MealEntry(String meal, String time, Button button) {
        this.meal = meal;
        this.time = time;
        this.optButton = button;
    }

	public String getMeal() {
        return meal;
    }

    public String getTime() {
        return time;
    }

    public Button getOptButton() {
        return optButton;
    }
}