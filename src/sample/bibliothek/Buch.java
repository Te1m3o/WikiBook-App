/** @author Teimurazi Euashvili(Matrikelnummer:18808) */
package sample.bibliothek;

import java.util.StringJoiner;

public class Buch extends Medium {
  private String isbn;
  private int erscheinungsjahr;
  private String verlag;
  private String verfasser;

  public Buch(
      String _titel, int _erscheinungsjahr, String _verlag, String _isbn, String _verfasser) {
    super(_titel);
    this.erscheinungsjahr = _erscheinungsjahr;
    this.verlag = _verlag;
    this.verfasser = _verfasser;
    setIsbn(_isbn);
  }

  /**
   * sets ISBN
   *
   * @param _isbn is null if it's not valid
   */
  public void setIsbn(String _isbn) {
    if (check(convertString(_isbn))) {
      this.isbn = _isbn;
    } else {
      System.out.printf("ISBN : %s doesn't match any of ISBN%n", _isbn);
    }
  }
  /**
   * Split the String and convert it into the int Array
   *
   * @param str String, which must be converted
   * @return int Array with the content of the String
   */
  public int[] convertString(String str) {
    /** split the String between "-" and digits pack it into the String array */
    String[] arrOfStr = str.split("-");
    /** convert the String array into the String */
    StringJoiner joiner = new StringJoiner("");
    for (int i = 0; i < arrOfStr.length; i++) {
      joiner.add(arrOfStr[i]);
    }
    str = joiner.toString();
    /** split the string between each characters and save it in the String Array */
    arrOfStr = str.split("");
    /** // convert the String array into the int array; */
    int size = arrOfStr.length;
    int[] intarr = new int[size];
    for (int i = 0; i < size; i++) {
      intarr[i] = Integer.parseInt(arrOfStr[i]);
    }
    return intarr;
  }

  public int getErscheinungsjahr() {
    return erscheinungsjahr;
  }

  public void setErscheinungsjahr(int erscheinungsjahr) {
    this.erscheinungsjahr = erscheinungsjahr;
  }

  public String getVerlag() {
    return verlag;
  }

  public void setVerlag(String verlag) {
    this.verlag = verlag;
  }

  public String getVerfasser() {
    return verfasser;
  }

  public void setVerfasser(String verfasser) {
    this.verfasser = verfasser;
  }

  /**
   * check if the ISBN is valid
   *
   * @param isbn which must be checked
   * @return true if the ISBN is valid, otherwise false
   */
  public boolean check(int[] isbn) {
    if (checkISBN10(isbn) || checkISBN13(isbn)) {
      return true;
    } else {
      return false;
    }
  }

  public String getIsbn() {
    return isbn;
  }

  /**
   * Check Algorithm for the Array(digit:10)
   *
   * @param isbn ISBN that must be checked
   * @return true if the ISBN is valid, otherwise false
   */
  public static boolean checkISBN10(int[] isbn) {
    int sum = 0;
    for (int i = 1; i <= isbn.length; i++) {
      sum += i * isbn[i - 1];
    }
    if (sum % 11 == 0) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * Check Algorithm for the Array(digit:13)
   *
   * @param isbn ISBN which must be checked
   * @return true if the isbn fits, otherwise false
   */
  public static boolean checkISBN13(int[] isbn) {
    int sum = 0;
    for (int i = 1; i < isbn.length; i++) {
      if (i % 2 == 0) {
        sum += isbn[i - 1] * 3;
      } else {
        sum += isbn[i - 1];
      }
    }
    int lastDigit = sum % 10;
    int check = (10 - lastDigit) % 10;
    if (isbn[isbn.length - 1] == check) {
      return true;
    } else {
      return false;
    }
  }

  @Override
  public String calculateRepresentation() {
    StringBuilder buch = new StringBuilder();
    buch.append(super.calculateRepresentation());
    buch.append("\n");
    buch.append("Erscheinigungsjahr: ");
    buch.append(getErscheinungsjahr());
    buch.append("\n");
    buch.append("Verlag: ");
    buch.append(getVerlag());
    buch.append("\n");
    buch.append("ISBN: ");
    buch.append(getIsbn());
    buch.append("\n");
    buch.append("Verfasser: ");
    buch.append(getVerfasser());
    return String.valueOf(buch);
  }
}
