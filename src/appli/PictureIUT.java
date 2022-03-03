package appli;

import fr.unistra.pelican.Image;
import fr.unistra.pelican.algorithms.io.ImageLoader;

public class PictureIUT {
    Image img;
    String name;
    String path;
    double[] rouge;
    double[] vert;
    double[] bleu;



    public PictureIUT(String name, String path) {
        this.name = name;
        this.path = path;
        this.img = ImageLoader.exec(path);
        initHisto();
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

    public void setImg(Image img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
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

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString(){
        return "{nom : " + name + ", histoRouge : " + rouge + ", histoVert : " + vert + ", histoBleu : " + bleu + "}\n";
    }
}
