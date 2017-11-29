package base;

import java.util.ArrayList;
import java.util.TreeSet;

public class AbstractObject {
    Scene scene;
    Vector3d position;

    public ArrayList<Vertex3d> points = new ArrayList<>();
    public TreeSet<Triangle> triangles = new TreeSet<>();
}
