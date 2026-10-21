import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class Reper {
    public static double v0Pristrelochnogo;
    public static List<ReperName> reperNameList122 = new ArrayList<>();
    public ManagerOP managerOpReper;
    public ManagerOP managerOpPerenos;
    public Zel zel;
    public int sistema;
    public PristYst122 pristYst122;
    public PristYstBM21 pristYstBM21;
    public PristYst130 pristYst130;
    public KoordRepera koordRepera;
    public ReperName reperName122;
    public POR por;
    Sopriajenka sopriajenka;

    public static void main(String[] args) {
        Reper reper = new Reper();
        reper.go();
    }

    public void go() {
        while (true) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("********************************************************************************************");
            System.out.println("0 - Ввести ОП для создания(пристрелки репера).  2 - Ввести пристрелянные установки.");
            System.out.println("3 - Ввести координаты Репера                    1 - Составить Бюл ПОР ");
            System.out.println("4 - Посчитать поправки по Реперу.               5 - Ввести ОП для Переноса");
            System.out.println("6 - Ввести Цель и использовать пристрелянные поправки пo Реперу");
            System.out.println("7 - Показать БП.                                8 - Сопряженка забить НП");
            System.out.println("10 - Сопряженка засечки.                        11 - Получить среднее по засечке Сопряженки");
            System.out.println("12 - Показать БП Сопряженки.                    9 - EXIT");
            System.out.println("13 - Показать Реперы.                    ");

            System.out.println("********************************************************************************************");
            int a = scanner.nextInt();
            switch (a) {
                case 0:
                    managerOpReper = new ManagerOP();
                    break;
                case 1:
                    por = new POR();
                    por.porRun();
                    break;
                case 2:
                    System.out.println("0 - 122mm, 1 - 130mm, БМ21 - 2.");
                    sistema = scanner.nextInt();
                    if (sistema == 0) {
                        pristYst122 = new PristYst122();
                    } else if (sistema == 1) {
                        pristYst130 = new PristYst130();
                    } else {
                        pristYstBM21 = new PristYstBM21();
                    }
                    break;
                case 3:
                    koordRepera = new KoordRepera();
                    break;
                case 4:
                    if (koordRepera == null) {
                        System.out.println("Ввести координаты Репера!!!");
                    } else if (managerOpReper == null) {
                        System.out.println("Вввести ОП пристрелочного орудия!!!");
                    } else {
                        if (sistema == 0) {
                            this.poprPoRepery(koordRepera, pristYst122, managerOpReper);
                        } else if (sistema == 1) {
                            this.poprPoRepery(koordRepera, pristYst130, managerOpReper);
                        } else {
                            pristYstBM21.poprPoRepery(koordRepera, managerOpReper);
                        }
                    }
                    break;
                case 5:
                    managerOpPerenos = new ManagerOP();
                    break;
                case 6:
                    zel = new Zel();
                    if (sistema == 0) {
                        this.opredYstPoZeli(pristYst122);
                    } else if (sistema == 1) {
                        this.opredYstPoZeli(pristYst130);
                    } else {
                        pristYstBM21.opredYstPoZeli(managerOpPerenos, zel);
                    }
                    break;
                case 7:
                    this.watchBP();
                    break;
                case 8:
                    sopriajenka = new Sopriajenka();
                    break;
                case 10:
                    if (sopriajenka != null) {
                        sopriajenka.ochistka();
                        sopriajenka.getXY();
                    } else {
                        System.out.println("Объект не инициализирован");
                    }
                case 11:
                    if (sopriajenka != null) {
                        sopriajenka.getSredn();
                    } else {
                        System.out.println("Объект не инициализирован");
                    }
                    break;
                case 12:
                    if (sopriajenka != null) {
                        sopriajenka.getBP();
                    } else {
                        System.out.println("Объект не инициализирован");
                    }
                    break;
                case 13:
                    for (ReperName reperName : reperNameList122) {
                        System.out.println(reperName);
                    }
                    break;
                case 9:
                    return;
            }
        }
    }

    public double[] OGZ(double xop, double yop, double xr, double yr, double hop, double hr) {
        double[] ogz;
        double dx = xr - xop;
        double dy = yr - yop;
        double dh = hr - hop;

        double dalTopo = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
        double a = Math.abs(Math.atan(dy / dx) / Math.PI * 30 * 100);
        double ygolTopo = 0;
        if (dx > 0 && dy > 0) {
            ygolTopo = a;
        } else if (dx < 0 && dy > 0) {
            ygolTopo = 3000 - a;
        } else if (dx < 0 && dy < 0) {
            ygolTopo = 3000 + a;
        } else if (dx > 0 && dy < 0) {
            ygolTopo = 6000 - a;
        }
        ogz = new double[]{dalTopo, ygolTopo, dh};
        return ogz;
    }

    public void poprPoRepery(KoordRepera koordRepera, PristYst pristYst, ManagerOP managerOp) {
        double dalRepTopo;
        double ygolRepTopo;
        double dhRep;

        double[] dataReper = this.OGZ(managerOp.getOp().getX(),
                managerOp.getOp().getY(),
                koordRepera.getX(),
                koordRepera.getY(),
                managerOp.getOp().getH(),
                koordRepera.getH());
        dalRepTopo = dataReper[0];
        ygolRepTopo = dataReper[1];
        dhRep = dataReper[2];

        double dYr = (dhRep / dalRepTopo * 1000) * 0.95;
        double pricBezYrovn = pristYst.getPricel() - dYr;
        //выбрасываем с прицела kpe или dae
        if (sistema == 0) {
            pricBezYrovn = pricBezYrovn - (this.getKpe(pricBezYrovn, dYr, pristYst) * dYr);
        } else {
            pricBezYrovn = pricBezYrovn - this.getKPETabl(dYr, pricBezYrovn, pristYst);
        }
        double[] dalPristDerivac = this.getDalPristIDerivac(pristYst, pricBezYrovn);

        double dDpopRepPristr = dalPristDerivac[0] - dalRepTopo;
        double koefPerenosa = dDpopRepPristr / dalRepTopo;

        double ygolPristr = managerOp.getOp().getON() + pristYst.getDovorot();
        if (ygolPristr < 0) {
            ygolPristr += 6000;
        }
        double dDovRepPrist = ygolPristr - ygolRepTopo - (-dalPristDerivac[1]);
        double dDovRepPristForVuvod = ygolPristr - ygolRepTopo;

        String snar;
        String strDd;
        String strdDov;
        String strDov;
        System.out.println("---------------------------------------------------------------------");
        if (sistema == 0) {
            System.out.println("*** 2C1(Д30) ***");
            if (pristYst.getSnar() == 0) {
                snar = "ОФ56(24)";
            } else if (pristYst.getSnar() == 2) {
                snar = "ОФ99";
            } else {
                snar = "3ОФ56И-1";
            }
        } else {
            System.out.println("*** М46 ***");
            if (pristYst.getSnar() == 0) {
                snar = "ОФ482M";
            } else if (pristYst.getSnar() == 1) {
                snar = "Izdelie130";
            } else {
                snar = "Izdelie130-1";
            }
        }
        System.out.println("В " + new Date() + ", пристрелял наземный действительный репер.");
        System.out.println("Координаты репера: " + Math.round(koordRepera.getX()) + " " + Math.round(koordRepera.getY()) + " " + Math.round(koordRepera.getH()));
        if (pristYst.getDovorot() > 0) {
            strDov = "+" + pristYst.getDovorot();
        } else {
            strDov = String.valueOf(pristYst.getDovorot());
        }
        System.out.println("Установки пристрелянные: Снаряд " + snar + " Прицел " + pristYst.getPricel() + " Доворот ОН " + strDov);
        if (dDpopRepPristr > 0) {
            strDd = "+" + Math.round(dDpopRepPristr);
        } else {
            strDd = String.valueOf(Math.round(dDpopRepPristr));
        }
        if (dDovRepPristForVuvod > 0) {
            strdDov = "+" + Math.round(dDovRepPristForVuvod);
        } else {
            strdDov = String.valueOf(Math.round(dDovRepPristForVuvod));
        }
        System.out.println("Поправки по реперу: в дальность " + strDd + " направления " + strdDov);
        System.out.println("Дальность Репера тпографическая: " + Math.round(dalRepTopo) + ". Угол Репера топо: " + Math.round(ygolRepTopo));
        System.out.format("Коефициент переноса: %.2f", koefPerenosa);
        System.out.println();
        System.out.println("---------------------------------------------------------------------");

        System.out.print("Записать репер? 1 - да, 0 - нет: ");
        Scanner scanner = new Scanner(System.in);
        int vvod = scanner.nextInt();
        scanner.nextLine();
        if (vvod == 1) {
            System.out.print("Ввести имя репера: ");
            reperNameList122.add(new ReperName(scanner.nextLine(),
                    dalRepTopo,
                    ygolRepTopo,
                    koefPerenosa,
                    dDovRepPrist));
        }
    }

    public double[] getDalPristIDerivac(PristYst pristYst, double pricel) {
        double[] dalPristIDeriv = null;

        String file = this.getFileTS(pristYst);
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String[] mass;
            String line;
            double dalTab = 0;
            double deriv = 0;
            double pricTab = 0;
            while ((line = reader.readLine()) != null) {
                mass = line.split(",");
                dalTab = Double.parseDouble(mass[0]);
                deriv = Double.parseDouble(mass[5]);
                pricTab = Double.parseDouble(mass[1]);
                if (sistema == 0) {
                    if (pricTab <= pricel) {
                        reader.close();
                        break;
                    }
                } else {
                    //если таблица наоборот составлена
                    if (pricTab >= pricel) {
                        reader.close();
                        break;
                    }
                }
            }
            BufferedReader reader2 = new BufferedReader(new FileReader(file));
            String[] mass2;
            String line2;
            double dalTab2;
            if (sistema == 0) {
                dalTab2 = dalTab + 200;
            } else {
                dalTab2 = dalTab - 200;
            }
            double dalTab3 = 0;
            double deriv2 = 0;
            double pricTab2 = 0;
            while ((line2 = reader2.readLine()) != null) {
                mass2 = line2.split(",");
                dalTab3 = Double.parseDouble(mass2[0]);
                deriv2 = Double.parseDouble(mass2[5]);
                pricTab2 = Double.parseDouble(mass2[1]);
                if (dalTab2 == dalTab3) {
                    reader2.close();
                    break;
                }
            }
            double dalPrist;
            double derivacia;
            if (sistema == 0) {
                dalPrist = 200 / (pricTab2 - pricTab) * (pricel - pricTab) + dalTab;
                derivacia = (deriv2 - deriv) / (pricTab2 - pricTab) * (pricel - pricTab) + deriv;
            } else {
                dalPrist = 200 / (pricTab - pricTab2) * (pricel - pricTab2) + dalTab3;
                derivacia = (deriv - deriv2) / (pricTab - pricTab2) * (pricel - pricTab2) + deriv2;
            }
            dalPristIDeriv = new double[]{dalPrist, derivacia};
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dalPristIDeriv;
    }

    public ReperName getOptRepDlaPerenosa(double dalnost, double ygol) {
        List<ReperName> list = reperNameList122;
        ReperName reperName = null;
        int raznicDaln;
        int raznicYgol;
        int raznicDalnOpt = 2000;
        int raznicYgolOpt = 200;
        for (ReperName name : list) {
            raznicDaln = Math.abs((int) (dalnost - name.getDalnos()));
            if (ygol > 4500 && name.getYgol() < 1500) {
                raznicYgol = Math.abs((int) (ygol - (name.getYgol() + 6000)));
            } else if (ygol < 1500 && name.getYgol() > 4500) {
                raznicYgol = Math.abs((int) ((ygol + 6000) - name.getYgol()));
            } else {
                raznicYgol = Math.abs((int) (ygol - name.getYgol()));
            }
            if (raznicDaln < raznicDalnOpt && raznicYgol < raznicYgolOpt) {
                reperName = name;
                raznicDalnOpt = raznicDaln;
                raznicYgolOpt = raznicYgol;
            }

        }
        if (reperName != null) {
            System.out.println("Репер " + reperName.getName() + " - оптимален для переноса!!!");
        }
        return reperName;
    }

    public void opredYstPoZeli(PristYst pristYst) {
        double[] dalYgoldHTopo = this.OGZ(managerOpPerenos.getOp().getX(),
                managerOpPerenos.getOp().getY(),
                zel.getX(),
                zel.getY(),
                managerOpPerenos.getOp().getH(),
                zel.getH());
        //опредиляем оптимальный репер для переноса
        ReperName reperName = getOptRepDlaPerenosa(dalYgoldHTopo[0], dalYgoldHTopo[1]);
        if (reperName == null) {
            System.out.println("Нет пристрелянных реперов соответствующих условиям преноса!!!");
            return;
        }
        double dalIsch = dalYgoldHTopo[0] * reperName.getKoef() + dalYgoldHTopo[0];

        String file = this.getFileTS(pristYst);
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            String[] mass;
            double dalTab = 0;
            double pricTab = 0;
            double deriv = 0;
            while ((line = reader.readLine()) != null) {
                mass = line.split(",");
                dalTab = Double.parseDouble(mass[0]);
                pricTab = Double.parseDouble(mass[1]);
                deriv = Double.parseDouble(mass[5]);
                if (sistema == 0) {
                    if (dalTab < dalIsch) {
                        reader.close();
                        break;
                    }
                } else {
                    if (dalTab > dalIsch) {
                        reader.close();
                        break;
                    }
                }
            }
            BufferedReader reader2 = new BufferedReader(new FileReader(file));
            String line2;
            String[] mass2;
            double dalTab3;
            if (sistema == 0) {
                dalTab3 = dalTab + 200;
            } else {
                dalTab3 = dalTab - 200;
            }
            double dalTab2 = 0;
            double pricTab2 = 0;
            double deriv2 = 0;
            while ((line2 = reader2.readLine()) != null) {
                mass2 = line2.split(",");
                dalTab2 = Double.parseDouble(mass2[0]);
                pricTab2 = Double.parseDouble(mass2[1]);
                deriv2 = Double.parseDouble(mass2[5]);
                if (dalTab3 == dalTab2) {
                    reader.close();
                    break;
                }
            }
            double dYr = (dalYgoldHTopo[2] / dalYgoldHTopo[0] * 1000);
            double pricIsch;
            double dYrPlEps;
            double derivacia;
            if (sistema == 0) {
                pricIsch = (pricTab2 - pricTab) / 200 * (dalIsch - dalTab) + pricTab;
                dYrPlEps = dYr + (this.getKpe(pricIsch, dalYgoldHTopo[2], pristYst) * dYr);
                derivacia = (deriv2 - deriv) / 200 * (dalIsch - dalTab) + deriv;
            } else {
                pricIsch = (pricTab - pricTab2) / 200 * (dalIsch - dalTab2) + pricTab2;
                dYrPlEps = dYr + this.getKPETabl(dYr, pricIsch, pristYst);
                derivacia = (deriv - deriv2) / 200 * (dalIsch - dalTab2) + deriv2;
            }

            /*
            Здесь добавим логику учёта разности потерь начальных скоростей
             */
            YchetVO2C1D30 ychetVO2C1D30 = new YchetVO2C1D30((int) pricIsch,
                    this.getZariad(pristYst),
                    managerOpReper.getOp().getV0() - managerOpPerenos.getOp().getV0());
            pricIsch += ychetVO2C1D30.opredilitPoprV0();

            pricIsch += dYrPlEps;
            double ygolIsch = dalYgoldHTopo[1] + reperName.getPoprYgol() - derivacia;

            System.out.println("---------------------------------------------------------------------");
            String snar;
            if (sistema == 0) {
                System.out.println("*** 2C1(Д30) ***");
                if (pristYst.getSnar() == 0) {
                    snar = "ОФ56(24)";
                } else if (pristYst.getSnar() == 2) {
                    snar = "ОФ99";
                } else {
                    snar = "3ОФ56И-1";
                }
            } else {
                System.out.println("*** М46 ***");
                if (pristYst.getSnar() == 0) {
                    snar = "ОФ482M";
                } else if (pristYst.getSnar() == 1) {
                    snar = "Izdelie130";
                } else {
                    snar = "Izdelie130-1";
                }
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
            String zariad = this.getZariad(pristYst);
            System.out.println("Установки по цели: Снаряд " + snar + ". Заряд " + zariad + ". Прицел " + Math.round(pricIsch) + ". Доворот ОН" + strDovIsch);
            System.out.println("Дальность цели топо: " + Math.round(dalYgoldHTopo[0]) + ". Угол топо: " + Math.round(dalYgoldHTopo[1]));
            System.out.println("---------------------------------------------------------------------");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String getFileTS(PristYst pristYst) {
        String file;
        if (sistema == 0) {
            file = this.getFileTS122(pristYst);
        } else {
            file = this.getFileTS130(pristYst);
        }
        return file;
    }

    public String getFileTS130(PristYst pristYst) {
        String file;
        Scanner scanner = new Scanner(System.in);
        int snar;
        System.out.print("Выбрать тип снаряда ОФ482М - 0, Изделие130 - 1, Изделие130-1 - 2: ");
        snar = scanner.nextInt();
        if (pristYst.getZar() == 0) {
            switch (snar) {
                case 0 -> file = "M46/OF482MP";
                case 1 -> file = "M46/polnui130.csv";
                default -> file = "M46/polnui130-1.csv";
            }
        } else {
            switch (snar) {
                case 0 -> file = "M46/OF482M2";
                default -> file = "M46/ymenshen130-1.csv";
            }
        }
        return file;
    }

    public String getFileTS122(PristYst pristYst) {
        String file;
        if (pristYst.getSnar() == 0) {
            if (pristYst.getZar() == 0) {
                file = "2C1/OF56/2C1P.txt";
            } else if (pristYst.getZar() == 5) {
                file = "2C1/OF56/2C1Y.txt";
            } else if (pristYst.getZar() == 1) {
                file = "2C1/OF56/2C11.txt";
            } else if (pristYst.getZar() == 2) {
                file = "2C1/OF56/2C12.txt";
            } else if (pristYst.getZar() == 3) {
                file = "2C1/OF56/2C13.txt";
            } else if (pristYst.getZar() == 4) {
                file = "2C1/OF56/2C14.txt";
            } else {
                file = "2C1/OF56/2C1P.txt";
            }
        } else if (pristYst.getSnar() == 2) {
            if (pristYst.getZar() == 0) {
                file = "OF99/2C1P";
            } else if (pristYst.getZar() == 5) {
                file = "OF99/2C1Y";
            } else if (pristYst.getZar() == 1) {
                file = "OF99/2C11";
            } else if (pristYst.getZar() == 2) {
                file = "OF99/2C12";
            } else if (pristYst.getZar() == 3) {
                file = "OF99/2C13";
            } else if (pristYst.getZar() == 4) {
                file = "OF99/2C14";
            } else {
                file = "OF99/2C1P";
            }
        } else {
            if (pristYst.getZar() == 0) {
                file = "2C1/3OF56I1/OF56Pol.txt";
            } else if (pristYst.getZar() == 5) {
                file = "2C1/3OF56I1/OF56Ymen.txt";
            } else if (pristYst.getZar() == 1) {
                file = "2C1/3OF56I1/OF56Perv.txt";
            } else if (pristYst.getZar() == 2) {
                file = "2C1/3OF56I1/OF56Vtor.txt";
            } else if (pristYst.getZar() == 3) {
                file = "2C1/3OF56I1/OF56Tret.txt";
            } else if (pristYst.getZar() == 4) {
                file = "2C1/3OF56I1/OF56Chetvert.txt";
            } else {
                file = "2C1/3OF56I1/OF56Pol.txt";
            }
        }
        return file;
    }

    public String getZariad(PristYst pristYst) {
        String zariad = null;
        if (pristYst.getZar() == 0) {
            zariad = "Полный";
        } else if (pristYst.getZar() == 5) {
            zariad = "Уменшеный";
        } else if (pristYst.getZar() == 1) {
            zariad = "Первый";
        } else if (pristYst.getZar() == 2) {
            zariad = "Второй";
        } else if (pristYst.getZar() == 3) {
            zariad = "Третий";
        } else if (pristYst.getZar() == 4) {
            zariad = "Четвертый";
        } else {
            zariad = "Полный";
        }
        return zariad;
    }

    public double getKpe(double pricel, double dh, PristYst pristYst) {
        double kpe = 0;
        String file;
        String zariad = this.getZariad(pristYst);
        file = this.getFileKPE(zariad, dh);
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            String[] mass;
            double pricTab;
            double kpew = 0;
            double kpen = 0;
            while ((line = reader.readLine()) != null) {
                mass = line.split(",");
                pricTab = Double.parseDouble(mass[0]);
                kpew = Double.parseDouble(mass[1]);
                kpen = Double.parseDouble(mass[2]);
                if (pricel <= pricTab) {
                    reader.close();
                    break;
                }
            }
            BufferedReader reader2 = new BufferedReader(new FileReader(file));
            String line2;
            String[] mass2;
            double pricTab2 = 0;
            double kpew2 = 0;
            double kpen2 = 0;
            double pricel2 = pricel - 20;
            while ((line2 = reader2.readLine()) != null) {
                mass2 = line2.split(",");
                pricTab2 = Double.parseDouble(mass2[0]);
                kpew2 = Double.parseDouble(mass2[1]);
                kpen2 = Double.parseDouble(mass2[2]);
                if (pricel2 <= pricTab2) {
                    reader2.close();
                    break;
                }
            }
            double kpew3 = (kpew - kpew2) / 20 * (pricel - pricTab2) + kpew2;
            double kpen3 = (kpen - kpen2) / 20 * (pricel - pricTab2) + kpen2;
            if (dh >= 0) {
                kpe = kpew3 * 0.1;
            } else {
                kpe = kpen3 * 0.1;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return kpe;
    }

    public double getKPETabl(double dyr, double pricel, PristYst pristYst) {
        double kpe0 = 0;
        double[] masKpe;
        double dYr = Math.abs(dyr);
        String file = this.getFileKPE(this.getZariad(pristYst), dyr);
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

    public String getFileKPE(String zariad, double dh) {
        String file;
        if (sistema == 0) {
            if (zariad.equals("Полный")) {
                file = "2C1/2C1KPEP";
            } else if (zariad.equals("Уменшеный")) {
                file = "2C1/2C1KPEY";
            } else if (zariad.equals("Первый")) {
                file = "2C1/2C1KPE1";
            } else if (zariad.equals("Второй")) {
                file = "2C1/2C1KPE2";
            } else if (zariad.equals("Третий")) {
                file = "2C1/2C1KPE3";
            } else if (zariad.equals("Четвертый")) {
                file = "2C1/2C1KPE3";
            } else {
                file = "2C1/2C1KPEP";
            }
        } else {
            if (dh >= 0) {
                if (zariad.equals("Полный")) {
                    file = "M46/M46daeP+";
                } else {
                    file = "M46/M46dae2+";
                }
            } else {
                if (zariad.equals("Полный")) {
                    file = "M46/M46daeP-";
                } else {
                    file = "M46/M46dae2-";
                }
            }
        }
        return file;
    }

    public void watchBP() {
        System.out.println("---------------------------------------------------------------------");
        if (managerOpReper != null) {
            System.out.println("Пристрелочная ОП:");
            System.out.println(managerOpReper.getOp());
        }
        if (managerOpPerenos != null) {
            System.out.println("ОП для использования пристрелянных поправок:");
            System.out.println(managerOpPerenos.getOp());
        }
        System.out.println("---------------------------------------------------------------------");

    }

}
