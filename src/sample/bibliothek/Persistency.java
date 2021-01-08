/** @author Teimurazi Euashvili(Matrikelnummer:18808) */
package sample.bibliothek;

import java.io.IOException;

/**
 * The basis interface of the classes BibTexPersistency and BinaryPersistency
 */
public interface Persistency {
    void save(Zettelkasten _zettelkasten, String _dateiName) throws IOException;
    Zettelkasten load(String _dateiname) throws IOException;

}
