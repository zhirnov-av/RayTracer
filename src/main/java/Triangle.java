import base.Color;
import base.Point3D;

public class Triangle implements Comparable{
    public int v1;
    public int v2;
    public int v3;
    private Scene scene;
    private Color color;
    public Point3D n;
    public double distanceToCamera;

    Point3D pointV1;
    Point3D pointV2;
    Point3D pointV3;

    public Triangle(Scene scene, int v1, int v2, int v3) {
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
        this.scene = scene;
        this.n = computeNormal();
        this.color = new Color(255, 255, 255);
        this.pointV1 = scene.points.get(v1);
        this.pointV2 = scene.points.get(v2);
        this.pointV3 = scene.points.get(v3);

        this.distanceToCamera = this.pointV1.subtract(scene.camera).getLength();
    }

    public Triangle(Scene scene, int v1, int v2, int v3, Color color) {
        this(scene, v1, v2, v3);
        this.color = color;
    }

    public Point3D computeNormal(){
        Point3D a1 = scene.points.get(this.v2).subtract(scene.points.get(this.v1));
        Point3D b1 = scene.points.get(this.v3).subtract(scene.points.get(this.v1));
        Point3D n = a1.vMultiply(b1);
        double nLength = n.getLength();
        n.x = n.x / nLength;
        n.y = n.y / nLength;
        n.z = n.z / nLength;

        return n;
    }


    public Point3D getNormal(){
        return this.n;
    }

    public Point3D getIntersection(Point3D point1, Point3D point2, Point3D w){
        Point3D n = this.n;
        Point3D v = pointV1.subtract(point1);
        double d = n.sMultiply(v);
        double e = n.sMultiply(w);
        if (e != 0){
            return point1.add(w.multiply(d/e));
        }else if( d == 0){
            return null;// Прямая на плоскости
        }else{
            return null; // не пересекается
        }
    }

    public boolean isPointIn(Point3D point){
        Point3D tmp1 = scene.points.get(this.v2).subtract(scene.points.get(this.v1));
        Point3D tmp2 = scene.points.get(this.v2).subtract(point);
        Point3D v1 = tmp1.vMultiply(tmp2);

        tmp1 = scene.points.get(this.v3).subtract(scene.points.get(this.v2));
        tmp2 = scene.points.get(this.v3).subtract(point);
        Point3D v2 = tmp1.vMultiply(tmp2);

        tmp1 = scene.points.get(this.v1).subtract(scene.points.get(this.v3));
        tmp2 = scene.points.get(this.v1).subtract(point);
        Point3D v3 = tmp1.vMultiply(tmp2);

        return ((v1.sMultiply(v2)*v1.sMultiply(v3)) > 0.0d);
    }

    public boolean isPointInV2(Point3D point){
        Point3D v1 = pointV2.subtract(pointV1).vMultiply(point.subtract(pointV1));
        double a1 = v1.sMultiply(getNormal());
        if (a1 < 0d) return false;

        Point3D v2 = point.subtract(pointV1).vMultiply(pointV3.subtract(pointV1));
        double a2 = v2.sMultiply(getNormal());
        if (a2 < 0d) return false;

        Point3D v3 = pointV2.subtract(point).vMultiply(pointV3.subtract(point));
        double a3 = v3.sMultiply(getNormal());
        return !(a3 < 0d);

        /*
        Point3D tmp1 = pointV2.subtract(pointV1);
        Point3D tmp2 = point.subtract(pointV1);
        Point3D v1 = tmp1.vMultiply(tmp2);
        double a1 = v1.sMultiply(getNormal());
        if (a1 < 0d) return false;

        tmp1 = point.subtract(pointV1);
        tmp2 = pointV3.subtract(pointV1);
        Point3D v2 = tmp1.vMultiply(tmp2);
        double a2 = v2.sMultiply(getNormal());
        if (a2 < 0d) return false;

        tmp1 = pointV2.subtract(point);
        tmp2 = pointV3.subtract(point);
        Point3D v3 = tmp1.vMultiply(tmp2);
        double a3 = v3.sMultiply(getNormal());
        return !(a3 < 0d);
        */
    }


    public Color getColor() {
        return color;
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof Triangle){
            double d1 = ((Triangle)o).distanceToCamera;
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
        temp = Double.doubleToLongBits(distanceToCamera);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
