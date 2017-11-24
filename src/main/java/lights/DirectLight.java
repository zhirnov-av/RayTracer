package lights;


import base.Point3d;

public class DirectLight extends Light {

    Point3d target;

    public DirectLight(double intensity) {
        super(intensity, LightTypes.DIRECTED);
        this.target = new Point3d();
    }
    public DirectLight(double intensity, Point3d target) {
        super(intensity, LightTypes.DIRECTED);
        this.target = target;
    }

    public Point3d getTarget() {
        return target;
    }
}
