package appli;

import fr.unistra.pelican.Image;
import fr.unistra.pelican.algorithms.io.ImageLoader;

public class PictureIUT {
    Image img;
    String path;
    double[] rouge;
    double[] vert;
    double[] bleu;

    public PictureIUT(String path) {
        this.path = path;
        this.img = ImageLoader.exec(path);
    }

    public double[] getRouge() {
        return rouge;
    }

    public double[] getVert() {
        return vert;
    }

    public double[] getBleu() {
        return bleu;
    }

    /**
     * Calcule l'histogramme de l'image
     */
    public void initHisto() {
        if (img.getBDim() > 2) {
            vert = Main.getHisto(this.img, 1);
            bleu = Main.getHisto(this.img, 2);
        }
            rouge = Main.getHisto(this.img, 0);
    }

    public Image getImg() {
        return img;
    }
}
