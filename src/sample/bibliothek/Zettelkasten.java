/** @author Teimurazi Euashvili(Matrikelnummer:18808) */
package sample.bibliothek;
import sample.exceptions.BookNotFoundException;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

/** Saves all mediums and includes functions to change, remove, or insert medium */
public class Zettelkasten implements Iterable<Medium>, Serializable {
  private ArrayList<Medium> myZettelkasten = new ArrayList<Medium>();

  public ArrayList<Medium> getMyZettelkasten() {
    return myZettelkasten;
  }

  public void setMyZettelkasten(ArrayList<Medium> _myZettelkasten) {
    myZettelkasten = _myZettelkasten;
  }

  /**
   * Adds the Medium to the ArrayList myZettelKasten
   *
   * @param _medium, which must be added
   * @return true if it's successfully added, else false
   */
  public boolean addMedium(Medium _medium) {
    boolean check = true;
    if (_medium.getClass().getName().intern() == "hausaufgabe11.Buch") {
      String buch = _medium.calculateRepresentation();
      /** Check if the ISBN is valid * */
      if (buch.contains("null")) {
        check = false;
      }
    }
    if (_medium.getClass().getName().intern() == "hausaufgabe11.ElektronischesMedium") {
      String elektronischesMedium = _medium.calculateRepresentation();
      /** Check if the URL is valid and title is not empty * */
      if (elektronischesMedium.contains("null") || _medium.getTitel() == null) {
        check = false;
      }
    }
    if (check) {
      myZettelkasten.add(_medium);
    }
    if (!check) {
      System.out.printf("Media %s can't add the Zettelkasten ", _medium);
      System.out.println();
    }
    return check;
  }

  public class DuplicateEntry extends Exception {
    public DuplicateEntry() {
      super("there are several elements that can be deleted with the given title");
    }
  }
  /**
   * Removes the Medium from the ArrayList myZettelKasten
   *
   * @param _titel helps to find the Medium, which must be removed
   * @return true if the Medium is removed, else return false
   * @throws DuplicateEntry if there are more than one element to delete with the given title
   */
  public boolean dropMedium(String _titel) throws DuplicateEntry {
    boolean check = false;
    int count = 0;
    for (int i = 0; i < myZettelkasten.size(); i++) {
      if ((myZettelkasten.get(i).getTitel().equals(_titel))) {
        count++;
        check = true;
        myZettelkasten.remove(i);
        if (count > 1) {
          throw new DuplicateEntry();
        }
      }
    }
    if (!(check)) {
      System.out.println("Zettelkasten can't removed, the title could not find");
    }
    return check;
  }

  /**
   * Removes the Medium from the ArrayList - myZettelKasten
   *
   * @param _titel title of the Medium which must be removed
   * @param klass klass of the medium which must be removed
   * @return true if one of the Medium is removed, else return false
   */
  public boolean dropMedium(String _titel, String klass) {
    boolean checked = false;
    for (int i = 0; i < myZettelkasten.size(); i++) {
      if ((myZettelkasten.get(i).getTitel().equals(_titel))) {
        String elemenet = myZettelkasten.get(i).calculateRepresentation();
        if ((elemenet.contains("ISBN")) && klass.equals("Buch")) {
          myZettelkasten.remove(i);
          checked = true;
        }
        if ((elemenet.contains("ISSN")) && klass.equals("Zeitschrift")) {
          myZettelkasten.remove(i);
          checked = true;
        }
        if ((elemenet.contains("label")) && klass.equals("Cd")) {
          myZettelkasten.remove(i);
          checked = true;
        }
        if ((elemenet.contains("URL")) && klass.equals("ElMed")) {
          myZettelkasten.remove(i);
          checked = true;
        }
      }
    }
    return checked;
  }

  /**
   * finds the Medium with the title in the ArrayList: myZettelKasten
   *
   * @param titel helps to find the Medium
   * @return ArrayList of Mediums, which are founded in the list
   */
  public ArrayList<Medium> findMedium(String titel) {
    ArrayList<Medium> foundMedium = new ArrayList<>();
    for (int i = 0; i < myZettelkasten.size(); i++) {
      if ((myZettelkasten.get(i).getTitel().equals(titel))) {
        foundMedium.add(myZettelkasten.get(i));
      }
    }
    return foundMedium;
  }

