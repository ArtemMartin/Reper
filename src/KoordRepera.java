import java.util.Scanner;

public class KoordRepera {
    double x;
    double y;
    double h;
    Scanner scanner = new Scanner(System.in);


    public KoordRepera() {
        System.out.print("X Репера: ");
        x = scanner.nextInt();
        System.out.print("Y Репера: ");
        y = scanner.nextInt();
        System.out.print("h Репера: ");
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
