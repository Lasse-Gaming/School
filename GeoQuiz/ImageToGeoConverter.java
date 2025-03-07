public class ImageToGeoConverter {
    // Eckkoordinaten der Karte
    // 54.849877, 3.499151
    // 46.911620, 17.208829

    static double latTop = 55.65; // Breitengrad oben
    static double lonLeft = 3.05; // Längengrad rechts
    static double latBottom = 46.9; // Breitengrad unten
    static double lonRight = 17.808829; // Längengrad links

    // Bildgröße in Pixeln
    static int imageWidth = 626;
    static int imageHeight = 626;

    public static void main(String[] args) {
        // Beispiel: Pixel zu Geokoordinaten
        int pixelX = 500;
        int pixelY = 500;
        double[] latLon = convertPixelToGeo(pixelX, pixelY);
        System.out.printf("Pixel (%d, %d) → Breitengrad: %.6f°, Längengrad: %.6f°\n",
                pixelX, pixelY, latLon[0], latLon[1]);

        // Beispiel: Geokoordinaten zu Pixel
        double lat = 53.5670;
        double lon = 10.0330;
        int[] pixelCoords = convertGeoToPixel(lat, lon);
        System.out.printf("Breitengrad %.6f°, Längengrad %.6f° → Pixel (%d, %d)\n",
                lat, lon, pixelCoords[0], pixelCoords[1]);
    }

    // Methode zur Umrechnung von Pixelkoordinaten zu Geokoordinaten
    public static double[] convertPixelToGeo(int x, int y) {
        double latitude = latBottom + ((imageHeight - y) / (double) imageHeight) * (latTop - latBottom);
        double longitude = lonLeft + (x / (double) imageWidth) * (lonRight - lonLeft);
        return new double[] { latitude, longitude };
    }

    // Methode zur Umrechnung von Geokoordinaten zu Pixelkoordinaten
    public static int[] convertGeoToPixel(double lat, double lon) {
        int x = (int) ((lon - lonLeft) / (lonRight - lonLeft) * imageWidth);
        int y = imageHeight - (int) ((lat - latBottom) / (latTop - latBottom) * imageHeight);
        return new int[] { x, y };
    }
}
