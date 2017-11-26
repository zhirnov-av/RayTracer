import base.Color;
import base.MathUtil;
import base.Vector3d;
import base.Vertex3d;

public class Triangle implements Comparable{
    public int v1;
    public int v2;
    public int v3;
    private Object object;
    private Scene scene;
    private Color color;
    public Vector3d n;
    public float distanceToCamera;

    Vertex3d pv1;
    Vertex3d pv2;
    Vertex3d pv3;

    Vector3d v_p1_p2;
    Vector3d v_p2_p3;
    Vector3d v_p3_p1;

    Vector3d n1;
    Vector3d n2;
    Vector3d n3;

    float d;


    public Triangle(Object3d object, int v1, int v2, int v3) {
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
        this.object = object;
        this.scene = object.scene;

        this.color = new Color(255, 255, 255);

        this.pv1 = object.points.get(v1);
        this.pv2 = object.points.get(v2);
        this.pv3 = object.points.get(v3);

        this.n = computeNormal();

        this.pv1.nList.add(new Vector3d(this.n));
        this.pv2.nList.add(new Vector3d(this.n));
        this.pv3.nList.add(new Vector3d(this.n));

        this.d = -(pv1.p.x * n.x + pv1.p.y * n.y + pv1.p.z * n.z);

        //////////////////////////////////////
        //this.pv1.nx += this.n.x;
        //this.pv1.ny += this.n.y;
        //this.pv1.nz += this.n.z;

        //Vertex3d tmp = new Vertex3d(this.pv1.nx, this.pv1.ny, this.pv1.nz);
        //float module = tmp.getLength();

        //this.pv1.nx /= module;
        //this.pv1.ny /= module;
        //this.pv1.nz /= module;
        
        //n1 = new Vertex3d(this.pv1.nx, this.pv1.ny, this.pv1.nz);

        //////////////////////////////////////
        //this.pv2.nx += this.n.x;
        //this.pv2.ny += this.n.y;
        //this.pv2.nz += this.n.z;

        //module = new Vertex3d(this.pv2.nx, this.pv2.ny, this.pv2.nz).getLength();

        //this.pv2.nx /= module;
        //this.pv2.ny /= module;
        //this.pv2.nz /= module;

        //n2 = new Vertex3d(this.pv2.nx, this.pv2.ny, this.pv2.nz);

        //////////////////////////////////////
        //this.pv3.nx += this.n.x;
        //this.pv3.ny += this.n.y;
        //this.pv3.nz += this.n.z;

        //tmp = new Vertex3d(this.pv3.nx, this.pv3.ny, this.pv3.nz);
        //module = tmp.getLength();

        //this.pv3.nx /= module;
        //this.pv3.ny /= module;
        //this.pv3.nz /= module;

        //n3 = new Vertex3d(this.pv3.nx, this.pv3.ny, this.pv3.nz);

        this.v_p1_p2 = MathUtil.subtract(pv1.p, pv2.p);
        this.v_p2_p3 = MathUtil.subtract(pv2.p, pv3.p); // pv2.subtract(pv3);
        this.v_p3_p1 = MathUtil.subtract(pv3.p, pv1.p); // pv3.subtract(pv1);

        //this.distanceToCamera = this.pv1.subtract(scene.camera).getLength();
        Vector3d v = MathUtil.subtract(scene.camera, this.pv1.p);
        this.distanceToCamera = v.x * v.x + v.y * v.y + v.z * v.z;
    }

    public void updateNomrs(){
        /*
        n1.x = pv1.nx;
        n1.y = pv1.ny;
        n1.z = pv1.nz;

        n2.x = pv2.nx;
        n2.y = pv2.ny;
        n2.z = pv2.nz;

        n3.x = pv3.nx;
        n3.y = pv3.ny;
        n3.z = pv3.nz;
        */
    }

