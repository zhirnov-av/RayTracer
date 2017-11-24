package base;

public class MathUtil {

    public static Vector3d crossProduct(Vector3d v1, Vector3d v2){
        Vector3d result = new Vector3d();
        result.x = v1.y * v2.z - v1.z * v2.y;
        result.y = v1.z * v2.x - v1.x * v2.z;
        result.z = v1.x * v2.y - v1.y * v2.x;

        return result;
    }

    public static float dotProduct(Vector3d v1, Vector3d v2){
        return v1.x * v2.x + v1.y * v2.y + v1.z * v2.z;
    }

    public static Vector3d subtract(Vector3d v1, Vector3d v2){
        Vector3d result = new Vector3d();
        result.x = v1.x - v2.x;
        result.y = v1.y - v2.y;
        result.z = v1.z - v2.z;

        return result;
    }

    public static Vector3d add(Vector3d v1, Vector3d v2){
        Vector3d result = new Vector3d();
        result.x = v1.x + v2.x;
        result.y = v1.y + v2.y;
        result.z = v1.z + v2.z;

        return result;
    }

    public static float module(Vector3d v){
        return (float)Math.sqrt(Math.abs(v.x * v.x + v.y * v.y + v.z * v.z));
    }

    public static Vector3d multiply(Vector3d v, float val){
        Vector3d result = new Vector3d();
        result.x = v.x * val;
        result.y = v.y * val;
        result.z = v.z * val;

        return result;
    }


}
