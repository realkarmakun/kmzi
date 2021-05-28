package kmzi.commands.crc;

import kmzi.commands.affine.AFDecrypt;
import kmzi.commands.affine.AFEncrypt;
import picocli.CommandLine;

import java.util.concurrent.Callable;

@CommandLine.Command(name = "hash")
public class CRCHash implements Callable<Integer> {

    @CommandLine.Parameters(description = "Коэффиценты G(x)=a₈x⁸+a₇x⁷+a₆x⁶+a₅x⁵+a₄x⁴+a₃x³+a₂x²+a₁x+a₀")
    public int g_x;

    @CommandLine.Parameters(description = "Коэффиценты P(x)=a₈x⁸+a₇x⁷+a₆x⁶+a₅x⁵+a₄x⁴+a₃x³+a₂x²+a₁x+a₀")
    public int p_x;

    @CommandLine.Option(names = {"-c"}, description = "Вывод информации по коллизиям.")
    public boolean collisions;

    @Override
    public Integer call() throws Exception {
        System.out.println("Результат: "+ calculateCrc(p_x, g_x));
        if (collisions) {
            collisions(g_x);
        }
        return 0;
    }
    
    // Функция нахождения CRC хеша
    public static int calculateCrc(int p_x, int g_x) {

        // Если p=0 -> остаток = 0
        if (p_x == 0) return 0;
        else {
            String p_str = Integer.toString(p_x);
            String g_str = Integer.toString(g_x);

            //  Добавим в конец нули (количество нулей = количеству разрядов g_x)
            for (int i = 0; i < g_str.length(); i++) {
                p_str += '0';
            }
            char[] p_arr = p_str.toCharArray();
            char[] g_arr = g_str.toCharArray();
            int[] p = new int[p_str.length()];
            int[] g = new int[g_str.length()];
            for (int i = 0; i < p_str.length(); i++) {
                p[i] = p_arr[i] - '0';
            }
            for (int i = 0; i < g_str.length(); i++) {
                g[i] = g_arr[i] - '0';
            }
            int g_bi = 0;
            for (int k : g) {
                g_bi = 10 * g_bi + k;
            }
            int len = p.length;

            // Проходим по P(X)
            for (int i = 0; i < len - g.length + 1; i++) {
                while (p[i] == 0) {
                    i++;
                }
                // Проводим деление
                for (int j = 0; j < g.length; j++) {
                    p[i + j] = p[i + j] ^ g[j];
                }

                int c = 0;
                int num = 0;
                for (int j = 0; j < p.length; j++) {
                    if (j > len - g_arr.length - 1) {
                        num = num * 10 + p[j];
                    } else {
                        if (p[j] == 0) c++;
                    }
                }
                // Когда остаток < g, завершаем деление
                if ((c == len - g_arr.length) && (num < g_bi))
                    i = len;
            }
            int rez = 0;
            for (int i = len - g_arr.length; i < p.length; i++) {
                rez = rez * 10 + p[i];
            }
            return rez;
        }
    }
    
    // Функция поиска коллизий
    public static void collisions(int g_x){

        int c = Integer.parseInt(String.valueOf(g_x), 2);
        int [] collisions = new int[c];

        // Переберем все восьмибитные числа
        for (int i=0; i<256; i++){
            String bi = Integer.toBinaryString(i);
            char [] bin = bi.toCharArray();
            int p = 0;
            for (char value : bin) {
                p = p * 10 + (value - '0');
            }
            for (int j=0; j<collisions.length;j++) {
                if(Integer.parseInt(String.valueOf(calculateCrc(p,g_x)), 2) == j) {
                    collisions[j]++;
                }
            }
        }
        for (int j=0; j < collisions.length; j++) {
            System.out.println(Integer.toBinaryString(j) + " - " + collisions[j] + " коллизий");
        }
    }


}
