package org.retroa.game.entities;

import org.lwjgl.util.vector.Vector3f;
import org.retroa.game.helper.BoundingBox;
import org.retroa.game.helper.SimplexNoise;
import org.retroa.game.helper.VoxelData;
import org.retroa.game.models.Loader;

/**
 * Created by NinthWorld on 9/24/2016.
 */
public class TerrainEntity extends VoxelEntity {

    @SuppressWarnings("unused")
	private Vector3f centerPosition;
    private int width, height, depth;

    public TerrainEntity(Loader loader, Vector3f centerPosition, int cubicLengths, float radius, float noiseAmp, int largestFeature, double persistence, int seed){
        super(new VoxelData(cubicLengths, cubicLengths, cubicLengths));
        this.centerPosition = centerPosition;
        this.width = 64;
        this.height = 32;
        this.depth = 64;
        SimplexNoise simplexNoise = new SimplexNoise(largestFeature, persistence, seed);
        Vector3f volumeCenter = new Vector3f(width/2f, height/2f, depth/2f);
        for(int x=0; x<width; x++){
            for(int y=0; y<height; y++){
                for(int z=0; z<depth; z++){
                    Vector3f currentPos = new Vector3f((float) x, (float) y, (float) z);

                    float distToCenter = Vector3f.sub(volumeCenter, currentPos, null).length();
                    float surfaceDist = radius + (float) simplexNoise.getNoise(x, y, z)*noiseAmp;

                    if(distToCenter < surfaceDist){
                        this.getVoxelData().setVoxelDataAt(x, y, z, (distToCenter > (radius)/2f  ? 1 : 2)); //(distToCenter < (radius+noiseAmp)/3f ? 1 : (distToCenter >= (radius+noiseAmp)/3f && distToCenter < 2f*(radius+noiseAmp)/3f ? 2 : 2)));
                    }
                }
            }
        }

        this.setPosition(centerPosition);
        //this.setPosition(new Vector3f(centerPosition.x-width/2f, centerPosition.y-height/2f, centerPosition.z-depth/2f));
        this.generateRawModel(loader);
    }

//    public Vector3f getCenterPosition() {
//        return new Vector3f(this.getPosition().x+width/2f, this.getPosition().y+height/2f, this.getPosition().z+depth/2f);
//    }

    public Vector3f getDrawPosition(){
        return Vector3f.sub(this.getPosition(), new Vector3f(this.getVoxelData().getVoxelData().length/2f, this.getVoxelData().getVoxelData()[0].length/2f,this.getVoxelData().getVoxelData()[0][0].length/2f), null);
    }

    public Vector3f getCenterPosition(){
        return this.getPosition();
    }

    public BoundingBox getBoundingBox(){
        return new BoundingBox(getPosition(),
        		new Vector3f(this.getVoxelData().getVoxelData().length,
        				this.getVoxelData().getVoxelData()[0].length - 30,
        				this.getVoxelData().getVoxelData()[0][0].length));
    }
    
    public BoundingBox getComplexBoundingBox(){
        return new BoundingBox(getPosition(),
        		new Vector3f(this.getDrawPosition().x,
        				this.getDrawPosition().y,
        				this.getDrawPosition().z));
    }

    public void setCenterPosition(Vector3f centerPosition) {
        this.centerPosition = centerPosition;
    }
}
