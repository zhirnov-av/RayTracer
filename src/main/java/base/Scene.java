package base;


import lights.DirectLight;
import lights.Light;
import lights.LightTypes;
import lights.PointLight;
import tree.BinaryTree;
import tree.TreeNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

public class Scene extends AbstractObject{

    BoundingBox boundingBox;
    public BinaryTree bBoxes;

    ArrayList<TreeNode> nodeList = new ArrayList<>();


    public ArrayList<Object3d> objects = new ArrayList<>();
    ArrayList<Light> lights = new ArrayList<>();
    public HashMap<String, Long> times = new HashMap<>();

    Vector3d camera = new Vector3d(0f, 0f, -100f);
    float viewPortWidth = 1;
    float viewPortHeight = 1;

    float dWidth = 0;
    float dHeight = 0;

    Vector3d cameraVector = new Vector3d(0f, -100f, 1f);
    Canvas canvas;

    public void addLight(Light light){
        lights.add(light);
    }

    public void addObject(Object3d object){
        object.scene = this;
        objects.add(object);

        int lastIndex = points.size();
        points.addAll(object.points);
        for(Triangle tr: object.triangles){
            triangles.add(tr);
        }

    }

    public Color traceRayOld(int x, int y){
        double minDistance = Double.MAX_VALUE;
        Color color = null;
        float xVp = (float)x * viewPortWidth/(float)canvas.getWidth();
        float yVp = (float)y * viewPortHeight/(float)canvas.getHeight();
        Vector3d vwp = new Vector3d(xVp, yVp, cameraVector.z);
        Vector3d diff = MathUtil.subtract(vwp, camera); // new Vector3d(vwp.subtract(camera));



        for(Object3d obj: objects){
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

                    /*
                    if (flgFound)
                        break;
                    */
                }
            }
        }
        return color;
    }

    public Color traceRay(int x, int y){
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

                    /*
                    if (flgFound)
                        break;
                    */
                }
            }
        }
        return color;
    }

    public boolean isNodeIntersected(int x, int y, TreeNode node){
        float xVp = (float)x * dWidth;
        float yVp = (float)y * dHeight;
        Vector3d vwp = new Vector3d(xVp, yVp, cameraVector.z);

        for( Triangle tr: node.getElement().triangles) {

            Vector3d intersect = tr.getIntersection(camera, vwp);


            if (intersect != null){
                Vector3d v = MathUtil.subtract(intersect, camera);
                node.getElement().distanceToCamera = v.x * v.x + v.y * v.y + v.z * v.z;

                return true;
            }
        }
        return false;
    }

    public boolean isNodeIntersectedV2(Vector3d from, Vector3d to, TreeNode node){
        for( Triangle tr: node.getElement().triangles) {

            Vector3d intersect = tr.getIntersection(from, to);

            if (intersect != null){
                Vector3d v = MathUtil.subtract(intersect, camera);
                node.getElement().distanceToCamera = v.x * v.x + v.y * v.y + v.z * v.z;

                return true;
            }
        }
        return false;
    }

    public TreeSet<TreeNode> fillListNodes(Canvas canvas, int x, int y, TreeNode node, TreeSet<TreeNode> list){
        Profiler.init("isNodeIntersection");
        boolean ini = isNodeIntersected(x, y, node);
        Profiler.check("isNodeIntersection");
        if (ini){
            if (node.isHasChild()) {
                list = fillListNodes(canvas, x, y, node.getLeft(), list);
                list = fillListNodes(canvas, x, y, node.getRight(), list);
            }else{
                if (list == null){
                    list = new TreeSet<>();
                }
                if (node.getElement().innerTriangles.size() > 0)
                    list.add(node);
            }
        }
        return list;
    }

    public TreeSet<TreeNode> fillListNodesV2(Vector3d from, Vector3d to, TreeNode node, TreeSet<TreeNode> list){
        Profiler.init("isNodeIntersection");
        boolean ini = isNodeIntersectedV2(from, to, node);
        Profiler.check("isNodeIntersection");
        if (ini){
            if (node.isHasChild()) {
                list = fillListNodesV2(from, to, node.getLeft(), list);
                list = fillListNodesV2(from, to, node.getRight(), list);
            }else{
                if (list == null){
                    list = new TreeSet<>();
                }
                if (node.getElement().innerTriangles.size() > 0)
                    list.add(node);
            }
        }
        return list;
    }


    public void prepareTracing(Canvas canvas){
        this.canvas = canvas;
        this.dWidth = viewPortWidth/(float)canvas.getWidth();
        this.dHeight = viewPortHeight/(float)canvas.getHeight();
    }

    public Color getIntersectionColor(Vector3d from, Vector3d to, int depth){
        //TreeSet<TreeNode> nodesToRendering = null;
        //nodesToRendering = fillListNodesV2(from, to, bBoxes.getRoot(), nodesToRendering);

        Vector3d intersect = null;
        double distance;
        double minDistance = Double.MAX_VALUE;
        Color color = null;

        Vector3d minusVwp = MathUtil.subtract(from, to);

        for(Object3d obj : objects){
        //if (nodesToRendering != null) {
            //for (TreeNode node : nodesToRendering) {
                boolean isRendered = false;
                Triangle foundTr = null;
                Vector3d foundIntersect = null;
                for (Triangle tr : obj.triangles) {
                //for (Triangle tr : node.getElement().innerTriangles) {
                    Profiler.init("Triangle.getIntersection");
                    intersect = tr.getIntersection(from, to);
                    Profiler.check("Triangle.getIntersection");

                    if (intersect != null) {
                        Vector3d v = MathUtil.subtract(intersect, from);
                        distance = v.x * v.x + v.y * v.y + v.z * v.z;// intersect.subtract(camera).getLength();
                        if (distance < minDistance) {
                            foundTr = tr;
                            minDistance = distance;
                            color = new Color(tr.getColor());
                            float intensity =  computeLighting(intersect, tr.getNormalInPoint(intersect), minusVwp);
                            color = color.multiplyIntensity(intensity);
                            foundIntersect = intersect;
                            isRendered = true;
                        }

                    }
                }

                if (foundIntersect != null && depth >= 0 && foundTr != null) {
                    Vector3d n = foundTr.getNormalInPoint(foundIntersect);
                    Vector3d dir = MathUtil.subtract(from, to);
                    Vector3d reflectRay = calcReflectRay(MathUtil.multiply(to, -1f), foundTr.getNormalInPoint(foundIntersect));
                    Vector3d i = new Vector3d(foundIntersect.x + n.x, foundIntersect.y + n.y, foundIntersect.z + n.z);
                    Color cl = getIntersectionColor(i, reflectRay, depth-1);
                    if (cl != null) {
                        color = color.multiplyIntensity(0.5f);
                        cl = cl.multiplyIntensity(0.5f);
                        color = color.addColor(cl);
                    }
                }

                //if (isRendered) break;


            //}
        }

        /*
        if (depth >= 2) {
            for (Object3d obj : objects) {
                if (obj.isNeedToRenderer) {
                    for (Triangle tr : obj.triangles) {
                        Profiler.init("Triangle.getIntersection");
                        intersect = tr.getIntersection(from, to);
                        Profiler.check("Triangle.getIntersection");

                        if (intersect != null) {
                            Vector3d v = MathUtil.subtract(intersect, camera);
                            distance = v.x * v.x + v.y * v.y + v.z * v.z;// intersect.subtract(camera).getLength();
                            if (distance < minDistance) {
                                minDistance = distance;
                                color = new Color(tr.getColor());
                                float intensity = computeLighting(intersect, tr.getNormalInPoint(intersect), minusVwp);
                                color = color.multiplyIntensity(intensity);
                            }

                        if (intersect != null && depth >= 0) {
                            Vector3d reflectRay = calcReflectRay(to, tr.getNormalInPoint(intersect));
                            Vector3d i = new Vector3d(intersect.x + reflectRay.x, intersect.y + reflectRay.y, intersect.z + reflectRay.z);
                            Color cl = getIntersectionColor(intersect, reflectRay, depth-1);
                            if (cl != null) {
                                color = color.multiplyIntensity(0.5f);
                                cl = cl.multiplyIntensity(0.5f);
                                color = color.addColor(cl);
                            }
                        }

                        }
                    }
                }
            }
        }
        */
        return color;
    }

    public Color traceRayV2(int x, int y, int depth){
        double minDistance = Double.MAX_VALUE;
        Color color = null;
        float xVp = (float)x * dWidth;
        float yVp = (float)y * dHeight;
        Vector3d vwp = new Vector3d(xVp, yVp, cameraVector.z);
        Vector3d minusVwp = MathUtil.subtract(new Vector3d(0f, 0f, 0f), vwp);

        return getIntersectionColor(camera, vwp, 2);

        //Vector3d diff = MathUtil.subtract(vwp, camera); // new Vector3d(vwp.subtract(camera));

//        TreeSet<TreeNode> nodesToRendering = null;
//        Profiler.init("fillListNodes");
//        nodesToRendering = fillListNodes(canvas, x, y, bBoxes.getRoot(), nodesToRendering);
//        Profiler.check("fillListNodes");
//
//        Vector3d intersect = null;
//
//        double distance = Double.MAX_VALUE;
//        if (nodesToRendering != null) {
//            for (TreeNode node : nodesToRendering) {
//                /*
//                for (Triangle tr : node.getElement().triangles) {
//                    Vector3d intersect = tr.getIntersection(camera, vwp);
//                    if (intersect != null) {
//                        Vector3d v = MathUtil.subtract(intersect, camera);
//                        double distance = v.x * v.x + v.y * v.y + v.z * v.z;// intersect.subtract(camera).getLength();
//                        if (distance < minDistance) {
//                            minDistance = distance;
//                            color = new Color(tr.getColor());
//                            color = color.multiplyIntensity(computeLighting(intersect, tr.getNormalInPoint(intersect), MathUtil.subtract(new Vector3d(0f, 0f, 0f), vwp)));
//                        }
//
//                    }
//                }
//
//                */
//                boolean isRendered = false;
//                Triangle foundTr = null;
//                for (Triangle tr : node.getElement().innerTriangles) {
//                    Profiler.init("Triangle.getIntersection");
//                    intersect = tr.getIntersection(camera, vwp);
//                    foundTr = tr;
//                    Profiler.check("Triangle.getIntersection");
//
//                    if (intersect != null) {
//                        Vector3d v = MathUtil.subtract(intersect, camera);
//                        distance = v.x * v.x + v.y * v.y + v.z * v.z;// intersect.subtract(camera).getLength();
//                        if (distance < minDistance) {
//                            minDistance = distance;
//                            color = new Color(tr.getColor());
//                            float intensity =  computeLighting(intersect, tr.getNormalInPoint(intersect), minusVwp);
//                            color = color.multiplyIntensity(intensity);
//
//
//
//
//                            isRendered = true;
//                        }
//
//                    }
//                }
//
//
//                if (isRendered) break;
//
//                /*
//                if (intersect != null) {
//                    Vector3d reflectRay = calcReflectRay(intersect, foundTr.getNormalInPoint(intersect));
//
//                    Vector3d = traceRayV2(intersect, reflectRay);
//                }
//                */
//            }
//        }
//
//        for (Object3d obj : objects) {
//            if (obj.isNeedToRenderer) {
//                for (Triangle tr : obj.triangles) {
//                    Profiler.init("Triangle.getIntersection");
//                    intersect = tr.getIntersection(camera, vwp);
//                    Profiler.check("Triangle.getIntersection");
//
//                    if (intersect != null) {
//                        Vector3d v = MathUtil.subtract(intersect, camera);
//                        distance = v.x * v.x + v.y * v.y + v.z * v.z;// intersect.subtract(camera).getLength();
//                        if (distance < minDistance) {
//                            minDistance = distance;
//                            color = new Color(tr.getColor());
//                            float intensity = computeLighting(intersect, tr.getNormalInPoint(intersect), minusVwp);
//                            color = color.multiplyIntensity(intensity);
//
//
//
//
//
//                            //isRendered = true;
//                        }
//
//                    }
//                }
//            }
//        }
//
//
//
//        return color;
    }

    Vector3d calcReflectRay(Vector3d from, Vector3d n){

        Vector3d reflectRay = MathUtil.multiply(n, 2f);
        float val = MathUtil.dotProduct(n, from);
        return MathUtil.subtract(MathUtil.multiply(reflectRay, val), from);

        /*
        float k = 2 * MathUtil.dotProduct(from, n) / MathUtil.dotProduct(n, n);

        float x = from.x - n.x * k;
        float y = from.y - n.y * k;
        float z = from.z - n.z * k;

        return new Vector3d(x, y, z);
        */

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

    public Vector3d findIntersection(Vector3d from, Vector3d to) {
        /*
        TreeSet<TreeNode> nodesToRendering = null;
        nodesToRendering = fillListNodesV2(from, to, bBoxes.getRoot(), nodesToRendering);

        if (nodesToRendering != null) {
            for (TreeNode node : nodesToRendering) {
                for (Triangle tr : node.getElement().innerTriangles) {
                    Vector3d intersect = tr.getIntersection(from, to);
                    if (intersect != null)
                        return intersect;
                }
            }
        }
        */
        for (Object3d obj : objects){
            for (Triangle tr : obj.triangles){
                Vector3d intersect = tr.getIntersection(from, to);
                if (intersect != null)
                    return intersect;
            }
        }
        return null;
    }

    public float computeLighting(Vector3d point, Vector3d n, Vector3d v){
        float intensity = 0;
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

                Vector3d intersect = findIntersection(point, l);
                //Color cl = getIntersectionColor(point, l, 0);
                if (intersect != null){
                    continue;
                }

                double tmp = MathUtil.dotProduct(n,l);
                if (tmp > 0){
                    intensity += intensity * tmp / (MathUtil.module(n) * MathUtil.module(l));
                }



                float s = 50f;
                Vector3d r = MathUtil.subtract(MathUtil.multiply(n, 2 * MathUtil.dotProduct(n, l)), l);
                float rDotV = MathUtil.dotProduct(r, v);
                if (rDotV > 0d){
                    intensity += light.getIntensity() * Math.pow(rDotV/(MathUtil.module(r)*MathUtil.module(v)), s);
                }


            }
        }
        return intensity;
    }

    public void defineBoundingBound(){

        boundingBox = new BoundingBox(this);
        boundingBox.defineBoundings(triangles);

        bBoxes = new BinaryTree(boundingBox);
        TreeNode root = bBoxes.getRoot();

        buildTree(boundingBox, root, "X");

        /*
        for(int i = 0; i < 10; i++) {
            divideBoundingBoxByPlane(this.boundingBox, "X", root);
            divideBoundingBoxByPlane(bBoxes.getRoot().getLeft().getElement(), "Y", bBoxes.getRoot().getLeft());
            divideBoundingBoxByPlane(bBoxes.getRoot().getRight().getElement(), "Y", bBoxes.getRoot().getRight());
        }
        */

        /*
        divideBoundingBoxByPlane(bBoxes.getRoot().getLeft().getLeft().getElement(), "Z", bBoxes.getRoot().getLeft().getLeft());
        divideBoundingBoxByPlane(bBoxes.getRoot().getLeft().getRight().getElement(), "Z", bBoxes.getRoot().getLeft().getRight());

        divideBoundingBoxByPlane(bBoxes.getRoot().getRight().getLeft().getElement(), "Z", bBoxes.getRoot().getRight().getLeft());
        divideBoundingBoxByPlane(bBoxes.getRoot().getRight().getRight().getElement(), "Z", bBoxes.getRoot().getRight().getRight());

        */
    }

    public static void buildTree(BoundingBox box, TreeNode root, String plane){
        String newPlane;
        if (plane.equals("X")){
            newPlane = "Y";
        }else if(plane.equals("Y")){
            newPlane = "Z";
        }else{
            newPlane = "X";
        }
        if (root.getLevel() == 6) {
            return;
        }
        divideBoundingBoxByPlane(box, plane, root);
        buildTree(root.getLeft().getElement(), root.getLeft(), newPlane);
        buildTree(root.getRight().getElement(), root.getRight(), newPlane);
    }



    public static BoundingBox[] divideBoundingBoxByPlane(BoundingBox boundingBox, String plane, TreeNode root) {
        BoundingBox[] boxes = new BoundingBox[2];

        if (plane.equals("X")){
            float minX = boundingBox.minX;
            float maxX = minX + (boundingBox.maxX - boundingBox.minX)/2.0f;

            boxes[0] = new BoundingBox(boundingBox.getObject());
            boxes[0].minX = minX;
            boxes[0].maxX = maxX;
            boxes[0].minY = boundingBox.minY;
            boxes[0].maxY = boundingBox.maxY;
            boxes[0].minZ = boundingBox.minZ;
            boxes[0].maxZ = boundingBox.maxZ;


            boxes[0].points.add(new Vertex3d(minX, boundingBox.minY, boundingBox.minZ)); // 0
            boxes[0].points.add(new Vertex3d(minX, boundingBox.maxY, boundingBox.minZ)); // 1
            boxes[0].points.add(new Vertex3d(maxX, boundingBox.maxY, boundingBox.minZ)); // 2
            boxes[0].points.add(new Vertex3d(maxX, boundingBox.minY, boundingBox.minZ)); // 3

            boxes[0].points.add(new Vertex3d(minX, boundingBox.minY, boundingBox.maxZ)); // 4
            boxes[0].points.add(new Vertex3d(minX, boundingBox.maxY, boundingBox.maxZ)); // 5
            boxes[0].points.add(new Vertex3d(maxX, boundingBox.maxY, boundingBox.maxZ)); // 6
            boxes[0].points.add(new Vertex3d(maxX, boundingBox.minY, boundingBox.maxZ)); // 7

            boxes[0].triangles.add(new Triangle(boxes[0], 0, 1, 2));
            boxes[0].triangles.add(new Triangle(boxes[0], 2, 3, 0));
            boxes[0].triangles.add(new Triangle(boxes[0], 4, 7, 6));
            boxes[0].triangles.add(new Triangle(boxes[0], 4, 6, 5));
            boxes[0].triangles.add(new Triangle(boxes[0], 5, 4, 1));
            boxes[0].triangles.add(new Triangle(boxes[0], 1, 4, 0));
            boxes[0].triangles.add(new Triangle(boxes[0], 2, 3, 6));
            boxes[0].triangles.add(new Triangle(boxes[0], 6, 3, 7));
            boxes[0].triangles.add(new Triangle(boxes[0], 6, 1, 5));
            boxes[0].triangles.add(new Triangle(boxes[0], 6, 2, 1));
            boxes[0].triangles.add(new Triangle(boxes[0], 4, 3, 7));
            boxes[0].triangles.add(new Triangle(boxes[0], 4, 0, 3));

            for (Triangle tr: boxes[0].triangles){
                tr.updateNomrs();
            }
            for (Triangle tr: boxes[0].triangles){
                tr.doNormalizeSt();
            }

            minX = maxX;
            maxX = boundingBox.maxX;

            boxes[1] = new BoundingBox(boundingBox.getObject());
            boxes[1].minX = minX;
            boxes[1].maxX = maxX;
            boxes[1].minY = boundingBox.minY;
            boxes[1].maxY = boundingBox.maxY;
            boxes[1].minZ = boundingBox.minZ;
            boxes[1].maxZ = boundingBox.maxZ;

            boxes[1].points.add(new Vertex3d(minX, boundingBox.minY, boundingBox.minZ));
            boxes[1].points.add(new Vertex3d(minX, boundingBox.maxY, boundingBox.minZ));
            boxes[1].points.add(new Vertex3d(maxX, boundingBox.maxY, boundingBox.minZ));
            boxes[1].points.add(new Vertex3d(maxX, boundingBox.minY, boundingBox.minZ));

            boxes[1].points.add(new Vertex3d(minX, boundingBox.minY, boundingBox.maxZ));
            boxes[1].points.add(new Vertex3d(minX, boundingBox.maxY, boundingBox.maxZ));
            boxes[1].points.add(new Vertex3d(maxX, boundingBox.maxY, boundingBox.maxZ));
            boxes[1].points.add(new Vertex3d(maxX, boundingBox.minY, boundingBox.maxZ));

            boxes[1].triangles.add(new Triangle(boxes[1], 0, 1, 2));
            boxes[1].triangles.add(new Triangle(boxes[1], 2, 3, 0));
            boxes[1].triangles.add(new Triangle(boxes[1], 6, 7, 4));
            boxes[1].triangles.add(new Triangle(boxes[1], 6, 4, 5));
            boxes[1].triangles.add(new Triangle(boxes[1], 4, 5, 1));
            boxes[1].triangles.add(new Triangle(boxes[1], 4, 1, 0));
            boxes[1].triangles.add(new Triangle(boxes[1], 3, 2, 6));
            boxes[1].triangles.add(new Triangle(boxes[1], 3, 6, 7));
            boxes[1].triangles.add(new Triangle(boxes[1], 6, 1, 5));
            boxes[1].triangles.add(new Triangle(boxes[1], 6, 2, 1));
            boxes[1].triangles.add(new Triangle(boxes[1], 4, 3, 7));
            boxes[1].triangles.add(new Triangle(boxes[1], 4, 0, 3));

            for (Triangle tr: boxes[1].triangles){
                tr.updateNomrs();
            }
            for (Triangle tr: boxes[1].triangles){
                tr.doNormalizeSt();
            }

        }else if(plane.equals("Y")){
            float minY = boundingBox.minY;
            float maxY = minY + (boundingBox.maxY - boundingBox.minY)/2.0f;

            boxes[0] = new BoundingBox(boundingBox.getObject());
            boxes[0].minY = minY;
            boxes[0].maxY = maxY;
            boxes[0].minX = boundingBox.minX;
            boxes[0].maxX = boundingBox.maxX;
            boxes[0].minZ = boundingBox.minZ;
            boxes[0].maxZ = boundingBox.maxZ;

            boxes[0].points.add(new Vertex3d(boundingBox.minX, minY, boundingBox.minZ));
            boxes[0].points.add(new Vertex3d(boundingBox.minX, maxY, boundingBox.minZ));
            boxes[0].points.add(new Vertex3d(boundingBox.maxX, maxY, boundingBox.minZ));
            boxes[0].points.add(new Vertex3d(boundingBox.maxX, minY, boundingBox.minZ));

            boxes[0].points.add(new Vertex3d(boundingBox.minX, minY, boundingBox.maxZ));
            boxes[0].points.add(new Vertex3d(boundingBox.minX, maxY, boundingBox.maxZ));
            boxes[0].points.add(new Vertex3d(boundingBox.maxX, maxY, boundingBox.maxZ));
            boxes[0].points.add(new Vertex3d(boundingBox.maxX, minY, boundingBox.maxZ));

            boxes[0].triangles.add(new Triangle(boxes[0], 0, 1, 2));
            boxes[0].triangles.add(new Triangle(boxes[0], 2, 3, 0));
            boxes[0].triangles.add(new Triangle(boxes[0], 6, 7, 4));
            boxes[0].triangles.add(new Triangle(boxes[0], 6, 4, 5));
            boxes[0].triangles.add(new Triangle(boxes[0], 4, 5, 1));
            boxes[0].triangles.add(new Triangle(boxes[0], 4, 1, 0));
            boxes[0].triangles.add(new Triangle(boxes[0], 3, 2, 6));
            boxes[0].triangles.add(new Triangle(boxes[0], 3, 6, 7));
            boxes[0].triangles.add(new Triangle(boxes[0], 6, 1, 5));
            boxes[0].triangles.add(new Triangle(boxes[0], 6, 2, 1));
            boxes[0].triangles.add(new Triangle(boxes[0], 4, 3, 7));
            boxes[0].triangles.add(new Triangle(boxes[0], 4, 0, 3));
            for (Triangle tr: boxes[0].triangles){
                tr.updateNomrs();
            }
            for (Triangle tr: boxes[0].triangles){
                tr.doNormalizeSt();
            }

            minY = maxY;
            maxY = boundingBox.maxY;

            boxes[1] = new BoundingBox(boundingBox.getObject());
            boxes[1].minY = minY;
            boxes[1].maxY = maxY;
            boxes[1].minX = boundingBox.minX;
            boxes[1].maxX = boundingBox.maxX;
            boxes[1].minZ = boundingBox.minZ;
            boxes[1].maxZ = boundingBox.maxZ;

            boxes[1].points.add(new Vertex3d(boundingBox.minX, minY, boundingBox.minZ));
            boxes[1].points.add(new Vertex3d(boundingBox.minX, maxY, boundingBox.minZ));
            boxes[1].points.add(new Vertex3d(boundingBox.maxX, maxY, boundingBox.minZ));
            boxes[1].points.add(new Vertex3d(boundingBox.maxX, minY, boundingBox.minZ));

            boxes[1].points.add(new Vertex3d(boundingBox.minX, minY, boundingBox.maxZ));
            boxes[1].points.add(new Vertex3d(boundingBox.minX, maxY, boundingBox.maxZ));
            boxes[1].points.add(new Vertex3d(boundingBox.maxX, maxY, boundingBox.maxZ));
            boxes[1].points.add(new Vertex3d(boundingBox.maxX, minY, boundingBox.maxZ));

            boxes[1].triangles.add(new Triangle(boxes[1], 0, 1, 2));
            boxes[1].triangles.add(new Triangle(boxes[1], 2, 3, 0));
            boxes[1].triangles.add(new Triangle(boxes[1], 6, 7, 4));
            boxes[1].triangles.add(new Triangle(boxes[1], 6, 4, 5));
            boxes[1].triangles.add(new Triangle(boxes[1], 4, 5, 1));
            boxes[1].triangles.add(new Triangle(boxes[1], 4, 1, 0));
            boxes[1].triangles.add(new Triangle(boxes[1], 3, 2, 6));
            boxes[1].triangles.add(new Triangle(boxes[1], 3, 6, 7));
            boxes[1].triangles.add(new Triangle(boxes[1], 6, 1, 5));
            boxes[1].triangles.add(new Triangle(boxes[1], 6, 2, 1));
            boxes[1].triangles.add(new Triangle(boxes[1], 4, 3, 7));
            boxes[1].triangles.add(new Triangle(boxes[1], 4, 0, 3));

            for (Triangle tr: boxes[1].triangles){
                tr.updateNomrs();
            }
            for (Triangle tr: boxes[1].triangles){
                tr.doNormalizeSt();
            }

        }else if(plane.equals("Z")){

            float minZ = boundingBox.minZ;
            float maxZ = minZ + (boundingBox.maxZ - boundingBox.minZ)/2.0f;

            boxes[0] = new BoundingBox(boundingBox.getObject());
            boxes[0].minZ = minZ;
            boxes[0].maxZ = maxZ;
            boxes[0].minX = boundingBox.minX;
            boxes[0].maxX = boundingBox.maxX;
            boxes[0].minY = boundingBox.minY;
            boxes[0].maxY = boundingBox.maxY;

            boxes[0].points.add(new Vertex3d(boundingBox.minX, boundingBox.minY, minZ));
            boxes[0].points.add(new Vertex3d(boundingBox.minX, boundingBox.maxY, minZ));
            boxes[0].points.add(new Vertex3d(boundingBox.maxX, boundingBox.maxY, minZ));
            boxes[0].points.add(new Vertex3d(boundingBox.maxX, boundingBox.minY, minZ));

            boxes[0].points.add(new Vertex3d(boundingBox.minX, boundingBox.minY, maxZ));
            boxes[0].points.add(new Vertex3d(boundingBox.minX, boundingBox.maxY, maxZ));
            boxes[0].points.add(new Vertex3d(boundingBox.maxX, boundingBox.maxY, maxZ));
            boxes[0].points.add(new Vertex3d(boundingBox.maxX, boundingBox.minY, maxZ));

            boxes[0].triangles.add(new Triangle(boxes[0], 0, 1, 2));
            boxes[0].triangles.add(new Triangle(boxes[0], 2, 3, 0));
            boxes[0].triangles.add(new Triangle(boxes[0], 6, 7, 4));
            boxes[0].triangles.add(new Triangle(boxes[0], 6, 4, 5));
            boxes[0].triangles.add(new Triangle(boxes[0], 4, 5, 1));
            boxes[0].triangles.add(new Triangle(boxes[0], 4, 1, 0));
            boxes[0].triangles.add(new Triangle(boxes[0], 3, 2, 6));
            boxes[0].triangles.add(new Triangle(boxes[0], 3, 6, 7));
            boxes[0].triangles.add(new Triangle(boxes[0], 6, 1, 5));
            boxes[0].triangles.add(new Triangle(boxes[0], 6, 2, 1));
            boxes[0].triangles.add(new Triangle(boxes[0], 4, 3, 7));
            boxes[0].triangles.add(new Triangle(boxes[0], 4, 0, 3));

            for (Triangle tr: boxes[0].triangles){
                tr.updateNomrs();
            }
            for (Triangle tr: boxes[0].triangles){
                tr.doNormalizeSt();
            }

            minZ = maxZ;
            maxZ = boundingBox.maxZ;

            boxes[1] = new BoundingBox(boundingBox.getObject());
            boxes[1].minZ = minZ;
            boxes[1].maxZ = maxZ;
            boxes[1].minX = boundingBox.minX;
            boxes[1].maxX = boundingBox.maxX;
            boxes[1].minY = boundingBox.minY;
            boxes[1].maxY = boundingBox.maxY;

            boxes[1].points.add(new Vertex3d(boundingBox.minX, boundingBox.minY, minZ));
            boxes[1].points.add(new Vertex3d(boundingBox.minX, boundingBox.maxY, minZ));
            boxes[1].points.add(new Vertex3d(boundingBox.maxX, boundingBox.maxY, minZ));
            boxes[1].points.add(new Vertex3d(boundingBox.maxX, boundingBox.minY, minZ));

            boxes[1].points.add(new Vertex3d(boundingBox.minX, boundingBox.minY, maxZ));
            boxes[1].points.add(new Vertex3d(boundingBox.minX, boundingBox.maxY, maxZ));
            boxes[1].points.add(new Vertex3d(boundingBox.maxX, boundingBox.maxY, maxZ));
            boxes[1].points.add(new Vertex3d(boundingBox.maxX, boundingBox.minY, maxZ));

            boxes[1].triangles.add(new Triangle(boxes[1], 0, 1, 2));
            boxes[1].triangles.add(new Triangle(boxes[1], 2, 3, 0));
            boxes[1].triangles.add(new Triangle(boxes[1], 6, 7, 4));
            boxes[1].triangles.add(new Triangle(boxes[1], 6, 4, 5));
            boxes[1].triangles.add(new Triangle(boxes[1], 4, 5, 1));
            boxes[1].triangles.add(new Triangle(boxes[1], 4, 1, 0));
            boxes[1].triangles.add(new Triangle(boxes[1], 3, 2, 6));
            boxes[1].triangles.add(new Triangle(boxes[1], 3, 6, 7));
            boxes[1].triangles.add(new Triangle(boxes[1], 6, 1, 5));
            boxes[1].triangles.add(new Triangle(boxes[1], 6, 2, 1));
            boxes[1].triangles.add(new Triangle(boxes[1], 4, 3, 7));
            boxes[1].triangles.add(new Triangle(boxes[1], 4, 0, 3));

            for (Triangle tr: boxes[1].triangles){
                tr.updateNomrs();
            }
            for (Triangle tr: boxes[1].triangles){
                tr.doNormalizeSt();
            }
        }

        for(Triangle tr: boundingBox.getObject().triangles){

            if (isInBoundingBox(boxes[0], tr)) boxes[0].innerTriangles.add(tr);
            if (isInBoundingBox(boxes[1], tr)) boxes[1].innerTriangles.add(tr);
        }

        root.addLeft(boxes[0]);
        root.addRight(boxes[1]);

        return boxes;
    }

    public static boolean isInBoundingBox(BoundingBox box, Triangle tr){

        float x = ( tr.pv1.p.x + tr.pv2.p.x + tr.pv3.p.x ) / 3;
        float y = ( tr.pv1.p.y + tr.pv2.p.y + tr.pv3.p.y ) / 3;
        float z = ( tr.pv1.p.z + tr.pv2.p.z + tr.pv3.p.z ) / 3;

        if (    x <= box.maxX + 3 && x >= box.minX - 3 &&
                y <= box.maxY + 3 && y >= box.minY - 3 &&
                z <= box.maxZ - 3 && z >= box.minZ - 3){
            return true;
        }


        if (    tr.pv1.p.x <= box.maxX + 3 && tr.pv1.p.x >= box.minX - 3 &&
                tr.pv1.p.y <= box.maxY + 3 && tr.pv1.p.y >= box.minY - 3 &&
                tr.pv1.p.z <= box.maxZ + 3 && tr.pv1.p.z >= box.minZ - 3){
            return true;
        }
        if (    tr.pv2.p.x <= box.maxX + 3 && tr.pv2.p.x >= box.minX - 3 &&
                tr.pv2.p.y <= box.maxY + 3 && tr.pv2.p.y >= box.minY - 3 &&
                tr.pv2.p.z <= box.maxZ + 3 && tr.pv2.p.z >= box.minZ - 3){
            return true;
        }
        if (    tr.pv3.p.x <= box.maxX + 3 && tr.pv3.p.x >= box.minX - 3 &&
                tr.pv3.p.y <= box.maxY + 3 && tr.pv3.p.y >= box.minY - 3 &&
                tr.pv3.p.z <= box.maxZ + 3 && tr.pv3.p.z >= box.minZ - 3){
            return true;
        }



        return false;
    }
}