    public void doNormalize(){
        n1 = new Vector3d(0f, 0f, 0f);
        for (Vector3d p: pv1.nList){
            n1.x += p.x;
            n1.y += p.y;
            n1.z += p.z;
        }
        float length = MathUtil.module(n1); //.getLength();
        n1.x /= length;
        n1.y /= length;
        n1.z /= length;

        n2 = new Vector3d(0f, 0f, 0f);
        for (Vector3d p: pv2.nList){
            n2.x += p.x;
            n2.y += p.y;
            n2.z += p.z;
        }
        length = MathUtil.module(n2); // .getLength();
        n2.x /= length;
        n2.y /= length;
        n2.z /= length;

        n3 = new Vector3d(0f, 0f, 0f);
        for (Vector3d p: pv3.nList){
            n3.x += p.x;
            n3.y += p.y;
            n3.z += p.z;
        }
        length =  MathUtil.module(n3); //.getLength();
        n3.x /= length;
        n3.y /= length;
        n3.z /= length;
    }

    public Triangle(Object3d object, int v1, int v2, int v3, Color color) {
        this(object, v1, v2, v3);
        this.color = color;
    }

    public Vector3d computeNormal(){
        Vector3d a1 = MathUtil.subtract(pv2.p, pv1.p);
        Vector3d b1 = MathUtil.subtract(pv3.p, pv1.p);
        Vector3d n = MathUtil.crossProduct(a1, b1);
        float nLength = MathUtil.module(n);
        n.x = n.x / nLength;
        n.y = n.y / nLength;
        n.z = n.z / nLength;

        return n;
    }


    public Vector3d getNormal(){
        return this.n;
    }

    public Vector3d getIntersection(Vector3d point1, Vector3d point2, Vector3d w){
        Vector3d n = this.n;
        Vector3d v = MathUtil.subtract(pv1.p, point1);  // Vertex3d v = pv1.subtract(point1);
        float sProd = MathUtil.dotProduct(n, point2);   // float sProd = n.sMultiply(point2);
        if (Math.abs(sProd) < 0.0001f){
            return null;
        }

        float k = -(n.x * point1.x + n.y * point1.y + n.z * point1.z + this.d) / sProd;

        if (k < 0.0001f){
            return null;
        }
        float x = point1.x + point2.x * k;
        float y = point1.y + point2.y * k;
        float z = point1.z + point2.z * k;

        Vector3d intersection = new Vector3d(x, y, z);

        if (checkSameClockDir(v_p1_p2, MathUtil.subtract(intersection, pv1.p), n) //  intersection.subtract(pv1)
                && checkSameClockDir(v_p2_p3, MathUtil.subtract(intersection, pv2.p), n)
                && checkSameClockDir(v_p3_p1, MathUtil.subtract(intersection, pv3.p), n)){
            return intersection;
        }
        return null;
    }

    /*
    public Vertex3d getIntersectionV2(Vertex3d point1, Vertex3d point2, Vertex3d w){
        Vertex3d n = this.n;
        Vertex3d v = pv1.subtract(point1);
        float d = n.sMultiply(v);
        float e = n.sMultiply(w);
        if (e != 0){
            return point1.add(w.multiply(d/e));
        }else if( d == 0){
            return null;// Прямая на плоскости
        }else{
            return null; // не пересекается
        }
    }
    */

    public boolean isPointIn(Vertex3d point){


        return false;
    }

    public static boolean checkSameClockDir(Vector3d v1, Vector3d v2, Vector3d norm) {
        Vector3d norm_v1_v2 = MathUtil.crossProduct(v2, v1);    //  v2.vMultiply(v1);
        if( MathUtil.dotProduct(norm_v1_v2, norm) < 0)          //  norm_v1_v2.sMultiply(norm)
            return false;
        else
            return true;
    }

    /*
    public boolean isPointInV2(Vertex3d point){
        Vertex3d v1 = pv2.subtract(pv1).vMultiply(point.subtract(pv1));
        float a1 = v1.sMultiply(getNormal());
        if (a1 < 0d) return false;

        Vertex3d v2 = point.subtract(pv1).vMultiply(pv3.subtract(pv1));
        float a2 = v2.sMultiply(getNormal());
        if (a2 < 0d) return false;

        Vertex3d v3 = pv2.subtract(point).vMultiply(pv3.subtract(point));
        float a3 = v3.sMultiply(getNormal());
        return !(a3 < 0d);
    }
    */

