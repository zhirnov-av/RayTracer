import base.Color;
import base.Point3d;

public class Triangle3D {
    public Point3d a;
    public Point3d b;
    public Point3d c;
    private Color color;

    public Triangle3D(Point3d a, Point3d b, Point3d c) {
        this.a = a;
        this.b = b;
        this.c = c;

        color = new Color(255, 255, 255);
    }

    public Triangle3D(Point3d a, Point3d b, Point3d c, Color color) {
        this(a, b, c);
        this.color = color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
