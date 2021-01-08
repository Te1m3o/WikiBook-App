/** @author Teimurazi Euashvili(Matrikelnummer:18808) */
package sample.bibliothek;

public class CD extends Medium {
  private String label;
  private String kuenstler;

  public CD(String _titel, String _label, String _kuenstler) {
    super(_titel);
    this.label = _label;
    this.kuenstler = _kuenstler;
  }

  // Getter und Setter
  public String getLabel() {
    return label;
  }

  public void setLabel(String label) {
    this.label = label;
  }

  public String getKuenstler() {
    return kuenstler;
  }

  public void setKuenstler(String kuenstler) {
    this.kuenstler = kuenstler;
  }
@Override
  public String calculateRepresentation() {
    StringBuilder cd = new StringBuilder();
    cd.append(super.calculateRepresentation());
    cd.append("\n");
    cd.append("Label: ");
    cd.append(getLabel());
    cd.append("\n");
    cd.append("Künstler: ");
    cd.append(getKuenstler());
    return String.valueOf(cd);
  }
}
