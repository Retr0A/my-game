package org.retroa.game.shaders;

import org.lwjgl.util.vector.Matrix4f;
import org.retroa.game.entities.PlayerEntity;
import org.retroa.game.entities.LightEntity;
import org.retroa.game.helper.MatrixHelper;

/**
 * Created by NinthWorld on 9/22/2016.
 */
public class MainShader extends AbstractShader {

    private static final String VERTEX_FILE = "/shaders/main/main.vert";
    private static final String FRAGMENT_FILE = "/shaders/main/main.frag";

    private int location_transformationMatrix;
    private int location_projectionMatrix;
    private int location_viewMatrix;

    private int location_lightPosition;
    private int location_lightColor;
    private int location_lightAmbient;

    private int location_texture0;
    private int location_texture1;

    private int location_normal0;
    private int location_normal1;

    public MainShader(){
        super(VERTEX_FILE, FRAGMENT_FILE);
    }

    @Override
    protected void bindAttributes(){
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "material");
        super.bindAttribute(2, "normal");
    }

    @Override
    protected void getAllUniformLocations() {
        location_transformationMatrix = super.getUniformLocation("transformationMatrix");
        location_projectionMatrix = super.getUniformLocation("projectionMatrix");
        location_viewMatrix = super.getUniformLocation("viewMatrix");

        location_lightPosition = super.getUniformLocation("lightPosition");
        location_lightColor = super.getUniformLocation("lightColor");
        location_lightAmbient = super.getUniformLocation("lightAmbient");


        location_texture0 = super.getUniformLocation("texture0");
        location_texture1 = super.getUniformLocation("texture1");

        location_normal0 = super.getUniformLocation("normal0");
        location_normal1 = super.getUniformLocation("normal1");
    }

    public void connectTextureUnits(){
        super.loadInteger(location_texture0, 0);
        super.loadInteger(location_texture1, 1);

        super.loadInteger(location_normal0, 2);
        super.loadInteger(location_normal1, 3);
    }

    public void loadLight(LightEntity light){
        super.loadVector3f(location_lightPosition, light.getPosition());
        super.loadVector3f(location_lightColor, light.getColor());
        super.loadVector3f(location_lightAmbient, light.getAmbient());
    }

    public void loadTransformationMatrix(Matrix4f matrix){
        super.loadMatrix(location_transformationMatrix, matrix);
    }

    public void loadViewMatrix(PlayerEntity camera){
        Matrix4f viewMatrix = MatrixHelper.createViewMatrix(camera);
        super.loadMatrix(location_viewMatrix, viewMatrix);
    }

    public void loadProjectionMatrix(Matrix4f projection){
        super.loadMatrix(location_projectionMatrix, projection);
    }
}
