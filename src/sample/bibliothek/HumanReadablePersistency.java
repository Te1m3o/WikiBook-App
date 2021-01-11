/** @author Teimurazi Euashvili(Matrikelnummer:18808) */
package sample.bibliothek;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

public class HumanReadablePersistency implements Persistency {

  /**
   * saves the representations of all Zettelkasten in the file as UTF_8 coded text
   * @param _zettelkasten Object of the class Zettelkasten, where all Mediums are located
   * @param dateiName in which the representation of all mediums must be saved
   */
  @Override
  public void save(Zettelkasten _zettelkasten, String dateiName) {
    try {
      OutputStreamWriter writer =
          new OutputStreamWriter(new FileOutputStream(dateiName), StandardCharsets.UTF_8);
      for (int i = 0; i < _zettelkasten.getMyZettelkasten().size(); i++) {
        writer.write(_zettelkasten.getMyZettelkasten().get(i).calculateRepresentation());
        writer.write("\r\n\n");
      }
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public Zettelkasten load(String _dateiname) {
    try {
      if (_dateiname != null) {
        throw new UnsupportedOperationException();
      }
    } catch (UnsupportedOperationException _e) {
      System.out.println("The Method load of the class HumanReadablePersistency is not ready yet");
    }
    return null;
  }
}
