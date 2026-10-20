import java.util.Scanner;

public class Zel {
    double x;
    double y;
    double h;
    Scanner scanner = new Scanner(System.in);

    public Zel() {
        System.out.println("Х Цели");
        x = scanner.nextInt();
        System.out.println("У Цели");
        y = scanner.nextInt();
        System.out.println("h Цели");
        h = scanner.nextInt();
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
}
