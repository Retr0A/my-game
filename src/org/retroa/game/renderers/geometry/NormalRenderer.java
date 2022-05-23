package org.retroa.game.renderers.geometry;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.retroa.game.entities.CameraEntity;
import org.retroa.game.entities.ModelEntity;
import org.retroa.game.helper.MatrixHelper;
import org.retroa.game.models.Loader;
import org.retroa.game.models.RawModel;
import org.retroa.game.shaders.NormalShader;

import java.util.List;

/**
 * Created by NinthWorld on 9/25/2016.
 */
public class NormalRenderer {

    private NormalShader normalShader;

    public NormalRenderer(Loader loader, Matrix4f projectionMatrix){
        normalShader = new NormalShader();
        normalShader.start();
        normalShader.loadProjectionMatrix(projectionMatrix);
        normalShader.stop();
    }

    public void cleanUp(){
        normalShader.cleanUp();
    }

    public void render(List<ModelEntity> modelEntities, CameraEntity camera){

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glClearColor(0, 0, 0, 0);

        normalShader.start();
        normalShader.loadViewMatrix(camera);
        renderEntities(modelEntities, normalShader);
        normalShader.stop();
    }

    private void renderEntities(List<ModelEntity> modelEntities, NormalShader shader){
       for(ModelEntity modelEntity : modelEntities){
            prepareRawModel(modelEntity.getRawModel());
            prepareEntity(modelEntity, shader);
            GL11.glDrawElements(GL11.GL_TRIANGLES, modelEntity.getRawModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
            unbindRawModel();
        }
    }

    private void prepareEntity(ModelEntity modelEntity, NormalShader shader) {
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
