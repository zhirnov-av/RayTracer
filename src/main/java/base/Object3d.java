package base;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Object3d extends AbstractObject{


    BoundingBox boundingBox;

    //Object3d boundingBox;

    //ArrayList<Vertex3d> boundingPoints = new ArrayList<>();
    //TreeSet<Triangle> boundingTriangles = new TreeSet<>();

    public void setScene(Scene scene){
        this.scene = scene;
    }

    public void setObjectParam(float reflection, float specular){
        this.reflection = reflection;
        this.specular = specular;
    }

    public void setObjectParam(float reflection, float specular, Color color){
        this.reflection = reflection;
        this.specular = specular;
        for(Primitive pr : primitives){
            pr.color = color;
        }
    }


    public void loadFromTdf(String file, float scale, Vector3d position, Color color) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        BufferedReader br = new BufferedReader( new InputStreamReader(classLoader.getResourceAsStream(file)));
        try {
            br.readLine();
            int numPoints = Integer.parseInt(br.readLine());
            int numTriangles = Integer.parseInt(br.readLine());

            for (int i = 0; i < numPoints; i++){
                String strLine = br.readLine().trim();
                String[] arr = strLine.split("\\s+");
                this.points.add(new Vertex3d(Float.parseFloat(arr[0]) * scale + position.x, Float.parseFloat(arr[1]) * scale + position.y, Float.parseFloat(arr[2]) * scale + position.z));
            }
            for (int i = 0; i < numTriangles; i++){
                String strLine = br.readLine().trim();
                String[] arr = strLine.split("\\s+");
                this.primitives.add(new Triangle(this, Integer.parseInt(arr[0]), Integer.parseInt(arr[1]), Integer.parseInt(arr[2]), color));
            }


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

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
        primitives.add(new Triangle(this, indexA, indexB, indexC));
    }


    public void defineBoundingBox(){
        boundingBox = new BoundingBox(this);
        boundingBox.defineBoundings(primitives);
    }


}
