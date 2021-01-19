/** @author Teimurazi Euashvili(Matrikelnummer:18808) */
package sample.bibliothek;

import java.util.Calendar;
import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;

import java.util.GregorianCalendar;
import java.util.TimeZone;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

/**
 * Filters out the needed information from the xml file of wikiBook
 */
public class WikiBookContentHandler implements ContentHandler {
  boolean contributor = false;
  private String currentValue;
  Wikibook wikibook;
  StringBuilder currentText = new StringBuilder();
  public Wikibook getWikiBook() {
    return wikibook;
  }

  @Override
  public void characters(char[] ch, int start, int length) throws SAXException {
    currentValue = new String(ch, start, length);
    currentText.append(ch, start, length);
  }

  @Override
  public void startElement(String uri, String localName, String qName, Attributes atts)
      throws SAXException {
    if (localName.equals("contributor")){
      contributor = true;
    }

  }

  /**
   * If the item tag is opened. prints the title of the item out
   * @param uri
   * @param localName
   * @param qName
   * @throws SAXException
   */
  @Override
  public void endElement(String uri, String localName, String qName) throws SAXException {
    /** Set title of the wikiBook **/
    if (localName.equals("title")) {
     // System.out.println(currentValue);
      wikibook = new Wikibook(currentValue,null);
    }
    /** Last edition **/
    if (localName.equals("timestamp")){
      String Uhr = null;
      Date result = null;
      DateFormat timestramp  = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
      try {
        result = (Date) timestramp.parse(currentValue);
      } catch (ParseException _e) {
        _e.printStackTrace();
      }
      //  System.out.println(result.toString());
      Calendar cal = Calendar.getInstance();
      cal.setTime(result);
      int year = cal.get(Calendar.YEAR);
      int month = cal.get(Calendar.MONTH)+1;
      int day = cal.get(Calendar.DAY_OF_MONTH);
      int uhr = cal.get(Calendar.HOUR_OF_DAY);
      int minute = cal.get(Calendar.MINUTE);
      /** if It's Winter current time+1 else current time+2**/
      if (month==12 || month==1 || month==2){
        uhr = uhr + 1;
      }else {
        uhr = uhr + 2;
      }
      String letzteberarbeitung = day + "." + month + "." + year + " " + uhr + ":" + minute;
      wikibook.setLetzteBearbeitung(letzteberarbeitung);
      String output = "Letzte Änderung: " + day  + "." + month + "." + year + " um " + uhr + ":"
          + minute + " Uhr";
     // System.out.println(output);
    }
    /** Author **/
    if (localName.equals("ip") && (contributor)){
      wikibook.setAutor(currentValue);
      //System.out.println("Urheber: " + currentValue);
    }
    if (localName.equals("username") && (contributor)) {
      wikibook.setAutor(currentValue);
     // System.out.println("Urheber: " + currentValue);
    }
    /** Categories and the Chapters **/
    if (localName.equals("text")){
      StringBuilder information = new StringBuilder();
      String regal;
      final String content = currentText.toString().trim();
      information.append("Regal: ");
      int index = content.indexOf("Regal|");
      int endIndex = content.indexOf("}");
      /** check if the book has several regals **/
      if (content.substring(index+6,index+9).equals("ort")){
        regal = content.substring(index+10, endIndex);
        wikibook.setRegal(regal);
      }else {
        regal = content.substring(index+6, endIndex);
        wikibook.setRegal(regal);
      }
      information.append(regal);
      information.append("\nKapitel:\n");
      String[] splited = content.split("==");
      int counter = 1;
      for (int i = 1; i<splited.length; i=i+2){
        information.append(counter);
        information.append(". ");
        information.append(splited[i]);
        wikibook.getKapitel().add(splited[i]);
        information.append("\n");
        counter++;
      }
     // System.out.println(information.toString());
    }
  }

  @Override
  public void setDocumentLocator(Locator locator) {

  }

  @Override
  public void startDocument() throws SAXException {

  }

  @Override
  public void endDocument() throws SAXException {

  }

  @Override
  public void startPrefixMapping(String prefix, String uri) throws SAXException {

  }

  @Override
  public void endPrefixMapping(String prefix) throws SAXException {

  }


  @Override
  public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {

  }

  @Override
  public void processingInstruction(String target, String data) throws SAXException {

  }

  @Override
  public void skippedEntity(String name) throws SAXException {

  }
}
