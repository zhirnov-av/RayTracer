package base;

import java.util.HashSet;

public class Vertex3d {

    public Vector3d p;
    public Vector3d n;

    public HashSet<Vector3d> nList = new HashSet<>();

    public Vertex3d(float x, float y, float z) {
        this.p = new Vector3d(x, y, z);
    }

    public Vertex3d(){
        this.p = new Vector3d(0f, 0f, 0f);
    }

    public Vertex3d(Vector3d p){
        this.p = new Vector3d(p.x, p.y, p.z);
    }

    @Deprecated
    public Vertex3d subtract(Vertex3d point){
        return new Vertex3d(MathUtil.subtract(this.p, point.p));
    }

    @Deprecated
    public Vertex3d add(Vertex3d point){
        return new Vertex3d(MathUtil.add(this.p, point.p));
    }

    @Deprecated
    public Vertex3d vMultiply(Vertex3d point){
        return new Vertex3d(MathUtil.crossProduct(this.p, point.p));
    }

    @Deprecated
    public float sMultiply(Vertex3d point){
        return MathUtil.dotProduct(this.p, point.p);
    }

    @Deprecated
    public Vertex3d multiply(float v){
        return new Vertex3d(MathUtil.multiply(this.p, v));
    }

    @Deprecated
    public float getLength(){
        return MathUtil.module(this.p);
    }

    @Override
    public String toString() {
        return "base.Vertex3d{" +
                "x=" + p.x +
                ", y=" + p.y +
                ", z=" + p.z +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vertex3d vertex3D = (Vertex3d) o;

        if (p.x == -0f) p.x = 0f;
        if (p.y == -0f) p.y = 0f;
        if (p.z == -0f) p.z = 0f;

        if (vertex3D.p.x == -0f) vertex3D.p.x = 0f;
        if (vertex3D.p.y == -0f) vertex3D.p.y = 0f;
        if (vertex3D.p.z == -0f) vertex3D.p.z = 0f;

        if (Float.compare(vertex3D.p.x, p.x) != 0) return false;
        if (Float.compare(vertex3D.p.y, p.y) != 0) return false;
        return Float.compare(vertex3D.p.z, p.z) == 0;
    }

    @Override
    public int hashCode() {
        int result = (p.x != +0.0f ? Float.floatToIntBits(p.x) : 0);
        result = 31 * result + (p.y != +0.0f ? Float.floatToIntBits(p.y) : 0);
        result = 31 * result + (p.z != +0.0f ? Float.floatToIntBits(p.z) : 0);
        return result;
    }
}
