public class Point3D {
    public double x = 0;
    public double y = 0;
    public double z = 0;

    public Point3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Point3D subtract(Point3D point){
        return new Point3D(this.x - point.x, this.y - point.y, this.z - point.z);
    }

    public Point3D add(Point3D point){
        return new Point3D(this.x + point.x, this.y + point.y, this.z + point.z);
    }



    public Point3D vMultiply(Point3D point){
        return new Point3D(this.y * point.z - this.z * point.y, this.z * point.x - this.x * point.z, this.x * point.y - this.y * point.x);
    }

    public double sMultiply(Point3D point){
        return this.x * point.x + this.y * point.y + this.z * point.z;
    }

    public Point3D multiply(double v){
        return new Point3D(this.x * v, this.y * v, this.z * v);
    }


    public double getLength(){
        return Math.sqrt(Math.abs(this.x * this.x + this.y * this.y + this.z * this.z));
    }

    @Override
    public String toString() {
        return "Point3D{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }
}
