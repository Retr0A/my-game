package org.retroa.game.renderers.geometry;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.retroa.game.entities.CameraEntity;
import org.retroa.game.entities.LightEntity;
import org.retroa.game.entities.ModelEntity;
import org.retroa.game.entities.PlaneEntity;
import org.retroa.game.helper.MatrixHelper;
import org.retroa.game.models.Loader;
import org.retroa.game.models.RawModel;
import org.retroa.game.shaders.PlaneShader;

import java.util.List;

/**
 * Created by NinthWorld on 9/25/2016.
 */
public class PlaneRenderer {

    private PlaneShader planeShader;

    public PlaneRenderer(Loader loader, Matrix4f projectionMatrix){
        planeShader = new PlaneShader();
        planeShader.start();
        planeShader.loadProjectionMatrix(projectionMatrix);
        planeShader.stop();
    }

    public void cleanUp(){
        planeShader.cleanUp();
    }

    public void render(List<PlaneEntity> planeEntities, LightEntity light, CameraEntity camera){
        //GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glClearColor(0, 0, 0, 0);

        planeShader.start();
        planeShader.loadLight(light);
        planeShader.loadViewMatrix(camera);
        renderEntities(planeEntities, planeShader);
        planeShader.stop();

    }

    private void renderEntities(List<PlaneEntity> planeEntities, PlaneShader shader){
        for(PlaneEntity planeEntity : planeEntities){
            prepareRawModel(planeEntity.getRawModel());
            prepareEntity(planeEntity, shader);
            GL11.glDrawElements(GL11.GL_LINE_LOOP, planeEntity.getRawModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
            unbindRawModel();
        }
    }

    private void prepareEntity(ModelEntity modelEntity, PlaneShader shader) {
        Matrix4f transformationMatrix = MatrixHelper.createTransformationMatrix(modelEntity.getPosition(), modelEntity.getRotation(), modelEntity.getScale());
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
