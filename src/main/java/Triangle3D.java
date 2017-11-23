import base.Color;
import base.Point3D;

public class Triangle3D {
    public Point3D a;
    public Point3D b;
    public Point3D c;
    private Color color;

    public Triangle3D(Point3D a, Point3D b, Point3D c) {
        this.a = a;
        this.b = b;
        this.c = c;

        color = new Color(255, 255, 255);
    }

    public Triangle3D(Point3D a, Point3D b, Point3D c, Color color) {
        this(a, b, c);
        this.color = color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
