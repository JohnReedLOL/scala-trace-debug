package scala.trace;

/**
 * Created by johnreed on 5/7/16.
 * Used for formatting strings.
 */
public class Format {
    private static int _lineWrap = 100;

    public static int getLineWrap() {
        return _lineWrap;
    }

    /**
     * Sets the line wrap to the given length
     *
     * @param length min length before line wrap occurs
     */
    public static void setLineWrap(int length) {
        if (length < 1) {
            throw new IllegalArgumentException("Line wrap must occur after one or more characters");
        }
        _lineWrap = length;
    }

    /**
     * Get the formatted text with default delimiter (" ")
     */
    public static String text(String toFormat) {
        return text(toFormat, " "); // default delimiter is whitespace
    }

    /**
     * Get the formatted text
     * @param toFormat the text to format
     * @param delimiter the delimiter
     */
    public static String text(String toFormat, String delimiter) {
        String[] splitText = toFormat.split(delimiter);
        int lineLength = 0;
        String formattedText = "";
        for (String word : splitText) {
            formattedText += word + delimiter;
            lineLength += word.length() + delimiter.length();
            if (lineLength > _lineWrap) {
                formattedText += "\n"; // newline every lineWrap elements
                lineLength = 0;
            }
        }
        return formattedText;
    }
}
