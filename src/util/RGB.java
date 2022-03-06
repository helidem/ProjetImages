package util;

import appli.PictureIUT;
import fr.unistra.pelican.Image;
import java.io.File;
import java.util.*;
import static appli.Main.*;
import static java.lang.Math.pow;

public class RGB {

    /**
     * Calcule la distance des histogrammes entre l'image req et p2
     *
     * @param req l'image en entrée
     * @param p2  l'image avec laquelle on compare
     * @return la distance des histogrammes
     */
    public static double distanceRGB(PictureIUT req, PictureIUT p2) {
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

    /**
     * Permet de calculer l'histogramme de l'image
     *
     * @param image
     * @return tableau représentant l'histogramme
     */
    public static double[] getHistoRGB(Image image, int canal) {
        double histo[] = new double[256];
        Arrays.fill(histo, 0.0);

        for (int x = 0; x < image.getXDim(); x++) {
            for (int y = 0; y < image.getYDim(); y++) {
                histo[image.getPixelXYBByte(x, y, canal)] += 1;
            }
        }
        return histo;
    }


    public static void rechercheRGB(PictureIUT req, ArrayList<PictureIUT> images) {
        req = traiterImageRGB(req);

        System.out.println("req : " + Arrays.toString(req.getRouge()));
        for (PictureIUT image : images) {
            double distance = distanceRGB(req, image);
            imageMap.put(image.getName(), distance);
        }
    }



    public static PictureIUT traiterImageRGB(PictureIUT image) {
        image.setImg(median(image.getImg()));
        if (image.getImg().getBDim() > 2) {
            image.setBleu(normaliserHisto(image.getBleu()));
            image.setVert(normaliserHisto(image.getVert()));
            image.setBleu(getDividedHisto(image.getBleu()));
            image.setVert(getDividedHisto(image.getVert()));
        }
        image.setRouge(normaliserHisto(image.getRouge()));
        image.setRouge(getDividedHisto(image.getRouge()));

        return image;
    }


    /**
     * Recherche les images similaires dans le dossier de l'image
     *
     * @param req l'image en question
     */
    public static void rechercheRGB(PictureIUT req) {
        req = traiterImageRGB(req);

        File dir = new File("misc\\motos");
        File[] directoryListing = dir.listFiles();
        for (File image : directoryListing) {
            if (image.getName().equals(req.getName())) {
                continue;
            }

            PictureIUT image2 = new PictureIUT(image.getName(), image.getPath());
            image2 = traiterImageRGB(image2);
            double distance = distanceRGB(req, image2);
            imageMap.put(image.getName(), distance);
        }
    }



}
