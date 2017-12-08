package base;

import java.util.ArrayList;
import java.util.TreeSet;

public class AbstractObject {
    Scene scene;
    Vector3d position;

    public boolean isNeedToRenderer = false;

    public float reflection = 0f;
    public float specular = 20f;
    public boolean usePhongNormals = true;


    public ArrayList<Vertex3d> points = new ArrayList<>();
    public TreeSet<Primitive> primitives = new TreeSet<>();
}
