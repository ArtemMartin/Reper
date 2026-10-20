import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ManagerOP {
    int nomerOP;
    OP op;
    List<OP> listOP = new ArrayList<>();
    Scanner scanner = new Scanner(System.in);

    public ManagerOP() {
        listOP = getListOP();
        vuvodOP();
        System.out.print("Выбрать ОП по номеру в списке: ");
        nomerOP = scanner.nextInt();
        op = new OP(listOP.get(nomerOP).getName(),
                listOP.get(nomerOP).getX(),
                listOP.get(nomerOP).getY(),
                listOP.get(nomerOP).getH(),
                listOP.get(nomerOP).getON(),
                listOP.get(nomerOP).getV0());
    }

    public OP getOp() {
        return op;
    }

    public void vuvodOP() {
        System.out.println("<<<Боевой порядок>>>");
        for (int i = 0; i < listOP.size(); i++) {
            System.out.println(i + " - " + listOP.get(i).getName());
        }
    }

    public List<OP> getListOP() {
        List<OP> list = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("OP.txt"), StandardCharsets.UTF_8))) {
            String line;
            String[] masStr;

            while ((line = reader.readLine()) != null) {
                masStr = line.split(",");
                list.add(new OP(masStr[0],
                        Double.parseDouble(masStr[1]),
                        Double.parseDouble(masStr[2]),
                        Double.parseDouble(masStr[3]),
                        Integer.parseInt(masStr[4]),
                        Double.parseDouble(masStr[5])));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return list;
    }


}
