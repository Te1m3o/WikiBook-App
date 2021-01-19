package sample;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.ResourceBundle;
import java.util.Scanner;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.scene.control.TextField;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.xml.sax.SAXException;
import sample.bibliothek.*;
import sample.exceptions.*;

/** Fehler abfangen, falls Synonim nicht gefunden ist */
public class Controller implements Initializable {
  @FXML public WebView webView;
  public TextField bookTitle;
  public Label lastEditor;
  public Label lastEdition;
  public ListView zListView;
  public ChoiceBox sort;
  public ListView synonymView;
  public Button synonymButton;
  public Button backButton;
  public Button nextButton;
  public ChoiceBox termHistory;
  String lastItem;
  String nextItem;
  int counter = 1;
  ActionEvent webAction;
  ActionEvent buttonClicked;
  ObservableList<String> sortWay = FXCollections.observableArrayList("A-Z", "Z-A");
  ObservableList<Integer> termItems = FXCollections.observableArrayList();
  ObservableList<String> zettelkastenItems = FXCollections.observableArrayList();
  ObservableList<String> synonymItems = FXCollections.observableArrayList();
  ActionEvent buttonEvent;
  public WebEngine engine;
  Zettelkasten myZettelkasten = new Zettelkasten();
  ArrayList<String> searchedItems = new ArrayList<String>();
  int termID = -1;
  boolean bookFound = true;

  @Override
  public void initialize(URL _url, ResourceBundle _resourceBundle) {
    sort.setItems(sortWay);
    sort.setValue("A-Z");
    termHistory.setValue(termID);
    engine = webView.getEngine();
    nextButton.setDisable(true);
  }

  public void showWebsite(ActionEvent _actionEvent)
      throws IOException, SAXException, BookNotFoundException, ParseException, SynonymNotFound {
    webAction = _actionEvent;
    Wikibook wikiBookMedium = null;
    /** Active the synonym area * */
    synonymView.setDisable(false);
    synonymButton.setDisable(false);
    /** parse title and load the link in the internet * */
    String title = bookTitle.getText().replace(" ", "_");
    String link = "http://de.wikibooks.org/wiki/" + bookTitle.getText();
    engine.load(link);
    /** Search Synonyms for the given title * */
    searchSynonym(buttonClicked);
    /** Give the error message, if the book not found in Wikibook * */
    try {
      wikiBookMedium = (Wikibook) myZettelkasten.findWikiBook(title);
      if (wikiBookMedium == null) {
        throw new BookNotFoundException(
            "Das Buch mit dem Titel " + "'" + bookTitle.getText() + "'" + " ist nicht gefunden");
      }
    } catch (IOException | BookNotFoundException _e) {
      Alert alert = new Alert(AlertType.ERROR);
      alert.setHeaderText("BookNotFound Error");
      alert.setContentText(_e.getMessage());
      alert.showAndWait();
      bookFound = false;
      nextButton.setDisable(true);
      counter--;
    }
    /** prints the last editor and the time of the last edit * */
    lastEditor.setText("Letzter Bearbeiter " + wikiBookMedium.getAutor());
    lastEdition.setText("Letzte Änderung " + wikiBookMedium.getLetzteBearbeitung());
    /** Add the item to the Searched history list * */
    if (!(searchedItems.contains(title) && bookFound)) {
      searchedItems.add(title);
      termID++;
      termItems.add(termID);
      termHistory.setItems(termItems);
      termHistory.setValue(termItems.get(termItems.size()-1));
    }
  }

  public void enterPressed(KeyEvent _keyEvent)
      throws IOException, SAXException, BookNotFoundException, ParseException, SynonymNotFound {
    if (_keyEvent.getCode() == KeyCode.ENTER) {
      showWebsite(buttonEvent);
    }
  }

