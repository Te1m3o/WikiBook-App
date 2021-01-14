package sample.exceptions;

public class SynonymNotFound extends Exception {
  public SynonymNotFound() {
    super("Synonym not found");
  }

  public SynonymNotFound(String message) {
    super(message);
  }
}
