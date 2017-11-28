package base;


import lights.DirectLight;
import lights.Light;
import lights.LightTypes;
import lights.PointLight;

import java.util.ArrayList;
import java.util.HashMap;

public class Scene {
//    ArrayList<Vertex3d> points = new ArrayList<>();
//    TreeSet<base.Triangle> triangles = new TreeSet<>();
    public ArrayList<Object3d> objects = new ArrayList<>();
    ArrayList<Light> lights = new ArrayList<>();
    public HashMap<String, Long> times = new HashMap<>();

    Vector3d camera = new Vector3d(0f, 00f, 0f);
    float viewPortWidth = 1;
    float viewPortHeight = 1;
    Vector3d cameraVector = new Vector3d(0f, -100f, 1f);
    Canvas canvas;

    public void addLight(Light light){
        lights.add(light);
    }

    public void addObject(Object3d object){
        object.scene = this;
        objects.add(object);
    }


    public Color traceRay(Canvas canvas, int x, int y){
        double minDistance = Double.MAX_VALUE;
        Color color = null;
        float xVp = (float)x * viewPortWidth/(float)canvas.getWidth();
        float yVp = (float)y * viewPortHeight/(float)canvas.getHeight();
        Vector3d vwp = new Vector3d(xVp, yVp, cameraVector.z);
        Vector3d diff = MathUtil.subtract(vwp, camera); // new Vector3d(vwp.subtract(camera));
        for(Object3d obj: objects){
            boolean needToRenderer = false;
            for( Triangle tr: obj.boundingBox.triangles) {
                Vector3d intersect = tr.getIntersection(camera, vwp);
                if (intersect != null){
                    needToRenderer = true;
                    break;
                }
            }
            if (!needToRenderer)
                continue;

            for( Triangle tr: obj.triangles) {
                long start = System.currentTimeMillis();
                Vector3d intersect = tr.getIntersection(camera, vwp);
                start = System.currentTimeMillis() - start;
                Long time = times.get("getIntersection");
                if (time == null) time = 0L;
                time += start;
                times.put("getIntersection", time);
                if (intersect != null) {
                    boolean flgFound = false;
                    start = System.currentTimeMillis();
                    Vector3d v = MathUtil.subtract(intersect, camera);
                    double distance = v.x * v.x + v.y * v.y + v.z * v.z;// intersect.subtract(camera).getLength();
                    //double distance = tr.distanceToCamera;
                    if (distance < minDistance) {
                        minDistance = distance;
                        color = new Color(tr.getColor());
                        color = color.multiplyIntensity(computeLighting(intersect, tr.getNormalInPoint(intersect), MathUtil.subtract(new Vector3d(0f, 0f, 0f), vwp)));
                        flgFound = true;
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
        }
        return color;
    }

    /*
    public Color traceRayV2(base.Canvas canvas, int x, int y){
        double minDistance = Double.MAX_VALUE;
        Color color = null;
        float xVp = (float)x * viewPortWidth/(float)canvas.getWidth();
        float yVp = (float)y * viewPortHeight/(float)canvas.getHeight();
        Vertex3d vwp = new Vertex3d(xVp, yVp, cameraVector.z);
        Vertex3d diff = new Vertex3d(vwp.subtract(camera));
        for(base.Triangle tr: triangles){

            long start = System.currentTimeMillis();
            Vertex3d intersect = tr.getIntersection(camera, vwp, diff);
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
                        color = color.multiplyIntensity(computeLighting(intersect, tr.getNormalInPoint(intersect), new Vertex3d(0f, 0f, 0f).subtract(vwp)));
                        //flgFound = true;
                    }
                }
                start = System.currentTimeMillis() - start;
                time = times.get("isPointInV2");
                if (time == null) time = 0L;
                time += start;
                times.put("isPointInV2", time);
                
                //if (flgFound)
                //    break;
                //
            }

        }
        return color;
    }
    */



    public double computeLighting(Vector3d point, Vector3d n, Vector3d v){
        double intensity = 0;
        for(Light light: lights){

            if (light.getType() == LightTypes.AIMBIENT){
                intensity += light.getIntensity();
            }else{
                Vector3d l = new Vector3d();
                switch(light.getType()){
                    case POINT:
                        l = MathUtil.subtract(((PointLight)light).getPosition(),point);
                        break;
                    case DIRECTED:
                        l = ((DirectLight)light).getTarget();
                        break;
                }
                double tmp = MathUtil.dotProduct(n,l);
                if (tmp > 0){
                    intensity += intensity * tmp / (MathUtil.module(n) * MathUtil.module(l));
                }


                double s = 10d;
                Vector3d r = MathUtil.subtract(MathUtil.multiply(n, 2 * MathUtil.dotProduct(n, l)), l);
                double rDotV = MathUtil.dotProduct(r, v);
                if (rDotV > 0d){
                    intensity += light.getIntensity() * Math.pow(rDotV/(MathUtil.module(r)*MathUtil.module(v)), s);
                }

            }
        }
        return intensity;
    }


}
