package util;

import appli.Main;
import appli.PictureIUT;
import fr.unistra.pelican.algorithms.io.ImageLoader;
/*import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;*/

import java.io.*;
import java.nio.file.*;
import java.nio.file.Files;

public class JSONProduction {


    /**
     * Permet d'indexer les fichiers images et créer le fichier json
     *//*
    public static void jsonEncode(String imageBankPath) throws IOException {
        File dir = new File(imageBankPath);
        File[] directoryListing = dir.listFiles();

        if (directoryListing != null) {
            if (Files.exists(FileSystems.getDefault().getPath("misc/out.json"))) {
                Files.delete(FileSystems.getDefault().getPath("misc/out.json"));
            }
            FileWriter file = null;
            file = new FileWriter("misc/out.json");
            JSONArray jsonArray = new JSONArray();
            for (File image : directoryListing) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("nom", image.getName());
                double[] histogram = Main.getHisto(ImageLoader.exec(image.getPath()), 0);
                histogram = Main.normaliserHisto(histogram);
                jsonObject.put("histogram", Main.histoToString(histogram)); // TODO: calculer l'histogramme de l'image
                jsonArray.add(jsonObject);
            }

            file.write(jsonArray.toJSONString());
            file.close();
        }
    }*/
/*
    public static PictureIUT jsonDecode(String imageBankPath, int i) throws IOException {
        JSONParser parser = new JSONParser();
        StringBuilder builder = new StringBuilder();
        BufferedReader buffer = new BufferedReader(new FileReader("misc/out.json"));
        String jsonFileOutput;
        while ((jsonFileOutput = buffer.readLine()) != null) {

            builder.append(jsonFileOutput).append("\n");
        }
        Object obj = null;
        try {
            obj = parser.parse(builder.toString());
            JSONArray array = (JSONArray)obj;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        PictureIUT pictureIUT = array.get(1);
        return new PictureIUT();
    }*/
}
