package base;

public class Vector3d {
    public float x;
    public float y;
    public float z;

    public Vector3d(){
        this.x = 0f;
        this.y = 0f;
        this.z = 0f;
    }

    public Vector3d(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3d(Vector3d v) {
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
    }

    /*
    public Vector3d crossProduct(Vector3d v){
        x = y * v.z - z * v.y;
        y = z * v.x - x * v.z;
        z = x * v.y - y * v.x;
        return this;
    }

    public float dotProduct(Vector3d v){
        return x * v.x + y * v.y + z * v.z;
    }
    */

}
