package appli;

import fr.unistra.pelican.*;
import fr.unistra.pelican.Image;
import fr.unistra.pelican.algorithms.io.ImageLoader;
import fr.unistra.pelican.algorithms.visualisation.Viewer2D;
import util.HistogramTools;
import util.JSONProduction;

import java.awt.*;
import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import static java.lang.Math.pow;

public class Main {
    public static TreeMap<Double, String> imageMap = new TreeMap();

    public static void main(String[] args) throws Exception {
        PictureIUT test = new PictureIUT("099.jpg", "misc\\motos\\099.jpg");
        // recherche(test);
       //  System.out.println(imageMap);
        // afficherImages();
        System.out.println(JSONProduction.jsonDecode("misc/out.json"));
    }

    private static void afficherImages() {
        int cpt = 0;
        for(Map.Entry<Double, String> entry : imageMap.entrySet()) {
            if(cpt == 10){
                break;
            }
            Image image = ImageLoader.exec("misc/motos/"+entry.getValue());
            image.setColor(true);
            Viewer2D.exec(image);
            cpt++;
        }
    }

    public static PictureIUT traiterImage(PictureIUT image){
        if(image.getImg().getBDim() > 2){
            image.setBleu(normaliserHisto(image.getBleu()));
            image.setVert(normaliserHisto(image.getVert()));
            image.setBleu(getDividedHisto(image.getBleu()));
            image.setVert(getDividedHisto(image.getVert()));
        }
        image.setRouge(normaliserHisto(image.getRouge()));
        image.setRouge(getDividedHisto(image.getRouge()));
        image.setImg(median(image.getImg()));
        return image;
    }

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


    /**
     * Affiche les histogrammes de l'image
     *
     * @param test l'image test
     * @throws Exception
     */
    public static void proto(PictureIUT test) throws Exception {
        HistogramTools.plotHistogram(test.getRouge(), Color.RED);
        HistogramTools.plotHistogram(test.getBleu(), Color.BLUE);
        HistogramTools.plotHistogram(test.getVert(), Color.GREEN);
    }

    /**
     * Permet de normaliser l'histogramme (pourcentage)
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
     * @return l'image traitée
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
        Arrays.fill(histo, 0.0);

        for (int x = 0; x < image.getXDim(); x++) {
            for (int y = 0; y < image.getYDim(); y++) {
                histo[image.getPixelXYBByte(x, y, canal)] += 1;
            }
        }
        return histo;
    }

    /**
     * Converti l'histogramme en chaine de caractères
     *
     * @param histo
     * @return
     */
    public static String histoToString(double[] histo) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (double val :
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
    private static double[] getDividedHisto(double[] histo) {
        double dividedHisto[] = new double[histo.length / 2];
        for (int i = 0; i < dividedHisto.length - 1; i = i + 2) {
            dividedHisto[i] = histo[i] + histo[i + 1];
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
                int moyenne = 0;
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
            // distance 1
            for (int i = 0; i < req.getVert().length; i++) {
                vert += pow(req.getVert()[i] - p2.getVert()[i], 2);

            }//System.out.println("vert : " + vert);
            // distance 2
            for (int i = 0; i < req.getBleu().length; i++) {
                bleu += pow(req.getBleu()[i] - p2.getBleu()[i], 2);

            }//System.out.println("bleu : " + bleu);
        }
        // distance 0
        for (int i = 0; i < req.getRouge().length; i++) {
            rouge += pow(req.getRouge()[i] - p2.getRouge()[i], 2);
            //System.out.println("barre n° : "+i+" & req : "+req.getRouge()[i] + " && p2 : " + p2.getRouge()[i]);
        }
        //System.out.println("rouge : " + rouge);
        //System.out.println(Math.sqrt(rouge));
        return Math.sqrt(rouge) + Math.sqrt(vert) + Math.sqrt(bleu);
    }
}