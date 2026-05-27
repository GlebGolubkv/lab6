package common.dataclasses;

/**
 * Координаты группы на плоскости: {@code x} — целое, {@code y} — вещественное.
 * При создании проверяются верхние границы: x ≤ 254, y ≤ 93.
 */
public class Coordinates {

    private int x;

    private double y;

    /**
     * Конструктор по умолчанию для десериализации JSON.
     */
    public Coordinates() {
    }

    /**
     * Создаёт координаты с проверкой ограничений коллекции.
     *
     * @param x координата X (не больше 254)
     * @param y координата Y (не больше 93)
     * @throws IllegalArgumentException при нарушении границ
     */
    public Coordinates(int x, double y) {

        if (x > 254) {
            throw new IllegalArgumentException("x must be less then 255");
        }
        if (y > 93) {
            throw new IllegalArgumentException("y must be less then 93");
        }
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "Coordinates{x=" + x + ", y=" + y + '}';
    }

}
