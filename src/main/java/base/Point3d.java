package base;

import java.util.HashSet;

public class Point3d {
    public float x = 0;
    public float y = 0;
    public float z = 0;

    public float nx = 0;
    public float ny = 0;
    public float nz = 0;

    public HashSet<Point3d> nList = new HashSet<>();

    public Point3d(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Point3d(){
        this.x = 0;
        this.y = 0;
        this.z = 0;
    }

    public Point3d(Point3d p){
        this.x = p.x;
        this.y = p.y;
        this.z = p.z;
    }

    public Point3d subtract(Point3d point){
        return new Point3d(this.x - point.x, this.y - point.y, this.z - point.z);
    }

    public Point3d add(Point3d point){
        return new Point3d(this.x + point.x, this.y + point.y, this.z + point.z);
    }



    public Point3d vMultiply(Point3d point){
        return new Point3d(this.y * point.z - this.z * point.y, this.z * point.x - this.x * point.z, this.x * point.y - this.y * point.x);
    }

    public float sMultiply(Point3d point){
        return this.x * point.x + this.y * point.y + this.z * point.z;
    }

    public Point3d multiply(float v){
        return new Point3d(this.x * v, this.y * v, this.z * v);
    }


    public float getLength(){
        return (float)Math.sqrt(Math.abs(this.x * this.x + this.y * this.y + this.z * this.z));
    }

    @Override
    public String toString() {
        return "base.Point3d{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Point3d point3d = (Point3d) o;

        if (x == -0f) x = 0f;
        if (y == -0f) y = 0f;
        if (z == -0f) z = 0f;

        if (point3d.x == -0f) point3d.x = 0f;
        if (point3d.y == -0f) point3d.y = 0f;
        if (point3d.z == -0f) point3d.z = 0f;

        if (Float.compare(point3d.x, x) != 0) return false;
        if (Float.compare(point3d.y, y) != 0) return false;
        return Float.compare(point3d.z, z) == 0;
    }

    @Override
    public int hashCode() {
        int result = (x != +0.0f ? Float.floatToIntBits(x) : 0);
        result = 31 * result + (y != +0.0f ? Float.floatToIntBits(y) : 0);
        result = 31 * result + (z != +0.0f ? Float.floatToIntBits(z) : 0);
        return result;
    }
}
