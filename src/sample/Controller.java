package sample;

import com.sun.nio.sctp.Notification;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.scene.control.TextField;
import org.xml.sax.SAXException;
import sample.bibliothek.*;
public class Controller implements Initializable {
  @FXML
  public WebView webView;
  public TextField bookTitle;
  public Label lastEditor;
  public Label lastEdition;
  ActionEvent buttonEvent;
  public WebEngine engine;
  Zettelkasten zettelkasten = new Zettelkasten();
  @Override
  public void initialize(URL _url, ResourceBundle _resourceBundle) {
    engine = webView.getEngine();
  }

  public void showWebsite(ActionEvent _actionEvent) throws IOException, SAXException {
    Wikibook wikiBookMedium;
    String title = bookTitle.getText().replace(" ", "_");
    String link = "http://de.wikibooks.org/wiki/" + bookTitle.getText();
    wikiBookMedium=zettelkasten.findWikiBook(title);
    lastEditor.setText("Letzter Bearbeiter " + wikiBookMedium.getAutor());
    lastEdition.setText("Letzte Änderung " + wikiBookMedium.getLetzteBearbeitung());
    engine.load(link);
    buttonEvent = _actionEvent;
  }

  public void enterPressed(KeyEvent _keyEvent) throws IOException, SAXException {
    if (_keyEvent.getCode()== KeyCode.ENTER){
      showWebsite(buttonEvent);
    }
  }
}
