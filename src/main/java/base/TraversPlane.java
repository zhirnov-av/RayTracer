package base;

import java.util.TreeSet;

public class TraversPlane {
    Plane plane;
    Vector3d planePos;

    TreeSet<Triangle> trianglesAtLeft = new TreeSet<>();
    TreeSet<Triangle> trianglesAtRight = new TreeSet<>();
}
