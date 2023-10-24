package pl.ksr.view;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import pl.ksr.classifier.algorithm.QualityMeasures;
import pl.ksr.classifier.algorithm.Knn;
import pl.ksr.classifier.metrics.MetricSelector;
import pl.ksr.classifier.similarity.NGramSelector;
import pl.ksr.data.*;
import pl.ksr.exception.DictionaryNotFoundException;
import pl.ksr.extractor.DataReader;

import java.io.*;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.*;

public class PrimaryController implements Initializable {
    public CheckBox currenciesInText = new CheckBox();
    public CheckBox countriesInTitle = new CheckBox();
    public CheckBox firstFromDictionaryInTitle = new CheckBox();
    public CheckBox country = new CheckBox();
    public CheckBox land = new CheckBox();
    public CheckBox person = new CheckBox();
    public CheckBox company = new CheckBox();
    public CheckBox longestWord = new CheckBox();
    public CheckBox longestWordFromDictionary = new CheckBox();
    public CheckBox lastWordFromDictionary = new CheckBox();
    public CheckBox bigLetterCount = new CheckBox();
    public CheckBox dictionaryCount = new CheckBox();
    @FXML
    private AnchorPane pane = new AnchorPane();
    @FXML
    private TextField kTextField = new TextField();
    @FXML
    private Slider slider = new Slider();
    @FXML
    private Label learningLabel = new Label();
    @FXML
    private Label testLabel = new Label();
    @FXML
    private ComboBox metricSelector;
    @FXML
    private ComboBox similaritySelector;
    @FXML
    private ProgressBar progressBar = new ProgressBar();

    @FXML
    private Button saveButton = new Button();
    @FXML
    private Button loadButton = new Button();

    @FXML
    private Button classifyButton = new Button();

    @FXML
    private Label progressLabel = new Label();

    private ArrayList<Article> articles = new ArrayList<>();
    private Dictionary dictionary;

