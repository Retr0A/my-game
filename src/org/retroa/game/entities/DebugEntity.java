package org.retroa.game.entities;

import org.lwjgl.util.vector.Vector3f;
import org.retroa.game.helper.VoxelData;
import org.retroa.game.models.Loader;

/**
 * Created by NinthWorld on 9/27/2016.
 */
public class DebugEntity extends VoxelEntity {

    public DebugEntity(Loader loader, Vector3f position) {
        super(new VoxelData(8, 8, 8));
        this.setPosition(position);

        for(int i=3; i<=5; i++){
            for(int j=3; j<=5; j++){
                this.getVoxelData().setVoxelDataAt(i, 3, j, 1);
                this.getVoxelData().setVoxelDataAt(i, 4, j, 1);
                this.getVoxelData().setVoxelDataAt(i, 5, j, 2);
                this.getVoxelData().setVoxelDataAt(i, 6, j, 2);
            }
        }

        this.generateRawModel(loader);
    }

    public Vector3f getDrawPosition(){
        return Vector3f.sub(getCenterPosition(), new Vector3f(this.getVoxelData().getVoxelData().length/2f, this.getVoxelData().getVoxelData()[0].length/2f,this.getVoxelData().getVoxelData()[0][0].length/2f), null);
    }

    public Vector3f getCenterPosition(){
        return this.getPosition();
    }
}
