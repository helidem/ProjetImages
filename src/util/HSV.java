package util;

import appli.PictureIUT;
import fr.unistra.pelican.Image;

import java.io.File;
import java.util.Arrays;

import static appli.Main.imageMap;
import static appli.Main.traiterImage;
import static java.lang.Math.pow;

public class HSV {


    public static double[] rgb_to_hsv(double r, double g, double b) {
        r /= 255;
        g /= 255;
        b /= 255;
        double maxc = Math.max(Math.max(r, g), b);
        double minc = Math.min(Math.min(r, g), b);
        double v = maxc;
        //System.out.println(maxc + " " + minc);
        if (minc == maxc) {
            return new double[]{0.0, 0.0, v};
        }

        double s = (maxc - minc) / maxc;
        double rc = (maxc - r) / (maxc - minc);
        double gc = (maxc - g) / (maxc - minc);
        double bc = (maxc - b) / (maxc - minc);
        double h;
        if (r == maxc) {
            h = 0.0 + bc - gc;
            //System.out.println("1");
        } else if (g == maxc) {
            h = 2.0 + rc - bc;
            ///System.out.println("2");
        } else {
            h = 4.0 + gc - rc;
            //System.out.println("3");
        }

        h = (h / 6.0) % 1.0;
        //System.out.println(h);
        if (h < 0) h += 1; // ne retourne pas le reste. il faut ajouter 1 si c'est negatif
        return new double[]{h * 360, s * 100, v * 100}; // avoir une valeur entre 0 et 100
    }

    public static double[] getH(Image image) {
        double H[] = new double[361];
        Arrays.fill(H, 0.0);

        for (int x = 0; x < image.getXDim(); x++) {
            for (int y = 0; y < image.getYDim(); y++) {
                H[(int) rgb_to_hsv(image.getPixelXYBByte(x, y, 0), image.getPixelXYBByte(x, y, 1), image.getPixelXYBByte(x, y, 2))[0]] += 1;
            }
        }
        System.out.println(Arrays.toString(H));
        return H;
    }

    public static double[] getS(Image image) {
        double S[] = new double[361];
        Arrays.fill(S, 0.0);

        for (int x = 0; x < image.getXDim(); x++) {
            for (int y = 0; y < image.getYDim(); y++) {
                S[(int) rgb_to_hsv(image.getPixelXYBByte(x, y, 0), image.getPixelXYBByte(x, y, 1), image.getPixelXYBByte(x, y, 2))[1]] += 1;
            }
        }

        return S;
    }

    public static double[] getV(Image image) {
        double V[] = new double[361];
        Arrays.fill(V, 0.0);

        for (int x = 0; x < image.getXDim(); x++) {
            for (int y = 0; y < image.getYDim(); y++) {
                V[(int) rgb_to_hsv(image.getPixelXYBByte(x, y, 0), image.getPixelXYBByte(x, y, 1), image.getPixelXYBByte(x, y, 2))[2]] += 1;
            }
        }

        return V;
    }

    public static double distance(PictureIUT req, PictureIUT p2) {
        double h = 0;
        double s = 0;
        double v = 0;

        for (int i = 0; i < req.getS().length; i++) {
            s += pow(req.getS()[i] - p2.getS()[i], 2);
        }
        for (int i = 0; i < req.getV().length; i++) {
            v += pow(req.getV()[i] - p2.getV()[i], 2);
        }

        for (int i = 0; i < req.getH().length; i++) {
            h += pow(req.getH()[i] - p2.getH()[i], 2);
        }
        return Math.sqrt(h) + Math.sqrt(s) + Math.sqrt(v);
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
}
