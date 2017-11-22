public class Triangle {
    public int a;
    public int b;
    public int c;
    private Scene scene;
    private Color color;

    public Triangle(Scene scene, int a, int b, int c) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.scene = scene;

        this.color = new Color(255, 255, 255);
    }

    public Triangle(Scene scene, int a, int b, int c, Color color) {
        this(scene, a, b, c);
        this.color = color;
    }



    public Point3D getNormal(){
        Point3D a1 = scene.points.get(this.b).subtract(scene.points.get(this.a));
        Point3D b1 = scene.points.get(this.c).subtract(scene.points.get(this.a));
        Point3D n = a1.vMultiply(b1);
        double nLength = n.getLength();
        n.x = n.x / nLength;
        n.y = n.y / nLength;
        n.z = n.z / nLength;

        return n;
    }

    public Point3D getIntersection(Point3D point1, Point3D point2){
        Point3D n = getNormal();
        Point3D v = scene.points.get(this.a).subtract(point1);
        double d = n.sMultiply(v);

        //if (d >= 0)
        //    return null;

        Point3D w = point2.subtract(point1);
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
        Point3D tmp1 = scene.points.get(this.b).subtract(scene.points.get(this.a));
        Point3D tmp2 = scene.points.get(this.b).subtract(point);
        Point3D v1 = tmp1.vMultiply(tmp2);

        tmp1 = scene.points.get(this.c).subtract(scene.points.get(this.b));
        tmp2 = scene.points.get(this.c).subtract(point);
        Point3D v2 = tmp1.vMultiply(tmp2);

        tmp1 = scene.points.get(this.a).subtract(scene.points.get(this.c));
        tmp2 = scene.points.get(this.a).subtract(point);
        Point3D v3 = tmp1.vMultiply(tmp2);

        return ((v1.sMultiply(v2)*v1.sMultiply(v3)) > 0.0d);
    }

    public boolean isPointInV2(Point3D point){
        Point3D tmp1 = scene.points.get(this.b).subtract(scene.points.get(this.a));
        Point3D tmp2 = point.subtract(scene.points.get(this.a));
        Point3D v1 = tmp1.vMultiply(tmp2);
        double a1 = v1.sMultiply(getNormal());
        if (a1 < 0d) return false;

        tmp1 = point.subtract(scene.points.get(this.a));
        tmp2 = scene.points.get(this.c).subtract(scene.points.get(this.a));
        Point3D v2 = tmp1.vMultiply(tmp2);
        double a2 = v2.sMultiply(getNormal());
        if (a2 < 0d) return false;

        tmp1 = scene.points.get(this.b).subtract(point);
        tmp2 = scene.points.get(this.c).subtract(point);
        Point3D v3 = tmp1.vMultiply(tmp2);
        double a3 = v3.sMultiply(getNormal());
        if (a3 < 0d) return false;

        return true;
    }


    public Color getColor() {
        return color;
    }
}
