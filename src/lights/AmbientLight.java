package lights;

public class AmbientLight extends Light{
    public AmbientLight(double intensity) {
        super(intensity, LightTypes.AIMBIENT);
    }
}
