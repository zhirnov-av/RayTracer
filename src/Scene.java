import java.util.ArrayList;

public class Scene {
    ArrayList<Point3D> points = new ArrayList<>();
    ArrayList<Triangle> triangles = new ArrayList<>();
    Point3D camera = new Point3D(0d, -1d, 0d);
    double viewPortWidth = 1;
    double viewPortHeight = 1;
    Point3D cameraVector = new Point3D(0d, 0d, 1d);
    Canvas canvas;

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

    public Color traceRay(Canvas canvas, int x, int y){
        double minDistance = Double.MAX_VALUE;
        Color color = null;
        for(Triangle tr: triangles){
            double xVp = (double)x * viewPortWidth/(double)canvas.getWidth();
            double yVp = (double)y * viewPortHeight/(double)canvas.getHeight();
            Point3D intersect = tr.getIntersection(camera, new Point3D(xVp, yVp, cameraVector.z));
            if (intersect != null) {
                double distance = intersect.getLength();
                if (tr.isPointInV2(intersect) && distance < minDistance) {
                    minDistance = distance;
                    color = tr.getColor();
                }
            }
        }
        return color;
    }

}
