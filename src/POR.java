import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

public class POR {
    static Scanner scanner = new Scanner(System.in);
    static int sistema;
    static int on;
    static double dalRepTopo;
    static double dDPrist;
    static double dDovPrist;
    static int[][] bylletenIsch;
    static int tz;
    static double v0;
    static int zar;
    static int snariad;

    public  void porRun() {
        System.out.println("************Составление бюлетня ПОР***********!");
        while (true) {
            System.out.print("Выбрать систему 0 - 2С1, 1 - M46: ");
            sistema = scanner.nextInt();
            System.out.print("OH(большие деления угломера!!!): ");
            on = scanner.nextInt();
            System.out.print("Дальность Репера топо (вводить ближайшую табличную кратную 200,400 и тд.: ");
            dalRepTopo = scanner.nextInt();
            System.out.println("Поправки пристрелянные:");
            System.out.print("Дальности: ");
            dDPrist = scanner.nextInt();
            System.out.print("Направления: ");
            dDovPrist = scanner.nextInt();
            System.out.print("Температура заряда: ");
            tz = scanner.nextInt();
            System.out.print("Потеря начальной скорости(через запятую): ");
            v0 = scanner.nextDouble();
            if (sistema == 0) {
                System.out.print("ОФ56 - 0, 3ОФ56И-1 - 1: ");
                snariad = scanner.nextInt();
                System.out.print("Заряд: 0 - Полный, 5 - Уменшеный, 1 - Первый, 2 - Второй, 3 - Третий, 4 - Четвертый: ");
                zar = scanner.nextInt();
            } else {
                System.out.print("Заряд: 0 - Полный, 2 - Уменшеный: ");
                zar = scanner.nextInt();
            }
            //переписываем файл в масив
            bylletenIsch = getMassBylIsch();
            //получаем масив поправку дальности без балист попправки
            double[] mas;
            if (sistema == 0) {
                mas = getDDischBezBalist();
            } else {
                mas = getDDischBezBalistM46();
            }
            double dDbezBalist = mas[0];
            double dXw = mas[1];
            double dZw = mas[2];
            double z = mas[3];
            int Ybyl = (int) mas[4];
            //считаем Wx, Wz
            int Wx = (int) Math.round((dDPrist - dDbezBalist) / (dXw * -0.1));
            int Wz = (int) Math.round((dDovPrist - (-z)) / (dZw * -0.1));
            //получить сорость и угол балист ветра
            int[] masASw = getASw(Wx, Wz);
            int sw = masASw[1];
            int aBalW = masASw[0];
            int aw = 0;
            if (on <= 15 && aBalW >= 45) {
                aw = (on + 60) - aBalW;
            } else if (on >= 45 && aBalW <= 15) {
                aw = on - (aBalW + 60);
            } else {
                aw = on - aBalW;
            }
            if (aw < 0) aw += 60;
            obnovitBylleten(Ybyl, aw, sw);
            System.out.print("Продолжить - 0, Нет - 1: ");
            int prod = scanner.nextInt();
            if (prod == 1) break;
            System.out.println("_______________________________________________");
            bylletenIsch = getMassBylIsch();
        }
    }

