package org.knalpot.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

<<<<<<< Updated upstream:server/core/src/org/knalpot/server/ServerFoundation.java
import org.knalpot.server.ServerPlayer.PacketAddPlayer;
import org.knalpot.server.ServerPlayer.PacketRemovePlayer;
import org.knalpot.server.ServerPlayer.PacketUpdateX;
import org.knalpot.server.ServerPlayer.PacketUpdateY;
import org.knalpot.server.ServerPlayer.ServerPlayer;
=======
import ServerPlayer.ServerPlayer;
import ServerPlayer.PacketUpdateX;
import ServerPlayer.PacketUpdateY;
import ServerPlayer.PacketAddPlayer;
import ServerPlayer.PacketRemovePlayer;
import ServerPlayer.PacketUpdateDirection;
>>>>>>> Stashed changes:server/app/src/main/java/ServerFoundation.java

public class ServerFoundation extends Listener {
    private static Server server;
    private static final int port = 8080;
    static Map<Integer, ServerPlayer> players = new HashMap<>();

    public static void main(String[] args) throws IOException {
        server = new Server();
        server.start();
        server.getKryo().register(PacketUpdateX.class);
        server.getKryo().register(PacketUpdateY.class);
        server.getKryo().register(PacketAddPlayer.class);
        server.getKryo().register(PacketRemovePlayer.class);
        server.getKryo().register(PacketUpdateDirection.class);
        try {
            server.bind(port, port);
            server.start();
            System.out.print("Server started on port: " + port +
                    " | start time: " + LocalDateTime.now());
        } catch (IOException e) {
            e.printStackTrace();
        }
        server.addListener(new ServerFoundation());
    }

    public void connected(Connection connection) {
        ServerPlayer player = new ServerPlayer();
        player.x = 256;
        player.y = 256;
        player.c = connection;

        PacketAddPlayer packet = new PacketAddPlayer();
        packet.id = connection.getID();
        server.sendToAllExceptTCP(connection.getID(), packet);

        for (ServerPlayer p : players.values()) {
            PacketAddPlayer packet2 = new PacketAddPlayer();
            packet2.id = p.c.getID();
            connection.sendTCP(packet2);
        }

        players.put(connection.getID(), player);
        System.out.println("Connection received.");
    }

    public void received(Connection c, Object o) {
        if (o instanceof PacketUpdateX) {
            PacketUpdateX packet = (PacketUpdateX) o;
            players.get(c.getID()).x = packet.x;

            packet.id = c.getID();
            server.sendToAllExceptUDP(c.getID(), packet);
            System.out.println("received and sent an update X packet");

        } else if (o instanceof PacketUpdateY) {
            PacketUpdateY packet = (PacketUpdateY) o;
            players.get(c.getID()).y = packet.y;

            packet.id = c.getID();
            server.sendToAllExceptUDP(c.getID(), packet);

        } else if (o instanceof PacketUpdateDirection) {
            PacketUpdateDirection packet = (PacketUpdateDirection) o;
            players.get(c.getID()).isFacingRight = packet.isFacingRight;

            packet.id = c.getID();
            server.sendToAllExceptUDP(c.getID(), packet);
            System.out.println("direction uptadet");
        }
    }
    public void disconnected(Connection c) {
        players.remove(c.getID());
        PacketRemovePlayer packet = new PacketRemovePlayer();
        packet.id = c.getID();
        server.sendToAllExceptTCP(c.getID(), packet);
        System.out.println("Connection dropped.");
    }
}
