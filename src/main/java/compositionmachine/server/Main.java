package compositionmachine.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;

import com.sun.net.httpserver.HttpServer;

/**
 * Run this class to launch the visualizer web server.
 */
public class Main {

    public static void browserVisit(String address) {
        URI uri = URI.create(address);
        browserVisit(uri);
    }

    public static void browserVisit(URI uri) {
        // I want to check if java.awt exists.
        try {
            Class.forName("java.awt.Desktop");
            if (java.awt.Desktop.isDesktopSupported()) {
                java.awt.Desktop dp = java.awt.Desktop.getDesktop();
                if (dp.isSupported(java.awt.Desktop.Action.BROWSE)) {
                    dp.browse(uri);
                }
            }
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        HttpServer httpServer;
        WebClientHandler webClientHandler;
        try {
            httpServer = HttpServer.create(new InetSocketAddress(8081), 10);
            webClientHandler = new WebClientHandler(true);

            httpServer.createContext("/client", webClientHandler);
            httpServer.start();

            browserVisit("http://localhost:8081/client/index.html");

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("Shutting down...");
                httpServer.stop(0);
                webClientHandler.shutdownExecutor();
            }, "ShutDownHook"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}