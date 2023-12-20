package com.mist;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.core.net.NetServer;
import io.vertx.core.net.NetSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SocketServerVerticle extends AbstractVerticle {

    private static final Logger logger = LoggerFactory.getLogger(SocketServerVerticle.class);

    @Override
    public void start() {
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
            tryParseJson(dataBuffer); // JSON 파싱 시도
        });

        socket.closeHandler(v -> logger.info("Client disconnected: {}", clientAddress));
        socket.exceptionHandler(e -> logger.error("Error with client {}: {}", clientAddress, e.getMessage(), e));
    }

    private void tryParseJson(StringBuilder dataBuffer) {
        try {
            JsonObject jsonMessage = new JsonObject(dataBuffer.toString());
            logger.info("Received complete JSON message: " + jsonMessage.encodePrettily());
            dataBuffer.setLength(0); // 유효한 JSON 메시지 처리 후 버퍼 초기화
        } catch (Exception e) {
            // 유효한 JSON 메시지가 아니면 아직 더 많은 데이터를 기다림
        }
    }

    private void handleNonJsonMessage(String message) {
        logger.warn("Received non-JSON message: {}", message);
    }
}
