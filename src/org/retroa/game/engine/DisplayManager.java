package org.retroa.game.engine;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.*;
import org.lwjgl.opengl.DisplayMode;

import java.awt.*;

import static org.lwjgl.opengl.GL11.glViewport;

/**
 * Created by NinthWorld on 6/6/2016.
 */
public class DisplayManager {

	private static final GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

	private static final int WIDTH = (int) (gd.getDisplayMode().getWidth() * 0.8); // 1280;
	private static final int HEIGHT = (int) (gd.getDisplayMode().getHeight() * 0.8); // 720;
	private static final int FPS_CAP = 60;
	private static final boolean VSYNC = true;
	private static final String TITLE = "Game";

	public static void createDisplay() {
		try {
			Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
			Display.setVSyncEnabled(VSYNC);
			// Display.create(new PixelFormat(4, 24, 0, 4));
			// Display.create(new PixelFormat().withSamples(8), new ContextAttribs(3,
			// 3).withForwardCompatible(true).withProfileCore(true));
			Display.create(new PixelFormat(),
					new ContextAttribs(3, 3).withForwardCompatible(true).withProfileCore(true));
			GL11.glEnable(GL13.GL_MULTISAMPLE);
			Display.setTitle(TITLE);
			Display.setResizable(true);
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}

		glViewport(0, 0, WIDTH, HEIGHT);
	}

	public static void updateDisplay() {
		//boolean isFullscreen = false;
		
		Display.sync(FPS_CAP);
		Display.update();
		
		/*if (Keyboard.isKeyDown(Keyboard.KEY_F11)) {
			
			if (!isFullscreen) {
				try {
					Display.setDisplayModeAndFullscreen(Display.getDesktopDisplayMode());
				} catch (LWJGLException e) {
					e.printStackTrace();
				}
				
				isFullscreen = true;
			} else if (isFullscreen) {
				try {
					Display.setDisplayModeAndFullscreen(new DisplayMode(WIDTH, HEIGHT));
				} catch (LWJGLException e) {
					e.printStackTrace();
				}
				isFullscreen = false;
			}
		}*/
	}

	public static void closeDisplay() {
		Display.destroy();
	}
}
