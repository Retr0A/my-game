package org.retroa.game.engine;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.retroa.game.entities.*;
import org.retroa.game.fbo.Fbo;
import org.retroa.game.fbo.PostProcessing;
import org.retroa.game.helper.ProjectionMatrix;
import org.retroa.game.helper.VectorHelper;
import org.retroa.game.models.Loader;
import org.retroa.game.models.RawModel;
import org.retroa.game.renderers.*;
import org.retroa.game.renderers.geometry.CuboidRenderer;
import org.retroa.game.renderers.geometry.NormalRenderer;
import org.retroa.game.renderers.geometry.PlaneRenderer;

import java.util.*;

/**
 * Created by NinthWorld on 9/22/2016.
 */
public class Main {

    private Loader loader;

    private PlaneRenderer planeRenderer;
    private CuboidRenderer cuboidRenderer;
    private SkyboxRenderer skyboxRenderer;
    private AsteroidRenderer Renderer;
    private NormalRenderer normalRenderer;
    private DebugRenderer debugRenderer;

    private LightEntity light;
    private CameraEntity camera;

    private Fbo multisampleFbo;
    private Fbo skyboxFbo;
    private Fbo gridFbo;
    private Fbo cuboidFbo;
    private Fbo Fbo;
    private Fbo NormalFbo;
    private Fbo spaceShipFbo;

    @SuppressWarnings("unused")
	private Map<RawModel, List<ModelEntity>> modelEntities;
    private List<CuboidEntity> cuboidEntities;
    private List<PlaneEntity> planeEntities;
    private List<AsteroidEntity> Entities;
    private List<DebugEntity> debugEntities;

    private Queue<AsteroidEntity> UpdateQueue;

    public Main(){
        loader = new Loader();

        DisplayManager.createDisplay();
        PostProcessing.init(loader);

        light = new LightEntity(new Vector3f(1, 1, 1), new Vector3f(1f, 0.8f, 0.4f));
        light.setAmbient(new Vector3f(0.1f, 0.1f, 0.2f));

        camera = new CameraEntity(new Vector3f(25, 25, 25));
        camera.setRotation(new Vector3f((float) Math.PI/6f, (float) -Math.PI/6f, 0f));

        modelEntities = new HashMap<>();
        Entities = new ArrayList<>();
        cuboidEntities = new ArrayList<>();
        planeEntities = new ArrayList<>();
        debugEntities = new ArrayList<>();

        UpdateQueue = new LinkedList<>();

        Matrix4f projectionMatrix = ProjectionMatrix.create();
        planeRenderer = new PlaneRenderer(loader, projectionMatrix);
        cuboidRenderer = new CuboidRenderer(loader, projectionMatrix);
        skyboxRenderer = new SkyboxRenderer(loader, projectionMatrix);
        Renderer = new AsteroidRenderer(loader, projectionMatrix);
        normalRenderer = new NormalRenderer(loader, projectionMatrix);
        debugRenderer = new DebugRenderer(loader, projectionMatrix);

        multisampleFbo = new Fbo(Display.getWidth(), Display.getHeight());
        skyboxFbo = new Fbo(Display.getWidth(), Display.getHeight(), org.retroa.game.fbo.Fbo.DEPTH_TEXTURE);
        gridFbo = new Fbo(Display.getWidth(), Display.getHeight(), org.retroa.game.fbo.Fbo.DEPTH_TEXTURE);
        cuboidFbo = new Fbo(Display.getWidth(), Display.getHeight(), org.retroa.game.fbo.Fbo.DEPTH_TEXTURE);
        Fbo = new Fbo(Display.getWidth(), Display.getHeight(), org.retroa.game.fbo.Fbo.DEPTH_TEXTURE);
        NormalFbo = new Fbo(Display.getWidth(), Display.getHeight(), org.retroa.game.fbo.Fbo.DEPTH_TEXTURE);
        spaceShipFbo = new Fbo(Display.getWidth(), Display.getHeight(), org.retroa.game.fbo.Fbo.DEPTH_TEXTURE);

        setup();
    }

    private void cleanUp(){
        planeRenderer.cleanUp();
        cuboidRenderer.cleanUp();
        skyboxRenderer.cleanUp();
        Renderer.cleanUp();
        normalRenderer.cleanUp();
        debugRenderer.cleanUp();

        multisampleFbo.cleanUp();
        skyboxFbo.cleanUp();
        gridFbo.cleanUp();
        cuboidFbo.cleanUp();
        Fbo.cleanUp();
        NormalFbo.cleanUp();
        spaceShipFbo.cleanUp();

        loader.cleanUp();

        PostProcessing.cleanUp();
        DisplayManager.closeDisplay();
    }

    CuboidEntity cuboidEntityRight;
    CuboidEntity cuboidEntityLeft;

    private void setup(){
    	Random random = new Random();

        AsteroidEntity Entity = new AsteroidEntity(loader, new Vector3f(0, 0, 0), 64, 36, 32, 128, 0.5, random.nextInt());
        Entities.add(Entity);

        //float cubicLength = Entity.getVoxelData().getVoxelData().length*Entity.getScale();
        cuboidEntityRight = new CuboidEntity(loader, new Vector3f(0, 0, 0), new Vector3f(1, 1, 1));
        cuboidEntities.add(cuboidEntityRight);
        cuboidEntityLeft = new CuboidEntity(loader, new Vector3f(0, 0, 0), new Vector3f(1, 1, 1));
        cuboidEntities.add(cuboidEntityLeft);

        loop();
    }

    long queueUpdateTime = 0;
    long time = 0;
    int frames = 0;

