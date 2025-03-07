import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Scanner;

public class GeoGuesserPanel extends JPanel {
    private Image image;
    private final ArrayList<Point> points = new ArrayList<>();
    private int mouseX = 0, mouseY = 0;
    private static ImageToGeoConverter converter = new ImageToGeoConverter();
    static int hamburgx = 0;
    static int hamburgy = 0;
    static Scanner sc = new Scanner(System.in);

    public GeoGuesserPanel(
            String imagePath) {
        double lat = 53.5670;
        double lon = 10.0330;
        int[] pixelCoords = ImageToGeoConverter.convertGeoToPixel(lat, lon);
        points.add(new Point(pixelCoords[0], pixelCoords[1]));
        lat = 52.5230;
        lon = 13.4130;
        pixelCoords = ImageToGeoConverter.convertGeoToPixel(lat, lon);
        points.add(new Point(pixelCoords[0], pixelCoords[1]));
        // Lade das Bild
        image = new ImageIcon(imagePath).getImage();

        // Mausbewegung verfolgen
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                mouseX = e.getX();
                mouseY = e.getY();
                repaint(); // Panel aktualisieren

            }
        });

        // Punkte bei Klick hinzufügen
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                points.add(new Point(e.getX(), e.getY()));
                SQL_Tester.coordianteValidation(ImageToGeoConverter.convertPixelToGeo(e.getX(), e.getY())[0],
                        ImageToGeoConverter.convertPixelToGeo(e.getX(), e.getY())[1]);
                System.out.println(ImageToGeoConverter.convertPixelToGeo(e.getX(), e.getY())[0]);
                repaint(); // Panel aktualisieren
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Bild zeichnen
        if (image != null) {
            g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
        }

        // Punkte zeichnen
        g.setColor(Color.RED);
        for (Point p : points) {
            g.fillOval(p.x - 5, p.y - 5, 10, 10); // Punkte mit Durchmesser 10px
        }

        // Mausposition anzeigen
        g.setColor(Color.BLACK);
        g.drawString("X: " + mouseX + ", Y: " + mouseY, 10, 20);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Bild mit Punkten und Maus-Tracking");
            GeoGuesserPanel panel = new GeoGuesserPanel(
                    "C:\\Users\\Lasse\\Documents\\OOP\\Adventure\\School\\GeoQuiz\\germany.jpg"); // Bilddatei anpassen

            frame.add(panel);
            frame.setSize(626, 626);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }
}
