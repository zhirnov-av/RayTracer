import base.Color;
import base.Point3D;
import lights.DirectLight;
import lights.Light;
import lights.LightTypes;
import lights.PointLight;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

public class Scene {
    ArrayList<Point3D> points = new ArrayList<>();
    TreeSet<Triangle> triangles = new TreeSet<>();
    ArrayList<Light> lights = new ArrayList<>();
    HashMap<String, Long> times = new HashMap<>();

    Point3D camera = new Point3D(0d, 0.4d, 0d);
    double viewPortWidth = 1;
    double viewPortHeight = 1;
    Point3D cameraVector = new Point3D(0d, 0.0d, 1d);
    Canvas canvas;

    public void addLight(Light light){
        lights.add(light);
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

    public Color traceRay(Canvas canvas, int x, int y){
        double minDistance = Double.MAX_VALUE;
        Color color = null;
        double xVp = (double)x * viewPortWidth/(double)canvas.getWidth();
        double yVp = (double)y * viewPortHeight/(double)canvas.getHeight();
        Point3D vwp = new Point3D(xVp, yVp, cameraVector.z);
        Point3D diff = new Point3D(vwp.subtract(camera));
        for(Triangle tr: triangles){

            long start = System.currentTimeMillis();
            Point3D intersect = tr.getIntersection(camera, vwp, diff);
            start = System.currentTimeMillis() - start;
            Long time = times.get("getIntersection");
            if (time == null) time = 0L;
            time += start;
            times.put("getIntersection", time);
            if (intersect != null) {

                boolean flgFound = false;
                start = System.currentTimeMillis();

                if (tr.isPointInV2(intersect)) {
                    //double distance = intersect.subtract(camera).getLength();
                    double distance = tr.distanceToCamera;
                    if (distance < minDistance) {
                        minDistance = distance;
                        color = new Color(tr.getColor());
                        color = color.multiplyIntensity(computeLighting(intersect, tr.getNormal()));
                        flgFound = true;
                    }
                }
                start = System.currentTimeMillis() - start;
                time = times.get("isPointInV2");
                if (time == null) time = 0L;
                time += start;
                times.put("isPointInV2", time);
                if (flgFound)
                    break;
            }

        }
        return color;
    }

    public double computeLighting(Point3D point, Point3D n){
        double intensity = 0;
        for(Light light: lights){

            if (light.getType() == LightTypes.AIMBIENT){
                intensity += light.getIntensity();
            }else{
                Point3D l = new Point3D();
                switch(light.getType()){
                    case POINT:
                        l = ((PointLight)light).getPosition().subtract(point);
                        break;
                    case DIRECTED:
                        l = ((DirectLight)light).getTarget();
                        break;
                }
                double tmp = n.sMultiply(l);
                if (tmp > 0){
                    intensity += intensity * tmp / (n.getLength() * l.getLength());
                }
            }
        }
        return intensity;
    }

}
