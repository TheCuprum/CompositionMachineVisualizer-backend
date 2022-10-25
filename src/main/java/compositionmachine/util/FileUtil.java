package compositionmachine.util;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;

public class FileUtil {
    private static int RETRIES = 1000;

    public static URL[] uri2url(URI[] uris) {
        ArrayList<URL> urlList = new ArrayList<>();
        for (URI uri : uris) {
            try {
                urlList.add(uri.toURL());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        URL[] urls = new URL[urlList.size()];
        return urlList.toArray(urls);
    }

    public static File createOrChangeDirectory(String path) {
        String actualPath = path;
        int rename = 0;
        for (int i = 0; i < RETRIES; i++) {
            File actualFile = new File(actualPath);
            if (actualFile.exists()) {
                if (actualFile.isDirectory()) {
                    return actualFile;
                } else {
                    rename++;
                    actualPath = path + "_" + rename;
                }
            } else {
                actualFile.mkdirs();
                return actualFile;
            }
        }
        return null;
    }
}
