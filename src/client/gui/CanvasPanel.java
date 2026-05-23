package client.gui;

import common.dataclasses.MusicBandEntry;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.function.Consumer;

/**
 * Visualises collection entries with coordinates and sizes; colour per owner.
 */
public class CanvasPanel extends StackPane {

    private static final Color[] OWNER_COLORS = {
            Color.CORAL, Color.DODGERBLUE, Color.MEDIUMSEAGREEN, Color.GOLD,
            Color.MEDIUMPURPLE, Color.DARKORANGE, Color.TEAL, Color.HOTPINK
    };

    private final Canvas canvas = new Canvas(520, 420);
    private final List<DrawnShape> shapes = new ArrayList<>();
    private final Set<Integer> animatedKeys = new HashSet<>();
    private List<MusicBandEntry> entries = List.of();
    private Consumer<MusicBandEntry> onSelect;
    private Locale locale = Localization.LOCALE_RU;

    public CanvasPanel() {
        getChildren().add(canvas);
        canvas.widthProperty().bind(widthProperty());
        canvas.heightProperty().bind(heightProperty());
        widthProperty().addListener((o, a, b) -> redraw(false));
        heightProperty().addListener((o, a, b) -> redraw(false));
        canvas.setOnMouseClicked(e -> {
            Point2D p = new Point2D(e.getX(), e.getY());
            for (DrawnShape shape : shapes) {
                if (shape.contains(p)) {
                    if (onSelect != null) {
                        onSelect.accept(shape.entry());
                    }
                    break;
                }
            }
        });
        setMinSize(320, 240);
    }

    public void setOnSelect(Consumer<MusicBandEntry> onSelect) {
        this.onSelect = onSelect;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public void setEntries(List<MusicBandEntry> entries, boolean playAnimation) {
        this.entries = entries != null ? List.copyOf(entries) : List.of();
        redraw(playAnimation);
    }

    private void redraw(boolean playAnimation) {
        double w = canvas.getWidth() > 0 ? canvas.getWidth() : 520;
        double h = canvas.getHeight() > 0 ? canvas.getHeight() : 420;
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, w, h);
        gc.setFill(Color.web("#1e1e2e"));
        gc.fillRect(0, 0, w, h);
        shapes.clear();

        for (MusicBandEntry entry : entries) {
            DrawnShape shape = computeShape(entry, w, h);
            shapes.add(shape);
            Color fill = OWNER_COLORS[Math.floorMod(entry.getOwnerId(), OWNER_COLORS.length)];
            gc.setFill(fill.deriveColor(0, 1, 1, 0.75));
            gc.setStroke(Color.WHITE);
            gc.setLineWidth(1.5);
            gc.fillOval(shape.x(), shape.y(), shape.size(), shape.size());
            gc.strokeOval(shape.x(), shape.y(), shape.size(), shape.size());
            gc.setFill(Color.WHITE);
            gc.fillText(entry.getMusicBand().getName(), shape.x() + 4, shape.y() + shape.size() / 2.0);

            if (playAnimation && animatedKeys.add(entry.getBandKey())) {
                playAppearAnimation(shape);
            }
        }
    }

    private void playAppearAnimation(DrawnShape shape) {
        FadeTransition fade = new FadeTransition(Duration.millis(600), canvas);
        fade.setFromValue(0.3);
        fade.setToValue(1.0);
        ScaleTransition scale = new ScaleTransition(Duration.millis(500), canvas);
        scale.setFromX(0.85);
        scale.setFromY(0.85);
        scale.setToX(1.0);
        scale.setToY(1.0);
        fade.play();
        scale.play();
    }

    public void resetAnimationKeys() {
        animatedKeys.clear();
    }

    private DrawnShape computeShape(MusicBandEntry entry, double w, double h) {
        double xCoord = entry.getMusicBand().getCoordinates().getX();
        double yCoord = entry.getMusicBand().getCoordinates().getY();
        double px = (xCoord / 254.0) * (w - 40) + 20;
        double py = (yCoord / 93.0) * (h - 40) + 20;
        double size = 18 + Math.min(80, entry.getMusicBand().getAlbumsCount() * 3
                + entry.getMusicBand().getNumberOfParticipants() / 5.0);
        return new DrawnShape(entry, px, py, size);
    }

    public String infoText(MusicBandEntry entry) {
        var b = entry.getMusicBand();
        return Localization.getInstance().get("info.selected") + "\n"
                + "key=" + entry.getBandKey() + ", id=" + b.getId() + "\n"
                + b.getName() + "\n"
                + "x=" + Formats.formatNumber(locale, b.getCoordinates().getX())
                + ", y=" + Formats.formatNumber(locale, b.getCoordinates().getY());
    }

    private record DrawnShape(MusicBandEntry entry, double x, double y, double size) {
        boolean contains(Point2D p) {
            double cx = x + size / 2;
            double cy = y + size / 2;
            double r = size / 2;
            return p.distance(cx, cy) <= r;
        }
    }
}
