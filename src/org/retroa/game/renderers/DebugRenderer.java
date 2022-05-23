package org.retroa.game.renderers;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.retroa.game.entities.*;
import org.retroa.game.helper.MatrixHelper;
import org.retroa.game.models.Loader;
import org.retroa.game.models.RawModel;
import org.retroa.game.shaders.DebugShader;

import java.util.List;

/**
 * Created by NinthWorld on 9/27/2016.
 */
public class DebugRenderer {

    private static final String texture0 = "/textures/texture3.png";
    private static final String texture1 = "/textures/texture2.png";

    private static final String normal0 = "/textures/normal1.png";
    private static final String normal1 = "/textures/normal1.png";

    private DebugShader spaceShipShader;

    private int texture0Id;
    private int texture1Id;

    private int normal0Id;
    private int normal1Id;

    public DebugRenderer(Loader loader, Matrix4f projectionMatrix){
        texture0Id = loader.loadTexture(texture0);
        texture1Id = loader.loadTexture(texture1);

        normal0Id = loader.loadTexture(normal0);
        normal1Id = loader.loadTexture(normal1);

        spaceShipShader = new DebugShader();
        spaceShipShader.start();
        spaceShipShader.loadProjectionMatrix(projectionMatrix);
        spaceShipShader.stop();
    }

    public void cleanUp(){
        spaceShipShader.cleanUp();
    }

    public void render(List<DebugEntity> spaceShipEntities, LightEntity light, CameraEntity camera){
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glClearColor(0, 0, 0, 0);

        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_FRONT_FACE);

        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture0Id);
        GL13.glActiveTexture(GL13.GL_TEXTURE1);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture1Id);
        GL13.glActiveTexture(GL13.GL_TEXTURE2);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, normal0Id);
        GL13.glActiveTexture(GL13.GL_TEXTURE3);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, normal1Id);

        spaceShipShader.start();
        spaceShipShader.connectTextureUnits();
        spaceShipShader.loadLight(light);
        spaceShipShader.loadViewMatrix(camera);
        renderEntities(spaceShipEntities, spaceShipShader);
        spaceShipShader.stop();
    }

    private void renderEntities(List<DebugEntity> spaceShipEntities, DebugShader shader){
       for(DebugEntity spaceShipEntity : spaceShipEntities){
            prepareRawModel(spaceShipEntity.getRawModel());
            prepareEntity(spaceShipEntity, shader);
            GL11.glDrawElements(GL11.GL_TRIANGLES, spaceShipEntity.getRawModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
            unbindRawModel();
        }
    }

    private void prepareEntity(DebugEntity spaceShipEntity, DebugShader shader) {
        Matrix4f transformationMatrix = MatrixHelper.createTransformationMatrix(spaceShipEntity.getDrawPosition(), spaceShipEntity.getRotation(), spaceShipEntity.getScale());
        shader.loadTransformationMatrix(transformationMatrix);
    }

    private void prepareRawModel(RawModel rawModel) {
        GL30.glBindVertexArray(rawModel.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);
    }

    private void unbindRawModel() {
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL30.glBindVertexArray(0);
    }
}
