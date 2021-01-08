/** @author Teimurazi Euashvili(Matrikelnummer:18808) */
package sample.bibliothek;

import java.io.Serializable;

/**
 * Basis class for all books
 */
public abstract class Medium implements Comparable<Medium>, Serializable {
  public String title;

  public Medium(String _titel) {
    this.title = _titel;
  }

  public String getTitel() {
    return title;
  }

  public void setTitel(String _titel) {
    title = _titel;
  }

  @Override
  public int compareTo(Medium object) {
    return this.getTitel().compareTo(object.getTitel());
  }

  public String calculateRepresentation() {
    StringBuilder medium = new StringBuilder("Titel: ");
    //medium.append("\"");
    medium.append(this.title);
    //medium.append("\"");
    return medium.toString();
  }
}