  /**
   * Adds the WikiBook to the Zettelkasten and displays it on the LisView
   *
   * @param _actionEvent
   * @throws IOException
   * @throws SAXException
   * @throws BookNotFoundException
   */
  public void addMedium(ActionEvent _actionEvent)
      throws IOException, SAXException, BookNotFoundException {
    Wikibook wikiBookMedium;
    String title = bookTitle.getText().replace(" ", "_");
    wikiBookMedium = (Wikibook) myZettelkasten.findWikiBook(title);
    myZettelkasten.addMedium(wikiBookMedium);
    zettelkastenItems.add(
        myZettelkasten
            .getMyZettelkasten()
            .get(myZettelkasten.getMyZettelkasten().size() - 1)
            .getTitel());
    zListView.setItems(zettelkastenItems);
  }

  /**
   * Sorts the Zettelkasten Array and displays it sorted on the LisView
   *
   * @param _actionEvent
   */
  public void sortArray(ActionEvent _actionEvent) {
    String sortAlgorithm = (String) sort.getValue();
    zettelkastenItems.clear();
    myZettelkasten.sort(sortAlgorithm);
    for (int i = 0; i < myZettelkasten.getMyZettelkasten().size(); i++) {
      zettelkastenItems.add(myZettelkasten.getMyZettelkasten().get(i).getTitel());
    }
    zListView.setItems(zettelkastenItems);
  }

  /**
   * Serialises the object of Zettelkasten and saves it in the file 'myZettelkasten'
   *
   * @param _actionEvent
   * @throws IOException
   */
  public void save(ActionEvent _actionEvent) throws IOException {
    BinaryPersisenty dataSaver = new BinaryPersisenty();
    dataSaver.save(myZettelkasten, "myZettelkasten");
    Alert alert = new Alert(AlertType.CONFIRMATION);
    alert.setHeaderText("Gespeichert");
    alert.setContentText("Zettelkasten ist mit der Name 'myZettelkasten' erfolgreich gespeichert");
    alert.showAndWait();
  }

  /**
   * Deserializes the Object from the file: 'myZettelkasten' and displays it on the ListView
   *
   * @param _actionEvent
   * @throws FileNotFoundException
   */
  public void load(ActionEvent _actionEvent) throws FileNotFoundException {
    BinaryPersisenty dataLoader = new BinaryPersisenty();
    myZettelkasten = dataLoader.load("myZettelkasten");
    zettelkastenItems.clear();
    for (int i = 0; i < myZettelkasten.getMyZettelkasten().size(); i++) {
      zettelkastenItems.add(myZettelkasten.getMyZettelkasten().get(i).getTitel());
    }
    zListView.setItems(zettelkastenItems);
    Alert alert = new Alert(AlertType.CONFIRMATION);
    alert.setHeaderText("Loaded");
    alert.setContentText("Die zuletzt gespeicherte Zettelkasten ist erfolgreich geladen");
    alert.showAndWait();
  }

