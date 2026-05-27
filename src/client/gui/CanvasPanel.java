package client.gui;

import common.dataclasses.MusicBandEntry;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * Панель визуализации коллекции: координаты X/Y, размер маркера по числу альбомов, цвет по владельцу.
 */
public class CanvasPanel extends StackPane {

    private static final double X_MAX = 254;
    private static final double Y_MAX = 93;
    private static final int TICK_STEP = 20;
    private static final double BASE_SIZE = 22;
    /** Общий масштаб диаметра маркеров (2/3 ≈ в 1.5 раза меньше базовых). */
    private static final double MARKER_SCALE = 2.0 / 3.0;
    /** Опорное число альбомов для нормализации размера (логарифмическая шкала). */
    private static final double ALBUMS_SIZE_REF = 200.0;
    private static final double SIZE_SCALE_MIN = 0.55;
    private static final double SIZE_SCALE_SPAN = 2.25;

    private static final Color[] OWNER_COLORS = {
            Color.CORAL, Color.DODGERBLUE, Color.MEDIUMSEAGREEN, Color.GOLD,
            Color.MEDIUMPURPLE, Color.DARKORANGE, Color.TEAL, Color.HOTPINK
    };

    private final Canvas canvas = new Canvas();
    private final CanvasMarkerAnimator markerAnimator = new CanvasMarkerAnimator();
    private final List<DrawnShape> shapes = new ArrayList<>();
    private List<MusicBandEntry> entries = List.of();
    private Consumer<MusicBandEntry> onSelect;
    private Consumer<MusicBandEntry> onDoubleSelect;
    private Integer selectedBandKey;

    /**
     * Создаёт холст с обработкой клика по маркеру группы.
     */
    public CanvasPanel() {
        getChildren().add(canvas);
        setMinSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
        setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        markerAnimator.setRedrawRequest(this::redraw);

        canvas.setOnMouseClicked(e -> {
            Point2D p = new Point2D(e.getX(), e.getY());
            for (int i = shapes.size() - 1; i >= 0; i--) {
                DrawnShape shape = shapes.get(i);
                if (shape.contains(p)) {
                    MusicBandEntry entry = shape.entry();
                    selectEntry(entry);
                    if (e.getClickCount() >= 2) {
                        if (onDoubleSelect != null) {
                            onDoubleSelect.accept(entry);
                        }
                    } else if (onSelect != null) {
                        onSelect.accept(entry);
                    }
                    return;
                }
            }
        });
    }

    /**
     * Подстраивает размер холста под панель и перерисовывает график при изменении layout.
     */
    @Override
    protected void layoutChildren() {
        super.layoutChildren();
        double w = getWidth();
        double h = getHeight();
        if (w > 1 && h > 1) {
            canvas.setWidth(w);
            canvas.setHeight(h);
            redraw();
        }
    }

    /**
     * @param onSelect вызывается при выборе группы кликом по маркеру
     */
    public void setOnSelect(Consumer<MusicBandEntry> onSelect) {
        this.onSelect = onSelect;
    }

    /**
     * @param onDoubleSelect вызывается при двойном клике по маркеру
     */
    public void setOnDoubleSelect(Consumer<MusicBandEntry> onDoubleSelect) {
        this.onDoubleSelect = onDoubleSelect;
    }

    /**
     * Обновляет отображаемые записи и перерисовывает график.
     *
     * @param entries        список записей коллекции
     * @param playAnimation не используется: анимации запускаются по diff в {@link CanvasMarkerAnimator}
     */
    public void setEntries(List<MusicBandEntry> entries, boolean playAnimation) {
        this.entries = entries != null ? List.copyOf(entries) : List.of();
        markerAnimator.sync(this.entries);
        if (selectedBandKey != null
                && this.entries.stream().noneMatch(e -> Objects.equals(selectedBandKey, e.getBandKey()))) {
            selectedBandKey = null;
            markerAnimator.setSelectedKey(null);
        }
        requestLayout();
        redraw();
    }

