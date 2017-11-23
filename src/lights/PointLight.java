package lights;


import base.Point3D;

public class PointLight extends Light{
    protected Point3D position;

    public PointLight(double intensity) {
        super(intensity, LightTypes.POINT);
        this.position = new Point3D();
    }
    public PointLight(double intensity, Point3D point) {
        super(intensity, LightTypes.POINT);
        this.position = point;
    }

    public Point3D getPosition() {
        return position;
    }
}
