package base;

public class Point3d {
    public double x = 0;
    public double y = 0;
    public double z = 0;

    public double nx = 0;
    public double ny = 0;
    public double nz = 0;

    public Point3d(double x, double y, double z) {
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

    public double sMultiply(Point3d point){
        return this.x * point.x + this.y * point.y + this.z * point.z;
    }

    public Point3d multiply(double v){
        return new Point3d(this.x * v, this.y * v, this.z * v);
    }


    public double getLength(){
        return Math.sqrt(Math.abs(this.x * this.x + this.y * this.y + this.z * this.z));
    }

    @Override
    public String toString() {
        return "base.Point3d{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }
}
