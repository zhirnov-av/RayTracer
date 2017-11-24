import base.Color;
import base.Point3d;

public class Triangle implements Comparable{
    public int v1;
    public int v2;
    public int v3;
    private Scene scene;
    private Color color;
    public Point3d n;
    public float distanceToCamera;

    Point3d pv1;
    Point3d pv2;
    Point3d pv3;

    Point3d v_p1_p2;
    Point3d v_p2_p3;
    Point3d v_p3_p1;

    Point3d n1;
    Point3d n2;
    Point3d n3;

    float d;


    public Triangle(Scene scene, int v1, int v2, int v3) {
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
        this.scene = scene;

        this.n = computeNormal();
        
        this.color = new Color(255, 255, 255);

        this.pv1 = scene.points.get(v1);
        this.pv2 = scene.points.get(v2);
        this.pv3 = scene.points.get(v3);

        this.pv1.nList.add(new Point3d(this.n));
        this.pv2.nList.add(new Point3d(this.n));
        this.pv3.nList.add(new Point3d(this.n));

        this.d = -(pv1.x * n.x + pv1.y * n.y + pv1.z * n.z);

        //////////////////////////////////////
        //this.pv1.nx += this.n.x;
        //this.pv1.ny += this.n.y;
        //this.pv1.nz += this.n.z;

        //Point3d tmp = new Point3d(this.pv1.nx, this.pv1.ny, this.pv1.nz);
        //float module = tmp.getLength();

        //this.pv1.nx /= module;
        //this.pv1.ny /= module;
        //this.pv1.nz /= module;
        
        //n1 = new Point3d(this.pv1.nx, this.pv1.ny, this.pv1.nz);

        //////////////////////////////////////
        //this.pv2.nx += this.n.x;
        //this.pv2.ny += this.n.y;
        //this.pv2.nz += this.n.z;

        //module = new Point3d(this.pv2.nx, this.pv2.ny, this.pv2.nz).getLength();

        //this.pv2.nx /= module;
        //this.pv2.ny /= module;
        //this.pv2.nz /= module;

        //n2 = new Point3d(this.pv2.nx, this.pv2.ny, this.pv2.nz);

        //////////////////////////////////////
        //this.pv3.nx += this.n.x;
        //this.pv3.ny += this.n.y;
        //this.pv3.nz += this.n.z;

        //tmp = new Point3d(this.pv3.nx, this.pv3.ny, this.pv3.nz);
        //module = tmp.getLength();

        //this.pv3.nx /= module;
        //this.pv3.ny /= module;
        //this.pv3.nz /= module;

        //n3 = new Point3d(this.pv3.nx, this.pv3.ny, this.pv3.nz);

        this.v_p1_p2 = pv1.subtract(pv2);
        this.v_p2_p3 = pv2.subtract(pv3);
        this.v_p3_p1 = pv3.subtract(pv1);

        this.distanceToCamera = this.pv1.subtract(scene.camera).getLength();
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
        n1 = new Point3d(0f, 0f, 0f);
        for (Point3d p: pv1.nList){
            n1.x += p.x;
            n1.y += p.y;
            n1.z += p.z;
        }
        float length = n1.getLength();
        n1.x /= length;
        n1.y /= length;
        n1.z /= length;

        n2 = new Point3d(0f, 0f, 0f);
        for (Point3d p: pv2.nList){
            n2.x += p.x;
            n2.y += p.y;
            n2.z += p.z;
        }
        length = n2.getLength();
        n2.x /= length;
        n2.y /= length;
        n2.z /= length;

        n3 = new Point3d(0f, 0f, 0f);
        for (Point3d p: pv3.nList){
            n3.x += p.x;
            n3.y += p.y;
            n3.z += p.z;
        }
        length = n3.getLength();
        n3.x /= length;
        n3.y /= length;
        n3.z /= length;
    }

    public Triangle(Scene scene, int v1, int v2, int v3, Color color) {
        this(scene, v1, v2, v3);
        this.color = color;
    }

    public Point3d computeNormal(){
        Point3d a1 = scene.points.get(this.v2).subtract(scene.points.get(this.v1));
        Point3d b1 = scene.points.get(this.v3).subtract(scene.points.get(this.v1));
        Point3d n = a1.vMultiply(b1);
        float nLength = n.getLength();
        n.x = n.x / nLength;
        n.y = n.y / nLength;
        n.z = n.z / nLength;

        return n;
    }


    public Point3d getNormal(){
        return this.n;
    }

