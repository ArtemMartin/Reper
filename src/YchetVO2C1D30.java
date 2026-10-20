import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class YchetVO2C1D30 {
    public double raznicaV0;
    public int pricelIsch;
    public String zariad;

    public YchetVO2C1D30(int pricelIsch, String zariad,double raznicaV0) {
        this.raznicaV0 = raznicaV0;
        this.pricelIsch = pricelIsch;
        this.zariad = zariad;
    }

    public double opredilitPoprV0() {
        String file = switch (this.zariad) {
            case "Уменшеный" -> "2C1/OF56/2C1Y.txt";
            case "Первый" -> "2C1/OF56/2C11.txt";
            case "Второй" -> "2C1/OF56/2C12.txt";
            case "Третий" -> "2C1/OF56/2C13.txt";
            case "Четвертый" -> "2C1/OF56/2C14.txt";
            default -> "2C1/OF56/2C1P.txt";
        };

        String line;
        String[] masStr;
        double v0;
        double dXtus;
        double popravka = 0.0;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            while ((line = reader.readLine()) != null) {
                masStr = line.split(",");
                if (this.pricelIsch > Double.parseDouble(masStr[1])) {
                    v0 = Double.parseDouble(masStr[11]);
                    dXtus = Double.parseDouble(masStr[2]);
                    popravka = (this.raznicaV0 * v0) / dXtus;
                    break;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return popravka;
    }

}
