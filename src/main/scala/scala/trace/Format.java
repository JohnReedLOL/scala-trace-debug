package scala.trace;

/**
 * Used for formatting strings.
 */
public class Format {
    private static int _lineLength = 100;

    public static int getLineLength() {
        return _lineLength;
    }

    /**
     * Sets the line wrap to the given length
     *
     * @param length min length before line wrap occurs
     */
    public static void setLineLength(int length) {
        if (length < 1) {
            throw new IllegalArgumentException("Line wrap must occur after one or more characters");
        }
        _lineLength = length;
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
            if (lineLength > _lineLength) {
                formattedText += "\n"; // newline every lineWrap elements
                lineLength = 0;
            }
        }
        return formattedText;
    }
}
