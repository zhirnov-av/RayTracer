package base;

import base.*;

import java.util.TreeSet;

public class BoundingBox extends AbstractObject{
    AbstractObject object;
    public float minX = 0, minY = 0, minZ = 0, maxX = 0, maxY = 0, maxZ = 0;
    public double distanceToCamera = 0;

    public TreeSet<Triangle> innerTriangles = new TreeSet<>();

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

        this.points.add(new Vertex3d(minX, minY, minZ));
        this.points.add(new Vertex3d(minX, maxY, minZ));
        this.points.add(new Vertex3d(maxX, maxY, minZ));
        this.points.add(new Vertex3d(maxX, minY, minZ));

        this.points.add(new Vertex3d(minX, minY, maxZ));
        this.points.add(new Vertex3d(minX, maxY, maxZ));
        this.points.add(new Vertex3d(maxX, maxY, maxZ));
        this.points.add(new Vertex3d(maxX, minY, maxZ));

        this.triangles.add(new Triangle(this, 0, 1, 2));
        this.triangles.add(new Triangle(this, 2, 3, 0));
        this.triangles.add(new Triangle(this, 6, 7, 4));
        this.triangles.add(new Triangle(this, 6, 4, 5));
        this.triangles.add(new Triangle(this, 4, 5, 1));
        this.triangles.add(new Triangle(this, 4, 1, 0));
        this.triangles.add(new Triangle(this, 3, 2, 6));
        this.triangles.add(new Triangle(this, 3, 6, 7));

        innerTriangles.addAll(triangles);

    }

}

