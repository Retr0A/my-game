package org.retroa.marchingcubes.fbo;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.retroa.marchingcubes.helper.ProjectionMatrix;
import org.retroa.marchingcubes.models.RawModel;
import org.retroa.marchingcubes.models.Loader;

/**
 * Created by NinthWorld on 9/25/2016.
 */
public class PostProcessing {
	
	private static final float[] POSITIONS = { -1, 1, -1, -1, 1, 1, 1, -1 };	
	private static RawModel quad;

	private static MainFXRenderer mainFXRenderer;

	public static void init(Loader loader){
		quad = loader.loadToVao(POSITIONS, 2);
		mainFXRenderer = new MainFXRenderer(ProjectionMatrix.create());
	}

	public static void doPostProcessing(int ... texture){
		start();
		mainFXRenderer.render(texture);
		end();
	}
	
	public static void cleanUp(){
        mainFXRenderer.cleanUp();
	}
	
	private static void start(){
		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
	}
	
	private static void end(){
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
	}



}
