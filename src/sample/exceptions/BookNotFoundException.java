package sample.exceptions;

public class BookNotFoundException extends Exception {
  public BookNotFoundException(String message){
    super(message);
  }
  public BookNotFoundException() {
    super("Das Buch könnte nicht gefunden werden");
  }
}