  /**
   * Searches the synonymous with the given title and displays the result on the ListView
   *
   * @throws IOException
   * @throws ParseException
   */
  public void searchSynonym(ActionEvent _actionEvent)
      throws IOException, ParseException, SynonymNotFound, BookNotFoundException, SAXException {
    String toFind;
    String selectedItem = (String) synonymView.getSelectionModel().getSelectedItem();
    _actionEvent = buttonClicked;
    if (selectedItem == null) {
      toFind = bookTitle.getText();
      synonymItems.clear();
    } else {
      toFind = selectedItem;
      bookTitle.setText(toFind);
      synonymItems.clear();
      synonymView.setItems(synonymItems);
      showWebsite(webAction);
    }
    String BasisUrl = "http://api.corpora.uni-leipzig.de/ws/similarity/";
    String Corpus = "deu_news_2012_1M";
    String Anfragetyp = "/coocsim/";
    String Parameter = "?minSim=0.1&limit=50";
    String Suchbegriff = toFind;
    URL myURL;
    myURL = new URL(BasisUrl + Corpus + Anfragetyp + Suchbegriff + Parameter);
    JSONParser jsonParser = new JSONParser();
    try {
      String jsonResponse = streamToString(myURL.openStream());
      // den gelieferten String in ein Array parsen
      JSONArray jsonResponseArr = (JSONArray) jsonParser.parse(jsonResponse);
      try {
        if (jsonResponseArr.size() < 1) {
          synonymItems.clear();
          throw new SynonymNotFound();
        }
        // das erste Element in diesem Array
        JSONObject firstEntry = (JSONObject) jsonResponseArr.get(0);
        // aus dem Element den Container für das Synonym beschaffen
        JSONObject wordContainer = (JSONObject) firstEntry.get("word");
        // aus dem Container das Synonym beschaffen
        String synonym = (String) wordContainer.get("word");
        // alle abgefragten Synonyme
        for (Object el : jsonResponseArr) {
          wordContainer = (JSONObject) ((JSONObject) el).get("word");
          synonym = (String) wordContainer.get("word");
          synonymItems.add(synonym);
        }
        /** Sort the Array Ascending * */
        Collections.sort(synonymItems);
        /** Display Synonyms on the Synonym view * */
        synonymView.setItems(synonymItems);
        /** If Synonym not found hide the synonym view and the button */
      } catch (SynonymNotFound err) {
        synonymItems.add("<keine>");
        synonymView.setItems(synonymItems);
        synonymView.setDisable(true);
        synonymButton.setDisable(true);
      }
      /** Catch the Network error * */
    } catch (Exception err) {
      Alert alert = new Alert(AlertType.ERROR);
      alert.setHeaderText("ERROR");
      alert.setContentText("Fehler beim Zugriff auf den Wortschatzes");
      alert.showAndWait();
    }
  }
  /**
   * Reads InputStream's contents into String
   *
   * @param is - the InputStream
   * @return String representing is' contents
   * @throws IOException
   */
  public static String streamToString(InputStream is) throws IOException {
    String result = "";
    // other options:
    // https://stackoverflow.com/questions/309424/read-convertan-inputstream-to-a-string
    try (Scanner s = new Scanner(is)) {
      s.useDelimiter("\\A");
      result = s.hasNext() ? s.next() : "";
      is.close();
    }
    return result;
  }

  public void mouseClicked(MouseEvent _mouseEvent)
      throws SynonymNotFound, BookNotFoundException, SAXException, ParseException, IOException {
    if (_mouseEvent.getClickCount() == 2) {
      searchSynonym(buttonClicked);
    }
  }

  public void previousItem(ActionEvent _actionEvent)
      throws SynonymNotFound, SAXException, BookNotFoundException, ParseException, IOException {
    counter++;
    nextButton.setDisable(false);
    lastItem = (searchedItems.get(searchedItems.size() - counter));
    System.out.println(searchedItems.indexOf(lastItem));
    bookTitle.setText(lastItem);
    showWebsite(webAction);
    if (searchedItems.indexOf(lastItem) == 0) {
      backButton.setDisable(true);
    }
    termHistory.setValue(termItems.get(termItems.indexOf(termHistory.getValue())-1));
  }

  public void nextItem(ActionEvent _actionEvent)
      throws SynonymNotFound, SAXException, BookNotFoundException, ParseException, IOException {
    nextItem = searchedItems.get(searchedItems.indexOf(lastItem) + 1);
    System.out.println(searchedItems.indexOf(nextItem));
    bookTitle.setText(nextItem);
    showWebsite(webAction);
    backButton.setDisable(false);
    counter--;
    lastItem = (searchedItems.get(searchedItems.size() - counter));
    if (searchedItems.indexOf(nextItem) == searchedItems.size() - 1) {
      nextButton.setDisable(true);
    }
    termHistory.setValue(termItems.get(termItems.indexOf(termHistory.getValue())+1));
  }
}
