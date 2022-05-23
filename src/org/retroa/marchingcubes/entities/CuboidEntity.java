package org.retroa.marchingcubes.entities;

import org.lwjgl.util.vector.Vector3f;
import org.retroa.marchingcubes.helper.VertexAttribData;
import org.retroa.marchingcubes.models.Loader;
import org.retroa.marchingcubes.models.RawModel;

/**
 * Created by NinthWorld on 9/24/2016.
 */
public class CuboidEntity extends ModelEntity {

    private Vector3f dimensions;

    public CuboidEntity(Loader loader, Vector3f position, Vector3f dimensions){
        super(null);
        this.setPosition(position);
        this.dimensions = dimensions;
        this.setRawModel(createCuboidEntityWireframe(loader, dimensions));
    }

    private static RawModel createCuboidEntityWireframe(Loader loader, Vector3f dimensions){
        float x = dimensions.x/2f;
        float y = dimensions.y/2f;
        float z = dimensions.z/2f;

        float[] vertices = new float[]{
                -x, -y, -z, x, -y, -z, x, -y, z, -x, -y, z,
                -x, y, -z, x, y, -z, x, y, z, -x, y, z,
        };

        float[] colors = new float[]{
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
        };

        float[] normals = new float[]{

        		-1.0f, 0.0f, 0.0f, // Left Side
        		 0.0f, 0.0f, -1.0f, // Back Side
        		 0.0f,-1.0f, 0.0f, // Bottom Side
        		 0.0f, 0.0f, -1.0f, // Back Side
        		-1.0f, 0.0f, 0.0f, // Left Side
        		 0.0f, -1.0f, 0.0f, // Bottom Side
        		 0.0f, 0.0f, 1.0f, // front Side
        		 1.0f, 0.0f, 0.0f, // right Side
        		 1.0f, 0.0f, 0.0f, // right Side
        		 0.0f, 1.0f, 0.0f, // top Side
        		 0.0f, 1.0f, 0.0f, // top Side
        		 0.0f, 0.0f, 1.0f, // front Side
        };

        int[] indices = new int[]{
        		// front
        	       0, 1, 3, 
        	       1, 2, 3,  
        	       // back
        	       4, 5, 7,
        	       5, 6, 7,
        	       // right
        	       0, 1, 4,
        	       1, 4, 5,
        	       // left
        	       2, 3, 7,
        	       2, 6, 7,
        	       // top
        	       0, 3, 4,
        	       3, 4, 7,
        	       // bottom
        	       1, 2, 5,
        	       2, 5, 6
        };

        return loader.loadToVao(vertices, colors, normals, indices);
    }

    private static RawModel createCuboidEntityTriangles(Loader loader, Vector3f dimensions){
        VertexAttribData vertexAttribData = new VertexAttribData();

        float x = dimensions.x/2f;
        float y = dimensions.y/2f;
        float z = dimensions.z/2f;

        float[][][] vertices = new float[][][]{
                // Top
                {{x, y, -z}, {-x, y, -z}, {x, y, z}},
                {{-x, y, z}, {x, y, z}, {-x, y, -z}},
                // Bottom
                {{x, -y, -z}, {x, -y, z}, {-x, -y, -z}},
                {{-x, -y, -z}, {x, -y, z}, {-x, -y, z}},
                // Front
                {{x, y, z}, {-x, y, z}, {x, -y, z}},
                {{-x, -y, z}, {x, -y, z}, {-x, y, z}},
                // Back
                {{x, -y, -z}, {-x, y, -z}, {x, y, -z}},
                {{-x, y, -z}, {x, -y, -z}, {-x, -y, -z}},
                // Left
                {{-x, y, z}, {-x, y, -z}, {-x, -y, z}},
                {{-x, -y, -z}, {-x, -y, z}, {-x, y, -z}},
                // Right
                {{x, -y, z}, {x, y, -z}, {x, y, z}},
                {{x, y, -z}, {x, -y, z}, {x, -y, -z}}
        };

        Vector3f color = new Vector3f(1, 1, 1);
        for(int i=0; i<vertices.length; i++){
            Vector3f[] verts = new Vector3f[3];
            for(int j=0; j<vertices[i].length; j++){
                verts[j] = new Vector3f(vertices[i][j][0], vertices[i][j][1], vertices[i][j][2]);
            }

            vertexAttribData.addTriangle(verts[0], verts[1], verts[2], color);
        }

        return vertexAttribData.loadToVaoColor(loader);
    }

}
