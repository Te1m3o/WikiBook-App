/** @author Teimurazi Euashvili(Matrikelnummer:18808) */
package sample.bibliothek;

import java.security.MessageDigest;
import java.util.ArrayList;

/**
 * ElBook, which is created from the website wikibooks.org
 */
public class Wikibook extends ElektronischesMedium {
private String autor;
private String letzteBearbeitung;
private String regal;
ArrayList<String> kapitel = new ArrayList<String>();

  public Wikibook(String _title, String _URL) {
    super(_title, _URL);
  }

  public ArrayList<String> getKapitel() {
    return kapitel;
  }

  public void setKapitel(ArrayList<String> _kapitel) {
    kapitel = _kapitel;
  }

  public String getAutor() {
    return autor;
  }

  public void setAutor(String _autor) {
    autor = _autor;
  }

  public String getTitel() {
    return title;
  }

  public void setTitel(String _titel) {
    title = _titel;
  }

  public String getLetzteBearbeitung() {
    return letzteBearbeitung;
  }

  public void setLetzteBearbeitung(String _letzteBearbeitung) {
    letzteBearbeitung = _letzteBearbeitung;
  }

  public String getRegal() {
    return regal;
  }

  public void setRegal(String _regal) {
    regal = _regal;
  }
}
