package compositionmachine.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.HashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import compositionmachine.util.FileUtil;

public class WebClientHandler extends ThreadedHttpHandler {

    private static final String CLIENT_RESOURCE_PATH = "/webclient";
    private static HashMap<String, byte[]> resourceCache = new HashMap<>();

    public WebClientHandler(boolean multiThreaded) {
        super(multiThreaded, 6, 12, 6, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(120));
        // adjust it
    }

    public synchronized static byte[] getFileBytes(File resourceFile) {
        byte[] contentBytes = resourceCache.get(resourceFile.getPath());

        if (contentBytes != null) {
            return contentBytes;
        } else if (resourceFile.exists() && resourceFile.isFile()) {
            FileInputStream contentStream;
            try {
                contentStream = new FileInputStream(resourceFile);
                contentBytes = contentStream.readAllBytes();
                contentStream.close();

                resourceCache.put(resourceFile.getPath(), contentBytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return contentBytes;
        } else {
            return null;
        }
    }

    @Override
    protected void handleRequest(HttpExchange exchange) throws IOException {
        String contextPath = exchange.getHttpContext().getPath();
        URI requestUri = exchange.getRequestURI();
        String requestPath = requestUri.getPath();

        String resourcePath = requestPath.replaceFirst(contextPath, CLIENT_RESOURCE_PATH);

        InputStream fileStream = this.getClass().getResourceAsStream(resourcePath);

        // System.out.println(contextPath);
        System.out.println(requestPath);
        // System.out.println(resourcePath);
        // System.out.println(this.getClass().getResource(resourcePath));

        if (fileStream != null) {
            byte[] contentBytes = fileStream.readAllBytes();
            Headers responseHeaders = exchange.getResponseHeaders();
            
            ContentType type = ContentType.getTypeByExtension(FileUtil.getFileExtension(resourcePath));
            if (type.mainType.equals("text")) {
                String tstr = type.toString().concat(";charset=utf-8");
                responseHeaders.set("Content-Type", tstr);
            } else {
                responseHeaders.set("Content-Type", type.toString());
            }
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, contentBytes.length);
            OutputStream stream = exchange.getResponseBody();
            stream.write(contentBytes);
            stream.close();
        } else {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_NOT_FOUND, 0);
        }
        exchange.close();
    }
}
