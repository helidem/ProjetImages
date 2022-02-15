package appli;

import fr.unistra.pelican.*;
import fr.unistra.pelican.Image;
import fr.unistra.pelican.algorithms.io.ImageLoader;
import fr.unistra.pelican.algorithms.visualisation.Viewer2D;
import util.HistogramTools;
import util.JSONProduction;

import java.awt.*;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) throws Exception {
        Image test = ImageLoader.exec("misc/images/maldive.jpg");
        proto(test);
        JSONProduction.indexation("misc/images");
        Image med = median(test);
        med.setColor(true); //si false => affichage de chaque canal, si true => affichage d'une image couleur
        Viewer2D.exec(med);
    }

    /**
     * Affiche les histogrammes de l'image
     *
     * @param test l'image test
     * @throws Exception
     */
    public static void proto(Image test) throws Exception {
        assert (test.getBDim() == 3);
        double[] array = getHisto(test, 2);

        /*double[] histoRouge = getDividedHisto(getDividedHisto(getHisto(test)));
        double[][] histoVert = getDividedHisto(getDividedHisto(getHisto(test)));
        double[][] histoBleu = getDividedHisto(getDividedHisto(getHisto(test)));
        HistogramTools.plotHistogram(histoRouge, Color.red);
        HistogramTools.plotHistogram(histoVert, Color.green);
        HistogramTools.plotHistogram(histoBleu, Color.blue);*/
    }

    /**
     * Permet de normaliser l'histogramme
     *
     * @param histo
     * @return
     */
    public static double[] normaliserHisto(double[] histo) {
        double[] normal = new double[histo.length];
        for (int i = 0; i < histo.length; i++) {
            normal[i] = (histo[i] * 100) / histo.length;
        }
        return normal;
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
    public static double[] getHisto(Image image, int canal) {
        double histo[] = new double[256];
        for (int i = 0; i < histo.length; i++) {
            histo[i] = 0;
        }
        for (int x = 0; x < image.getXDim(); x++) {
            for (int y = 0; y < image.getYDim(); y++) {
                histo[image.getPixelXYBByte(x, y, canal)] += 1;
            }
        }
        return histo;
    }


    public static String histoToString(double[] histo){
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (double val:
             histo) {
            sb.append(val).append(",");
        }
        sb.append("]");
        return sb.toString();
    }


    /**
     * Divise l'histogramme
     *
     * @param histo l'histogramme à modifier
     * @return
     */
    private static double[][] getDividedHisto(double[][] histo) {
        double dividedHisto[][] = new double[histo.length / 2][3];
        for (int i = 0; i < dividedHisto.length - 1; i = i + 2) {
            dividedHisto[i][0] = histo[i][0] + histo[i + 1][0];
            dividedHisto[i][1] = histo[i][1] + histo[i + 1][1];
            dividedHisto[i][2] = histo[i][2] + histo[i + 1][2];
        }
        return dividedHisto;
    }

    /**
     * Cette fonction applique le filtre moyenneur sur l'image
     *
     * @param image l'image bruitée à traiter
     * @return l'image traitée
     */
    public static Image moyenneur(Image image) {
        ByteImage new_image = new ByteImage(image.getXDim(), image.getYDim(), 1, 1, 1);
        for (int x = 1; x < image.getXDim() - 1; x++) {
            for (int y = 1; y < image.getYDim() - 1; y++) {
                // calcul de la moyenne
                int moyenne = 0;/* = ( // TODO : A CHANGER URGENT
                        image.getPixelXYBByte(x,y,0) +
                        image.getPixelXYBByte(x+1,y+1,0) +
                        image.getPixelXYBByte(x-1,y-1,0) +
                        image.getPixelXYBByte(x-1,y,0) +
                        image.getPixelXYBByte(x,y-1,0) +
                        image.getPixelXYBByte(x-1,y+1,0) +
                        image.getPixelXYBByte(x+1,y-1,0) +
                        image.getPixelXYBByte(x,y+1,0) +
                        image.getPixelXYBByte(x+1,y,0))/9;*/
                for (int xx = -1; xx <= 1; xx++) {
                    for (int yy = -1; yy <= 1; yy++) {
                        moyenne += image.getPixelXYBByte(x + xx, y + yy, 0);
                    }
                }
                moyenne /= 9;
                // attribue le pixel dans la nouvelle image
                new_image.setPixelXYBByte(x, y, 0, moyenne);
            } // y for
        } // x for
        return new_image;
    } // moyenneur


    /**
     * Applique le filtre médian sur une image
     *
     * @param image l'image à transformer
     * @return l'image transformée
     */
    public static Image median(Image image) {
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
    } // median

    public static Image contours(Image image) {
        ByteImage new_image = new ByteImage(image.getXDim(), image.getYDim(), 1, 1, 1);
        for (int x = 1; x < image.getXDim() - 1; x++) {
            for (int y = 1; y < image.getYDim() - 1; y++) {
                new_image.setPixelXYBByte(x, y, 0, image.getPixelXYBByte(x, y, 0) * -2);
                new_image.setPixelXYBByte(x, y, 0, image.getPixelXYBByte(x + 1, y + 1, 0) * 0);
                new_image.setPixelXYBByte(x, y, 0, image.getPixelXYBByte(x - 1, y - 1, 0) * 0);
                new_image.setPixelXYBByte(x, y, 0, image.getPixelXYBByte(x - 1, y, 0) * 1);
                new_image.setPixelXYBByte(x, y, 0, image.getPixelXYBByte(x, y - 1, 0) * 1);
                new_image.setPixelXYBByte(x, y, 0, image.getPixelXYBByte(x - 1, y + 1, 0) * 0);
                new_image.setPixelXYBByte(x, y, 0, image.getPixelXYBByte(x + 1, y - 1, 0) * 0);
                new_image.setPixelXYBByte(x, y, 0, image.getPixelXYBByte(x, y + 1, 0) * 0);
                new_image.setPixelXYBByte(x, y, 0, image.getPixelXYBByte(x + 1, y, 0) * 0);
            } // y for
        } // x for

        return new_image;
    }
}