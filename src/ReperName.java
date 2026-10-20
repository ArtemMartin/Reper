public class ReperName {
    private String name;
    private double dalnos;
    private double ygol;
    private double koef;
    private double poprYgol;

    public ReperName(String name, double dalnos, double ygol, double koef, double poprYgol) {
        this.name = name;
        this.dalnos = dalnos;
        this.ygol = ygol;
        this.koef = koef;
        this.poprYgol = poprYgol;
    }

    public String getName() {
        return name;
    }

    public double getDalnos() {
        return dalnos;
    }

    public double getYgol() {
        return ygol;
    }

    public double getKoef() {
        return koef;
    }

    public double getPoprYgol() {
        return poprYgol;
    }
}
