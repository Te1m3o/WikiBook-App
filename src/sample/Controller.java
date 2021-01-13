package sample;

import bibliothek.exceptions.BookNotFoundException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.scene.control.TextField;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.xml.sax.SAXException;
import sample.bibliothek.*;

/**
 * Fehler abfangen, falls Synonim nicht gefunden ist
 *
 */
public class Controller implements Initializable {
  @FXML
  public WebView webView;
  public TextField bookTitle;
  public Label lastEditor;
  public Label lastEdition;
  public ListView zListView;
  public ChoiceBox sort;
  public ListView synonymView;
  ObservableList<String>sortWay = FXCollections.observableArrayList("A-Z", "Z-A");
  ObservableList<String> zettelkastenItems =FXCollections.observableArrayList();
  ObservableList<String> synonymItems =FXCollections.observableArrayList();
  ActionEvent buttonEvent;
  public WebEngine engine;
  Zettelkasten myZettelkasten = new Zettelkasten();
  @Override
  public void initialize(URL _url, ResourceBundle _resourceBundle) {
    sort.setItems(sortWay);
    sort.setValue("A-Z");
    engine = webView.getEngine();
  }

  public void showWebsite(ActionEvent _actionEvent) throws IOException, SAXException, BookNotFoundException {
    Wikibook wikiBookMedium = null;
    String title = bookTitle.getText().replace(" ", "_");
    String link = "http://de.wikibooks.org/wiki/" + bookTitle.getText();
    try{
      wikiBookMedium= (Wikibook) myZettelkasten.findWikiBook(title);
      if (wikiBookMedium==null){
        throw new BookNotFoundException("The book not found, please enter another book");
      }
    }catch (IOException | BookNotFoundException _e){
      Alert alert = new Alert(AlertType.ERROR);
      alert.setHeaderText("BookNotFound Error");
      alert.setContentText(_e.getMessage());
      alert.showAndWait();
    }
    lastEditor.setText("Letzter Bearbeiter " + wikiBookMedium.getAutor());
    lastEdition.setText("Letzte Änderung " + wikiBookMedium.getLetzteBearbeitung());
    engine.load(link);
  }

  public void enterPressed(KeyEvent _keyEvent)
      throws IOException, SAXException, BookNotFoundException {
    if (_keyEvent.getCode()== KeyCode.ENTER){
      showWebsite(buttonEvent);
    }
  }

  /**
   * Adds the WikiBook to the Zettelkasten and displays it on the LisView
   * @param _actionEvent
   * @throws IOException
   * @throws SAXException
   * @throws BookNotFoundException
   */
  public void addMedium(ActionEvent _actionEvent)
      throws IOException, SAXException, BookNotFoundException {
    Wikibook wikiBookMedium;
    String title = bookTitle.getText().replace(" ", "_");
    wikiBookMedium= (Wikibook) myZettelkasten.findWikiBook(title);
    myZettelkasten.addMedium(wikiBookMedium);
    zettelkastenItems.add(myZettelkasten.getMyZettelkasten().get(myZettelkasten.getMyZettelkasten().size()-1).getTitel());
    zListView.setItems(zettelkastenItems);

  }

  /**
   * Sorts the Zettelkasten Array and displays it sorted on the LisView
   * @param _actionEvent
   */
  public void sortArray(ActionEvent _actionEvent) {
    String sortAlgorithm = (String) sort.getValue();
    zettelkastenItems.clear();
    myZettelkasten.sort(sortAlgorithm);
    for (int i=0; i<myZettelkasten.getMyZettelkasten().size();i++){
      zettelkastenItems.add(myZettelkasten.getMyZettelkasten().get(i).getTitel());
    }
    zListView.setItems(zettelkastenItems);
  }

  /**
   * Serialises the object of Zettelkasten and saves it in the file 'myZettelkasten'
   * @param _actionEvent
   * @throws IOException
   */
  public void save(ActionEvent _actionEvent) throws IOException {
    BinaryPersisenty dataSaver = new BinaryPersisenty();
    dataSaver.save(myZettelkasten,"myZettelkasten");
    Alert alert = new Alert(AlertType.CONFIRMATION);
    alert.setHeaderText("Gespeichert");
    alert.setContentText("Zettelkasten ist mit der Name 'myZettelkasten' erfolgreich gespeichert");
    alert.showAndWait();
  }

  /**
   * Deserializes the Object from the file: 'myZettelkasten' and displays it on the ListView
   * @param _actionEvent
   * @throws FileNotFoundException
   */
  public void load(ActionEvent _actionEvent) throws FileNotFoundException {
    BinaryPersisenty dataLoader = new BinaryPersisenty();
    myZettelkasten = dataLoader.load("myZettelkasten");
    zettelkastenItems.clear();
    for (int i=0; i<myZettelkasten.getMyZettelkasten().size();i++){
      zettelkastenItems.add(myZettelkasten.getMyZettelkasten().get(i).getTitel());
    }
    zListView.setItems(zettelkastenItems);
    Alert alert = new Alert(AlertType.CONFIRMATION);
    alert.setHeaderText("Loaded");
    alert.setContentText("Die zuletzt gespeicherte Zettelkasten ist erfolgreich geladen");
    alert.showAndWait();
  }

  /**
   * Searches the synonymous of with the given title and displays the result on the ListView
   * @param _actionEvent
   * @throws IOException
   * @throws ParseException
   */
  public void searchSynonym(ActionEvent _actionEvent) throws IOException, ParseException {
    String suchBegriff = bookTitle.getText();
    String BasisUrl = "http://api.corpora.uni-leipzig.de/ws/similarity/";
    String Corpus = "deu_news_2012_1M";
    String Anfragetyp = "/coocsim/";
    String Suchbegriff = suchBegriff;
    String Parameter = "?minSim=0.1&limit=5";
    URL myURL;
    myURL = new URL(BasisUrl + Corpus + Anfragetyp + Suchbegriff + Parameter);
    JSONParser jsonParser = new JSONParser();
    String jsonResponse = streamToString(myURL.openStream());
// den gelieferten String in ein Array parsen
    JSONArray jsonResponseArr = (JSONArray) jsonParser.parse(jsonResponse);
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
    Collections.sort(synonymItems);
    synonymView.setItems(synonymItems);
  }
  /**
   * Reads InputStream's contents into String
   * @param is - the InputStream
   * @return String representing is' contents
   * @throws IOException
   */
  public static String streamToString(InputStream is) throws IOException {
    String result = "";
// other options: https://stackoverflow.com/questions/309424/read-convertan-inputstream-to-a-string
    try (Scanner s = new Scanner(is)) {
      s.useDelimiter("\\A");
      result = s.hasNext() ? s.next() : "";
      is.close();
    }
    return result;
  }
}
