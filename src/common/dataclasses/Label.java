package common.dataclasses;


public class Label {

    /**
     * The number of bands associated with the label. Can be null.
     */
    private Integer bands; //Поле может быть null

    /**
     * Default constructor for Label.
     */
    public Label() {
    }

    /**
     * Constructs a Label with the specified number of bands.
     *
     * @param bands the number of bands, may be null
     */
    public Label(Integer bands) {
        this.bands = bands;
    }

    /**
     * Returns the number of bands.
     *
     * @return the bands value, may be null
     */
    public Integer getBands() {
        return bands;
    }

    /**
     * Sets the number of bands.
     *
     * @param bands the new bands value, may be null
     */
    public void setBands(Integer bands) {
        this.bands = bands;
    }

    /**
     * Returns a string representation of the Label object with color formatting.
     *
     * @return a colored string containing the bands value
     */
    @Override
    public String toString() {
        return Colors.WHITE + "Labels= " + Colors.RESET + bands.toString();
    }
}