import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Sopriajenka {
    double xLev;
    double yLev;
    double xPrav;
    double yPrav;
    Scanner scanner = new Scanner(System.in);
    double[] xy = null;
    List<double[]> listKoord = new ArrayList<>();

    public Sopriajenka() {
        System.out.println("ПРАВЫЙ");
        System.out.println("X");
        xPrav = scanner.nextInt();
        System.out.println("Y");
        yPrav = scanner.nextInt();
        System.out.println("ЛЕВЫЙ");
        System.out.println("X");
        xLev = scanner.nextInt();
        System.out.println("Y");
        yLev = scanner.nextInt();
    }

    public double[] getXY() {
        int danet = 0;
        while (danet == 0) {
            System.out.println("Угол с ПРАВОГО:");
            double aprav = scanner.nextInt();
            System.out.println("Угол с ЛЕВОГО:");
            double alev = scanner.nextInt();

            double dxso = xPrav - xLev;
            double dyso = yPrav - yLev;
            double baz = Math.sqrt(Math.pow(dxso, 2) + Math.pow(dyso, 2));
            double aso = Math.abs((Math.atan(dyso / dxso) / Math.PI * 30) * 100);

            double ygolBaz = 0;
            if (dxso > 0 && dyso > 0) {
                ygolBaz = Math.round(aso);
            }
            if (dxso < 0 && dyso > 0) {
                ygolBaz = Math.round(3000 - aso);
            }
            if (dxso < 0 && dyso < 0) {
                ygolBaz = Math.round(3000 + aso);
            }
            if (dxso > 0 && dyso < 0) {
                ygolBaz = Math.round(6000 - aso);
            }

            double fi = 0;
            if (alev < 1500 && aprav > 4500) {
                fi = Math.abs(alev + 6000 - aprav);
            } else if (alev > 4500 && aprav < 1500) {
                fi = Math.abs(alev - (aprav + 6000));
            } else {
                fi = Math.abs(alev - aprav);
            }

            double blev = 0;
            if (alev < 1500 && ygolBaz > 4500) {
                blev = Math.abs(alev + 6000 - ygolBaz);
            } else if (alev > 4500 && ygolBaz < 1500) {
                blev = Math.abs(alev - (ygolBaz + 6000));
            } else {
                blev = Math.abs(alev - ygolBaz);
            }

            double ybazp = 0;
            if (ygolBaz - 3000 < 0) {
                ybazp = ygolBaz + 3000;
            } else {
                ybazp = ygolBaz - 3000;
            }

            double bprav = 0;
            if (aprav < 1500 && ybazp > 4500) {
                bprav = Math.abs(aprav + 6000 - ybazp);
            } else if (aprav > 4500 && ybazp < 1500) {
                bprav = Math.abs(aprav - (ybazp + 6000));
            } else {
                bprav = Math.abs(aprav - ybazp);
            }

            double dlev = baz / (Math.sin(fi / 100 * 6 * Math.PI / 180) + 0.001) * Math.sin(bprav / 100 * 6 * Math.PI / 180);
            double dprav = baz / (Math.sin(fi / 100 * 6 * Math.PI / 180) + 0.001) * Math.sin(blev / 100 * 6 * 3.141592 / 180);
            double xcso = Math.cos((alev / 100) * 6 * Math.PI / 180) * dlev + xLev;
            double ycso = Math.sin((alev / 100) * 6 * Math.PI / 180) * dlev + yLev;

            double xcso2 = Math.cos((aprav / 100) * 6 * Math.PI / 180) * dprav + xPrav;
            double ycso2 = Math.sin((aprav / 100) * 6 * Math.PI / 180) * dprav + yPrav;

            double xsr = (xcso + xcso2) / 2;
            double ysr = (ycso + ycso2) / 2;
            xy = new double[]{xsr, ysr};

            System.out.println("Х = " + Math.round(xsr) + " У = " + Math.round(ysr));
            listKoord.add(xy);
            System.out.println("Продолжить - 0, Выйти - 1:");
            danet = scanner.nextInt();
        }
        return xy;
    }

    public void getSredn() {
        double xSredn = 0;
        double ySredn = 0;
        if (listKoord != null) {
            int scetchik = listKoord.size();
            double xSym = 0;
            double ySym = 0;
            for (double[] doubles : listKoord) {
                xSym += doubles[0];
                ySym += doubles[1];
            }
            xSredn = xSym / scetchik;
            ySredn = ySym / scetchik;
        }
        System.out.println("---------------------------------------------------------------------");
        System.out.println("Среднее по группе засечек:");
        System.out.println("X = " + Math.round(xSredn) + " Y = " + Math.round(ySredn));
        System.out.println("---------------------------------------------------------------------");
    }

    public void getBP() {
        System.out.println("---------------------------------------------------------------------");
        System.out.println("ПРАВЫЙ:");
        System.out.println("X = " + xPrav + " Y = " + yPrav);
        System.out.println("ЛЕВЫЙ:");
        System.out.println("X = " + xLev + " Y = " + yLev);
        System.out.println("---------------------------------------------------------------------");
    }
    public void ochistka(){
        listKoord.clear();
    }
}