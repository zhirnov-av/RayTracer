
import base.Color;
import base.Vertex3d;

public class Triangle3D {
    public Vertex3d a;
    public Vertex3d b;
    public Vertex3d c;
    private Color color;

    public Triangle3D(Vertex3d a, Vertex3d b, Vertex3d c) {
        this.a = a;
        this.b = b;
        this.c = c;

        color = new Color(255, 255, 255);
    }

    public Triangle3D(Vertex3d a, Vertex3d b, Vertex3d c, Color color) {
        this(a, b, c);
        this.color = color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
