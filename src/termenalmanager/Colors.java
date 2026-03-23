package termenalmanager;

/**
 * Provides ANSI color codes for console output formatting.
 * Each constant represents a specific color that can be used to change
 * the appearance of text printed to the terminal.
 */
public enum Colors {
    /** Resets all previous color formatting. */
    RESET("\033[0;0m"),
    /** Black color. */
    BLACK("\033[0;30m"),
    /** Red color. */
    RED("\033[0;31m"),
    /** Green color. */
    GREEN("\033[0;32m"),
    /** Yellow color. */
    YELLOW("\033[0;33m"),
    /** Blue color. */
    BLUE("\033[0;34m"),
    /** Purple color. */
    PURPLE("\033[0;35m"),
    /** Cyan color. */
    CYAN("\033[0;36m"),
    /** White color. */
    WHITE("\033[0;37m");

    /** The ANSI color code string. */
    private final String color;

    /**
     * Constructs a Colors enum constant with the specified ANSI color code.
     *
     * @param color the ANSI color code string
     */
    Colors(String color) {
        this.color = color;
    }

    /**
     * Returns the ANSI color code associated with this enum constant.
     *
     * @return the color code string
     */
    @Override
    public String toString() {
        return color;
    }
}