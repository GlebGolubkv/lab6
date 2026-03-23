package dataclasses;

import termenalmanager.Colors;

/**
 * Represents the coordinates of a music band.
 * Contains x and y coordinates with specific value constraints.
 */
public class Coordinates {
    /**
     * The x-coordinate. Maximum allowed value is 254.
     */
    private int x; //Максимальное значение поля: 254

    /**
     * The y-coordinate. Maximum allowed value is 93.
     */
    private double y; //Максимальное значение поля: 93

    /**
     * Default constructor for Coordinates.
     */
    public Coordinates() {
    }

    /**
     * Constructs a Coordinates object with the specified x and y values.
     *
     * @param x the x-coordinate, must be less than 255
     * @param y the y-coordinate, must be less than 93
     * @throws IllegalArgumentException if x >= 255 or y >= 93
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

    /**
     * Returns the x-coordinate.
     *
     * @return the x value
     */
    public int getX() {
        return x;
    }

    /**
     * Returns the y-coordinate.
     *
     * @return the y value
     */
    public double getY() {
        return y;
    }

    /**
     * Sets the x-coordinate.
     *
     * @param x the new x value (constraints should be enforced by caller)
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Sets the y-coordinate.
     *
     * @param y the new y value (constraints should be enforced by caller)
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * Returns a string representation of the Coordinates object with color formatting.
     *
     * @return a colored string containing the coordinates
     */
    @Override
    public String toString() {
        return Colors.WHITE + "Coordinates{" + "x=" + Colors.RESET + x + Colors.WHITE + ", y=" + Colors.RESET + y + Colors.WHITE + '}' + Colors.RESET;
    }

}