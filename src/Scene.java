import base.Point3D;
import lights.DirectLight;
import lights.Light;
import lights.LightTypes;
import lights.PointLight;

import java.util.ArrayList;
import java.util.HashMap;

public class Scene {
    ArrayList<Point3D> points = new ArrayList<>();
    ArrayList<Triangle> triangles = new ArrayList<>();
    ArrayList<Light> lights = new ArrayList<Light>();
    HashMap<String, Long> times = new HashMap<>();

    Point3D camera = new Point3D(0d, 0.2d, 0d);
    double viewPortWidth = 1;
    double viewPortHeight = 1;
    Point3D cameraVector = new Point3D(0d, 0.1d, 1d);
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
        for(Triangle tr: triangles){
            double xVp = (double)x * viewPortWidth/(double)canvas.getWidth();
            double yVp = (double)y * viewPortHeight/(double)canvas.getHeight();
            long start = System.currentTimeMillis();
            Point3D intersect = tr.getIntersection(camera, new Point3D(xVp, yVp, cameraVector.z));
            start = System.currentTimeMillis() - start;
            Long time = times.get("getIntersection");
            if (time == null) time = new Long(0);
            time += start;
            times.put("getIntersection", time);
            if (intersect != null) {
                double distance = intersect.getLength();

                start = System.currentTimeMillis();

                if (tr.isPointInV2(intersect) && distance < minDistance) {
                    minDistance = distance;
                    color = new Color(tr.getColor());
                    color = color.multiplyIntensity(computeLighting(intersect, tr.getNormal()));
                }
                start = System.currentTimeMillis() - start;
                time = times.get("isPointInV2");
                if (time == null) time = new Long(0);
                time += start;
                times.put("isPointInV2", time);

            }
            System.out.println(String.format("getIntersection = %d isPointInV2 = %d", times.get("getIntersection"), times.get("isPointInV2")));
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