    float angle = 0;
    boolean drawCuboid = false;
    boolean rightMouse = false;
    boolean leftMouse = false;
    private void loop(){
        time = queueUpdateTime = System.nanoTime();
        while(!Display.isCloseRequested()){
            if(System.nanoTime() - time < 1000000000L){
                frames++;
            }else{
                Display.setTitle("Game");
                time = System.nanoTime();
                frames = 0;
            }

            if(System.nanoTime() - queueUpdateTime >= 1000000000L / 2L){
                queueUpdateTime = System.nanoTime();
                for(AsteroidEntity Entity : UpdateQueue){
                    loader.cleanRawModel(Entity.getRawModel());
                    Entity.generateRawModel(loader);
                }
                UpdateQueue.clear();
            }

            camera.move();

            //debugEntities.get(0).setPosition(Vector3f.add(camera.getPosition(), new Vector3f(0, -5, 0), null));


            light.setPosition(new Vector3f((float) Math.cos(angle)*10f, 5f, (float) Math.sin(angle)*10f));

            if(Keyboard.isKeyDown(Keyboard.KEY_LEFT)){
                angle += Math.PI/200f;
            }
            if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT)){
                angle -= Math.PI/200f;
            }

            if(Mouse.isButtonDown(1)){
                rightMouse = true;
            }else{
                rightMouse = false;
            }

            if(Mouse.isButtonDown(0)){
                leftMouse = true;
            }else{
                leftMouse = false;
            }

            drawCuboid = false;
            for(AsteroidEntity Entity : Entities){
                for(int i=0; i<100; i++){
                    Vector3f point = Vector3f.add(camera.getPosition(), VectorHelper.scalar((float) i, camera.getDirectionVector()), null);
                    if(Entity.getBoundingBox().isPointInBB(point)){
                        Vector3f voxelPoint = Vector3f.sub(point, Entity.getDrawPosition(), null);
                        voxelPoint = new Vector3f((float) Math.floor(voxelPoint.x), (float) Math.floor(voxelPoint.y), (float) Math.floor(voxelPoint.z));

                        if(Entity.getVoxelData().getVoxelDataAt((int) voxelPoint.x, (int) voxelPoint.y, (int) voxelPoint.z) > 0){
                            drawCuboid = true;
                            cuboidEntityRight.setPosition(Vector3f.add(Vector3f.add(voxelPoint, Entity.getDrawPosition(), null), new Vector3f(0.5f, 0.5f, 0.5f), null));
                            Vector3f placePos = VectorHelper.floor(Vector3f.sub(voxelPoint, VectorHelper.scalar(1.2f, camera.getDirectionVector()), null));
                            cuboidEntityLeft.setPosition(Vector3f.add(Vector3f.add(placePos, Entity.getDrawPosition(), null), new Vector3f(0.5f, 0.5f, 0.5f), null));

                            if(rightMouse){
                                Entity.getVoxelData().setVoxelDataAt((int) voxelPoint.x, (int) voxelPoint.y, (int) voxelPoint.z, 0);
                                if(!UpdateQueue.contains(Entity)){
                                    UpdateQueue.add(Entity);
                                }
                            }else if(leftMouse){
                                Entity.getVoxelData().setVoxelDataAt((int) placePos.x, (int) placePos.y, (int) placePos.z, 2);
                                if(!UpdateQueue.contains(Entity)){
                                    UpdateQueue.add(Entity);
                                }
                            }
                            break;
                        }
                    }
                }
            }

            multisampleFbo.bindFrameBuffer();
            skyboxRenderer.renderSkybox(camera);
            multisampleFbo.unbindFrameBuffer();
            multisampleFbo.resolveToFbo(skyboxFbo);

            multisampleFbo.bindFrameBuffer();
            planeRenderer.render(planeEntities, light, camera);
            multisampleFbo.unbindFrameBuffer();
            multisampleFbo.resolveToFbo(gridFbo);

            multisampleFbo.bindFrameBuffer();
            cuboidRenderer.render((drawCuboid ? cuboidEntities : new ArrayList<CuboidEntity>()), light, camera);
            multisampleFbo.unbindFrameBuffer();
            multisampleFbo.resolveToFbo(cuboidFbo);

            multisampleFbo.bindFrameBuffer();
            debugRenderer.render(debugEntities, light, camera);
            multisampleFbo.unbindFrameBuffer();
            multisampleFbo.resolveToFbo(spaceShipFbo);

            multisampleFbo.bindFrameBuffer();
            Renderer.render(Entities, light, camera);
            multisampleFbo.unbindFrameBuffer();
            multisampleFbo.resolveToFbo(Fbo);

            List<ModelEntity> models = new ArrayList<>();
            models.addAll(Entities);
            multisampleFbo.bindFrameBuffer();
            normalRenderer.render(models, camera);
            multisampleFbo.unbindFrameBuffer();
            multisampleFbo.resolveToFbo(NormalFbo);

            PostProcessing.doPostProcessing(
                    skyboxFbo.getColorTexture(), skyboxFbo.getDepthTexture(),
                    gridFbo.getColorTexture(), gridFbo.getDepthTexture(),
                    cuboidFbo.getColorTexture(), cuboidFbo.getDepthTexture(),
                    Fbo.getColorTexture(), Fbo.getDepthTexture(), NormalFbo.getColorTexture(),
                    spaceShipFbo.getColorTexture(), spaceShipFbo.getDepthTexture()
            );

            DisplayManager.updateDisplay();
        }

        cleanUp();
    }

    public static void main(String[] args){
        //System.setProperty("org.lwjgl.librarypath", new File("lib/native/windows").getAbsolutePath());
        new Main();
    }
}