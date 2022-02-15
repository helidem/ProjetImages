package util;
import appli.Main;
import fr.unistra.pelican.algorithms.io.ImageLoader;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.awt.*;
import java.io.File;
import java.nio.file.*;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

public class JSONProduction {


        /**
         * Permet d'indexer les fichiers images et créer le fichier json
         */
    public static void indexation(String imageBankPath) throws IOException{
        File dir = new File(imageBankPath);
        File[] directoryListing = dir.listFiles();

        if (directoryListing != null) {
            if(Files.exists(FileSystems.getDefault().getPath("misc/out.json"))) {
                Files.delete(FileSystems.getDefault().getPath("misc/out.json"));
            }
            FileWriter file = null;
            file = new FileWriter("misc/out.json");
            JSONArray jsonArray = new JSONArray();
            for (File image : directoryListing) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("nom", image.getName());
                double[] histogram = Main.getHisto(ImageLoader.exec(image.getPath()), 0);
                jsonObject.put("histogram", Main.histoToString(histogram)); // TODO: calculer l'histogramme de l'image
                jsonArray.add(jsonObject);
            }

            System.out.println(jsonArray.toJSONString());

            file.write(jsonArray.toJSONString());
            file.close();
        }
    }
}
