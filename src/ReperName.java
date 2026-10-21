import java.time.LocalTime;

public class ReperName {
    private String name;
    private double dalnos;
    private double ygol;
    private double koef;
    private double poprYgol;
    private LocalTime currentTime;

    public ReperName(String name, double dalnos, double ygol, double koef, double poprYgol) {
        this.name = name;
        this.dalnos = dalnos;
        this.ygol = ygol;
        this.koef = koef;
        this.poprYgol = poprYgol;
        currentTime = LocalTime.now();
        Reper.startCountdown(this);
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

    @Override
    public String toString() {
        return name +
                ": Дт = " + (int) dalnos +
                ", Угол.т = " + (int) ygol +
                ", koef = " + (int) (koef * 1000) / 1000.0 +
                ", попр. в нап. = " + (int) poprYgol +
                ", время создания = " + currentTime.getHour() + ":" + currentTime.getMinute();
    }
}

