package compositionmachine.server;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public abstract class ThreadedHttpHandler implements HttpHandler {

    protected ThreadPoolExecutor executor;

    public ThreadedHttpHandler(boolean multiThreaded, int corePoolSize, int maximumPoolSize, long keepAliveTime,
            TimeUnit unit, LinkedBlockingQueue<Runnable> linkedBlockingQueue) {
        if (multiThreaded)
            this.executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, unit, linkedBlockingQueue);
        else
            this.executor = null;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (executor == null) {
            handleRequest(exchange);
        } else {
            executor.submit(() -> {
                try {
                    handleRequest(exchange);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    protected abstract void handleRequest(HttpExchange exchange) throws IOException;

    public void shutdownExecutor() {
        if (this.executor != null && this.executor.isShutdown())
            this.executor.shutdown();
    }

}
