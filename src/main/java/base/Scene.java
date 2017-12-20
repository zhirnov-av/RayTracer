package base;


import lights.DirectLight;
import lights.Light;
import lights.LightTypes;
import lights.PointLight;
import tree.BinaryTree;
import tree.TreeNode;
import tree.TreeNodeV2;

import java.util.*;

public class Scene extends AbstractObject{

    BoundingBox boundingBox;
    public BinaryTree bBoxes;

    ArrayList<TreeNode> nodeList = new ArrayList<>();


    public ArrayList<Object3d> objects = new ArrayList<>();
    ArrayList<Light> lights = new ArrayList<>();
    public HashMap<String, Long> times = new HashMap<>();

    Vector3d camera = new Vector3d(0f, 0f, -200f);
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
        for(Primitive tr: object.primitives){
            primitives.add(tr);
        }
    }

    /*
    public Color traceRayOld(int x, int y){
        double minDistance = Double.MAX_VALUE;
        Color color = null;
        float xVp = (float)x * viewPortWidth/(float)canvas.getWidth();
        float yVp = (float)y * viewPortHeight/(float)canvas.getHeight();
        Vector3d vwp = new Vector3d(xVp, yVp, cameraVector.z);
        Vector3d diff = MathUtil.subtract(vwp, camera); // new Vector3d(vwp.subtract(camera));



        for(Object3d obj: objects){
            for( Triangle tr: obj.primitives) {
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
                        color = color.multiplyIntensity(computeLighting(intersect, tr.getNormalInPoint(intersect), MathUtil.subtract(new Vector3d(0f, 0f, 0f), vwp), tr));
                        flgFound = true;
                    }
                    start = System.currentTimeMillis() - start;
                    time = times.get("isPointInV2");
                    if (time == null) time = 0L;
                    time += start;
                    times.put("isPointInV2", time);

                }
            }
        }
        return color;
    }
    */

    public Color traceRay(int x, int y){
        double minDistance = Double.MAX_VALUE;
        Color color = null;
        float xVp = (float)x * viewPortWidth/(float)canvas.getWidth();
        float yVp = (float)y * viewPortHeight/(float)canvas.getHeight();
        Vector3d vwp = new Vector3d(xVp, yVp, cameraVector.z);
        Vector3d diff = MathUtil.subtract(vwp, camera); // new Vector3d(vwp.subtract(camera));



        for(Object3d obj: objects){
            boolean needToRenderer = false;
            for( Primitive tr: obj.boundingBox.primitives) {
                Vector3d intersect = tr.getIntersection(camera, vwp);
                if (intersect != null){
                    needToRenderer = true;
                    break;
                }
            }
            if (!needToRenderer)
                continue;

            for( Primitive tr: obj.primitives) {
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
                        color = color.multiplyIntensity(computeLighting(intersect, tr.getNormalInPoint(intersect), MathUtil.subtract(new Vector3d(0f, 0f, 0f), vwp), tr));
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

        for( Primitive tr: node.getElement().primitives) {

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
        for( Primitive tr: node.getElement().triangles) {

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
                if (node.getElement().innerPrimitives.size() > 0)
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
                if (node.getElement().innerPrimitives.size() > 0)
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
        TreeSet<TreeNode> nodesToRendering = null;

        /*
        BoundingBox bBox = new BoundingBox(scene);
        TreeNode nodeToRenderer = new TreeNode(null, bBox);
        for(Object3d obj : objects){
            if (obj.isNeedToRenderer){
                if (bBox.innerPrimitives == null)
                    bBox.innerPrimitives = new TreeSet<>();
                for (Primitive tr : obj.primitives){
                    bBox.innerPrimitives.add(tr);
                }
            }
        }
        if (bBox.innerPrimitives.size() > 0){
            nodesToRendering = new TreeSet<>();
            nodesToRendering.add(nodeToRenderer);
        }
        */

        nodesToRendering = fillListNodesV2(from, to, bBoxes.getRoot(), nodesToRendering);

        Vector3d intersect = null;
        double distance;
        double minDistance = Double.MAX_VALUE;
        Color color = null;

        Vector3d minusVwp = MathUtil.subtract(from, to);

        boolean isRendered = false;
        //for(Object3d obj : objects){
        if (nodesToRendering != null) {
            for (TreeNode node : nodesToRendering) {
                Primitive foundTr = null;
                Vector3d foundIntersect = null;
                Vector3d foundN = null;
                //for (Triangle tr : obj.primitives) {
                for (Primitive tr : node.getElement().innerPrimitives) {
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
                            foundN = tr.getNormalInPoint(intersect);
                            float intensity =  computeLighting(intersect, foundN, minusVwp, foundTr);
                            color = color.multiplyIntensity(intensity);
                            foundIntersect = intersect;
                            isRendered = true;
                        }

                    }
                }


                if (foundTr != null && ((Object3d)foundTr.object).reflection > 0 && depth >= 0) {
                    Vector3d reflectRay = calcReflectRay(MathUtil.multiply(to, -1f), foundN);
                    Vector3d i = new Vector3d(foundIntersect.x + foundN.x, foundIntersect.y + foundN.y, foundIntersect.z + foundN.z);
                    Color cl = getIntersectionColor(i, reflectRay, depth-1);
                    if (cl != null) {
                        color = color.multiplyIntensity(1f - ((Object3d)foundTr.object).reflection);
                        cl = cl.multiplyIntensity(((Object3d)foundTr.object).reflection);
                        color = color.addColor(cl);
                    }
                }

                //if (isRendered) break;


            }
        }

        /*
        if (!isRendered) {
            for (Object3d obj : objects) {
                if (obj.isNeedToRenderer) {
                    for (Triangle tr : obj.primitives) {
                        Profiler.init("Triangle.getIntersection");
                        intersect = tr.getIntersection(from, to);
                        Profiler.check("Triangle.getIntersection");
                        Vector3d foundIntersect = null;
                        Vector3d foundN = null;
                        if (intersect != null) {
                            Vector3d v = MathUtil.subtract(intersect, camera);
                            distance = v.x * v.x + v.y * v.y + v.z * v.z;// intersect.subtract(camera).getLength();
                            if (distance < minDistance) {
                                minDistance = distance;
                                color = new Color(tr.getColor());
                                foundN = tr.getNormalInPoint(intersect);
                                float intensity = computeLighting(intersect, foundN, minusVwp, tr);
                                color = color.multiplyIntensity(intensity);
                                foundIntersect = intersect;
                            }
                        }

                        if (foundIntersect != null && ((Object3d)tr.object).reflection > 0 && depth >= 0) {

                            Vector3d reflectRay = calcReflectRay(MathUtil.multiply(to, -1f), foundN);
                            Vector3d i = new Vector3d(foundIntersect.x + foundN.x, foundIntersect.y + foundN.y, foundIntersect.z + foundN.z);
                            Color cl = getIntersectionColor(i, reflectRay, depth-1);
                            if (cl != null) {
                                color = color.multiplyIntensity(1f - ((Object3d)tr.object).reflection);
                                cl = cl.multiplyIntensity(((Object3d)tr.object).reflection);
                                color = color.addColor(cl);
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
        for(base.Triangle tr: primitives){

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

        TreeSet<TreeNode> nodesToRendering = null;
        nodesToRendering = fillListNodesV2(from, to, bBoxes.getRoot(), nodesToRendering);

        if (nodesToRendering != null) {
            for (TreeNode node : nodesToRendering) {
                for (Primitive tr : node.getElement().innerPrimitives) {
                    Vector3d intersect = tr.getIntersection(from, to);
                    if (intersect != null)
                        return intersect;
                }
            }
        }

        /*
        for(Light light : lights){
            if (light instanceof PointLight){
                Primitive pr = ((PointLight) light).contur;
                Vector3d intersect = pr.getIntersection(from, to);
                if (intersect != null)
                    return pr;
            }
        }
        */




        for (Object3d obj : objects){
            for (Primitive tr : obj.primitives){
                Vector3d intersect = tr.getIntersection(from, to);
                if (intersect != null)
                    return intersect;
            }
        }

        return null;
    }

    public float computeLighting(Vector3d point, Vector3d n, Vector3d v, Primitive tr){
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

                if (light.getType() == LightTypes.POINT) {

                    for (float x = ((PointLight)light).getPosition().x - 6; x <= ((PointLight)light).getPosition().x + 6; x += 6f ){
                        for (float z = ((PointLight)light).getPosition().z - 6; z <= ((PointLight)light).getPosition().z + 6; z += 6f ){

                            Vector3d lPoint = new Vector3d(x, ((PointLight)light).getPosition().y, z);
                            l = MathUtil.subtract(lPoint ,point);
                            Vector3d intersect = findIntersection(point, l);
                            if (intersect != null) {
                                continue;
                            }else{
                                double tmp = MathUtil.dotProduct(n,l);
                                if (tmp > 0){
                                    intensity += ( intensity * tmp / (MathUtil.module(n) * MathUtil.module(l)) )/9f;
                                }
                            }

                        }
                    }

                    /*
                    Vector3d intersect = findIntersection(point, l);
                    if (intersect != null) {
                        continue;
                    }else{
                        double tmp = MathUtil.dotProduct(n,l);
                        if (tmp > 0){
                            intensity += ( intensity * tmp / (MathUtil.module(n) * MathUtil.module(l)) );
                        }
                    }
                    */


                }else {
                    double tmp = MathUtil.dotProduct(n, l);
                    if (tmp > 0) {
                        intensity += intensity * tmp / (MathUtil.module(n) * MathUtil.module(l));
                    }
                }


                if (tr.object instanceof Object3d) {
                    float s = ((Object3d) (tr.object)).specular;
                    Vector3d r = MathUtil.subtract(MathUtil.multiply(n, 2 * MathUtil.dotProduct(n, l)), l);
                    float rDotV = MathUtil.dotProduct(r, v);
                    if (rDotV > 0d) {
                        intensity += light.getIntensity() * Math.pow(rDotV / (MathUtil.module(r) * MathUtil.module(v)), s);
                    }
                }


            }
        }
        return intensity;
    }

    public void defineBoundingBound(){

        boundingBox = new BoundingBox(this);
        boundingBox.defineBoundings(primitives);

        bBoxes = new BinaryTree(boundingBox);
        TreeNode root = bBoxes.getRoot();

        buildTree(boundingBox, root, "X");

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

    public static void buildTreeV2(TraversPlane plane, TreeNodeV2 root, Plane typePlane){
        Plane newPlane;
        if (typePlane == Plane.X){
            newPlane = Plane.Y;
        }else if(typePlane == Plane.Y){
            newPlane = Plane.Z;
        }else{
            newPlane = Plane.X;
        }

        if (root.getLevel() == 6) {
            return;
        }


        //divideTraversPlane(plane, typePlane, root);


        buildTreeV2(root.getLeft().getElement(), root.getLeft(), newPlane);
        buildTreeV2(root.getRight().getElement(), root.getRight(), newPlane);
    }

    public void divideTraversPlane(TraversPlane plane, Plane typePlane, TreeNodeV2 node){
        /*
        TraversPlane planeNext = new TraversPlane();
        switch (typePlane){
            case X:{
                planeNext.planePos.x = plane.planePos.x - this.boundingBox.
            }
        }


        if (plane == null){

        }
        */
    }

    /*
    public static TraversPlane initialDivideTraversPlane(BoundingBox box, Plane typePlane, TreeNodeV2 node){
        TraversPlane plane = new TraversPlane();

        plane.planePos.x = 0;
        plane.planePos.y = 0;
        plane.planePos.z = 0;

        plane.plane = typePlane;

        for(Triangle tr : box.innerPrimitives){
            switch (typePlane){
                case X:{
                    if (tr.pv1.p.x <= plane.planePos.x || tr.pv2.p.x <= plane.planePos.x || tr.pv3.p.x <= plane.planePos.x){
                        plane.trianglesAtLeft.add(tr);
                    }
                    if (tr.pv1.p.x >= plane.planePos.x || tr.pv2.p.x >= plane.planePos.x || tr.pv3.p.x >= plane.planePos.x){
                        plane.trianglesAtRight.add(tr);
                    }
                    break;
                }
                case Y:{
                    if (tr.pv1.p.y <= plane.planePos.y || tr.pv2.p.y <= plane.planePos.y || tr.pv3.p.y <= plane.planePos.y){
                        plane.trianglesAtLeft.add(tr);
                    }
                    if (tr.pv1.p.y >= plane.planePos.y || tr.pv2.p.y >= plane.planePos.y || tr.pv3.p.y >= plane.planePos.y){
                        plane.trianglesAtRight.add(tr);
                    }
                    break;
                }
                case Z:{
                    if (tr.pv1.p.z <= plane.planePos.z || tr.pv2.p.z <= plane.planePos.z || tr.pv3.p.z <= plane.planePos.z) {
                        plane.trianglesAtLeft.add(tr);
                    }
                    if (tr.pv1.p.z >= plane.planePos.z || tr.pv2.p.z >= plane.planePos.z || tr.pv3.p.z >= plane.planePos.z) {
                        plane.trianglesAtRight.add(tr);
                    }
                    break;
                }

            }
        }


        node = new TreeNodeV2(null, plane);
        return plane;
    }
    */


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

            BoundingBox.buildBox(boxes[0], boxes[0].points, boxes[0].triangles);
            /*
            boxes[0].primitives.add(new Triangle(boxes[0], 0, 1, 2));
            boxes[0].primitives.add(new Triangle(boxes[0], 2, 3, 0));
            boxes[0].primitives.add(new Triangle(boxes[0], 4, 7, 6));
            boxes[0].primitives.add(new Triangle(boxes[0], 4, 6, 5));
            boxes[0].primitives.add(new Triangle(boxes[0], 5, 4, 1));
            boxes[0].primitives.add(new Triangle(boxes[0], 1, 4, 0));
            boxes[0].primitives.add(new Triangle(boxes[0], 2, 3, 6));
            boxes[0].primitives.add(new Triangle(boxes[0], 6, 3, 7));
            boxes[0].primitives.add(new Triangle(boxes[0], 6, 1, 5));
            boxes[0].primitives.add(new Triangle(boxes[0], 6, 2, 1));
            boxes[0].primitives.add(new Triangle(boxes[0], 4, 3, 7));
            boxes[0].primitives.add(new Triangle(boxes[0], 4, 0, 3));
            */


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

            BoundingBox.buildBox(boxes[1], boxes[1].points, boxes[1].triangles);
            /*
            boxes[1].primitives.add(new Triangle(boxes[1], 0, 1, 2));
            boxes[1].primitives.add(new Triangle(boxes[1], 2, 3, 0));
            boxes[1].primitives.add(new Triangle(boxes[1], 6, 7, 4));
            boxes[1].primitives.add(new Triangle(boxes[1], 6, 4, 5));
            boxes[1].primitives.add(new Triangle(boxes[1], 4, 5, 1));
            boxes[1].primitives.add(new Triangle(boxes[1], 4, 1, 0));
            boxes[1].primitives.add(new Triangle(boxes[1], 3, 2, 6));
            boxes[1].primitives.add(new Triangle(boxes[1], 3, 6, 7));
            boxes[1].primitives.add(new Triangle(boxes[1], 6, 1, 5));
            boxes[1].primitives.add(new Triangle(boxes[1], 6, 2, 1));
            boxes[1].primitives.add(new Triangle(boxes[1], 4, 3, 7));
            boxes[1].primitives.add(new Triangle(boxes[1], 4, 0, 3));
            */
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

            BoundingBox.buildBox(boxes[0], boxes[0].points, boxes[0].triangles);
            /*
            boxes[0].primitives.add(new Triangle(boxes[0], 0, 1, 2));
            boxes[0].primitives.add(new Triangle(boxes[0], 2, 3, 0));
            boxes[0].primitives.add(new Triangle(boxes[0], 6, 7, 4));
            boxes[0].primitives.add(new Triangle(boxes[0], 6, 4, 5));
            boxes[0].primitives.add(new Triangle(boxes[0], 4, 5, 1));
            boxes[0].primitives.add(new Triangle(boxes[0], 4, 1, 0));
            boxes[0].primitives.add(new Triangle(boxes[0], 3, 2, 6));
            boxes[0].primitives.add(new Triangle(boxes[0], 3, 6, 7));
            boxes[0].primitives.add(new Triangle(boxes[0], 6, 1, 5));
            boxes[0].primitives.add(new Triangle(boxes[0], 6, 2, 1));
            boxes[0].primitives.add(new Triangle(boxes[0], 4, 3, 7));
            boxes[0].primitives.add(new Triangle(boxes[0], 4, 0, 3));
            */
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

            BoundingBox.buildBox(boxes[1], boxes[1].points, boxes[1].triangles);
            /*
            boxes[1].primitives.add(new Triangle(boxes[1], 0, 1, 2));
            boxes[1].primitives.add(new Triangle(boxes[1], 2, 3, 0));
            boxes[1].primitives.add(new Triangle(boxes[1], 6, 7, 4));
            boxes[1].primitives.add(new Triangle(boxes[1], 6, 4, 5));
            boxes[1].primitives.add(new Triangle(boxes[1], 4, 5, 1));
            boxes[1].primitives.add(new Triangle(boxes[1], 4, 1, 0));
            boxes[1].primitives.add(new Triangle(boxes[1], 3, 2, 6));
            boxes[1].primitives.add(new Triangle(boxes[1], 3, 6, 7));
            boxes[1].primitives.add(new Triangle(boxes[1], 6, 1, 5));
            boxes[1].primitives.add(new Triangle(boxes[1], 6, 2, 1));
            boxes[1].primitives.add(new Triangle(boxes[1], 4, 3, 7));
            boxes[1].primitives.add(new Triangle(boxes[1], 4, 0, 3));
            */
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

            BoundingBox.buildBox(boxes[0], boxes[0].points, boxes[0].triangles);

            /*
            boxes[0].primitives.add(new Triangle(boxes[0], 0, 1, 2));
            boxes[0].primitives.add(new Triangle(boxes[0], 2, 3, 0));
            boxes[0].primitives.add(new Triangle(boxes[0], 6, 7, 4));
            boxes[0].primitives.add(new Triangle(boxes[0], 6, 4, 5));
            boxes[0].primitives.add(new Triangle(boxes[0], 4, 5, 1));
            boxes[0].primitives.add(new Triangle(boxes[0], 4, 1, 0));
            boxes[0].primitives.add(new Triangle(boxes[0], 3, 2, 6));
            boxes[0].primitives.add(new Triangle(boxes[0], 3, 6, 7));
            boxes[0].primitives.add(new Triangle(boxes[0], 6, 1, 5));
            boxes[0].primitives.add(new Triangle(boxes[0], 6, 2, 1));
            boxes[0].primitives.add(new Triangle(boxes[0], 4, 3, 7));
            boxes[0].primitives.add(new Triangle(boxes[0], 4, 0, 3));
            */

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

            BoundingBox.buildBox(boxes[1], boxes[1].points, boxes[1].triangles);

            /*
            boxes[1].primitives.add(new Triangle(boxes[1], 0, 1, 2));
            boxes[1].primitives.add(new Triangle(boxes[1], 2, 3, 0));
            boxes[1].primitives.add(new Triangle(boxes[1], 6, 7, 4));
            boxes[1].primitives.add(new Triangle(boxes[1], 6, 4, 5));
            boxes[1].primitives.add(new Triangle(boxes[1], 4, 5, 1));
            boxes[1].primitives.add(new Triangle(boxes[1], 4, 1, 0));
            boxes[1].primitives.add(new Triangle(boxes[1], 3, 2, 6));
            boxes[1].primitives.add(new Triangle(boxes[1], 3, 6, 7));
            boxes[1].primitives.add(new Triangle(boxes[1], 6, 1, 5));
            boxes[1].primitives.add(new Triangle(boxes[1], 6, 2, 1));
            boxes[1].primitives.add(new Triangle(boxes[1], 4, 3, 7));
            boxes[1].primitives.add(new Triangle(boxes[1], 4, 0, 3));
            */
            for (Triangle tr: boxes[1].triangles){
                tr.updateNomrs();
            }
            for (Triangle tr: boxes[1].triangles){
                tr.doNormalizeSt();
            }
        }

        for(Primitive tr: boundingBox.innerPrimitives){
            if (plane.equals("X"))
                if (isInLeftPart(boxes[0].maxX, tr, plane))
                    boxes[0].innerPrimitives.add(tr);
                if (isInRightPart(boxes[0].maxX, tr, plane))
                    boxes[1].innerPrimitives.add(tr);
            if (plane.equals("Y"))
                if (isInLeftPart(boxes[0].maxY, tr, plane))
                    boxes[0].innerPrimitives.add(tr);
                if (isInRightPart(boxes[0].maxY, tr, plane))
                    boxes[1].innerPrimitives.add(tr);
            if (plane.equals("Z"))
                if (isInLeftPart(boxes[0].maxZ, tr, plane))
                    boxes[0].innerPrimitives.add(tr);
                if (isInRightPart(boxes[0].maxZ, tr, plane))
                    boxes[1].innerPrimitives.add(tr);

            /*
            if (isInBoundingBox(boxes[0], tr, plane)) boxes[0].innerPrimitives.add(tr);
            if (isInBoundingBox(boxes[1], tr, plane)) boxes[1].innerPrimitives.add(tr);
            */
        }

        root.addLeft(boxes[0]);
        root.addRight(boxes[1]);

        return boxes;
    }

    //public static boolean isInLeftSide(BoundingBox box, Triangle tr){


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

    public static boolean isInLeftPart(float coord, Primitive pr, String plane){
        if (plane.equals("X")){
            if (pr.getMinX() <= coord)
                return true;
            else
                return false;
        }else if (plane.equals("Y")){
            if (pr.getMinY() <= coord)
                return true;
            else
                return false;
        }else if (plane.equals("Z")){
            if (pr.getMinZ() <= coord)
                return true;
            else
                return false;
        }
        return false;
    }

    public static boolean isInRightPart(float coord, Primitive pr, String plane){
        if (plane.equals("X")){
            if (pr.getMaxX() >= coord)
                return true;
            else
                return false;
        }else if (plane.equals("Y")){
            if (pr.getMaxY() >= coord)
                return true;
            else
                return false;
        }else if (plane.equals("Z")){
            if (pr.getMaxZ() >= coord)
                return true;
            else
                return false;
        }
        return false;
    }

}
