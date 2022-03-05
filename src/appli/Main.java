package appli;

import fr.unistra.pelican.*;
import fr.unistra.pelican.Image;
import fr.unistra.pelican.algorithms.io.ImageLoader;
import fr.unistra.pelican.algorithms.visualisation.Viewer2D;
import util.*;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

import static java.lang.Math.pow;

public class Main {
    public static TreeMap<Double, String> imageMap = new TreeMap();

    public static void main(String[] args) throws Exception {
        // PictureIUT test = new PictureIUT("000.jpg", "misc\\motos\\000.jpg");
        // JSONProduction.jsonEncode("misc/motos");
        // ArrayList<PictureIUT> motos = JSONProduction.jsonDecode("misc/out.json");
        // recherche(test, motos);
        // recherche(test);
        // afficherImages();
        // System.out.println(imageMap)
        // System.out.println( Arrays.toString(HSV.rgb_to_hsv(52,11,44)));


        PictureIUT test = new PictureIUT("215.jpg", "misc/motos/215.jpg");
        HSV.recherche(test);
        System.out.println(imageMap);
        afficherImages();

        HistogramTools.plotHistogram(test.getH(), Color.BLUE);
    }

    private static void afficherImages() {
        int cpt = 0;
        for (Map.Entry<Double, String> entry : imageMap.entrySet()) {
            if (cpt == 10) {
                break;
            }
            Image image = ImageLoader.exec("misc/motos/" + entry.getValue());
            image.setColor(true);
            Viewer2D.exec(image);
            cpt++;
        }
    }

    public static PictureIUT traiterImage(PictureIUT image) {
        //image.setImg(median(image.getImg()));
        if (image.getImg().getBDim() > 2) {
            image.setBleu(normaliserHisto(image.getBleu()));
            image.setVert(normaliserHisto(image.getVert()));
            // image.setBleu(getDividedHisto(image.getBleu()));
            //  image.setVert(getDividedHisto(image.getVert()));
        }
        image.setRouge(normaliserHisto(image.getRouge()));
        //   image.setRouge(getDividedHisto(image.getRouge()));

        return image;
    }

    /**
     * Recherche les images similaires dans le dossier de l'image
     *
     * @param req l'image en question
     */
    public static void recherche(PictureIUT req) {
        req = traiterImage(req);
        File dir = new File("misc\\motos");
        File[] directoryListing = dir.listFiles();
        for (File image : directoryListing) {
            /*if (image.getName().equals(req.getName())) {
                continue;
            }*/
            // .out.println("je teste : " + image.getName());
            PictureIUT image2 = new PictureIUT(image.getName(), image.getPath());
            image2 = traiterImage(image2);
            double distance = distance(req, image2);
            imageMap.put(distance, image.getName());
        }
    }

    public static void recherche(PictureIUT req, ArrayList<PictureIUT> images) {
        req = traiterImage(req);
        for (PictureIUT image : images) {
            double distance = distance(req, image);
            imageMap.put(distance, image.getName());
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
        double pourcent = 0;
        for (double num : histo) {
            nbPixel += num;
        }
        //System.out.println(nbPixel);
        for (int i = 0; i < histo.length; i++) {
            normal[i] = (histo[i] * 100) / nbPixel;
            pourcent += normal[i];
        }
        //System.out.println(pourcent);
        return normal;
    }

    /**
     * Permet de calculer l'histogramme de l'image
     *
     * @param image
     * @return tableau représentant l'histogramme
     */
    public static double[] getHisto(Image image, int canal) {
        double histo[] = new double[256];
        Arrays.fill(histo, 0.0);

        for (int x = 0; x < image.getXDim(); x++) {
            for (int y = 0; y < image.getYDim(); y++) {
                histo[image.getPixelXYBByte(x, y, canal)] += 1;
            }
        }
        return histo;
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
            //  System.out.println("COULEUR");
            ByteImage new_image = new ByteImage(image.getXDim(), image.getYDim(), 1, 1, 3);
            //  System.out.println(image.getXDim() * image.getYDim());
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
            System.out.println("NOIR");
            ByteImage new_image = new ByteImage(image.getXDim(), image.getYDim(), 1, 1, 1);
            System.out.println(image.getXDim() * image.getYDim());
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

    /**
     * Calcule la distance des histogrammes entre l'image req et p2
     *
     * @param req l'image en entrée
     * @param p2  l'image avec laquelle on compare
     * @return la distance des histogrammes
     */
    public static double distance(PictureIUT req, PictureIUT p2) {
        double rouge = 0;
        double vert = 0;
        double bleu = 0;
        if (req.getImg().getBDim() > 2 && p2.getImg().getBDim() > 2) {
            for (int i = 0; i < req.getVert().length; i++) {
                vert += pow(req.getVert()[i] - p2.getVert()[i], 2);
            }
            for (int i = 0; i < req.getBleu().length; i++) {
                bleu += pow(req.getBleu()[i] - p2.getBleu()[i], 2);
            }
        }
        for (int i = 0; i < req.getRouge().length; i++) {
            rouge += pow(req.getRouge()[i] - p2.getRouge()[i], 2);
        }
        return Math.sqrt(rouge) + Math.sqrt(vert) + Math.sqrt(bleu);
    }
}