  /**
   * Sorts the ArrayList if it's not sorted
   *
   * @param sortAlgorithm either "A-Z" or "Z-A"
   * @return true if it's sorted else false
   */
  public boolean sort(String sortAlgorithm) {
    boolean sorted = true;
    /** Sort the ArrayList to: A-Z * */
    boolean check = false;
    if (sortAlgorithm.intern() == "A-Z") {
      /** check if the ArrayList is already sorted * */
      for (int i = 1; i < myZettelkasten.size(); i++) {
        if (myZettelkasten.get(i - 1).compareTo(myZettelkasten.get(i)) > 0) {
          sorted = false;
        }
      }
      /** if not sorted, sort it */
      if (!(sorted)) {
        Collections.sort(myZettelkasten);
        check = true;
      }
    }
    if (sortAlgorithm.intern() == "Z-A") {
      /** check if it's already sorted * */
      for (int i = 1; i < myZettelkasten.size(); i++) {
        if (myZettelkasten.get(i).compareTo(myZettelkasten.get(i - 1)) > 0) {
          sorted = false;
        }
      }
      /** if not sorted, sort it * */
      if (!(sorted)) {
        check = true;
        Collections.sort(
            myZettelkasten,
            Collections.reverseOrder(
                new Comparator<Medium>() {
                  @Override
                  public int compare(Medium o1, Medium o2) {
                    if ((o1.getTitel().compareTo(o2.getTitel()) > 0)) {
                      return 1;
                    } else if ((o1.getTitel().compareTo(o2.getTitel()) < 0)) {
                      return -1;
                    }
                    return 0;
                  }
                }));
      }
    }

    return check;
  }

  /**
   * Is tested with the titles: 1.)Die_Kunst,_glücklich_zu_leben 2.)Die_Sprache_der_Mathematik 3.)
   * Interessante_Messungen 4.) RFID_Technologie Argument: "String title" is given by configuration
   * for "Application" filters out the Author, last edition, date, shelf and Table of content from
   * the XML
   *
   * @param title finds wikiBook with the given title; title format: enter instead of spaces
   *     underscore
   * @return Wikibook elBook if the Book with the given title is found, else null with errormessage
   * @throws SAXException if exists error in XML parser
   * @throws IOException if the XML file not found
   */
  public Medium findWikiBook(String title) throws SAXException, IOException, BookNotFoundException {
    /** create URL * */
    String URL = "https://de.wikibooks.org/wiki/Spezial:Exportieren/" + title;
    StringBuilder xmlContent = new StringBuilder();
    try {
      URL url = new URL(URL);
      /** connect that URL * */
      URLConnection connection = url.openConnection();
      connection.setDoInput(true);
      /** Get content from the Website * */
      InputStream inStream = connection.getInputStream();
      /** pack content in the Buffer * */
      BufferedReader input = new BufferedReader(new InputStreamReader(inStream));

      /** print it out in the xml data * */
      try {
        FileWriter myWriter = new FileWriter("wikiBook.xml");
        String line = "";
        while ((line = input.readLine()) != null) {
          xmlContent.append(line);
          myWriter.write(line);
          myWriter.write("\n");
        }
        myWriter.close();
        inStream.close();
      } catch (FileNotFoundException _e) {
        _e.printStackTrace();
      }
    } catch (Exception _e) {
      _e.toString();
    }
    try {
      if (!(xmlContent.toString().contains("<page>"))) {
        throw new BookNotFoundException("The book not found, please enter another book");
      }
    } catch (BookNotFoundException _e) {
      System.out.println(_e.getMessage());
    }
    /** create XMLReader * */
    XMLReader xmlReader = XMLReaderFactory.createXMLReader();
    /** path to XML Data * */
    FileReader reader = new FileReader("wikiBook.xml");
    InputSource inputSource = new InputSource(reader);
    WikiBookContentHandler newWikiBook = new WikiBookContentHandler();
    /** urlContentHandler is passed * */
    xmlReader.setContentHandler(newWikiBook);
    /** Parsing is started * */
    xmlReader.parse(inputSource);
    try {
      if (newWikiBook.getWikiBook() == null) {
        throw new BookNotFoundException("The book not found, please enter another book");
      }
      newWikiBook.getWikiBook().setUrl(URL);
    } catch (BookNotFoundException _e) {
      _e.getMessage();
    }
    return newWikiBook.getWikiBook();
  }

  int index = 0;

  public class myZettelKastenIterator implements Iterator {
    private ArrayList<Medium> myZettelkasten = null;

    public myZettelKastenIterator(ArrayList<Medium> _myZettelkasten) {
      this.myZettelkasten = _myZettelkasten;
    }

    @Override
    public boolean hasNext() {
      if (this.myZettelkasten.size() >= index + 1) {
        return true;
      } else {
        return false;
      }
    }

    @Override
    public Medium next() {
      Medium medium = null;
      medium = this.myZettelkasten.get(index);
      index++;
      return medium;
    }
  }
  /** Iterates the ArrayList myZettelKasten * */
  @Override
  public Iterator<Medium> iterator() {
    return new myZettelKastenIterator(myZettelkasten);
  }
}
