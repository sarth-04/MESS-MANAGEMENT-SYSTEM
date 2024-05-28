import javafx.scene.control.TextFormatter;
import javafx.util.StringConverter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.function.UnaryOperator;

public class FormatterHelper {

    public static TextFormatter<LocalDate> createDateFormatter() {
        // Create a formatter for LocalDate
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // Create a converter to convert between String and LocalDate
        StringConverter<LocalDate> converter = new StringConverter<LocalDate>() {
            @Override
            public String toString(LocalDate date) {
                return (date != null) ? dateFormatter.format(date) : "";
            }

            @Override
            public LocalDate fromString(String string) {
                return (string != null && !string.isEmpty()) ? LocalDate.parse(string, dateFormatter) : null;
            }
        };

        // Create a filter to restrict input to valid date formats
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d{0,2}/\\d{0,2}/\\d{0,4}")) {
                return change;
            }
            return null;
        };

        // Return the TextFormatter with the specified converter and filter
        return new TextFormatter<>(converter, null, filter);
    }
}
