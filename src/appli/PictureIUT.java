package appli;

import fr.unistra.pelican.Image;
import fr.unistra.pelican.algorithms.io.ImageLoader;
import util.HSV;

public class PictureIUT {
    Image img;
    String name;
    String path;
    double[] rouge;
    double[] vert;
    double[] bleu;
    double[] h;
    double[] s;
    double[] v;



    public PictureIUT(String name, String path) {
        this.name = name;
        this.path = path;
        this.img = ImageLoader.exec(path);
        initHisto();
        initHSV();
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

    public void setRouge(double[] rouge) {
        this.rouge = rouge;
    }

    public void setVert(double[] vert) {
        this.vert = vert;
    }

    public void setBleu(double[] bleu) {
        this.bleu = bleu;
    }

    public double[] getH() {
        return h;
    }

    public void setH(double[] h) {
        this.h = h;
    }

    public double[] getS() {
        return s;
    }

    public void setS(double[] s) {
        this.s = s;
    }

    public double[] getV() {
        return v;
    }

    public void setV(double[] v) {
        this.v = v;
    }

    public String getName() {
        return name;
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

    public void initHSV(){
        h = HSV.getH(this.img);
        s = HSV.getS(this.img);
        v = HSV.getV(this.img);
    }


    @Override
    public String toString(){
        return "{nom : " + name + ", histoRouge : " + rouge + ", histoVert : " + vert + ", histoBleu : " + bleu + "}\n";
    }
}