    public static void obnovitBylleten(int Ybyl, int aw, int sw) {
        int scetchByl = 2;
        //исчем положение группы
        for (int i = 2; i < bylletenIsch.length; i++) {
            if (Ybyl == bylletenIsch[i][0]) {
                scetchByl = i;
                break;
            }
        }
        int kolPologDlaRascheta = scetchByl - 2;
        int aw02 = bylletenIsch[2][2];
        int sw02 = bylletenIsch[2][3];
        //определяем по часовой или против закручивать ветер
        double priraschAw = 0;
        if (aw <= 15 && aw02 >= 45) {
            aw += 60;
            priraschAw = (double) (aw - aw02) / kolPologDlaRascheta;
        } else if (aw >= 45 && aw02 <= 15) {
            aw02 += 60;
            priraschAw = (double) (aw - aw02) / kolPologDlaRascheta;
        } else {
            priraschAw = (double) (aw - aw02) / kolPologDlaRascheta;
        }
        double priraschSw = (double) (sw - sw02) / kolPologDlaRascheta;
        double priraschAw1 = priraschAw;
        double priraschSw1 = priraschSw;
        for (int i = 3; i < 17; i++) {
            if (i < (scetchByl + 2)) {
                bylletenIsch[i][2] = (int) Math.round(bylletenIsch[2][2] + priraschAw1);
                if (bylletenIsch[i][2] > 60) bylletenIsch[i][2] -= 60;
                if (bylletenIsch[i][2] < 0) bylletenIsch[i][2] += 60;
                bylletenIsch[i][3] = (int) Math.round(bylletenIsch[2][3] + priraschSw1);
                if (bylletenIsch[i][3] < 0) bylletenIsch[i][3] = 0;
                priraschAw1 = priraschAw1 + priraschAw;
                priraschSw1 = priraschSw1 + priraschSw;
            } else {
                bylletenIsch[i][2] = bylletenIsch[i - 1][2];
                if (bylletenIsch[i][2] > 60) bylletenIsch[i][2] -= 60;
                if (bylletenIsch[i][2] < 0) bylletenIsch[i][2] += 60;
                bylletenIsch[i][3] = bylletenIsch[i - 1][3];
                if (bylletenIsch[i][3] < 0) bylletenIsch[i][3] = 0;
            }
        }
        for (int[] isch : bylletenIsch) {
            System.out.println(Arrays.toString(isch));
        }
        try {
            FileWriter writer = new FileWriter("bylleten.txt", false);
            String line;
            for (int i = 0; i < bylletenIsch.length; i++) {
                line = bylletenIsch[i][0] + "," + bylletenIsch[i][1] + "," + bylletenIsch[i][2] + "," + bylletenIsch[i][3];
                writer.write(line + "\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int[] getASw(double Wx, double Wz) {
        int s = 0;
        int a = 0;
        String[] mas;
        String[][] masStr = new String[17][24];
        try {
            BufferedReader reader = new BufferedReader(new FileReader("razlogenie.csv"));
            String line;
            int i = 0;
            while ((line = reader.readLine()) != null) {
                mas = line.split(",");
                for (int j = 0; j < mas.length; j++) {
                    masStr[i][j] = mas[j];
                }
                i++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        int wxTab = 0;
        int wzTab = 0;
        boolean findWind = false;
        for (int i = 1; i < masStr.length; i++) {
            for (int j = 4; j < masStr[i].length; j++) {
                mas = masStr[i][j].split("/");
                wxTab = Integer.parseInt(mas[0]);
                wzTab = Integer.parseInt(mas[1]);
                double wxProm = Math.abs(Wx);
                if (wxProm > 20) wxProm = 20;
                double wzProm = Math.abs(Wz);
                if (wzProm > 20) wzProm = 20;
                //задаем поиск ветра
                if (wxProm == wxTab && wzProm < wzTab && wzProm == (wzTab - 1)) findWind = true;
                if (wxProm == wxTab && wzProm > wzTab && wzProm == (wzTab + 1)) findWind = true;
                if (wxProm == wxTab && wzProm == wzTab) findWind = true;
                if (wxProm < wxTab && wxProm == (wxTab - 1) && wzProm == wzTab) findWind = true;
                if (wxProm > wxTab && wxProm == (wxTab + 1) && wzProm == wzTab) findWind = true;

                if (findWind) {
                    s = Integer.parseInt(masStr[0][j]);
                    if (Wx < 0 && Wz >= 0) {
                        a = Integer.parseInt(masStr[i][0]);
                        return new int[]{a, s};
                    } else if (Wx >= 0 && Wz >= 0) {
                        a = Integer.parseInt(masStr[i][1]);
                        return new int[]{a, s};
                    } else if (Wx >= 0 && Wz < 0) {
                        a = Integer.parseInt(masStr[i][2]);
                        return new int[]{a, s};
                    } else if (Wx < 0 && Wz < 0) {
                        a = Integer.parseInt(masStr[i][3]);
                        return new int[]{a, s};
                    }
                }
            }
        }
        return new int[]{a, s};
    }

    public static int[][] getMassBylIsch() {
        int[][] masByl = new int[17][4];
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("bylleten.txt")));
            String line;
            String[] masStr;

            int j = 0;
            while ((line = reader.readLine()) != null) {
                masStr = line.split(",");
                for (int i = 0; i < masStr.length; i++) {
                    try {
                        masByl[j][i] = Integer.parseInt((masStr[i]));
                    } catch (NumberFormatException ex) {
                        masByl[j][i] = 0;
                    }
                }
                j++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return masByl;
    }

    public static double[] getDDischBezBalist() {
        double dXw = 0;
        double dZw = 0;
        double z = 0;
        double dd = 0;
        double dXV0 = 0;
        double dXt = 0;
        double dXh = 0;
        int dt = 0;
        double Ybyl = 2;
        int dH = bylletenIsch[1][1];
        if (dH >= 500) {
            dH = (dH - 500) * -1;
        }
        try {
            BufferedReader reader = new BufferedReader(new FileReader(getFile(zar)));
            String line;
            String[] masStr;
            while ((line = reader.readLine()) != null) {
                masStr = line.split(",");
                if (Double.parseDouble(masStr[0]) == dalRepTopo) {
                    dXh = Double.parseDouble(masStr[8]);
                    dXt = Double.parseDouble(masStr[10]);
                    dXV0 = Double.parseDouble(masStr[11]);
                    Ybyl = Double.parseDouble(masStr[17]);
                    dZw = Double.parseDouble(masStr[6]);
                    dXw = Double.parseDouble(masStr[7]);
                    z = Double.parseDouble(masStr[5]);
                    break;
                }
            }
            for (int i = 2; i < bylletenIsch.length; i++) {
                if (bylletenIsch[i][0] == Ybyl) {
                    dt = bylletenIsch[i][1];
                    if (dt >= 50) {
                        dt = (dt - 50) * -1;
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        double dV0Tz = getV0Tz();
        dd = dH * (dXh * 0.1) + dt * (dXt * -0.1) + (v0 + dV0Tz) * (dXV0 * -1);
        return new double[]{dd, dXw, dZw, z, Ybyl};
    }

    public static double[] getDDischBezBalistM46() {
        double dXw = 0;
        double dZw = 0;
        double z = 0;
        double dd = 0;
        double dXV0 = 0;
        double dXt = 0;
        double dXh = 0;
        int dt = 0;
        double Ybyl = 2;
        int dH = bylletenIsch[1][1];
        if (dH >= 500) {
            dH = (dH - 500) * -1;
        }
        try {
            BufferedReader reader = new BufferedReader(new FileReader(getFileM46(zar)));
            String line;
            String[] masStr;
            while ((line = reader.readLine()) != null) {
                masStr = line.split(",");
                if (Double.parseDouble(masStr[0]) == dalRepTopo) {
                    dXh = Double.parseDouble(masStr[8]);
                    dXt = Double.parseDouble(masStr[9]);
                    dXV0 = Double.parseDouble(masStr[11]);
                    Ybyl = Double.parseDouble(masStr[12]);
                    dZw = Double.parseDouble(masStr[6]);
                    dXw = Double.parseDouble(masStr[7]);
                    z = Double.parseDouble(masStr[5]);
                    break;
                }
            }
            for (int i = 2; i < bylletenIsch.length; i++) {
                if (bylletenIsch[i][0] == Ybyl) {
                    dt = bylletenIsch[i][1];
                    if (dt >= 50) {
                        dt = (dt - 50) * -1;
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        double dV0Tz = getV0Tz();
        dd = dH * (dXh * 0.1) + dt * (dXt * -0.1) + (v0 + dV0Tz) * (dXV0 * -1);
        return new double[]{dd, dXw, dZw, z, Ybyl};
    }

    static String getFileM46(int zar) {
        Scanner scanner1 = new Scanner(System.in);
        int izdelie;
        String file = "M46/OF482MP";
        System.out.print("Выбрать снаряд по типу OF482 - 0, izdelie-130 - 1, izdelie-130-1 - 2: ");
        izdelie = scanner1.nextInt();
        if (zar == 0) {
            file = switch (izdelie) {
                case 0 -> "M46/OF482MP";
                case 1 -> "M46/polnui130.csv";
                case 2 -> "M46/polnui130-1.csv";
                default -> file;
            };
        } else {
            file = switch (izdelie) {
                case 0 -> "M46/OF482M2";
                case 2 -> "M46/ymenshen130-1.csv";
                default -> file;
            };
        }
        return file;
    }

    static String getFile(int zar) {
        String file = null;
        if (sistema == 0) {
            if (snariad == 0) {
                switch (zar) {
                    case 0 -> file = "2C1/OF56/2C1P.txt";
                    case 1 -> file = "2C1/OF56/2C11.txt";
                    case 2 -> file = "2C1/OF56/2C12.txt";
                    case 3 -> file = "2C1/OF56/2C13.txt";
                    case 4 -> file = "2C1/OF56/2C14.txt";
                    case 5 -> file = "2C1/OF56/2C15.txt";
                }
            } else {
                file = switch (zar) {
                    case 0 -> "2C1/3OF56I1/OF56Pol.txt";
                    case 1 -> "2C1/3OF56I1/OF56Perv.txt";
                    case 2 -> "2C1/3OF56I1/OF56Vtor.txt";
                    case 3 -> "2C1/3OF56I1/OF56Tret.txt";
                    case 4 -> "2C1/3OF56I1/OF56Chetvert.txt";
                    case 5 -> "2C1/3OF56I1/OF56Ymen.txt";
                    default -> file;
                };
            }
        }
        return file;
    }

    public static double getV0Tz() {
        double dV0 = 0;
        double dV01 = 0;
        double[] mas;
        int tz0 = 0;
        BufferedReader reader;
        try {
            if (sistema == 0) {
                reader = new BufferedReader(new FileReader("TZ2C"));
            } else {
                reader = new BufferedReader(new FileReader("TZ2CM46"));
            }
            mas = getDV0Promegyt(reader, tz);
            dV0 = mas[0];
            tz0 = (int) mas[1];
        } catch (IOException e) {
            e.printStackTrace();
        }
        int tz11 = 0;
        if (tz < 0) tz11 = tz - 5;
        if (tz >= 0) tz11 = tz + 5;
        BufferedReader reader2;
        try {
            if (sistema == 0) {
                reader2 = new BufferedReader(new FileReader("TZ2C"));
            } else {
                reader2 = new BufferedReader(new FileReader("TZ2CM46"));
            }
            mas = getDV0Promegyt(reader2, tz11);
            dV01 = mas[0];
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (tz >= 0) dV0 = (dV01 - dV0) / 5 * (tz - tz0) + dV0;
        if (tz < 0) dV0 = ((dV01 - dV0) / 5 * (tz - tz0) - dV0) * -1;
        return dV0;
    }

    static public double[] getDV0Promegyt(BufferedReader reader, int tz) {
        double dV0 = 0;
        int tz0 = 0;
        String line;
        String[] masStr;
        int t;
        try {
            while ((line = reader.readLine()) != null) {
                masStr = line.split(",");
                t = Integer.parseInt(masStr[0]);
                if (t <= tz) {
                    switch (zar) {
                        case 0:
                            dV0 = Double.parseDouble(masStr[1]);
                            break;
                        case 1:
                            if (sistema == 1) {
                                dV0 = Double.parseDouble(masStr[1]);
                                break;
                            }
                        case 5:
                            dV0 = Double.parseDouble(masStr[2]);
                            break;
                        case 2:
                        case 3:
                            dV0 = Double.parseDouble(masStr[3]);
                            break;
                        case 4:
                            if (sistema == 1) {
                                dV0 = Double.parseDouble(masStr[3]);
                                break;
                            }
                            dV0 = Double.parseDouble(masStr[4]);
                            break;
                    }
                    tz0 = Integer.parseInt(masStr[0]);
                    break;
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new double[]{dV0, tz0};
    }
}