/** @author Teimurazi Euashvili(Matrikelnummer:18808) */
package sample.bibliothek;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

/**
 * Serialization and deserialization of the Zettelkasten object
 */
public class BinaryPersisenty implements Persistency {
  /**
   * Serialization
   * @param _zettelkasten object, which must be serialized
   * @param _dateiName the file, where must be saved serialized object
   * @throws IOException if the file could not open
   */
  @Override
  public void save(Zettelkasten _zettelkasten, String _dateiName) throws IOException {
    try {
      ObjectOutput out = new ObjectOutputStream(new FileOutputStream(_dateiName));
      out.writeObject(_zettelkasten);
      out.close();
    } catch (IOException _e) {
      _e.printStackTrace();
    }
  }
  /**
   * Deserialization
   * @param _dateiname file name wherefrom the object must be deserialized
   * @return the Zettelkasten obejct which is deserialized from the file
   * @throws FileNotFoundException if the file could not find
   */
  @Override
  public Zettelkasten load(String _dateiname) throws FileNotFoundException {
    Zettelkasten serializedZettelKasten = new Zettelkasten();
    try {
      ObjectInput in = new ObjectInputStream(new FileInputStream(_dateiname));
      serializedZettelKasten = (Zettelkasten) in.readObject();
      in.close();

    } catch (IOException | ClassNotFoundException _e) {
      _e.printStackTrace();
    }
    return serializedZettelKasten;
  }
}
