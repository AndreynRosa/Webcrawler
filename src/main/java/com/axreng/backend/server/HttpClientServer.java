package com.axreng.backend.server;

import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

public interface HttpClientServer {
    String get(String url) throws IOException;
}
