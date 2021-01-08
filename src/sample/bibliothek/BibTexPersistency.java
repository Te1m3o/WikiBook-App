/** @author Teimurazi Euashvili(Matrikelnummer:18808) */
package sample.bibliothek;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 */
public class BibTexPersistency implements Persistency {

  /**
   * Saves data in format of exercise B-10
   * @param _zettelkasten Data, which must be converted and saved
   * @param _dateiName file where the converted format will be saved
   */
  @Override
  public void save(Zettelkasten _zettelkasten, String _dateiName) {

    try {
      OutputStreamWriter writer =
          new OutputStreamWriter(new FileOutputStream(_dateiName), StandardCharsets.UTF_8);
      for (int i = 0; i < _zettelkasten.getMyZettelkasten().size(); i++) {
        writer.write( makeStringFormat(_zettelkasten.getMyZettelkasten().get(i).calculateRepresentation()));
        writer.write("\r\n");
      }
      writer.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * loads the object from the in String described text
   *
   * @param _dateiname String description of the object
   * @return new Zettelkasten Object
   * @throws IOException if the String can't be parsed into the Object
   */
  @Override
  public Zettelkasten load(String _dateiname) throws IOException {
    try {
      return parseBibTex(_dateiname);
    } catch (IOException _e) {
      _e.printStackTrace();
    }
    return null;
  }
  /**
   * @param _beschreibung String, which describes the class which instance must be created
   * @return the given class object, with the given Attribute values
   * @throws IOException if the String can't be parsed into the Object
   */
  public static Zettelkasten parseBibTex(String _beschreibung) throws IOException {
    /** Variables */
    String klass = null;
    /** book */
    String author = "author";
    String bookTitle = "title";
    String publisher = "publisher";
    int year = 0;
    String isbn = "isbn";
    /** journal */
    String journalTitle = "title";
    String issn = "issn";
    int volume = 0;
    int number = 0;

    /** cd */
    String cdTitle = "title";
    String artist = "artist";
    String label = "label";

    /** elMed */
    String elMedTitle = "title";
    String url = "URL";
    //
    /** split the String between "=" and pack it into the String array * */
    String[] arrOfBeschreibung = _beschreibung.split("=");
    /** Variable isChecked checks if the required String is available */
    boolean isChecked = false;
    /** Get Start and end index of the every bracket in the String */
    ArrayList<Integer> endIndex = new ArrayList<>();
    ArrayList<Integer> startIndex = new ArrayList<>();
    Pattern startPattern = Pattern.compile("\\{");
    Matcher startMatcher = startPattern.matcher(_beschreibung);
    while (startMatcher.find()) {
      startIndex.add(startMatcher.start() + 1);
    }
    Pattern endPattern = Pattern.compile("}");
    Matcher endMatcher = endPattern.matcher(_beschreibung);
    while (endMatcher.find()) {
      endIndex.add(endMatcher.start());
    }

    try {
      /**
       * Check if in the String special characters are included If special characters are not
       * included print out the error message
       */
      InputStream toRead =
          new ByteArrayInputStream(_beschreibung.getBytes(Charset.forName("UTF-8")));
      ArrayList<Character> check = new ArrayList<>();
      int[] classIndex = new int[2];
      int c;
      while ((c = toRead.read()) != -1) {
        char character = (char) c;
        switch (character) {
          case '@':
          case '{':
          case '}':
          case '=':
          case ',':
            if (!check.contains(character)) {
              check.add(character);
            }
            break;
        }
      }
      if (check.size() == 5) {
        isChecked = true;
      } else {
        isChecked = false;
        System.out.println("Input is invalid");
        System.out.println(
            "please enter the String in the following format: \n@book{author = {-}, title = { Duden 01. Die deutsche Rechtschreibung}, publisher = {Bibliog\n"
                + "raphisches Institut, Mannheim}, year = 2004, isbn = {3-411-04013-0}}\n"
                + "@journal{title = {Der Spiegel}, issn = {0038-7452}, volume = 54, number = 6}\n"
                + "@cd{title = {1}, artist = {Die Beatles}, label = { Apple (Bea (EMI))}}\n"
                + "@elMed{title = {Hochschule Stralsund}, URL = {http://www.hochschule-stralsund.de}}");
      }
      /** Get the class name */
      if (isChecked) {
        classIndex[0] = _beschreibung.indexOf(check.get(0));
        classIndex[1] = _beschreibung.indexOf(check.get(1));
        klass = _beschreibung.substring(classIndex[0], classIndex[1]);
      }

      if (klass.equals("@book") && isChecked) {
        /** check if the String all class needed elements includes */
        if (_beschreibung.contains("author")
            && _beschreibung.contains("title")
            && _beschreibung.contains("publisher")
            && _beschreibung.contains("isbn")
            && _beschreibung.contains("year")) {
          isChecked = true;
        } else {
          isChecked = false;
          System.out.println("Input is invalid");
          System.out.println(
              "please enter the String in the following format: \n@book{author = {-}, title = { Duden 01. Die deutsche Rechtschreibung}, publisher = {Bibliog\n"
                  + "raphisches Institut, Mannheim}, year = 2004, isbn = {3-411-04013-0}}\n"
                  + "@journal{title = {Der Spiegel}, issn = {0038-7452}, volume = 54, number = 6}\n"
                  + "@cd{title = {1}, artist = {Die Beatles}, label = { Apple (Bea (EMI))}}\n"
                  + "@elMed{title = {Hochschule Stralsund}, URL = {http://www.hochschule-stralsund.de}}");
        }
        if (isChecked) {
          /** init class attributes */
          author = _beschreibung.substring(startIndex.get(1), endIndex.get(0));
          bookTitle = _beschreibung.substring(startIndex.get(2), endIndex.get(1));
          publisher = _beschreibung.substring(startIndex.get(3), endIndex.get(2));
          isbn = _beschreibung.substring(startIndex.get(4), endIndex.get(3));
          year = Integer.parseInt(arrOfBeschreibung[4].replaceAll("[^0-9]", ""));
        }
      }

      if (klass.equals("@journal") && isChecked) {

        if (_beschreibung.contains("title")
            && _beschreibung.contains("issn")
            && _beschreibung.contains("volume")
            && _beschreibung.contains("number")) {
          isChecked = true;
        } else {
          isChecked = false;
          System.out.println("Input is invalid");
          System.out.println(
              "please enter the String in the following format: \n@book{author = {-}, title = { Duden 01. Die deutsche Rechtschreibung}, publisher = {Bibliog\n"
                  + "raphisches Institut, Mannheim}, year = 2004, isbn = {3-411-04013-0}}\n"
                  + "@journal{title = {Der Spiegel}, issn = {0038-7452}, volume = 54, number = 6}\n"
                  + "@cd{title = {1}, artist = {Die Beatles}, label = { Apple (Bea (EMI))}}\n"
                  + "@elMed{title = {Hochschule Stralsund}, URL = {http://www.hochschule-stralsund.de}}");
        }
        if (isChecked) {
          journalTitle = _beschreibung.substring(startIndex.get(1), endIndex.get(0));
          issn = _beschreibung.substring(startIndex.get(2), endIndex.get(1));
          volume = Integer.parseInt(arrOfBeschreibung[3].replaceAll("[^0-9]", ""));
          number = Integer.parseInt(arrOfBeschreibung[4].replaceAll("[^0-9]", ""));
        } else {
          isChecked = false;
          System.out.println("Input is invalid");
          System.out.println(
              "please enter the String in the following format: \n@book{author = {-}, title = { Duden 01. Die deutsche Rechtschreibung}, publisher = {Bibliog\n"
                  + "raphisches Institut, Mannheim}, year = 2004, isbn = {3-411-04013-0}}\n"
                  + "@journal{title = {Der Spiegel}, issn = {0038-7452}, volume = 54, number = 6}\n"
                  + "@cd{title = {1}, artist = {Die Beatles}, label = { Apple (Bea (EMI))}}\n"
                  + "@elMed{title = {Hochschule Stralsund}, URL = {http://www.hochschule-stralsund.de}}");
        }
      }

      if (klass.equals("@cd") && isChecked) {
        if (_beschreibung.contains("title")
            && _beschreibung.contains("artist")
            && _beschreibung.contains("label")) {
          isChecked = true;
        } else {
          isChecked = false;
          System.out.println("Input is invalid");
          System.out.println(
              "please enter the String in the following format: \n@book{author = {-}, title = { Duden 01. Die deutsche Rechtschreibung}, publisher = {Bibliog\n"
                  + "raphisches Institut, Mannheim}, year = 2004, isbn = {3-411-04013-0}}\n"
                  + "@journal{title = {Der Spiegel}, issn = {0038-7452}, volume = 54, number = 6}\n"
                  + "@cd{title = {1}, artist = {Die Beatles}, label = { Apple (Bea (EMI))}}\n"
                  + "@elMed{title = {Hochschule Stralsund}, URL = {http://www.hochschule-stralsund.de}}");
        }
        if (isChecked) {
          cdTitle = _beschreibung.substring(startIndex.get(1), endIndex.get(0));
          artist = _beschreibung.substring(startIndex.get(2), endIndex.get(1));
          label = _beschreibung.substring(startIndex.get(3), endIndex.get(2));
        } else {
          isChecked = false;
          System.out.println("Input is invalid");
          System.out.println(
              "please enter the String in the following format: \n@book{author = {-}, title = { Duden 01. Die deutsche Rechtschreibung}, publisher = {Bibliog\n"
                  + "raphisches Institut, Mannheim}, year = 2004, isbn = {3-411-04013-0}}\n"
                  + "@journal{title = {Der Spiegel}, issn = {0038-7452}, volume = 54, number = 6}\n"
                  + "@cd{title = {1}, artist = {Die Beatles}, label = { Apple (Bea (EMI))}}\n"
                  + "@elMed{title = {Hochschule Stralsund}, URL = {http://www.hochschule-stralsund.de}}");
        }
      }

      if (klass.equals("@elMed") && isChecked) {
        if (_beschreibung.contains("title") && _beschreibung.contains("URL")) {
          isChecked = true;
        } else {
          isChecked = false;
          System.out.println("Input is invalid");
          System.out.println(
              "please enter the String in the following format: \n@book{author = {-}, title = { Duden 01. Die deutsche Rechtschreibung}, publisher = {Bibliog\n"
                  + "raphisches Institut, Mannheim}, year = 2004, isbn = {3-411-04013-0}}\n"
                  + "@journal{title = {Der Spiegel}, issn = {0038-7452}, volume = 54, number = 6}\n"
                  + "@cd{title = {1}, artist = {Die Beatles}, label = { Apple (Bea (EMI))}}\n"
                  + "@elMed{title = {Hochschule Stralsund}, URL = {http://www.hochschule-stralsund.de}}");
        }
        if (isChecked) {
          elMedTitle = _beschreibung.substring(startIndex.get(1), endIndex.get(0));
          url = _beschreibung.substring(startIndex.get(2), endIndex.get(1));
        } else {
          isChecked = false;
          System.out.println("Input is invalid");
          System.out.println(
              "please enter the String in the following format: \n@book{author = {-}, title = { Duden 01. Die deutsche Rechtschreibung}, publisher = {Bibliog\n"
                  + "raphisches Institut, Mannheim}, year = 2004, isbn = {3-411-04013-0}}\n"
                  + "@journal{title = {Der Spiegel}, issn = {0038-7452}, volume = 54, number = 6}\n"
                  + "@cd{title = {1}, artist = {Die Beatles}, label = { Apple (Bea (EMI))}}\n"
                  + "@elMed{title = {Hochschule Stralsund}, URL = {http://www.hochschule-stralsund.de}}");
        }
      }

      if (!isChecked) {
        System.out.println(
            "please enter the String in the following format: \n@book{author = {-}, title = { Duden 01. Die deutsche Rechtschreibung}, publisher = {Bibliog\n"
                + "raphisches Institut, Mannheim}, year = 2004, isbn = {3-411-04013-0}}\n"
                + "@journal{title = {Der Spiegel}, issn = {0038-7452}, volume = 54, number = 6}\n"
                + "@cd{title = {1}, artist = {Die Beatles}, label = { Apple (Bea (EMI))}}\n"
                + "@elMed{title = {Hochschule Stralsund}, URL = {http://www.hochschule-stralsund.de}}");
      }

    } catch (FileNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    Zettelkasten zettelkasten = new Zettelkasten();
    if (klass.equals("@book")) {
      zettelkasten.addMedium(new Buch(bookTitle, year, publisher, isbn, author));
    } else if (klass.equals("@journal")) {
      zettelkasten.addMedium(new Zeitschrift(journalTitle, issn, volume, number));
    } else if (klass.equals("@cd")) {
      zettelkasten.addMedium(new CD(cdTitle, label, artist));
    } else if (klass.equals("@elMed")) {
      zettelkasten.addMedium(new ElektronischesMedium(elMedTitle, url));
    }
    return zettelkasten;
  }

  /**
   * creates String format of exercise B-10
   * @param calculateRepresentation String that must be formatted
   * @return formatted String; format: exercise B-10
   */
  public static String makeStringFormat(String calculateRepresentation) {
    StringBuilder s = new StringBuilder();
    if (calculateRepresentation.contains("ISBN")) {
      ArrayList<Integer> indexOfMedium = new ArrayList<>();
      String[] arrOfRepresentation = calculateRepresentation.split("\n");
      for (int i = 0; i < arrOfRepresentation.length; i++) {
        indexOfMedium.add(arrOfRepresentation[i].indexOf(":") + 2);
      }
      s.append("@book{author = {");
      s.append(arrOfRepresentation[4].substring(indexOfMedium.get(4)));
      s.append("}, title = { ");
      s.append(arrOfRepresentation[0].substring(indexOfMedium.get(0)));
      s.append("}, publisher = {");
      s.append(arrOfRepresentation[2].substring(indexOfMedium.get(2)));
      s.append("}, year = ");
      s.append(arrOfRepresentation[1].substring(indexOfMedium.get(1)));
      s.append(", isbn = {");
      s.append(arrOfRepresentation[3].substring(indexOfMedium.get(3)));
      s.append("}}");
      //System.out.println(s.toString());
      return s.toString();
    } else if (calculateRepresentation.contains("ISSN")) {
      ArrayList<Integer> indexOfMedium = new ArrayList<>();
      String[] arrOfRepresentation = calculateRepresentation.split("\n");
      for (int i = 0; i < arrOfRepresentation.length; i++) {
        indexOfMedium.add(arrOfRepresentation[i].indexOf(":") + 2);
      }
      s.append("@journal{title = {");
      s.append(arrOfRepresentation[0].substring(indexOfMedium.get(0)));
      s.append("}, issn = {");
      s.append(arrOfRepresentation[1].substring(indexOfMedium.get(1)));
      s.append("}, volume = ");
      s.append(arrOfRepresentation[2].substring(indexOfMedium.get(2)));
      s.append(" number = ");
      s.append(arrOfRepresentation[3].substring(indexOfMedium.get(3)));
      s.append("}");
      //System.out.println(s.toString());
      return s.toString();
    } else if (calculateRepresentation.contains("Künstler")) {
      ArrayList<Integer> indexOfMedium = new ArrayList<>();
      String[] arrOfRepresentation = calculateRepresentation.split("\n");
      for (int i = 0; i < arrOfRepresentation.length; i++) {
        indexOfMedium.add(arrOfRepresentation[i].indexOf(":") + 2);
      }
      s.append("@cd{title = {");
      s.append(arrOfRepresentation[0].substring(indexOfMedium.get(0)));
      s.append("}, artist = {");
      s.append(arrOfRepresentation[2].substring(indexOfMedium.get(2)));
      s.append("}, label = { ");
      s.append(arrOfRepresentation[1].substring(indexOfMedium.get(1)));
      s.append("}}");
      //System.out.println(s.toString());
      return s.toString();
    }else if(calculateRepresentation.contains("Url")){
      ArrayList<Integer> indexOfMedium = new ArrayList<>();
      String[] arrOfRepresentation = calculateRepresentation.split("\n");
      for (int i = 0; i < arrOfRepresentation.length; i++) {
        indexOfMedium.add(arrOfRepresentation[i].indexOf(":") + 2);
      }
      s.append("@elMed{title = {");
      s.append(arrOfRepresentation[0].substring(indexOfMedium.get(0)));
      s.append("}, URL = {");
      s.append(arrOfRepresentation[1].substring(indexOfMedium.get(1)));
      s.append("}}");
      //System.out.println(s.toString());
      return s.toString();
    } else {
      return null;
    }
  }
}
