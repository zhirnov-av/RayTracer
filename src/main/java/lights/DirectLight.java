package lights;


import base.Vector3d;
import base.Vertex3d;

public class DirectLight extends Light {

    Vector3d target;

    public DirectLight(double intensity) {
        super(intensity, LightTypes.DIRECTED);
        this.target = new Vector3d();
    }
    public DirectLight(double intensity, Vector3d target) {
        super(intensity, LightTypes.DIRECTED);
        this.target = target;
    }

    public Vector3d getTarget() {
        return target;
    }
}
