package pl.ksr.extractor;

import pl.ksr.data.Article;
import pl.ksr.data.PlacesList;
import pl.ksr.exception.DataNotFoundException;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;


public class DataReader {
    private static final String DATA_FOLDER = "/data/";
    private static final String PLACES_TAG = "<PLACES>";
    private static final String TEXT_TAG = "<TEXT";
    private static final String END_TEXT_TAG = "</TEXT>";

    public static ArrayList<Article> read() throws IOException {
        ArrayList<String> fileNames = getFiles();
        ArrayList<Article> articles = new ArrayList<>();
        for (String fileName : fileNames) {
            InputStream inputStream = DataReader.class.getResourceAsStream(DATA_FOLDER + fileName);
            try (InputStreamReader inputStreamReader =
                         new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                    BufferedReader reader = new BufferedReader(inputStreamReader)) {
                String place = "";
                String line = "";
                StringBuilder builder = new StringBuilder();
                boolean flag = false;
                boolean text = false;
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith(PLACES_TAG)) {
                        builder = new StringBuilder();
                        place = line;
                        place = Formatter.removeTags(place);
                        place = Formatter.splitTags(place);
                        flag = checkPlaces(place);
                    }
                    if (flag) {
                        if (line.contains(TEXT_TAG)) {
                            text = true;
                        }
                        if (text) {
                            builder.append(line);
                            builder.append(" ");
                        }
                        if (line.contains(END_TEXT_TAG)) {
                            text = false;
                            Article article = new Article(place,
                                    Formatter.extractTitle(builder.toString()),
                                    Formatter.extractBody(builder.toString()));
                            articles.add(article);
                        }
                    }

                }

            }
        }
        return articles;
    }

    private static ArrayList<String> getFiles() throws DataNotFoundException {
        ArrayList<String> fileNames = new ArrayList<>();
        try {
            URL url = DataReader.class.getResource(DATA_FOLDER);
            File[] files = new File(url.toURI().getPath()).listFiles();
            for (File file : files) {
                if (file.getName().matches("reut2-.*.sgm")) {
                    fileNames.add(file.getName());
                }
            }

        } catch (URISyntaxException e) {
            throw new DataNotFoundException("Path Not Found");
        }
        return fileNames;
    }

    public static boolean checkPlaces(String s) {
        for (String place : PlacesList.places) {
            if (s.equals(place)) {
                return true;
            }
        }
        return false;
    }
}
