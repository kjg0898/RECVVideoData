package com.mist;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.NetServer;
import io.vertx.core.net.NetSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * packageName    : PACKAGE_NAME
 * fileName       : com.mist.SocketServerVerticle.java
 * author         : kjg08
 * date           : 2023-12-20
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-12-20        kjg08           최초 생성
 */
public class SocketServerVerticle extends AbstractVerticle {

    private static final Logger logger = LoggerFactory.getLogger(SocketServerVerticle.class);


    @Override
    public void start()  {
        NetServer server = vertx.createNetServer();
        server.connectHandler(this::handleClientConnection);
        server.listen(9999, res -> {
            if (res.succeeded()) {
                logger.info("Server is listening on port " + server.actualPort());
            } else {
                logger.error("Failed to bind!");
            }
        });
    }

    private void handleClientConnection(NetSocket socket) {
        String clientAddress = socket.remoteAddress().toString();
        logger.info("Client connected: {}", clientAddress);
        StringBuilder dataBuffer = new StringBuilder();

        socket.handler(buffer -> {
            dataBuffer.append(buffer.toString());
            // '\n'은 메시지 끝을 나타냄
            if (dataBuffer.toString().endsWith("\n")) {
                try {
                    String completeMessage = dataBuffer.toString().trim();
                    JsonObject jsonMessage = new JsonObject(completeMessage);
                    logger.info("Received complete message: " + jsonMessage.encodePrettily());
                    dataBuffer.setLength(0); // 버퍼 초기화
                } catch (Exception e) {
                    logger.error("JSON parsing failed: " + e.getMessage());
                }
            }
        });

        socket.closeHandler(v -> logger.info("Client disconnected: {}", clientAddress));
        socket.exceptionHandler(e -> logger.error("Error with client {}: {}", clientAddress, e.getMessage(), e));
    }
}