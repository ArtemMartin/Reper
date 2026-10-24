public class OP {
    double x;
    double y;
    double h;
    int ON;
    String name;
    double v0;

    public OP(String name, double x, double y, double h, int ON, double v0) {
        this.x = x;
        this.y = y;
        this.h = h;
        this.ON = ON;
        this.name = name;
        this.v0 = v0;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getH() {
        return h;
    }

    public int getON() {
        return ON;
    }

    public void setON(int i) {
    }

    public String getName() {
        return name;
    }

    public double getV0() {
        return v0;
    }

    @Override
    public String toString() {
        return "OП позывной = " + name + " x = " + x + " y = " + y
                + " h = " + h + " ОН = " + ON + " V0 = " + v0;
    }
}
