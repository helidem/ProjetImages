package util;

import appli.Main;
import appli.PictureIUT;
import org.json.simple.JSONArray;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.nio.file.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;

public class JSONProduction {


    /**
     * Permet d'indexer les fichiers images et créer le fichier json
     */
    public static void jsonEncodeRGB(String imageBankPath) throws IOException {
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
                PictureIUT pic = new PictureIUT(image.getName(), image.getPath());
                pic = RGB.traiterImageRGB(pic);


                JSONObject jsonObject = new JSONObject();
                jsonObject.put("nom", image.getName());
                double[] histogram1 = pic.getRouge();



                jsonObject.put("histogram1", Arrays.toString(histogram1));
                if (pic.getImg().getBDim() > 2) { // si l'image est en couleur
                    double[] histogram2 = pic.getVert();
                    double[] histogram3 = pic.getBleu();
                    jsonObject.put("histogram2", Arrays.toString(histogram2));
                    jsonObject.put("histogram3", Arrays.toString(histogram3));
                }
                jsonArray.add(jsonObject);
            }
            file.write(jsonArray.toJSONString());
            file.close();
        }
    }

    public static void jsonEncodeHSV(String imageBankPath) throws IOException {
        File dir = new File(imageBankPath);
        File[] directoryListing = dir.listFiles();

        if (directoryListing != null) {
            if (Files.exists(FileSystems.getDefault().getPath("misc/outHSV.json"))) {
                Files.delete(FileSystems.getDefault().getPath("misc/outHSV.json"));
            }
            FileWriter file = null;
            file = new FileWriter("misc/outHSV.json");
            JSONArray jsonArray = new JSONArray();
            for (File image : directoryListing) {
                PictureIUT pic = new PictureIUT(image.getName(), image.getPath());

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("nom", image.getName());
                double[] histogram1 = pic.getH();
                double[] histogram2 = pic.getS();
                double[] histogram3 = pic.getV();
                jsonObject.put("histogram1", Arrays.toString(histogram1));
                jsonObject.put("histogram2", Arrays.toString(histogram2));
                jsonObject.put("histogram3", Arrays.toString(histogram3));
                jsonArray.add(jsonObject);
            }
            file.write(jsonArray.toJSONString());
            file.close();
        }
    }

    /**
     * Permet de créer une image à partir des données dans le fichier json
     *
     * @param jsonPath
     * @return l'image
     * @throws IOException
     */
    public static ArrayList<PictureIUT> jsonDecodeRGB(String jsonPath) throws IOException, ParseException {
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
            PictureIUT pictureIUT = new PictureIUT((String) jsonObject.get("nom"), "misc/motos/" + (String) jsonObject.get("nom"));
            String[] separatedDoublesHisto1 = jsonObject.get("histogram1").toString().substring(1, jsonObject.get("histogram1").toString().length() - 1).split(",");
            int cpt1 = 0;
            double[] histogram1 = new double[32];
            for (String s : separatedDoublesHisto1) {
                histogram1[cpt1] = Double.parseDouble(s);

                cpt1++;
            }
            pictureIUT.setRouge(histogram1);
            // si l'image est en couleur :
            if (jsonObject.containsKey("histogram3")) {
                String[] separatedDoublesHisto2 = jsonObject.get("histogram2").toString().substring(1, jsonObject.get("histogram2").toString().length() - 1).split(",");
                int cpt2 = 0;
                double[] histogram2 = new double[32];
                for (String s : separatedDoublesHisto2) {
                    histogram2[cpt2] = Double.parseDouble(s);
                    cpt2++;
                }

                String[] separatedDoublesHisto3 = jsonObject.get("histogram3").toString().substring(1, jsonObject.get("histogram3").toString().length() - 1).split(",");
                int cpt3 = 0;
                double[] histogram3 = new double[32];
                for (String s : separatedDoublesHisto3) {
                    histogram3[cpt3] = Double.parseDouble(s);
                    cpt3++;
                }
                pictureIUT.setBleu(histogram3);
                pictureIUT.setVert(histogram2);
            }
            listOfPictureIUT.add(pictureIUT);
        }
        return listOfPictureIUT;
    }

    public static ArrayList<PictureIUT> jsonDecodeHSV(String jsonPath) throws IOException, ParseException {
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
            PictureIUT pictureIUT = new PictureIUT((String) jsonObject.get("nom"), "misc/motos/" + (String) jsonObject.get("nom"));
            String[] separatedDoublesHisto1 = jsonObject.get("histogram1").toString().substring(1, jsonObject.get("histogram1").toString().length() - 1).split(",");
            int cpt1 = 0;
            double[] histogram1 = new double[360];
            for (String s : separatedDoublesHisto1) {
                histogram1[cpt1] = Double.parseDouble(s);
                cpt1++;
            }
            pictureIUT.setH(histogram1);

            String[] separatedDoublesHisto2 = jsonObject.get("histogram2").toString().substring(1, jsonObject.get("histogram2").toString().length() - 1).split(",");
            int cpt2 = 0;
            double[] histogram2 = new double[101];
            for (String s : separatedDoublesHisto2) {
                histogram2[cpt2] = Double.parseDouble(s);
                cpt2++;
            }

            String[] separatedDoublesHisto3 = jsonObject.get("histogram3").toString().substring(1, jsonObject.get("histogram3").toString().length() - 1).split(",");
            int cpt3 = 0;
            double[] histogram3 = new double[101];
            for (String s : separatedDoublesHisto3) {
                histogram3[cpt3] = Double.parseDouble(s);
                cpt3++;
            }
            pictureIUT.setV(histogram3);
            pictureIUT.setS(histogram2);

            listOfPictureIUT.add(pictureIUT);
        }
        return listOfPictureIUT;
    }
}
