package org.knalpot.knalpot.networking;

import java.util.HashMap;
import java.util.Map;

import org.knalpot.knalpot.actors.Actor;
import org.knalpot.knalpot.actors.player.Player;
import org.knalpot.knalpot.actors.Enemy;
import org.knalpot.knalpot.world.Network;
import org.knalpot.knalpot.world.World;

import com.badlogic.gdx.ApplicationAdapter;
import com.esotericsoftware.kryonet.Client;

public class ClientProgram extends ApplicationAdapter {
    static Network network;

    public static Map<Integer, Actor> players = new HashMap<>();
    public static Map<Integer, Enemy> enemies = new HashMap<>();

    private Client client;
    public static World world;
    private Player player;

    public ClientProgram(World world) {
        ClientProgram.world = world;
        this.player = world.getPlayer();

        network = new Network(this);
        client = network.getClient();
    }

    public int getID() {
        return client.getID();
    }

    public Map<Integer, Actor> getPlayers() {
        return players;
    }

    public Map<Integer, Enemy> getEnemies() {
        return enemies;
    }

    public void addOrbToWorld(Actor player) {
        world.addOrb(player);
    }

    public void addEnemyToWorld(Enemy enemy) {
        world.addEnemy(enemy);
    }

    @Override
    public void create() {
        System.out.println("Connecting");
        try {
            network.connect();
            System.out.println("Connected");
        } catch (Exception e) {
            System.out.println("Catched exception: " + e);
        }
    }

    public void updateNetwork() {
        // Update position
        if (player.getVelocity().x != 0 || player.getVelocity().y != 0) {
            // Send the player's Y value
            PacketUpdatePosition packet = new PacketUpdatePosition();
            packet.type = PacketType.PLAYER;
            packet.x = player.getPosition().x;
            packet.y = player.getPosition().y;
            client.sendUDP(packet);
        }

        if (player.direction != player.previousDirection) {
            // Send the player's direction
            PacketUpdateDirection packet = new PacketUpdateDirection();
            packet.type = PacketType.PLAYER;
            packet.direction = player.direction;
            client.sendUDP(packet);
        }

        if (player.state != player.previousState) {
            // Send the player's state
            PacketUpdateState packet = new PacketUpdateState();
            packet.type = PacketType.PLAYER;
            packet.state = player.state;
            client.sendUDP(packet);
            System.out.println("sent state");
        }

        if (player.health != player.previousHealth) {
            // Send the player's health
            PacketUpdateHealth packet = new PacketUpdateHealth();
            packet.type = PacketType.PLAYER;
            packet.health = player.health;
            client.sendUDP(packet);
            System.out.println("sent player`s health");
        }

        // enemies.values().forEach(enemy -> {
        //     if (enemy.health != enemy.previousHealth) {
        //         // Send the enemy's health
        //         PacketUpdateHealth packet = new PacketUpdateHealth();
        //         packet.type = PacketType.ENEMY;
        //         packet.health = enemy.health;
        //         client.sendUDP(packet);
        //         System.out.println("sent enemy`s health");
        //     }
        // });
    }

    @Override
    public void dispose () {
        client.stop();
    }
}
