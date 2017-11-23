package lights;


import base.Point3d;

public class PointLight extends Light{
    protected Point3d position;

    public PointLight(double intensity) {
        super(intensity, LightTypes.POINT);
        this.position = new Point3d();
    }
    public PointLight(double intensity, Point3d point) {
        super(intensity, LightTypes.POINT);
        this.position = point;
    }

    public Point3d getPosition() {
        return position;
    }
}
