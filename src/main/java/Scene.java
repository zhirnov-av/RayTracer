import base.Color;
import base.Point3d;
import lights.DirectLight;
import lights.Light;
import lights.LightTypes;
import lights.PointLight;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

public class Scene {
    ArrayList<Point3d> points = new ArrayList<>();
    TreeSet<Triangle> triangles = new TreeSet<>();
    ArrayList<Light> lights = new ArrayList<>();
    HashMap<String, Long> times = new HashMap<>();

    Point3d camera = new Point3d(0f, 00f, 0f);
    float viewPortWidth = 1;
    float viewPortHeight = 1;
    Point3d cameraVector = new Point3d(0f, -100f, 1f);
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
        float xVp = (float)x * viewPortWidth/(float)canvas.getWidth();
        float yVp = (float)y * viewPortHeight/(float)canvas.getHeight();
        Point3d vwp = new Point3d(xVp, yVp, cameraVector.z);
        Point3d diff = new Point3d(vwp.subtract(camera));
        for(Triangle tr: triangles){

            long start = System.currentTimeMillis();
            Point3d intersect = tr.getIntersection(camera, vwp, diff);
            start = System.currentTimeMillis() - start;
            Long time = times.get("getIntersection");
            if (time == null) time = 0L;
            time += start;
            times.put("getIntersection", time);
            if (intersect != null) {
                //boolean flgFound = false;
                start = System.currentTimeMillis();
                double distance = intersect.subtract(camera).getLength();
                //double distance = tr.distanceToCamera;
                if (distance < minDistance) {
                    minDistance = distance;
                    color = new Color(tr.getColor());
                    color = color.multiplyIntensity(computeLighting(intersect, tr.getNormalInPoint(intersect), new Point3d(0f, 0f, 0f).subtract(vwp)));
                    //flgFound = true;
                }
                start = System.currentTimeMillis() - start;
                time = times.get("isPointInV2");
                if (time == null) time = 0L;
                time += start;
                times.put("isPointInV2", time);
                /*
                if (flgFound)
                    break;
                    */
            }

        }
        return color;
    }

    public Color traceRayV2(Canvas canvas, int x, int y){
        double minDistance = Double.MAX_VALUE;
        Color color = null;
        float xVp = (float)x * viewPortWidth/(float)canvas.getWidth();
        float yVp = (float)y * viewPortHeight/(float)canvas.getHeight();
        Point3d vwp = new Point3d(xVp, yVp, cameraVector.z);
        Point3d diff = new Point3d(vwp.subtract(camera));
        for(Triangle tr: triangles){

            long start = System.currentTimeMillis();
            Point3d intersect = tr.getIntersection(camera, vwp, diff);
            start = System.currentTimeMillis() - start;
            Long time = times.get("getIntersection");
            if (time == null) time = 0L;
            time += start;
            times.put("getIntersection", time);
            if (intersect != null) {

                //boolean flgFound = false;
                start = System.currentTimeMillis();

                if (tr.isPointInV2(intersect)) {
                    double distance = intersect.subtract(camera).getLength();
                    //double distance = tr.distanceToCamera;
                    if (distance < minDistance) {
                        minDistance = distance;
                        color = new Color(tr.getColor());
                        color = color.multiplyIntensity(computeLighting(intersect, tr.getNormalInPoint(intersect), new Point3d(0f, 0f, 0f).subtract(vwp)));
                        //flgFound = true;
                    }
                }
                start = System.currentTimeMillis() - start;
                time = times.get("isPointInV2");
                if (time == null) time = 0L;
                time += start;
                times.put("isPointInV2", time);
                /*
                if (flgFound)
                    break;
                    */
            }

        }
        return color;
    }




    public double computeLighting(Point3d point, Point3d n, Point3d v){
        double intensity = 0;
        for(Light light: lights){

            if (light.getType() == LightTypes.AIMBIENT){
                intensity += light.getIntensity();
            }else{
                Point3d l = new Point3d();
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


                double s = 10d;
                Point3d r = n.multiply(2 * n.sMultiply(l)).subtract(l);
                double rDotV = r.sMultiply(v);
                if (rDotV > 0d){
                    intensity += light.getIntensity() * Math.pow(rDotV/(r.getLength()*v.getLength()), s);
                }

            }
        }
        return intensity;
    }


}
