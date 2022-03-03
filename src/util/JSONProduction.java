package util;

import appli.Main;
import appli.PictureIUT;
import fr.unistra.pelican.Image;
import fr.unistra.pelican.algorithms.io.ImageLoader;
import org.json.simple.JSONArray;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.nio.file.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JSONProduction {


    /**
     * Permet d'indexer les fichiers images et créer le fichier json
     */
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
                Image pic = ImageLoader.exec(image.getPath());
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("nom", image.getName());
                double[] histogram1 = Main.getHisto(pic, 0);
                histogram1 = Main.normaliserHisto(histogram1);
                jsonObject.put("histogram1", Main.histoToString(histogram1));
                if(pic.getBDim() > 2){ // si l'image est en couleur
                    double[] histogram2 = Main.getHisto(pic, 1);
                    double[] histogram3 = Main.getHisto(pic, 2);
                    histogram2 = Main.normaliserHisto(histogram2);
                    histogram3 = Main.normaliserHisto(histogram3);
                    jsonObject.put("histogram2", Main.histoToString(histogram2));
                    jsonObject.put("histogram3", Main.histoToString(histogram3));
                }
                jsonArray.add(jsonObject);
            }
            file.write(jsonArray.toJSONString());
            file.close();
        }
    }

    /**
     * Permet de créer une image à partir des données dans le fichier json
     * @param jsonPath
     * @return l'image
     * @throws IOException
     */
    public static List<PictureIUT> jsonDecode(String jsonPath) throws IOException, ParseException {
        JSONParser parser = new JSONParser();
        StringBuilder builder = new StringBuilder();
        BufferedReader buffer = new BufferedReader(new FileReader(jsonPath));
        String jsonFileOutput;
        while ((jsonFileOutput = buffer.readLine()) != null) {
            builder.append(jsonFileOutput).append("\n");
        }
        JSONArray array = (JSONArray) parser.parse(builder.toString());
        ArrayList<PictureIUT> listOfPictureIUT = new ArrayList();
        for (Object object : array) {
            JSONObject jsonObject = (JSONObject) object;
            PictureIUT pictureIUT = new PictureIUT((String) jsonObject.get("nom"), "misc/images/" + (String) jsonObject.get("nom"));
            jsonObject.get("histogram1");
            /*pictureIUT.setRouge((double[]) histogram1.get(1));
            if (jsonObject.containsKey("histogram3")) {
                pictureIUT.setBleu((double[]) jsonObject.get("histogram3"));
                pictureIUT.setVert((double[]) jsonObject.get("histogram2"));
            }
            listOfPictureIUT.add(pictureIUT);*/
        }
        System.out.println(listOfPictureIUT);
        return listOfPictureIUT;
    }
}
