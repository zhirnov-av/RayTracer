package base;

public abstract class Primitive {
    public AbstractObject object;
    protected Scene scene;
    protected Color color;

    public Color getColor(){
        return color;
    }
    public abstract Vector3d getNormalInPoint(Vector3d point);
    public abstract Vector3d getIntersection(Vector3d from, Vector3d to);

    public abstract float getMinX();
    public abstract float getMaxX();
    public abstract float getMinY();
    public abstract float getMaxY();
    public abstract float getMinZ();
    public abstract float getMaxZ();

}
