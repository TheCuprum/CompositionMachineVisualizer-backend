package util;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;

public class FileUtil {


    public static URL[] uri2url(URI[] uris){
        ArrayList<URL> urlList = new ArrayList<>();
        for (URI uri:uris){
            try {
                urlList.add(uri.toURL());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        URL[] urls = new URL[urlList.size()];
        return urlList.toArray(urls);
    }
}
