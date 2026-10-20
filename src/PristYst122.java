import java.util.Scanner;

public class PristYst122 extends PristYst {

    public PristYst122() {
        scanner = new Scanner(System.in);
        System.out.print("ОФ56(24) - 0, 3ОФ56И-1 - 1, ОФ99 - 2: ");
        snar = scanner.nextInt();
        System.out.print("Полный - 0, Уменшеный - 5, Первый - 1, Второй - 2, Третий - 3, Четвертый - 4: ");
        zar = scanner.nextInt();
        System.out.print("Ввести прицел пристрелянный: ");
        pricel = scanner.nextInt();
        System.out.print("Ввести доворот пристрелянный: ");
        dovorot = scanner.nextInt();
    }
}
