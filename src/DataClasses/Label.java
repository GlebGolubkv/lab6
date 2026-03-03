package DataClasses;

import TernemalManager.Colors;

/**
 * Класс, который описывает объект типа Label
 */
public class Label {

    private Integer bands; //Поле может быть null


    public Label() {
    }

    public Label(Integer bands) {
        this.bands = bands;
    }

    public Integer getBands() {
        return bands;
    }

    public void setBands(Integer bands) {
        this.bands = bands;
    }

    @Override
    public String toString() {
        return Colors.WHITE + "Labels= " + Colors.RESET + bands.toString();
    }
}
