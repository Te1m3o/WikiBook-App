/** @author Teimurazi Euashvili(Matrikelnummer:18808) */
package sample.bibliothek;
import java.net.URL;

public class ElektronischesMedium extends Medium {
    private String url;
    public ElektronischesMedium(String _titel, String _url){
        super(_titel);
        setUrl(_url);
    }
    /** Url = null if it's not valid **/
    public void setUrl(String _url){
        if (checkURL(_url)) {
            this.url = _url;
        }else {
            this.url=null;
            System.out.println("The URL doesn't match any of the Onlinemedien");
        }
    }

    public String getUrl() {
        return url;
    }

    public static boolean checkURL(String urlString) {
        try {
            URL url = new URL(urlString);
            url.toURI();
            return true;

        } catch (Exception exception) {
            return false;
        }
    }
    public String calculateRepresentation() {
        StringBuilder onlinemedien = new StringBuilder();
        onlinemedien.append(super.calculateRepresentation());
        onlinemedien.append("\nUrl: ");
        onlinemedien.append(getUrl());
        return String.valueOf(onlinemedien);
    }
}
