/** @author Teimurazi Euashvili(Matrikelnummer:18808) */
package sample.bibliothek;

public class Zeitschrift extends Medium {
  private String ISSN;
  private int volume;
  private int nummer;

  public Zeitschrift(String _titel, String _ISSN, int _volume, int _nummer) {
    super(_titel);
    this.ISSN = _ISSN;
    this.volume = _volume;
    this.nummer = _nummer;
  }

  public String getISSN() {
    return ISSN;
  }

  public void setISSN(String ISSN) {
    this.ISSN = ISSN;
  }

  public int getVolume() {
    return volume;
  }

  public void setVolume(int volume) {
    this.volume = volume;
  }

  public int getNummer() {
    return nummer;
  }

  public void setNummer(int nummer) {
    this.nummer = nummer;
  }

  @Override
  public String calculateRepresentation() {
    StringBuilder zeitschrift = new StringBuilder("");
    zeitschrift.append(super.calculateRepresentation());
    zeitschrift.append("\n");
    zeitschrift.append("ISSN: ");
    zeitschrift.append(getISSN());
    zeitschrift.append("\n");
    zeitschrift.append("Volume: ");
    zeitschrift.append(getVolume());
    zeitschrift.append("\n");
    zeitschrift.append("Nummer: ");
    zeitschrift.append(getNummer());
    return String.valueOf(zeitschrift);
  }
}
