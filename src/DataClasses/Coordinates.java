package DataClasses;

import TernemalManager.Colors;

/**
 * Класс, который описывает объект типа Coordinates
 */
public class Coordinates {
    private int x; //Максимальное значение поля: 254
    private double y; //Максимальное значение поля: 93

    public Coordinates() {
    }

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
        return Colors.WHITE + "Coordinates{" + "x=" + Colors.RESET + x + Colors.WHITE + ", y=" + Colors.RESET + y + Colors.WHITE + '}' + Colors.RESET;
    }

}
