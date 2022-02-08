package appli;

import fr.unistra.pelican.*;
import fr.unistra.pelican.algorithms.io.ImageLoader;
import util.HistogramTools;

public class Main {

    public static void main(String[] args) throws Exception {
        System.out.println("Test");
        Image test = ImageLoader.exec("Z:\\JAVAIMG_S4\\images\\brain.jpg");
        double[] histo = getHisto(test);
        HistogramTools.plotHistogram(histo);
    }

    /**
     * Transforme l'image couleur en image noire et blanc
     *
     * @param imageCouleur l'image en couleur
     * @return l'image transformée
     */
    public static Image colorToGray(Image imageCouleur) {
        ByteImage imageGris = new ByteImage(imageCouleur.getXDim(), imageCouleur.getYDim(), 1, 1, 1);
        for (int x = 0; x < imageCouleur.getXDim(); x++) {
            for (int y = 0; y < imageCouleur.getYDim(); y++) {
                imageGris.setPixelXYBByte(x, y, 0, (imageCouleur.getPixelXYBByte(x, y, 0) + imageCouleur.getPixelXYBByte(x, y, 1) + imageCouleur.getPixelXYBByte(x, y, 2)) / 3);
            }
        }
        return imageGris;
    }

    /**
     * @param image
     * @param s
     * @return
     */
    public static Image binarisation(Image image, int s) {
        ByteImage bin = new ByteImage(image.getXDim(), image.getYDim(), 1, 1, 1);
        for (int x = 0; x < image.getXDim(); x++) {
            for (int y = 0; y < image.getYDim(); y++) {
                if (image.getPixelXYBByte(x, y, 0) <= s) {
                    bin.setPixelXYBByte(x, y, 0, 0);
                } else {
                    bin.setPixelXYBByte(x, y, 0, 255);
                }
            }
        }
        return bin;
    }

    /**
     * Permet d'étirer le contraste de l'image
     *
     * @param image
     * @return
     * @throws Exception
     */
    public static Image etirementContraste(Image image) throws Exception {
        ByteImage img = new ByteImage(image.getXDim(), image.getYDim(), 1, 1, 1);
        int gmin = min(image);
        int gmax = max(image);

        for (int x = 0; x < image.getXDim(); x++) {
            for (int y = 0; y < image.getYDim(); y++) {
                img.setPixelXYBByte(x, y, 0, f(image.getPixelXYBByte(x, y, 0), gmin, gmax));
            }
        }
        return img;
    }

    /**
     * Retourne la valeur min de l'image
     *
     * @param image l'image
     * @return la valeur min
     * @throws Exception
     */
    public static int min(Image image) {

        int min = image.getPixelXYBByte(0, 0, 0);

        for (int x = 0; x < image.getXDim(); x++) {
            for (int y = 1; y <= image.getYDim(); y++) {
                if (min > image.getPixelXYBByte(x, y - 1, 0)) min = image.getPixelXYBByte(x, y - 1, 0);
            }
        }
        return min;

    }

    /**
     * Retourne la valeur max de l'image
     *
     * @param image l'image
     * @return la valeur max
     * @throws Exception
     */
    public static int max(Image image) {

        int max = image.getPixelXYBByte(0, 0, 0);
        for (int x = 0; x < image.getXDim(); x++) {
            for (int y = 1; y <= image.getYDim(); y++) {
                if (max < image.getPixelXYBByte(x, y - 1, 0)) {
                    max = image.getPixelXYBByte(x, y - 1, 0);
                }
            }
        }
        return max;

    }

    private static int f(int v, int gmin, int gmax) {
        return (255 * (v - gmin)) / (gmax - gmin);
    }

    /**
     * Permet de calculer l'histogramme de l'image
     *
     * @param image
     * @return tableau représentant l'histogramme
     */
    private static double[] getHisto(Image image) {
        double histo[] = new double[256];
        for (int i = 0; i < histo.length; i++) {
            histo[i] = 0;
        }
        for (int x = 0; x < image.getXDim(); x++) {
            for (int y = 0; y < image.getYDim(); y++) {
                histo[image.getPixelXYBByte(x, y, 0)] += 1;
            }
        }
        return histo;
    }

}

//import java.util.Scanner;
//
//public class Main {
//
//    public static void main(String[] args) {
//        Scanner scanner = new Scanner(System.in);
//        String imagePath = scanner.nextLine();
//        Research.signatureCalculation();
//    }