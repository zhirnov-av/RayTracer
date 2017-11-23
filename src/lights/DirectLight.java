package lights;


import base.Point3D;

public class DirectLight extends Light {

    Point3D target;

    public DirectLight(double intensity) {
        super(intensity, LightTypes.DIRECTED);
        this.target = new Point3D();
    }
    public DirectLight(double intensity, Point3D target) {
        super(intensity, LightTypes.DIRECTED);
        this.target = target;
    }

    public Point3D getTarget() {
        return target;
    }
}
