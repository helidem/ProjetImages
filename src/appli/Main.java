package appli;

import fr.unistra.pelican.*;
import fr.unistra.pelican.Image;
import fr.unistra.pelican.algorithms.io.ImageLoader;
import fr.unistra.pelican.algorithms.visualisation.Viewer2D;
import util.*;

import java.io.File;
import java.util.*;

import static java.lang.Math.pow;

public class Main {
    public static TreeMap<String, Double> imageMap = new TreeMap();
    static LinkedHashMap<String, Double> sortedMap = new LinkedHashMap<>();
    public static String path = "misc/broad/";


    public static void main(String[] args) throws Exception {
        PictureIUT test = new PictureIUT("0448.png", path+"0448.png");
        JSONProduction.jsonEncodeRGB(path);
        ArrayList<PictureIUT> banque = JSONProduction.jsonDecodeRGB("misc/out.json");
        RGB.rechercheRGB(test, banque);
        //HSV.recherche(test);
        //recherche(test);
        imageMap.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .forEachOrdered(x -> sortedMap.put(x.getKey(), x.getValue()));


        afficherImages();
        System.out.println(sortedMap);
        // System.out.println(Arrays.toString(test.getS()));

        //System.out.println(distance(traiterImage(test),banque.get(0)));
        //System.out.println(distanceHSV(new PictureIUT("bleu","misc/formes/bleu.png"),new PictureIUT("noir", "misc/formes/noir.png") ));


    }

    private static void afficherImages() {
        int cpt = 0;
        for (Map.Entry<String, Double> entry : sortedMap.entrySet()) {
            if (cpt == 10) {
                break;
            }
            Image image = ImageLoader.exec(path + entry.getKey());
            image.setColor(true);
            Viewer2D.exec(image, image.getName());
            cpt++;
        }
    }


    /**
     * Permet de normaliser l'histogramme (pourcentage)
     *
     * @param histo
     * @return
     */
    public static double[] normaliserHisto(double[] histo) {
        double[] normal = new double[histo.length];
        int nbPixel = 0;
        for (double num : histo) {
            nbPixel += num;
        }

        for (int i = 0; i < histo.length; i++) {
            normal[i] = (histo[i] * 100) / nbPixel;
        }
        return normal;
    }

    public static double[] getDividedHisto(double[] h) {
        int nbBarres = h.length / 8;
        double[] histo = new double[nbBarres];

        for (int x = 0; x < histo.length; x++) {
            histo[x] = h[x * 2] + h[(x * 2) + 1];
        }
        return histo;
    }

    /**
     * Applique le filtre médian sur une image
     *
     * @param image l'image à transformer
     * @return l'image transformée
     */
    public static Image median(Image image) {
        if (image.getBDim() > 2) {

            ByteImage new_image = new ByteImage(image.getXDim(), image.getYDim(), 1, 1, 3);

            for (int x = 1; x < image.getXDim() - 1; x++) {
                for (int y = 1; y < image.getYDim() - 1; y++) {
                    // calcul de la mediane
                    int[] arr0 = { // TODO : A CHANGER URGENT
                            image.getPixelXYBByte(x, y, 0),
                            image.getPixelXYBByte(x + 1, y + 1, 0),
                            image.getPixelXYBByte(x - 1, y - 1, 0),
                            image.getPixelXYBByte(x - 1, y, 0),
                            image.getPixelXYBByte(x, y - 1, 0),
                            image.getPixelXYBByte(x - 1, y + 1, 0),
                            image.getPixelXYBByte(x + 1, y - 1, 0),
                            image.getPixelXYBByte(x, y + 1, 0),
                            image.getPixelXYBByte(x + 1, y, 0)};
                    int[] arr1 = {
                            image.getPixelXYBByte(x, y, 1),
                            image.getPixelXYBByte(x + 1, y + 1, 1),
                            image.getPixelXYBByte(x - 1, y - 1, 1),
                            image.getPixelXYBByte(x - 1, y, 1),
                            image.getPixelXYBByte(x, y - 1, 1),
                            image.getPixelXYBByte(x - 1, y + 1, 1),
                            image.getPixelXYBByte(x + 1, y - 1, 1),
                            image.getPixelXYBByte(x, y + 1, 1),
                            image.getPixelXYBByte(x + 1, y, 1)};
                    int[] arr2 = {
                            image.getPixelXYBByte(x, y, 2),
                            image.getPixelXYBByte(x + 1, y + 1, 2),
                            image.getPixelXYBByte(x - 1, y - 1, 2),
                            image.getPixelXYBByte(x - 1, y, 2),
                            image.getPixelXYBByte(x, y - 1, 2),
                            image.getPixelXYBByte(x - 1, y + 1, 2),
                            image.getPixelXYBByte(x + 1, y - 1, 2),
                            image.getPixelXYBByte(x, y + 1, 2),
                            image.getPixelXYBByte(x + 1, y, 2)};
                    Arrays.sort(arr0);
                    Arrays.sort(arr1);
                    Arrays.sort(arr2);
                    int mediane1 = arr0[arr0.length / 2];
                    int mediane2 = arr1[arr1.length / 2];
                    int mediane3 = arr2[arr2.length / 2];
                    new_image.setPixelXYBByte(x, y, 0, mediane1);
                    new_image.setPixelXYBByte(x, y, 1, mediane2);
                    new_image.setPixelXYBByte(x, y, 2, mediane3);
                } // y for
            } // x for
            return new_image;
        } else {

            ByteImage new_image = new ByteImage(image.getXDim(), image.getYDim(), 1, 1, 1);

            for (int x = 1; x < image.getXDim() - 1; x++) {
                for (int y = 1; y < image.getYDim() - 1; y++) {
                    // calcul de la mediane
                    int[] arr0 = { // TODO : A CHANGER URGENT
                            image.getPixelXYBByte(x, y, 0),
                            image.getPixelXYBByte(x + 1, y + 1, 0),
                            image.getPixelXYBByte(x - 1, y - 1, 0),
                            image.getPixelXYBByte(x - 1, y, 0),
                            image.getPixelXYBByte(x, y - 1, 0),
                            image.getPixelXYBByte(x - 1, y + 1, 0),
                            image.getPixelXYBByte(x + 1, y - 1, 0),
                            image.getPixelXYBByte(x, y + 1, 0),
                            image.getPixelXYBByte(x + 1, y, 0)};
                    Arrays.sort(arr0);
                    int mediane1 = arr0[arr0.length / 2];
                    new_image.setPixelXYBByte(x, y, 0, mediane1);

                } // y for
            } // x for
            return new_image;
        }
    } // median

}