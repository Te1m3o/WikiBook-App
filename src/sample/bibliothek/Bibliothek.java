/** @author Teimurazi Euashvili(Matrikelnummer:18808) */

/**
 * Please check in the class Zettelkasten function findWikiBook
 * Added classes : WikiBook and WikiBookContentHandler
 */
package sample.bibliothek;

import java.io.IOException;
import org.xml.sax.SAXException;

/**
 * The main class of the program
 */
public class Bibliothek {
  public static void main(String[] args) throws IOException, SAXException {
    Zettelkasten zettelkasten = new Zettelkasten();
    zettelkasten.addMedium(
        new Buch(
            "Buden 01. Die deutsche Retchtsschreibung",
            2004,
            "Bibliographisches Institut, Mannheim",
            "3-411-04013-0",
            "-"));
    zettelkasten.addMedium(new CD("1", "Apple (Bae (EMI))", "The Beatles"));
    zettelkasten.addMedium(new Zeitschrift("Der Spiegel", "003-7452", 54, 6));
    zettelkasten.addMedium(
        new ElektronischesMedium("Aochschule Stralsund", "http://www.hochschule-stralsund.de"));
    zettelkasten.addMedium(new CD("Der Spiegel", "Apple (Bae (EMI))", "The Beatles"));
    zettelkasten.addMedium(new CD("Der Spiegel", "Apple (Bae (EMI))", "The Beatles"));
    System.out.println("\n");
    /** args[0] is the title, which must be found */
    zettelkasten.addMedium(zettelkasten.findWikiBook(args[0]));
    zettelkasten.addMedium(zettelkasten.findWikiBook(args[1]));
    zettelkasten.addMedium(zettelkasten.findWikiBook(args[2]));
  }
}
