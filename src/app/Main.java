package app;

import java.util.Locale;
import java.util.Scanner;

public class Main {

    private static final double R_MAX = 10_000.0;       // Ограничение на ввод радиуса окружности
    private static final double MIN_GAP = 0.2;          // Минимальная ширина между соседними элементами (в долях от длины элемента)
    private static final double EPSILON = 1.0E-15;      // Константа для более правильного округления (исп-ся в методе getInt)

    public static void main(String[] args) {
        Scanner reader = new Scanner(System.in);

        double R = 0.0; // радиус окружности
        for(;;) {
            System.out.println("Введите радиус окружности в диапазоне (0.0; " + R_MAX + "], либо нажмите q для выхода:");
            String entered = reader.nextLine();    // Ввод пользователя
            if (entered.equalsIgnoreCase("q")) return;
            try {
                R = Double.parseDouble(entered);
            } catch (NumberFormatException ex) {
                System.out.println("Ошибка. Вы ввели не число (раделителем дробной части д.б. символ '.').\n");
                continue;
            }
            if (0 < R && R <= R_MAX) break;
            System.out.println("Ошибка. Вам следует ввести число в требуемом диапазоне.\n");
        }
        System.out.println();

        int n = 0; // длина хорды (элемента)
        for(;;) {
            System.out.printf("Введите длину элемента как целое число в диапазоне (0; %d], либо нажмите q для выхода:%n", (int) (2 * R));
            String entered = reader.nextLine();    // Ввод пользователя
            if (entered.equalsIgnoreCase("q")) return;
            try {
                n = Integer.parseInt(entered);
            } catch (NumberFormatException ex) {
                System.out.println("Ошибка. Вы ввели не целое число.\n");
                continue;
            }
            if (0 < n && n <= (int)(2 * R)) break;
            System.out.println("Ошибка. Вам следует ввести целое число в требуемом диапазоне.\n");
        }

        System.out.println("\nИтак, Вы ввели R = " + R + " в качестве радиуса и n = " + n + " в качестве длины элемента.");

        // Расчет макс. числа элементов на окружности
        double theta = getTheta(2 * R, n); // Это величина центрального угла (в радианах), опирающегося на хорду n
        System.out.println("Величина центрального угла, опирающегося на хорду n, равна " + theta + " рад.\n");
        double _nMax = 2 * Math.PI * R / ((1.0 + MIN_GAP) * arcLength(2 * R, theta));
        int nMax = getInt(_nMax);   // округляем вниз число элементов
        System.out.println("На окружности заданного радиуса может поместиться максимум " + nMax + " (до округления = " + _nMax + ") элементов.");
        if (nMax == 0) return;

        System.out.println("Далее полагаем, что центр окружности находится в точке (0; 0), и все координаты будут выведены относительно этого центра.\n");

        double alpha = 0.0;     // Это центральный угол (в радианах), опирающийся на хорду-промежуток - рассчитывается ниже
        if (nMax > 1) {
            // Расчет длины хорды-промежутка между хордами-элементами
            double gap = (2 * Math.PI * R - (double) nMax * n) / nMax;
            System.out.println("Рассчитанная длина одной хорды-промежутка (для равномерного расположения элементов) равна " + gap + ".");
            // Расчет центрального угла (в радианах), опирающегося на хорду-промежуток gap
            alpha = getTheta(2 * R, gap);
            System.out.println("Величина центрального угла, опирающегося на хорду-промежуток, равна " + alpha + " рад.\n");
        }

        // Расчет и вывод всех координат
        System.out.println("Координаты элементов на окружности при их равномерном распределении:");
        double angle = 0.0; // это угол "обхода" окружности; начальное значение соответствует первой координате (R; 0)
        for (int i = 1; i <= nMax; i++) {
            // увеличиваем угол "обхода" окружности на величину alpha, чтобы получить точку начала элемента
            if (i != 1) angle += alpha;
            // считаем координаты начала элемента:
            double startX = R * Math.cos(angle);
            double startY = R * Math.sin(angle);
            // увеличиваем угол "обхода" окружности на величину theta, чтобы получить точку конца элемента
            angle += theta;
            // считаем координаты конца элемента
            double endX = R * Math.cos(angle);
            double endY = R * Math.sin(angle);
            // вывод координат на экран (в консоль); в случае маленьких входных данных может потребоваться увеличить число цифр после . для отображения
            System.out.printf(Locale.US, "%d-й элемент начинается в точке (%.3f; %.3f) и заканчивается в точке (%.3f; %.3f). // Угол = %.6f рад%n", i, startX, startY, endX, endY, angle);
        }
    }

    // Метод возвращает длину дуги окружности (диаметром D), соответствующей центральному углу theta
    private static double arcLength(double D, double theta) {
        if (D <= 0 || theta < 0 || theta > Math.PI )    // Т.к. центральный угол здесь д.б. от 0 до PI
            throw new IllegalArgumentException("Неверные значения аргументов");
        return 0.5 * D * theta;
    }

    // Метод возвращает величину центрального угла (в радианах), опирающегося на хорду длиной h, в окружности диаметром D
    private static double getTheta(double D, double h) {
        if (D <= 0.0 || h <= 0.0 || h > D)      // Т.к. длина хорды не м.б. больше диаметра
            throw new IllegalArgumentException("Неверные значения аргументов");
        return 2 * Math.asin(h / D);
    }

    private static int getInt(double x) {
        double temp = (double) Math.round(x);
        // System.out.println(Math.abs( temp - x ));
        if ( Math.abs( temp - x ) <= EPSILON ) return (int) temp ;
        return (int) x;
    }
}
