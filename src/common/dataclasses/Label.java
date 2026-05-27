package common.dataclasses;

/**
 * Метка (лейбл) музыкальной группы — число связанных групп ({@code bands}).
 */
public class Label {

    private Integer bands;

    /**
     * Конструктор по умолчанию для десериализации JSON.
     */
    public Label() {
    }

    /**
     * Создаёт метку с заданным числом связанных групп.
     *
     * @param bands количество групп на метке
     */
    public Label(Integer bands) {
        this.bands = bands;
    }

    /**
     * @return число групп на метке
     */
    public Integer getBands() {
        return bands;
    }

    /**
     * @param bands число групп на метке
     */
    public void setBands(Integer bands) {
        this.bands = bands;
    }

    @Override
    public String toString() {
        return "Labels=" + bands;
    }
}
