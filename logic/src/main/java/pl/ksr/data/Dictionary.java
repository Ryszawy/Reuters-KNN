package pl.ksr.data;

import pl.ksr.exception.DictionaryNotFoundException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class Dictionary {
    private ArrayList<String> currencies;
    private ArrayList<String> countries;
    private ArrayList<String> companies;
    private ArrayList<String> lands;
    private ArrayList<String> people;

    private ArrayList<String> dictionary;

    private static final String DICTIONARY_FOLDER = "/dictionary/";


    public Dictionary() throws DictionaryNotFoundException {
        currencies = read("currencies");
        countries = read("countries");
        companies = read("companies");
        lands = read("lands");
        people = read("people");
        dictionary = read("dictionary");
    }



    private ArrayList<String> read(String fileName) throws DictionaryNotFoundException {
        InputStream inputStream = Dictionary.class.getResourceAsStream(DICTIONARY_FOLDER
                + fileName + ".txt");
        ArrayList<String> readedData = new ArrayList<>();
        try (InputStreamReader inputStreamReader =
                     new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                BufferedReader reader = new BufferedReader(inputStreamReader)) {
            String line;
            while ((line = reader.readLine()) != null) {
                readedData.add(line);
            }

        } catch (IOException e) {
            throw new DictionaryNotFoundException("Dictionary Not Found");
        }
        return readedData;
    }

    public ArrayList<String> getCurrencies() {
        return currencies;
    }

    public ArrayList<String> getCountries() {
        return countries;
    }

    public ArrayList<String> getCompanies() {
        return companies;
    }

    public ArrayList<String> getLands() {
        return lands;
    }

    public ArrayList<String> getPeople() {
        return people;
    }

    public ArrayList<String> getDictionary() {
        return dictionary;
    }
}
