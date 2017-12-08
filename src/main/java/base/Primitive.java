package base;

public abstract class Primitive {
    public AbstractObject object;
    private Scene scene;
    private Color color;

    public Color getColor(){
        return color;
    }
    public abstract Vector3d getNormalInPoint(Vector3d point);
    public abstract Vector3d getIntersection(Vector3d from, Vector3d to);
}
