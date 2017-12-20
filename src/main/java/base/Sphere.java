package base;

public class Sphere extends Primitive implements Comparable{
    Vector3d position;
    float radius;

    public Sphere(Vector3d position, float radius, AbstractObject object){
        this.position = position;
        this.radius = radius;
        this.object = object;
        this.color = new Color(255, 100, 255);
    }

    public Color getIntersectionColor(Vector3d from, Vector3d to){
        if (getIntersection(from, to) != null)
            return color;
        else
            return null;

    }

    @Override
    public Vector3d getNormalInPoint(Vector3d point) {

        Vector3d n = MathUtil.subtract(point, position);

        float nLength = MathUtil.module(n);
        n.x = n.x / nLength;
        n.y = n.y / nLength;
        n.z = n.z / nLength;

        return n;
    }

    public Vector3d getIntersection(Vector3d from, Vector3d to){
        Vector3d center = position;
        float r = radius;

        float a = to.x * to.x + to.y * to.y + to.z * to.z;
        float b = 2 * (to.x * (from.x - center.x) + to.y * (from.y - center.y) + to.z * (from.z - center.z));
        float c = center.x * center.x
                + center.y * center.y
                + center.z * center.z
                + from.x * from.x
                + from.y * from.y
                + from.z * from.z
                - 2 * (from.x * center.x
                + from.y * center.y
                + from.z * center.z)
                - r * r;
        float D = b * b - 4 * a * c;
        if (D < 0)
            return null;

        float sqrt_D = (float)(Math.sqrt(D));
        float a_2 = 2 * a;

        float t1 = (-b + sqrt_D) / a_2;
        float t2 = (-b - sqrt_D) / a_2;

        float min_t = (t1 < t2) ? t1 : t2;
        float max_t = (t1 > t2) ? t1 : t2;

        float t = (min_t > 0.00001) ? min_t : max_t;

        if (t < 0.00001) return null;

        return new Vector3d(from.x + t * to.x,
                    from.y + t * to.y,
                    from.z + t * to.z);

    }

    @Override
    public float getMinX() {
        return position.x - radius;
    }

    @Override
    public float getMaxX() {
        return position.x + radius;
    }

    @Override
    public float getMinY() {
        return position.y - radius;
    }

    @Override
    public float getMaxY() {
        return position.y + radius;
    }

    @Override
    public float getMinZ() {
        return position.z - radius;
    }

    @Override
    public float getMaxZ() {
        return position.z + radius;
    }

    @Override
    public int compareTo(Object o) {
        return 1;
    }
}
