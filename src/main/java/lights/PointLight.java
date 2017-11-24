package lights;


import base.Vector3d;
import base.Vertex3d;

public class PointLight extends Light{
    protected Vector3d position;

    public PointLight(double intensity) {
        super(intensity, LightTypes.POINT);
        this.position = new Vector3d();
    }
    public PointLight(double intensity, Vector3d point) {
        super(intensity, LightTypes.POINT);
        this.position = point;
    }

    public Vector3d getPosition() {
        return position;
    }
}
