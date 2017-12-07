package base;

/**
 *
 */
public class Color {
    private int r = 0;
    private int g = 0;
    private int b = 0;

    public Color(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public Color() {
        r = 0;
        g = 0;
        b = 0;
    }

    public Color(Color color) {
        r = color.r;
        g = color.g;
        b = color.b;
    }

    public int getR() {
        return r;
    }

    public void setR(int r) {
        this.r = r;
    }

    public int getG() {
        return g;
    }

    public void setG(int g) {
        this.g = g;
    }

    public int getB() {
        return b;
    }

    public void setB(int b) {
        this.b = b;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Color){
            return (r == ((Color) obj).r && g == ((Color) obj).g && b == ((Color) obj).b);
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = (int) r;
        result = 31 * result + (int) g;
        result = 31 * result + (int) b;
        return result;
    }

    public Color addColor(int r, int g, int b){
        this.r += r;
        this.g += g;
        this.b += b;
        if (this.r > 255) this.r = 255;
        if (this.g > 255) this.g = 255;
        if (this.b > 255) this.b = 255;

        return this;
    }

    public Color addColor(Color color){
        this.r += color.r;
        this.g += color.g;
        this.b += color.b;
        if (this.r > 255) this.r = 255;
        if (this.g > 255) this.g = 255;
        if (this.b > 255) this.b = 255;

        return this;
    }

    public Color substructColor(int r, int g, int b){
        this.r -= r;
        this.g -= g;
        this.b -= b;
        if (this.r < 0) this.r = 0;
        if (this.g < 0) this.g = 0;
        if (this.b < 0) this.b = 0;

        return this;
    }

    public Color substructColor(Color color){
        this.r -= color.r;
        this.g -= color.g;
        this.b -= color.b;
        if (this.r < 0) this.r = 0;
        if (this.g < 0) this.g = 0;
        if (this.b < 0) this.b = 0;

        return this;
    }

    public Color multiplyIntensity(float v) {

        this.r *= v;
        this.g *= v;
        this.b *= v;
        return this;
    }
}
