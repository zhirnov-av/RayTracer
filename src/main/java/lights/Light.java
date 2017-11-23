package lights;

public abstract class Light {
    private double intensity;
    private LightTypes type;

    public Light(double intensity, LightTypes type) {
        this.intensity = intensity;
        this.type = type;
    }

    public double getIntensity() {
        return intensity;
    }

    public LightTypes getType() {
        return type;
    }
}
