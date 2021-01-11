package sample;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.util.Scanner;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class Synonim {
  public static void main(String[] args)
      throws ParseException, IOException, org.json.simple.parser.ParseException {
    String BasisUrl = "http://api.corpora.uni-leipzig.de/ws/similarity/";
    String Corpus = "deu_news_2012_1M";
    String Anfragetyp = "/coocsim/";
    String Suchbegriff = "sehen";
    String Parameter = "?minSim=0.1&limit=5";
    URL myURL;
    myURL = new URL(BasisUrl + Corpus + Anfragetyp + Suchbegriff + Parameter);
    JSONParser jsonParser = new JSONParser();
    String jsonResponse = streamToString(myURL.openStream());
// den gelieferten String in ein Array parsen
    JSONArray jsonResponseArr = (JSONArray) jsonParser.parse(jsonResponse);
// das erste Element in diesem Array
    JSONObject firstEntry = (JSONObject) jsonResponseArr.get(0);
// aus dem Element den Container für das Synonym beschaffen
    JSONObject wordContainer = (JSONObject) firstEntry.get("word");
// aus dem Container das Synonym beschaffen
    String synonym = (String) wordContainer.get("word");
// ausgeben
    System.out.println(synonym);
    System.out.println(System.lineSeparator());
// alle abgefragten Synonyme
    for (Object el : jsonResponseArr) {
      wordContainer = (JSONObject) ((JSONObject) el).get("word");
      synonym = (String) wordContainer.get("word");
      System.out.println(synonym);
    }
  }
  /**
   * Reads InputStream's contents into String
   * @param is - the InputStream
   * @return String representing is' contents
   * @throws IOException
   */
  public static String streamToString(InputStream is) throws IOException {
    String result = "";
// other options: https://stackoverflow.com/questions/309424/read-convertan-inputstream-to-a-string
    try (Scanner s = new Scanner(is)) {
      s.useDelimiter("\\A");
      result = s.hasNext() ? s.next() : "";
      is.close();
    }
    return result;
  }
}
