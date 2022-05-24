package org.retroa.game.entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;
import org.retroa.game.engine.Main;


/**
 * Created by NinthWorld on 9/22/2016.
 */
public class PlayerEntity extends Entity {

	private static final float maxLook = 85;
	private static final float mouseSensitivity = 0.08f;

	private boolean shouldFall = false;

	public boolean isColliding;

	Vector3f force = new Vector3f(0, 0, 0);

	public PlayerEntity(Vector3f position) {
		super(position, new Vector3f());
	}

	public PlayerEntity(Vector3f position, Vector3f rotation) {
		super(position, rotation);
	}

	public Vector3f getDirectionVector() {
		return new Vector3f((float) (Math.sin(this.getRotation().y) * Math.cos(this.getRotation().x)),
				(float) -Math.sin(this.getRotation().x),
				(float) -(Math.cos(this.getRotation().y) * Math.cos(this.getRotation().x)));
	}

	public void move() {
		float sinYaw = (float) Math.sin(getRotation().getY());
		float cosYaw = (float) Math.cos(getRotation().getY());

		float speed = 0.8f;
		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			increasePosition(sinYaw * speed, 0, -cosYaw * speed);
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			increasePosition(-sinYaw * speed, 0, cosYaw * speed);
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			increasePosition(cosYaw * speed, 0, sinYaw * speed);
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			increasePosition(-cosYaw * speed, 0, -sinYaw * speed);
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_SPACE) && Main.Entities.get(0).getBoundingBox().isPointInBB(getPosition())) {
			increasePosition(0, 10f, 0);
			System.out.println(getPosition().y);
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_R)) {
			increasePosition(0, 50, 0);
		}
		
		/*
		 * if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)){ increasePosition(0, -speed, 0);
		 * }
		 */

		if (Mouse.isInsideWindow() && Mouse.isButtonDown(0)) {
			Mouse.setGrabbed(true);
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
			Mouse.setGrabbed(false);
		}

		if (Mouse.isGrabbed()) {
			float mouseDX = Mouse.getDX();
			float mouseDY = -Mouse.getDY();
			increaseRotation((float) Math.toRadians(mouseDY * mouseSensitivity),
					(float) Math.toRadians(mouseDX * mouseSensitivity), 0);
			this.getRotation().setX(Math.max(-maxLook, Math.min(maxLook, getRotation().getX())));
		}

		if (Main.Entities.get(0).getBoundingBox().isPointInBB(getPosition())) {
			shouldFall = false;
			isColliding = true;
		} else {
			shouldFall = true;
			isColliding = false;
		}

		if (shouldFall && !isColliding) {
			increasePosition(0, -0.5f, 0);
		}
	}

	public void stopFalling() {
		shouldFall = false;
	}
}