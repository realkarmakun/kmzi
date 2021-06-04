package kmzi.utils;

public class tools {
    public static int modInverse(int a, int mod) {
        a = a % mod;
        for (int x = 1; x < mod; x++)
            if ((a * x) % mod == 1)
                return x;
        return 1;
    }

    // перевод из 2 системы в 10
    public static int binaryToMandatory(int[] bi) {
        int ten = 0;
        if (bi[0] == 0) { //число положительное
            for (int i = 0; i < bi.length; i++) {
                ten = ten + bi[i] * (int) (Math.pow(2, bi.length - i - 1));
            }
        }
        if (bi[0] == 1) { //число отрицательное
            for (int i = 0; i < bi.length; i++) {
                ten = ten + bi[i] * (int) (Math.pow(2, bi.length - i - 1));
            }
        }
        return ten;
    }

    //из массива битов в массив байтов
    public static byte[] asByte(int[] rez) {
        byte[] mass = new byte[rez.length / 8];
        for (int i = 0; i < mass.length; i++) {
            int[] m = new int[8];
            System.arraycopy(rez, i * 8, m, 0, 8);
            mass[i] = (byte) binaryToMandatory(m);
        }
        return mass;
    }

    // Битовая строка в битовый массив
    public static int[] stringToBitArray(String bi) {
        String[] items = bi.split("");
        int[] results = new int[items.length];
        for (int i = 0; i < items.length; i++) {
            results[i] = Integer.parseInt(items[i]);
        }
        return results;
    }

    // НОД
    public static int nod(int a, int b) {
        while (b != 0) {
            int tmp = a % b;
            a = b;
            b = tmp;
        }
        return a;
    }

    // Проверка e: НОД(e,(p-1)(q-1))=1
    public static int getE(int p, int q, int e) {
        if (nod(e, ((p - 1) * (q - 1))) == 1) return e;
        else return -1;
    }

    // Вычисление d
    public static int getD(int p, int q, int e) {
        int d = altModInverse(e, (p - 1) * (q - 1)) % ((p - 1) * (q - 1));
        return d;
    }

    // Альтернативный способ нахождения обратного по модулю
    public static int altModInverse(int a, int m) {
        int inv = -1;
        for (int i = 0; i < m; i++) {
            if (i * a % m == 1) inv = i;
        }
        return inv;
    }


    public static int gcd(int a,int b) {
        while (b !=0) {
            int c = a%b;
            a = b;
            b = c;
        }
        return a;
    }


}
