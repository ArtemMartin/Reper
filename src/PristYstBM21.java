import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Date;
import java.util.Scanner;

public class PristYstBM21 {
    int snar;
    int balVarSnar;
    int pricel;
    int dovorot;
    Scanner scanner = new Scanner(System.in);
    double[] poprPoReperyKoef;


    public PristYstBM21() {
        System.out.print("Снаряд ОФ21 - 0, ОФ28 - 1:");
        snar = scanner.nextInt();
        System.out.print("Без Тк - 0, МТК - 1, БТК - 2:");
        balVarSnar = scanner.nextInt();
        System.out.print("Прицел:");
        pricel = scanner.nextInt();
        System.out.print("Доворот ОН:");
        dovorot = scanner.nextInt();
    }

    public void opredYstPoZeli(ManagerOP managerOpPerenos, Zel zel) {
        double[] dalYgoldHTopo = new Reper().OGZ(managerOpPerenos.getOp().getX(),
                managerOpPerenos.getOp().getY(),
                zel.getX(),
                zel.getY(),
                managerOpPerenos.getOp().getH(),
                zel.getH());
        double dalIsch = dalYgoldHTopo[0] * poprPoReperyKoef[1] + dalYgoldHTopo[0];

        String file = getFile();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            String[] mass;
            double dalTab = 0;
            double pricTab = 0;
            while ((line = reader.readLine()) != null) {
                mass = line.split(",");
                dalTab = Double.parseDouble(mass[0]);
                pricTab = Double.parseDouble(mass[1]);
                if (dalTab < dalIsch) {
                    reader.close();
                    break;
                }
            }
            BufferedReader reader2 = new BufferedReader(new FileReader(file));
            String line2;
            String[] mass2;
            double dalTab3 = dalTab + 200;
            double dalTab2;
            double pricTab2 = 0;
            while ((line2 = reader2.readLine()) != null) {
                mass2 = line2.split(",");
                dalTab2 = Double.parseDouble(mass2[0]);
                pricTab2 = Double.parseDouble(mass2[1]);
                if (dalTab3 == dalTab2) {
                    reader.close();
                    break;
                }
            }
            double pricIsch = (pricTab2 - pricTab) / 200 * (dalIsch - dalTab) + pricTab;
            double dYr = (dalYgoldHTopo[2] / dalYgoldHTopo[0] * 1000 * 0.95);
            double eps = getKPE(dYr, pricIsch);
            pricIsch += dYr + eps;

            double ygolIsch = dalYgoldHTopo[1] + poprPoReperyKoef[2];

            System.out.println("---------------------------------------------------------------------");
            String snar;
            if (this.snar == 0) {
                snar = "ОФ21";
            } else {
                snar = "ОФ28";
            }
            String strDovIsch;
            if (ygolIsch <= 1500 && managerOpPerenos.getOp().getON() > 4500) {
                ygolIsch += 6000;
            } else if (ygolIsch > 4500 && managerOpPerenos.getOp().getON() <= 1500) {
                managerOpPerenos.getOp().setON(managerOpPerenos.getOp().getON() + 6000);
            }
            if (ygolIsch - managerOpPerenos.getOp().getON() < 0) {
                strDovIsch = String.valueOf(Math.round(ygolIsch - managerOpPerenos.getOp().getON()));
            } else {
                strDovIsch = "+" + Math.round(ygolIsch - managerOpPerenos.getOp().getON());
            }
            String zariad = "";
            switch (balVarSnar) {
                case 0:
                    zariad = "Без ТК";
                    break;
                case 1:
                    zariad = "МТК";
                    break;
                case 2:
                    zariad = "БТК";
                    break;
            }
            System.out.println("Установки по цели: Снаряд " + snar + ". Бал. вар. снар. " + zariad + ". Прицел " + Math.round(pricIsch) + ". Доворот ОН" + strDovIsch);
            System.out.println("Дальность цели топо: " + Math.round(dalYgoldHTopo[0]) + ". Угол топо: " + Math.round(dalYgoldHTopo[1]));
            System.out.println("---------------------------------------------------------------------");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void poprPoRepery(KoordRepera koordRepera, ManagerOP managerOp) {
        double dalRepTopo;
        double ygolRepTopo;
        double dhRep;

        double[] dataReper = new Reper().OGZ(managerOp.getOp().getX(),
                managerOp.getOp().getY(),
                koordRepera.getX(),
                koordRepera.getY(),
                managerOp.getOp().getH(),
                koordRepera.getH());
        dalRepTopo = dataReper[0];
        ygolRepTopo = dataReper[1];
        dhRep = dataReper[2];

        double dYr = (dhRep / dalRepTopo * 1000) * 0.95;
        double pricBezYrovn = pricel - dYr;
        double kpe = getKPE(dYr, pricBezYrovn);

        pricBezYrovn -= kpe;
        double dalPrist = getDalPrist(pricBezYrovn);

        double dDpopRepPristr = dalPrist - dalRepTopo;
        double koefPerenosa = dDpopRepPristr / dalRepTopo;

        double ygolPristr = managerOp.getOp().getON() + dovorot;
        if (ygolPristr < 0) {
            ygolPristr += 6000;
        }
        double dDovRepPrist = ygolPristr - ygolRepTopo;

        String snar;
        String strDd;
        String strdDov;
        String strDov;
        if (this.snar == 0) {
            snar = "ОФ21";
        } else {
            snar = "ОФ28";
        }
        System.out.println("---------------------------------------------------------------------");
        System.out.println("В " + new Date() + ", пристрелял наземный действительный репер.");
        System.out.println("Координаты репера: " + Math.round(koordRepera.getX()) + " " + Math.round(koordRepera.getY()) + " " + Math.round(koordRepera.getH()));
        if (dovorot > 0) {
            strDov = "+" + dovorot;
        } else {
            strDov = String.valueOf(dovorot);
        }
        System.out.println("Установки пристрелянные: Снаряд " + snar + " Прицел " + pricel + " Доворот ОН " + strDov);
        if (dDpopRepPristr > 0) {
            strDd = "+" + Math.round(dDpopRepPristr);
        } else {
            strDd = String.valueOf(Math.round(dDpopRepPristr));
        }
        if (dDovRepPrist > 0) {
            strdDov = "+" + Math.round(dDovRepPrist);
        } else {
            strdDov = String.valueOf(Math.round(dDovRepPrist));
        }
        System.out.println("Поправки по реперу: в дальность: " + strDd + " направления: " + strdDov);
        System.out.println("Дальность Репера тпографическая: " + Math.round(dalRepTopo) + ". Угол Репера топо: " + Math.round(ygolRepTopo));
        System.out.format("Коефициент переноса: %.2f", koefPerenosa);
        System.out.println();
        System.out.println("---------------------------------------------------------------------");

        poprPoReperyKoef = new double[]{dalRepTopo, koefPerenosa, dDovRepPrist};
    }

    public double getDalPrist(double pricell) {
        double dalPristr = 0;
        String file = getFile();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String[] mass;
            String line;
            double dalTab = 0;
            double pricTab = 0;
            while ((line = reader.readLine()) != null) {
                mass = line.split(",");
                dalTab = Double.parseDouble(mass[0]);
                pricTab = Double.parseDouble(mass[1]);
                if (pricTab <= pricell) {
                    reader.close();
                    break;
                }
            }
            BufferedReader reader2 = new BufferedReader(new FileReader(file));
            String[] mass2;
            String line2;
            double dalTab2 = dalTab + 200;
            double dalTab3 = 0;
            double pricTab2 = 0;
            while ((line2 = reader2.readLine()) != null) {
                mass2 = line2.split(",");
                dalTab3 = Double.parseDouble(mass2[0]);
                pricTab2 = Double.parseDouble(mass2[1]);
                if (dalTab2 == dalTab3) {
                    reader2.close();
                    break;
                }
            }
            dalPristr = 200 / (pricTab2 - pricTab) * (pricell - pricTab) + dalTab;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dalPristr;
    }

    public String getFile() {
        String file = null;
        if (snar == 0) {
            if (balVarSnar == 0) {
                file = "BM21/BBTK";
            } else if (balVarSnar == 1) {
                file = "BM21/MTK";
            } else if (balVarSnar == 2) {
                file = "BM21/BTK";
            } else {
                file = "BM21/BBTK";
            }
        } else {

        }
        return file;
    }

    public String getFileKpeBM(double dYr) {
        String file;
        if (balVarSnar == 0 && dYr > 0) {
            file = "BM21/BBTKEPS.VIS";
        } else if (balVarSnar == 0 && dYr < 0) {
            file = "BM21/BBTKEPS.NIG";
        } else if (balVarSnar == 1 && dYr > 0) {
            file = "BM21/MTKEPS.VIS";
        } else if (balVarSnar == 1 && dYr < 0) {
            file = "BM21/MTKEPS.NIG";
        } else if (balVarSnar == 2 && dYr > 0) {
            file = "BM21/BTKEPS.VIS";
        } else if (balVarSnar == 2 && dYr < 0) {
            file = "BM21/BTKEPS.NIG";
        } else {
            file = "BM21/BBTKEPS.VIS";
        }
        return file;
    }

    public double getKPE(double dyr, double pricel) {
        double kpe0 = 0;
        double[] masKpe;
        double dYr = Math.abs(dyr);
        String file = getFileKpeBM(dYr);
        try {
            BufferedReader reader0 = new BufferedReader(new FileReader(file));
            String line0 = reader0.readLine();
            String[] strMass = line0.split(",");
            double[] masDoub0 = new double[strMass.length];
            reader0.close();
            for (int i = 0; i < strMass.length; i++) {
                masDoub0[i] = Double.parseDouble(strMass[i]);
            }
            masKpe = kpe(file, dYr, masDoub0, pricel);
            double kpe = masKpe[0];
            double dYrTab = masKpe[1];
            double dYr2 = dYr - 10;
            if (dYr2 <= 0) {
                dYr2 = 0;
            }
            masKpe = kpe(file, dYr2, masDoub0, pricel);
            double kpe2;
            if (dYr2 == 0) {
                kpe2 = 0;
                dYrTab = 0;
            } else {
                kpe2 = masKpe[0];
            }
            kpe0 = (kpe - kpe2) / 10 * (dYr - dYrTab) + kpe2;
            if (dyr < 0) {
                kpe0 *= -1;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return kpe0;
    }

    public double[] kpe(String file, double dYr, double[] masDoub0, double pricel) {
        //счетчик по прицелу
        int schetch = 0;
        double dYrTab = 0;
        for (int i = 0; i < masDoub0.length; i++) {
            if (pricel < masDoub0[i]) {
                schetch = i;
                break;
            }
        }
        String line;
        String[] massStr = new String[0];
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            while ((line = reader.readLine()) != null) {
                massStr = line.split(",");
                dYrTab = Double.parseDouble(massStr[0]);
                if (dYr < dYrTab) {
                    reader.close();
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //стрроку в double
        double[] masDoub = new double[massStr.length];
        double kpeVerchGranic1;
        double kpeVerchGranic2;
        for (int i = 0; i < massStr.length; i++) {
            masDoub[i] = Double.parseDouble(massStr[i]);
        }
        kpeVerchGranic2 = masDoub[schetch];
        kpeVerchGranic1 = masDoub[schetch - 1];
        double pricNigGran = masDoub0[schetch - 1];
        double kpe = (kpeVerchGranic2 - kpeVerchGranic1) / 20 * (pricel - pricNigGran) + kpeVerchGranic1;
        return new double[]{kpe, dYrTab};
    }

    public int getSnar() {
        return snar;
    }

    public int getBalVarSnar() {
        return balVarSnar;
    }

    public int getPricel() {
        return pricel;
    }

    public int getDovorot() {
        return dovorot;
    }
}
