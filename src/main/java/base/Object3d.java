package base;

import tree.BoundingBox;

import java.util.ArrayList;
import java.util.TreeSet;

public class Object3d extends AbstractObject{


    BoundingBox boundingBox;
    //Object3d boundingBox;

    //ArrayList<Vertex3d> boundingPoints = new ArrayList<>();
    //TreeSet<Triangle> boundingTriangles = new TreeSet<>();

    public void setScene(Scene scene){
        this.scene = scene;
    }

    public Scene getScene() {
        return scene;
    }

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


    public void defineBoundingBox(){
        boundingBox = new BoundingBox(this);
        boundingBox.defineBoundings(triangles);
    }


}
