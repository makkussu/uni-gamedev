package org.knalpot.knalpot.world;

import java.util.ArrayList;
import java.util.List;

import org.knalpot.knalpot.actors.Actor;
import org.knalpot.knalpot.actors.Player;
import org.knalpot.knalpot.interactive.Static;
import org.knalpot.knalpot.networking.ClientProgram;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * {@code World} is responsible for creating all required objects
 * and returning them when requested. Basically it is the heart of everything.
 * @author Max Usmanov
 * @version 0.1
 */
public class World {
	//#region -- VARIABLES --
	// ==== OBJECT VARIABLES ==== //
	private Player player;
	private Static block1;

	// ==== NETWORKING ==== //
	private ClientProgram clientProgram;
	//#endregion

	// Tilemap temporary stuff
	private String tiledSrc = "level1/untitled1.tmx";
	public TiledMap tiledMap;

	public List<Static> collisionBlocks;

	//#region -- FUNCTIONS --
	/**
	 * {@code World} constructor.
	 */
	public World() {
		tiledMap = new TmxMapLoader().load(tiledSrc);
		collisionBlocks = new ArrayList<>();
		initializeWorld();
		initializeNetwork();
	}

	/**
	 * Returns {@code Player} object when requested.
	 * @return {@code Player} object.
	 */
	public Actor getPlayer() {
		return player;
	}

	/**
	 * @return ClientProgram
	 */
	public ClientProgram getClientProgram() {
		return clientProgram;
	}

	/**
	 * Returns all {@code Static} objects.
	 * @return {@code Static} object
	 */
	public Static getCollisionBlocks() {
		return block1;
	}

	/**
	 * Initializes all object needed for this 'world'.
	 */
	private void initializeWorld() {
		player = new Player(new Vector2(0, 200));
		block1 = new Static(new Vector2(300, 0), 16, 16);

		for (MapObject obj : tiledMap.getLayers().get(1).getObjects()) {
			RectangleMapObject rectObj = (RectangleMapObject) obj;
			Rectangle rect = rectObj.getRectangle();
			collisionBlocks.add(new Static(new Vector2(rect.getX() * 2, rect.getY() * 2), (int) rect.width * 2, (int) rect.height * 2));
		}
	}

	private void initializeNetwork() {
		clientProgram = new ClientProgram(player);
	}
	//#endregion
}