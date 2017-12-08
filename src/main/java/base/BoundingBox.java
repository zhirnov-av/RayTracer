package base;

import java.util.ArrayList;
import java.util.TreeSet;

public class BoundingBox extends AbstractObject{
    AbstractObject object;
    public float minX = 0, minY = 0, minZ = 0, maxX = 0, maxY = 0, maxZ = 0;
    public double distanceToCamera = 0;

    public TreeSet<Primitive> innerTriangles = new TreeSet<>();

    public BoundingBox(AbstractObject object){
        this.object = object;
    }

    public AbstractObject getObject() {
        return object;
    }

    public void defineBoundings(TreeSet<Triangle> triangles){



        minX = triangles.first().pv1.p.x;
        minY = triangles.first().pv1.p.y;
        minZ = triangles.first().pv1.p.z;
        maxX = triangles.first().pv1.p.x;
        maxY = triangles.first().pv1.p.y;
        maxZ = triangles.first().pv1.p.z;


        for (Triangle tr: triangles){

            if ((tr.object instanceof Object3d) && ((Object3d)(tr.object)).isNeedToRenderer)
                continue;

            // first point
            if (tr.pv1.p.x < minX){
                minX = tr.pv1.p.x;
            }
            if (tr.pv1.p.x > maxX){
                maxX = tr.pv1.p.x;
            }
            if (tr.pv1.p.y < minY){
                minY = tr.pv1.p.y;
            }
            if (tr.pv1.p.y > maxY){
                maxY = tr.pv1.p.y;
            }
            if (tr.pv1.p.z < minZ){
                minZ = tr.pv1.p.z;
            }
            if (tr.pv1.p.z > maxZ){
                maxZ = tr.pv1.p.z;
            }
            // second point
            if (tr.pv2.p.x < minX){
                minX = tr.pv2.p.x;
            }
            if (tr.pv2.p.x > maxX){
                maxX = tr.pv2.p.x;
            }
            if (tr.pv2.p.y < minY){
                minY = tr.pv2.p.y;
            }
            if (tr.pv2.p.y > maxY){
                maxY = tr.pv2.p.y;
            }
            if (tr.pv2.p.z < minZ){
                minZ = tr.pv2.p.z;
            }
            if (tr.pv2.p.z > maxZ){
                maxZ = tr.pv2.p.z;
            }
            // third point
            if (tr.pv3.p.x < minX){
                minX = tr.pv3.p.x;
            }
            if (tr.pv3.p.x > maxX){
                maxX = tr.pv3.p.x;
            }
            if (tr.pv3.p.y < minY){
                minY = tr.pv3.p.y;
            }
            if (tr.pv3.p.y > maxY){
                maxY = tr.pv3.p.y;
            }
            if (tr.pv3.p.z < minZ){
                minZ = tr.pv3.p.z;
            }
            if (tr.pv3.p.z > maxZ){
                maxZ = tr.pv3.p.z;
            }

        }
        minX -= 1; minY -= 1; minZ -= 1;
        maxX += 1; maxY += 1; maxZ += 1;

        this.points.add(new Vertex3d(minX, minY, minZ)); // 0
        this.points.add(new Vertex3d(minX, maxY, minZ)); // 1
        this.points.add(new Vertex3d(maxX, maxY, minZ)); // 2
        this.points.add(new Vertex3d(maxX, minY, minZ)); // 3

        this.points.add(new Vertex3d(minX, minY, maxZ)); // 4
        this.points.add(new Vertex3d(minX, maxY, maxZ)); // 5
        this.points.add(new Vertex3d(maxX, maxY, maxZ)); // 6
        this.points.add(new Vertex3d(maxX, minY, maxZ)); // 7

        buildBox(this, this.points, this.primitives);

        /*
        this.primitives.add(new Triangle(this, 0, 1, 2)); // front
        this.primitives.add(new Triangle(this, 2, 3, 0)); // front
        this.primitives.add(new Triangle(this, 6, 4, 7)); // back
        this.primitives.add(new Triangle(this, 6, 5, 4)); // back
        this.primitives.add(new Triangle(this, 4, 5, 1)); // left
        this.primitives.add(new Triangle(this, 4, 1, 0)); // left
        this.primitives.add(new Triangle(this, 3, 2, 6)); // right
        this.primitives.add(new Triangle(this, 3, 6, 7)); // right
        this.primitives.add(new Triangle(this, 4, 0, 3)); // bottom
        this.primitives.add(new Triangle(this, 4, 3, 7)); // bottom
        */


        innerTriangles.addAll(triangles);

    }

    public static void buildBox(AbstractObject obj, ArrayList<Vertex3d> points, TreeSet<Triangle> triangles){
        triangles.add(new Triangle(obj, 0, 1, 2)); // front
        triangles.add(new Triangle(obj, 2, 3, 0)); // front
        triangles.add(new Triangle(obj, 6, 4, 7)); // back
        triangles.add(new Triangle(obj, 6, 5, 4)); // back
        triangles.add(new Triangle(obj, 4, 5, 1)); // left
        triangles.add(new Triangle(obj, 4, 1, 0)); // left
        triangles.add(new Triangle(obj, 3, 2, 6)); // right
        triangles.add(new Triangle(obj, 3, 6, 7)); // right
        triangles.add(new Triangle(obj, 4, 0, 3)); // bottom
        triangles.add(new Triangle(obj, 4, 3, 7)); // bottom
    }

}

