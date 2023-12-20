package com.mist;

import io.vertx.core.Vertx;

/**
 * packageName    : PACKAGE_NAME
 * fileName       : com.mist.Gate.java
 * author         : kjg08
 * date           : 2023-12-20
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-12-20        kjg08           최초 생성
 */
public class Gate {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new SocketServerVerticle());
    }
}
