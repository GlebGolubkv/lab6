package TernemalManager;

public enum Colors {
    RESET("\033[0;0m"),
    BLACK("\033[0;30m"),
    RED("\033[0;31m"),
    GREEN("\033[0;32m"),
    YELLOW("\033[0;33m"),
    BLUE("\033[0;34m"),
    PURPLE("\033[0;35m"),
    CYAN("\033[0;36m"),
    WHITE("\033[0;37m");

    private final String color;

    /**
     * Enum хранит в себе все цвета, в которые можно красить текст при выводе
     * @param color
     */

    Colors(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return color;
    }
}