    public Point3d getIntersection(Point3d point1, Point3d point2, Point3d w){
        Point3d n = this.n;
        Point3d v = pv1.subtract(point1);

        float sProd = n.sMultiply(point2);
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

        Point3d intersection = new Point3d(x, y, z);

        if (checkSameClockDir(v_p1_p2, intersection.subtract(pv1), n)
                && checkSameClockDir(v_p2_p3, intersection.subtract(pv2), n)
                && checkSameClockDir(v_p3_p1, intersection.subtract(pv3), n)){
            return intersection;
        }
        return null;
        /*
        float d = n.sMultiply(v);
        float e = n.sMultiply(w);
        if (e != 0){
            return point1.add(w.multiply(d/e));
        }else if( d == 0){
            return null;// Прямая на плоскости
        }else{
            return null; // не пересекается
        }
        */
    }

    public Point3d getIntersectionV2(Point3d point1, Point3d point2, Point3d w){
        Point3d n = this.n;
        Point3d v = pv1.subtract(point1);
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

    public boolean isPointIn(Point3d point){


        return false;
    }

    public boolean checkSameClockDir(Point3d v1, Point3d v2, Point3d norm) {
        Point3d norm_v1_v2 = v2.vMultiply(v1);
        if(norm_v1_v2.sMultiply(norm) < 0)
            return false;
        else
            return true;
    }


    public boolean isPointInV2(Point3d point){
        Point3d v1 = pv2.subtract(pv1).vMultiply(point.subtract(pv1));
        float a1 = v1.sMultiply(getNormal());
        if (a1 < 0d) return false;

        Point3d v2 = point.subtract(pv1).vMultiply(pv3.subtract(pv1));
        float a2 = v2.sMultiply(getNormal());
        if (a2 < 0d) return false;

        Point3d v3 = pv2.subtract(point).vMultiply(pv3.subtract(point));
        float a3 = v3.sMultiply(getNormal());
        return !(a3 < 0d);

        /*
        Point3d tmp1 = pv2.subtract(pv1);
        Point3d tmp2 = point.subtract(pv1);
        Point3d v1 = tmp1.vMultiply(tmp2);
        float a1 = v1.sMultiply(getNormal());
        if (a1 < 0d) return false;

        tmp1 = point.subtract(pv1);
        tmp2 = pv3.subtract(pv1);
        Point3d v2 = tmp1.vMultiply(tmp2);
        float a2 = v2.sMultiply(getNormal());
        if (a2 < 0d) return false;

        tmp1 = pv2.subtract(point);
        tmp2 = pv3.subtract(point);
        Point3d v3 = tmp1.vMultiply(tmp2);
        float a3 = v3.sMultiply(getNormal());
        return !(a3 < 0d);
        */
    }

    public Point3d getNormalInPointV2(Point3d point){

        Point3d v_p1_p = point.subtract(this.pv1);
        Point3d v_p2_p = point.subtract(this.pv2);
        Point3d v_p3_p = point.subtract(this.pv3);

        float s1 = v_p1_p.getLength(); // module_vector(cross_product(v_p2_p, tr->v_p2_p3));
        float s2 = v_p2_p.getLength(); // module_vector(cross_product(v_p3_p, tr->v_p3_p1));
        float s3 = v_p3_p.getLength(); // module_vector(cross_product(v_p1_p, tr->v_p1_p2));

        float s_sum = s1 + s2 + s3;

        float w1 = s1 / s_sum;
        float w2 = s2 / s_sum;
        float w3 = s3 / s_sum;

        Point3d n = new Point3d(w1 * n1.x + w2 * n2.x + w3 * n3.x, w1 * n1.y + w2 * n2.y + w3 * n3.y, w1 * n1.z + w2 * n2.z + w3 * n3.z);
        return n;
    }

    public Point3d getNormalInPoint(Point3d point){

        /*
        Point3d v_p1_p = this.pv1.subtract(point);
        Point3d v_p2_p = this.pv2.subtract(point);
        Point3d v_p3_p = this.pv3.subtract(point);
        */
        Point3d v_p1_p = point.subtract(this.pv1);
        Point3d v_p2_p = point.subtract(this.pv2);
        Point3d v_p3_p = point.subtract(this.pv3);

        float s1 = v_p2_p.vMultiply(v_p2_p3).getLength(); // module_vector(cross_product(v_p2_p, tr->v_p2_p3));
        float s2 = v_p3_p.vMultiply(v_p3_p1).getLength(); // module_vector(cross_product(v_p3_p, tr->v_p3_p1));
        float s3 = v_p1_p.vMultiply(v_p1_p2).getLength(); // module_vector(cross_product(v_p1_p, tr->v_p1_p2));

        float s_sum = s1 + s2 + s3;

        float w1 = s1 / s_sum;
        float w2 = s2 / s_sum;
        float w3 = s3 / s_sum;

        return new Point3d(w1 * n1.x + w2 * n2.x + w3 * n3.x, w1 * n1.y + w2 * n2.y + w3 * n3.y, w1 * n1.z + w2 * n2.z + w3 * n3.z);
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
