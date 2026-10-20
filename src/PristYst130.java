import java.util.Scanner;

public class PristYst130 extends PristYst {

    public PristYst130() {
        scanner = new Scanner(System.in);
        System.out.print("Снаряд ОФ482M - 0, изделие130 - 1, изделие130-1 - 2 :");
        snar = scanner.nextInt();
        System.out.print("Заряд Полный - 0, Уменшеный - 2: ");
        zar = scanner.nextInt();
        System.out.print("Прицел пристрелянный: ");
        pricel = scanner.nextInt();
        System.out.print("Доворот пристрелянный: ");
        dovorot = scanner.nextInt();
    }

}