    private int division = 0;
    ArrayList<CheckBox> list = new ArrayList<>();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        classifyButton.setDisable(true);
        try {
            articles = DataReader.read();
        } catch (IOException e) {
            AlertBox.messageBox("Error",
                    "Data Not Found", Alert.AlertType.ERROR);
        }
        try {
            dictionary = new Dictionary();
        } catch (DictionaryNotFoundException e) {
            AlertBox.messageBox("Error",
                    "Dictionary Not Found", Alert.AlertType.ERROR);
        }
        progressBar.setVisible(false);
        metricSelector.setItems(FXCollections.observableArrayList(
                "Euclidean", "Manhattan", "Chebyshew"));
        similaritySelector.setItems(FXCollections.observableArrayList(
                "Trigram", "Tetragram"));
        slider.setMax(100);
        slider.setMin(0);
        learningLabel.setText(String.valueOf(division) + " %");
        testLabel.setText(String.valueOf(100 - division) + " %");
        slider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                division = (int) slider.getValue();
                learningLabel.setText(String.valueOf(division) + " %");
                testLabel.setText(String.valueOf(100 - division) + " %");
            }
        });

        list.add(currenciesInText);
        list.add(countriesInTitle);
        list.add(firstFromDictionaryInTitle);
        list.add(country);
        list.add(land);
        list.add(person);
        list.add(company);
        list.add(longestWord);
        list.add(longestWordFromDictionary);
        list.add(lastWordFromDictionary);
        list.add(bigLetterCount);
        list.add(dictionaryCount);
    }


    @FXML
    protected void onClassifyClick() throws ExecutionException, InterruptedException, IOException {
        pane.getChildren().clear();
        if (!validateK()) {
            AlertBox.messageBox("k Value Error",
                    "Wrong k value", Alert.AlertType.WARNING);
            return;
        }
        int k = Integer.parseInt(kTextField.getText());

        if (!validateMetricSelection()) {
            AlertBox.messageBox("Metric Error",
                    "Select Metric", Alert.AlertType.WARNING);
            return;
        }
        String metric = metricSelector.getSelectionModel().getSelectedItem().toString();

        if (!validateSimilaritySelection()) {
            AlertBox.messageBox("Similarity Error",
                    "Select Similarity", Alert.AlertType.WARNING);
            return;
        }
        String similarity = similaritySelector.getSelectionModel().getSelectedItem().toString();

        if (!validateDatasetDivision()) {
            AlertBox.messageBox("Dataset Division Error",
                    "Wrong Dataset Division", Alert.AlertType.WARNING);
            return;
        }
        runKNN(articles, k, metric, similarity, division);
    }

    @FXML
    protected void onSaveClick() throws ExecutionException, InterruptedException {
        if (!validateVector()) {
            AlertBox.messageBox("Vector Error",
                    "Select Features for Vector", Alert.AlertType.WARNING);
        } else {
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("txt file",
                    ".txt");
            fileChooser.getExtensionFilters().add(extensionFilter);
            final File file = fileChooser.showSaveDialog(new Stage());
            if (file != null) {

                Task<Void> generation = new Task<Void>() {

                    @Override
                    protected Void call() throws Exception {
                        int i = 0;
                        try (PrintWriter writer = new PrintWriter(file)) {
                            for (CheckBox checkBox : list) {
                                writer.println(checkBox.isSelected());
                            }
                            for (Article article : articles) {
                                prepareVectors(article, dictionary);
                                updateProgress(i + 1, articles.size());
                                DecimalFormat df = new DecimalFormat("## %");
                                updateMessage(String.valueOf(df.format(progressBar.getProgress())));
                                writer.println(article.getVector());
                                i++;
                            }
                            Thread.sleep(100);
                            writer.close();
                            updateProgress(i + 1, articles.size());
                            DecimalFormat df = new DecimalFormat("## %");
                            updateMessage(String.valueOf(df.format(progressBar.getProgress())));
                            classifyButton.setDisable(false);
                            return null;
                        } catch (IOException e) {
                            AlertBox.messageBox("Save Error",
                                    "Error while saving", Alert.AlertType.ERROR);
                            return null;
                        }

                    }

                };


                generation.messageProperty().addListener(new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                        progressLabel.setText(t1);
                    }
                });
                progressBar.setVisible(true);
                progressBar.progressProperty().unbind();
                progressBar.progressProperty().bind(generation.progressProperty());
                progressLabel.setVisible(true);


                Thread th = new Thread(generation);
                th.setDaemon(true);
                th.start();

            }
        }

    }

    @FXML
    protected void onLoadClick() {
        FileChooser fileChooser = new FileChooser();
        final File file = fileChooser.showOpenDialog(new Stage());
        if (file != null) {
            String path = file.getAbsolutePath();
            path = path.replace("\\", "/");
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(path)))) {
                String line;
                int i = 1;
                ArrayList<Boolean> features = new ArrayList<>();
                ArrayList<String> vectors = new ArrayList<>();
                while ((line = bufferedReader.readLine()) != null) {
                    if (i <= 12) {
                        features.add(Boolean.valueOf(line));
                    } else {
                        vectors.add(line);
                    }
                    i++;
                }
                loadFeatures(features);
                loadVectors(vectors);
            } catch (IOException e) {
                AlertBox.messageBox("Loading Error",
                        "Error while loading vectors", Alert.AlertType.ERROR);
            }
        }
    }

    private void loadFeatures(ArrayList<Boolean> features) {
        int i = 0;
        for (Boolean feature : features) {
            list.get(i).setSelected(feature);
            i++;
        }
    }

    private void loadVectors(ArrayList<String> vectors) {
        Task<Void> generation = new Task<Void>() {

            @Override
            protected Void call() throws Exception {
                int i = 0;
                    for (String vector : vectors) {
                        vector = vector.replace("[", "");
                        vector = vector.replace("]", "");
                        vector = vector.replace(" ", "");
                        String[] features = vector.split(",");
                        FeatureVector.Builder builder = new FeatureVector.Builder();
                        builder.setCurrenciesInText(Integer.parseInt(features[0]));
                        builder.setCountriesInTitle(Integer.parseInt(features[1]));
                        builder.setFirstInDictionaryInTitle(features[2]);
                        builder.setCountry(features[3]);
                        builder.setLand(features[4]);
                        builder.setPerson(features[5]);
                        builder.setCompany(features[6]);
                        builder.setLongestWord(features[7]);
                        builder.setLongestWordFromDictionary(features[8]);
                        builder.setLastWordFromDictionary(features[9]);
                        builder.setBigLetterCount(Double.parseDouble(features[10]));
                        builder.setDictionaryCount(Double.parseDouble(features[11]));
                        articles.get(i).setVector(builder.build());
                        updateProgress(i + 1, articles.size());
                        DecimalFormat df = new DecimalFormat("## %");
                        updateMessage(String.valueOf(df.format(progressBar.getProgress())));
                        i++;
                    }
                    Thread.sleep(100);
                    updateProgress(i + 1, articles.size());
                    DecimalFormat df = new DecimalFormat("## %");
                    updateMessage(String.valueOf(df.format(progressBar.getProgress())));
                    classifyButton.setDisable(false);
                    return null;
                }

            };
        generation.messageProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                progressLabel.setText(t1);
            }
        });
        progressBar.setVisible(true);
        progressBar.progressProperty().unbind();
        progressBar.progressProperty().bind(generation.progressProperty());
        progressLabel.setVisible(true);

        Thread th = new Thread(generation);
        th.setDaemon(true);
        th.start();
    }


    private boolean validateVector() {
        if (currenciesInText.isSelected()) {
            return true;
        }
        if (countriesInTitle.isSelected()) {
            return true;
        }
        if (firstFromDictionaryInTitle.isSelected()) {
            return true;
        }
        if (country.isSelected()) {
            return true;
        }
        if (land.isSelected()) {
            return true;
        }
        if (person.isSelected()) {
            return true;
        }
        if (company.isSelected()) {
            return true;
        }
        if (longestWord.isSelected()) {
            return true;
        }
        if (longestWordFromDictionary.isSelected()) {
            return true;
        }
        if (lastWordFromDictionary.isSelected()) {
            return true;
        }
        if (bigLetterCount.isSelected()) {
            return true;
        }
        if (dictionaryCount.isSelected()) {
            return true;
        }
        return false;
    }

    private boolean validateK() {
        int split = articles.size() * division / 100;
        int maxK = articles.subList(0, split).size();
        try {
            int value = Integer.parseInt(kTextField.getText());
            if (value > maxK) {
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean validateMetricSelection() {
        if (metricSelector.getSelectionModel().getSelectedItem() != null) {
            return true;
        } else {
            return false;
        }
    }

    private boolean validateSimilaritySelection() {
        if (similaritySelector.getSelectionModel().getSelectedItem() != null) {
            return true;
        } else {
            return false;
        }
    }

    private boolean validateDatasetDivision() {
        if (slider.getValue() == 100) {
            return false;
        }
        return slider.getValue() != 0;
    }


    private void runKNN(ArrayList<Article> articles, int k, String metric,
                        String similarity, int division) throws InterruptedException {
        int setSize = articles.size();
        int split = setSize * division / 100;
        List<Article> learningArticles = articles.subList(0, split);
        List<Article> testArticles = articles.subList(split, setSize);


        Task<Void> classification = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                int i = 0;
                for (Article article : testArticles) {
                    i++;
                    Knn.classify(learningArticles, article, k, similarityChooser(similarity), metricChooser(metric));
                    updateProgress(i + 1, testArticles.size());
                    DecimalFormat df = new DecimalFormat("## %");
                    updateMessage(String.valueOf(df.format(progressBar.getProgress())));
                }
                Thread.sleep(100);
                updateProgress(testArticles.size(), testArticles.size());
                DecimalFormat df = new DecimalFormat("## %");
                updateMessage(String.valueOf(df.format(progressBar.getProgress())));
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        prepareQualityMeasures(testArticles, learningArticles);
                    }
                });

                return null;
            }
        };

        classification.messageProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                progressLabel.setText(t1);
            }
        });
        progressBar.setVisible(true);
        progressBar.progressProperty().unbind();
        progressBar.progressProperty().bind(classification.progressProperty());
        progressLabel.setVisible(true);


        Thread th = new Thread(classification);
        th.setDaemon(true);
        th.start();


    }


    private MetricSelector metricChooser(String metric) {
        return switch (metric) {
            case "Chebyshew" -> MetricSelector.CHEBYSHEW;
            case "Manhattan" -> MetricSelector.MANHATTAN;
            default -> MetricSelector.EUCLIDEAN;
        };
    }

    private NGramSelector similarityChooser(String similarity) {
        return switch (similarity) {
            case "Tetragram" -> NGramSelector.TETRAGRAM;
            default -> NGramSelector.TRIGRAM;
        };
    }


    private void prepareVectors(Article article, Dictionary dictionary) throws ExecutionException, InterruptedException {
        FeatureVector.Builder builder = new FeatureVector.Builder();
        ArrayList<CompletableFuture<FeatureVector.Builder>> taskList = new ArrayList<>(12);
        for (int i = 0; i < 12; i++) {
            taskList.add(CompletableFuture.supplyAsync(() -> builder));
        }

        if (currenciesInText.isSelected()) {
            taskList.set(0, CompletableFuture.supplyAsync(() ->
                    builder.currenciesInText(article.getBody(), dictionary.getCurrencies())));
        }
        if (countriesInTitle.isSelected()) {
            taskList.set(1, CompletableFuture.supplyAsync(() ->
                    builder.countriesInTitle(article.getTitle(), dictionary.getCountries())));
        }
        if (firstFromDictionaryInTitle.isSelected()) {
            taskList.set(2, CompletableFuture.supplyAsync(() ->
                    builder.firstInDictionaryInTitle(article.getTitle(), dictionary.getDictionary())));
        }
        if (country.isSelected()) {
            taskList.set(3, CompletableFuture.supplyAsync(() ->
                    builder.country(article.getBody(), dictionary.getCountries())));
        }
        if (land.isSelected()) {
            taskList.set(4, CompletableFuture.supplyAsync(() ->
                    builder.land(article.getBody(), dictionary.getLands())));
        }
        if (person.isSelected()) {
            taskList.set(5, CompletableFuture.supplyAsync(() ->
                    builder.person(article.getBody(), dictionary.getPeople())));
        }
        if (company.isSelected()) {
            taskList.set(6, CompletableFuture.supplyAsync(() ->
                    builder.company(article.getBody(), dictionary.getCompanies())));
        }
        if (longestWord.isSelected()) {
            taskList.set(7, CompletableFuture.supplyAsync(() ->
                    builder.longestWord(article.getBody())));
        }
        if (longestWordFromDictionary.isSelected()) {
            taskList.set(8, CompletableFuture.supplyAsync(() ->
                    builder.longestWordFromDictionary(article.getBody(), dictionary.getDictionary())));
        }
        if (lastWordFromDictionary.isSelected()) {
            taskList.set(9, CompletableFuture.supplyAsync(() ->
                    builder.lastWordFromDictionary(article.getBody(), dictionary.getDictionary())));
        }
        if (bigLetterCount.isSelected()) {
            taskList.set(10, CompletableFuture.supplyAsync(() ->
                    builder.bigLetterCount(article.getBody())));
        }
        if (dictionaryCount.isSelected()) {
            taskList.set(11, CompletableFuture.supplyAsync(() ->
                    builder.dictionaryCount(article.getBody(), dictionary.getDictionary())));
        }

        CompletableFuture.allOf(
                taskList.get(0),
                taskList.get(1),
                taskList.get(2),
                taskList.get(3),
                taskList.get(4),
                taskList.get(5),
                taskList.get(6),
                taskList.get(7),
                taskList.get(8),
                taskList.get(9),
                taskList.get(10),
                taskList.get(11)).get();
        FeatureVector vector = builder.build();
        article.setVector(vector);
    }

    private void prepareQualityMeasures(List<Article> testArticles, List<Article> learningArticles) {
        DecimalFormat df = new DecimalFormat("#.##");
        String accuracy = "ACC: " + df.format(QualityMeasures.accuracy(testArticles));
        createLabel(50, accuracy, 20, 10, 30);
        ArrayList<Double> precisions = new ArrayList<>();
        ArrayList<Integer> precisionWeights = new ArrayList<>();
        ArrayList<Double> recalls = new ArrayList<>();
        ArrayList<Integer> recallWeights = new ArrayList<>();
        ArrayList<Double> f1s = new ArrayList<>();
        ArrayList<Integer> f1sWeights = new ArrayList<>();
        int i = 1;
        createLabel(170, "LEARNING SET ELEMENTS:", 15, 140, 20);
        String setDetails = prepareSetDetails(learningArticles);
        createLabel(200, setDetails, 12, 10, 15);
        createLabel(220, "TEST SET ELEMENTS:", 15, 160, 20);
        setDetails = prepareSetDetails(testArticles);
        createLabel(250, setDetails, 12, 10, 15);
        for (String place : PlacesList.places) {
            CountryQualityMeasures countryQualityMeasures = new CountryQualityMeasures(testArticles, place);
            precisions.add(countryQualityMeasures.getPrecision());
            precisionWeights.add(countryQualityMeasures.getTotalNumberOfBelonging());
            recalls.add(countryQualityMeasures.getRecall());
            recallWeights.add(countryQualityMeasures.getTotalNumberOfBelonging());
            f1s.add(countryQualityMeasures.getF1());
            f1sWeights.add(countryQualityMeasures.getTotalNumberOfBelonging());
            createLabel(530, " COUNTRY QUALITY MEASURES:", 15, 120, 20);
            createLabel(540 + 20 * i, countryQualityMeasures.toString(), 12, 10, 15);
            createLabel(240 + 40 * i, "CLASSIFIED TO: " + place.toUpperCase(), 15, 140, 20);
            createLabel(260 + 40 * i, countryQualityMeasures.getTotalNumberOfClassified(), 12, 10, 15);
            i++;
        }


        String averagePrecision = "AVG PPV: " + df.format(QualityMeasures.calculateAverage(precisions, precisionWeights));
        createLabel(80, averagePrecision, 20, 10, 30);
        String averageRecall = "AVG TPR: " + df.format(QualityMeasures.calculateAverage(recalls, recallWeights));
        createLabel(110, averageRecall, 20, 10, 30);
        String averageF1 = "AVG F1: " + df.format(QualityMeasures.calculateAverage(f1s, f1sWeights));
        createLabel(140, averageF1, 20, 10, 30);
    }

    private void createLabel(int y, String text, int fontSize, int x, int prefHeight) {
        Label label = new Label();
        label.setStyle("-fx-font-size:" + fontSize + "px");
        pane.getChildren().add(label);
        label.setText(text);
        label.setPrefWidth(450);
        label.setPrefHeight(prefHeight);
        label.setTextFill(Color.WHITE);
        label.setLayoutX(x);
        label.setLayoutY(y);
    }

    private String prepareSetDetails(List<Article> articles) {
        StringBuilder setDetails = new StringBuilder();
        for (String place : PlacesList.places) {
            setDetails.append(place.toUpperCase());
            setDetails.append(": ");
            CountryQualityMeasures countryQualityMeasures = new CountryQualityMeasures(articles, place);
            setDetails.append(countryQualityMeasures.getTotalNumberOfBelonging());
            setDetails.append(" ");
        }
        return setDetails.toString();
    }
}