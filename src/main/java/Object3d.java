import base.Vector3d;
import base.Vertex3d;

import java.util.ArrayList;
import java.util.TreeSet;

public class Object3d {
    Scene scene;
    Vector3d position;

    ArrayList<Vertex3d> points = new ArrayList<>();
    TreeSet<Triangle> triangles = new TreeSet<>();

    Object3d boundingBox;

    ArrayList<Vertex3d> boundingPoints = new ArrayList<>();
    TreeSet<Triangle> boundingTriangles = new TreeSet<>();

    public void addTriangle(Triangle3D triangle3D){
        int indexA = points.indexOf(triangle3D.a);
        int indexB = points.indexOf(triangle3D.b);
        int indexC = points.indexOf(triangle3D.c);

        if (indexA < 0) {
            points.add(triangle3D.a);
            indexA = points.size() - 1;
        }
        if (indexB < 0) {
            points.add(triangle3D.b);
            indexB = points.size() - 1;
        }
        if (indexC < 0) {
            points.add(triangle3D.c);
            indexC = points.size() - 1;
        }
        triangles.add(new Triangle(this, indexA, indexB, indexC));
    }

    public void defineBoundings(){

        float minX = points.get(0).p.x;
        float minY = points.get(0).p.y;
        float minZ = points.get(0).p.z;
        float maxX = points.get(0).p.x;
        float maxY = points.get(0).p.y;
        float maxZ = points.get(0).p.z;


        for (Vertex3d vertex: points){
            if (vertex.p.x < minX){
                minX = vertex.p.x;
            }
            if (vertex.p.x > maxX){
                maxX = vertex.p.x;
            }
            if (vertex.p.y < minY){
                minY = vertex.p.y;
            }
            if (vertex.p.y > maxY){
                maxY = vertex.p.y;
            }
            if (vertex.p.z < minZ){
                minZ = vertex.p.z;
            }
            if (vertex.p.z > maxZ){
                maxZ = vertex.p.z;
            }
        }

        boundingBox = new Object3d();

        boundingBox.scene = this.scene;
        boundingBox.scene = this.scene;

        boundingBox.points.add(new Vertex3d(minX, minY, minZ));
        boundingBox.points.add(new Vertex3d(minX, maxY, minZ));
        boundingBox.points.add(new Vertex3d(maxX, maxY, minZ));
        boundingBox.points.add(new Vertex3d(maxX, minY, minZ));

        boundingBox.points.add(new Vertex3d(minX, minY, maxZ));
        boundingBox.points.add(new Vertex3d(minX, maxY, maxZ));
        boundingBox.points.add(new Vertex3d(maxX, maxY, maxZ));
        boundingBox.points.add(new Vertex3d(maxX, minY, maxZ));

        boundingBox.triangles.add(new Triangle(boundingBox, 0, 1, 2));
        boundingBox.triangles.add(new Triangle(boundingBox, 2, 3, 0));
        boundingBox.triangles.add(new Triangle(boundingBox, 6, 7, 4));
        boundingBox.triangles.add(new Triangle(boundingBox, 6, 4, 5));
        boundingBox.triangles.add(new Triangle(boundingBox, 4, 5, 1));
        boundingBox.triangles.add(new Triangle(boundingBox, 4, 1, 0));
        boundingBox.triangles.add(new Triangle(boundingBox, 3, 2, 6));
        boundingBox.triangles.add(new Triangle(boundingBox, 3, 6, 7));

    }

}
