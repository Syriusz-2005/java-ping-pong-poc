package Server;

import jakarta.websocket.Session;

public record UserConfig(String id, String username, Session session, Lobby lobby, ConnectionEndpoint endpoint) {}