    /**
     * Выделяет запись на графике (синхронизация с таблицей).
     *
     * @param entry выбранная запись или {@code null} для сброса
     */
    public void setSelectedEntry(MusicBandEntry entry) {
        Integer key = entry != null ? entry.getBandKey() : null;
        if (Objects.equals(selectedBandKey, key)) {
            markerAnimator.setSelectedKey(key);
            return;
        }
        selectedBandKey = key;
        markerAnimator.setSelectedKey(key);
        redraw();
    }

    private void selectEntry(MusicBandEntry entry) {
        setSelectedEntry(entry);
    }

    private void redraw() {
        double w = canvas.getWidth();
        double h = canvas.getHeight();
        if (w <= 1 || h <= 1) {
            return;
        }

        PlotArea plot = computePlotArea(w, h);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, w, h);
        gc.setFill(Color.web("#1e1e2e"));
        gc.fillRect(0, 0, w, h);

        drawGridAndAxes(gc, plot, w, h);
        shapes.clear();

        List<MusicBandEntry> toDraw = markerAnimator.drawableEntries(entries);
        toDraw.sort(Comparator.comparingInt(e -> isSelected(e) ? 1 : 0));

        for (MusicBandEntry entry : toDraw) {
            boolean selected = isSelected(entry);
            CanvasMarkerAnimator.DrawOverride draw = markerAnimator.getDrawOverride(entry, selected);
            if (draw.opacity() < 0.02) {
                continue;
            }
            DrawnShape shape = computeShape(entry, plot, selected, draw);
            shapes.add(shape);
            drawBand(gc, shape, selected, draw.opacity());
        }
    }

    private boolean isSelected(MusicBandEntry entry) {
        return selectedBandKey != null && Objects.equals(selectedBandKey, entry.getBandKey());
    }

    
    private PlotArea computePlotArea(double totalW, double totalH) {
        final double marginLeft = 34;
        final double marginRight = 28;
        final double marginTop = 18;
        final double marginBottom = 26;

        double availW = totalW - marginLeft - marginRight;
        double availH = totalH - marginTop - marginBottom;
        availW = Math.max(60, availW);
        availH = Math.max(40, availH);

        double domainAspect = X_MAX / Y_MAX;
        double plotW;
        double plotH;
        if (availW / availH >= domainAspect) {
            plotH = availH;
            plotW = plotH * domainAspect;
        } else {
            plotW = availW;
            plotH = plotW / domainAspect;
        }

        double left = marginLeft + (availW - plotW) / 2;
        double top = marginTop + (availH - plotH) / 2;
        return new PlotArea(left, top, plotW, plotH);
    }

    private void drawGridAndAxes(GraphicsContext gc, PlotArea plot, double canvasW, double canvasH) {
        gc.setStroke(Color.web("#3a3a4a"));
        gc.setLineWidth(1);

        for (int x = 0; x <= (int) X_MAX; x += TICK_STEP) {
            double px = plot.xForValue(x);
            gc.strokeLine(px, plot.top(), px, plot.bottom());
        }
        for (int y = 0; y <= (int) Y_MAX; y += TICK_STEP) {
            double py = plot.yForValue(y);
            gc.strokeLine(plot.left(), py, plot.right(), py);
        }

        gc.setStroke(Color.web("#c8c8d8"));
        gc.setLineWidth(2);
        gc.strokeLine(plot.left(), plot.bottom(), plot.right(), plot.bottom());
        gc.strokeLine(plot.left(), plot.top(), plot.left(), plot.bottom());

        gc.setFill(Color.web("#a0a0b0"));
        gc.setFont(Font.font(10));

        gc.setTextAlign(TextAlignment.CENTER);
        for (int x = 0; x <= (int) X_MAX; x += TICK_STEP) {
            double px = plot.xForValue(x);
            gc.fillText(String.valueOf(x), px, plot.bottom() + 12);
        }

        gc.setTextAlign(TextAlignment.RIGHT);
        for (int y = 0; y <= (int) Y_MAX; y += TICK_STEP) {
            double py = plot.yForValue(y);
            gc.fillText(String.valueOf(y), plot.left() - 5, py + 4);
        }

        gc.setFont(Font.font(12));
        gc.setTextAlign(TextAlignment.LEFT);
        gc.fillText("Y", 6, 14);

        gc.setTextAlign(TextAlignment.RIGHT);
        gc.fillText("X", canvasW - 8, canvasH - 6);
    }

    private void drawBand(GraphicsContext gc, DrawnShape shape, boolean selected, double opacity) {
        MusicBandEntry entry = shape.entry();
        Color fill = OWNER_COLORS[Math.floorMod(entry.getOwnerId(), OWNER_COLORS.length)];
        double baseAlpha = (selected ? 0.92 : 0.75) * opacity;
        gc.setFill(fill.deriveColor(0, 1, 1, baseAlpha));
        gc.setStroke(selected ? Color.WHITE : fill.brighter());
        gc.setLineWidth((selected ? 2.5 : 1.2) * MARKER_SCALE);
        gc.fillOval(shape.drawX(), shape.drawY(), shape.displaySize(), shape.displaySize());
        gc.strokeOval(shape.drawX(), shape.drawY(), shape.displaySize(), shape.displaySize());
        gc.setFill(Color.WHITE);
        gc.setTextAlign(TextAlignment.LEFT);
        gc.setFont(Font.font(10 * MARKER_SCALE));
        String name = entry.getMusicBand().getName();
        if (name != null && name.length() > 12) {
            name = name.substring(0, 11) + "…";
        }
        gc.fillText(name != null ? name : "?", shape.drawX() + 2, shape.drawY() + shape.displaySize() / 2.0 + 4);
    }

    private DrawnShape computeShape(
            MusicBandEntry entry,
            PlotArea plot,
            boolean selected,
            CanvasMarkerAnimator.DrawOverride draw
    ) {
        double xCoord = clamp(draw.x(), 0, X_MAX);
        double yCoord = clamp(draw.y(), 0, Y_MAX);
        double cx = plot.xForValue(xCoord);
        double cy = plot.yForValue(yCoord);
        double display = BASE_SIZE * MARKER_SCALE * sizeScaleForAlbums(draw.albumsCount()) * draw.sizeMultiplier();
        if (selected) {
            display *= 1.15;
        }
        return new DrawnShape(entry, cx, cy, cx - display / 2, cy - display / 2, display);
    }

    private static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

    /**
     * Плавный масштаб по числу альбомов (log): низкие значения не скачут, высокие ещё различимы.
     * Примеры: 5 → ~1.3, 15 → ~1.7, 50 → ~2.2, 200 → ~2.8.
     */
    private double sizeScaleForAlbums(long albumsCount) {
        long albums = Math.max(1, albumsCount);
        double logRef = Math.log1p(ALBUMS_SIZE_REF);
        double t = Math.min(1.0, Math.log1p(albums) / logRef);
        return SIZE_SCALE_MIN + SIZE_SCALE_SPAN * t;
    }

    /** Область построения графика с преобразованием координат данных в пиксели. */
    private record PlotArea(double left, double top, double width, double height) {
        double right() {
            return left + width;
        }

        double bottom() {
            return top + height;
        }

        double xForValue(double x) {
            return left + (x / X_MAX) * width;
        }

        double yForValue(double y) {
            return top + height - (y / Y_MAX) * height;
        }
    }

    /**
     * Отрисованный маркер группы: позиция, размер и проверка попадания курсора.
     */
    /** Маркер группы на холсте с геометрией для обработки клика. */
    private record DrawnShape(
            MusicBandEntry entry,
            double centerX,
            double centerY,
            double drawX,
            double drawY,
            double displaySize
    ) {
        boolean contains(Point2D p) {
            double r = displaySize / 2;
            return p.distance(centerX, centerY) <= r;
        }
    }
}