    /*
    public Vertex3d getNormalInPointV2(Vector3d point){

        Vector3d v_p1_p = MathUtil.subtract(point, pv1.p); //point.subtract(this.pv1);
        Vector3d v_p2_p = MathUtil.subtract(point, pv2.p);
        Vector3d v_p3_p = MathUtil.subtract(point, pv3.p);

        float s1 = v_p1_p.getLength(); // module_vector(cross_product(v_p2_p, tr->v_p2_p3));
        float s2 = v_p2_p.getLength(); // module_vector(cross_product(v_p3_p, tr->v_p3_p1));
        float s3 = v_p3_p.getLength(); // module_vector(cross_product(v_p1_p, tr->v_p1_p2));

        float s_sum = s1 + s2 + s3;

        float w1 = s1 / s_sum;
        float w2 = s2 / s_sum;
        float w3 = s3 / s_sum;

        Vertex3d n = new Vertex3d(w1 * n1.x + w2 * n2.x + w3 * n3.x, w1 * n1.y + w2 * n2.y + w3 * n3.y, w1 * n1.z + w2 * n2.z + w3 * n3.z);
        return n;
    }
    */

    public Vector3d getNormalInPoint(Vector3d point){

        /*
        Vertex3d v_p1_p = this.pv1.subtract(point);
        Vertex3d v_p2_p = this.pv2.subtract(point);
        Vertex3d v_p3_p = this.pv3.subtract(point);
        */
        Vector3d v_p1_p = MathUtil.subtract(point, pv1.p); //point.subtract(this.pv1);
        Vector3d v_p2_p = MathUtil.subtract(point, pv2.p);
        Vector3d v_p3_p = MathUtil.subtract(point, pv3.p);

        float s1 = MathUtil.module(MathUtil.crossProduct(v_p2_p, v_p2_p3));
        float s2 = MathUtil.module(MathUtil.crossProduct(v_p3_p, v_p3_p1));
        float s3 = MathUtil.module(MathUtil.crossProduct(v_p1_p, v_p1_p2));

        //float s1 = v_p2_p.vMultiply(v_p2_p3).getLength(); // module_vector(cross_product(v_p2_p, tr->v_p2_p3));
        //float s2 = v_p3_p.vMultiply(v_p3_p1).getLength(); // module_vector(cross_product(v_p3_p, tr->v_p3_p1));
        //float s3 = v_p1_p.vMultiply(v_p1_p2).getLength(); // module_vector(cross_product(v_p1_p, tr->v_p1_p2));

        float s_sum = s1 + s2 + s3;

        float w1 = s1 / s_sum;
        float w2 = s2 / s_sum;
        float w3 = s3 / s_sum;

        return new Vector3d(w1 * n1.x + w2 * n2.x + w3 * n3.x, w1 * n1.y + w2 * n2.y + w3 * n3.y, w1 * n1.z + w2 * n2.z + w3 * n3.z);
        //return n;
    }


    public Color getColor() {
        return color;
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof Triangle){
            float d1 = ((Triangle)o).distanceToCamera;
            if (distanceToCamera < d1) return -1;
            else if (distanceToCamera > d1) return 1;
            else {
                if (((Triangle)o).equals(this))
                    return 0;
                else
                    return 1;
            }
        }else return 0;

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Triangle triangle = (Triangle) o;

        if (v1 != triangle.v1) return false;
        if (v2 != triangle.v2) return false;
        if (v3 != triangle.v3) return false;
        return Double.compare(triangle.distanceToCamera, distanceToCamera) == 0;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = v1;
        result = 31 * result + v2;
        result = 31 * result + v3;
        temp = Float.floatToIntBits(distanceToCamera);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
