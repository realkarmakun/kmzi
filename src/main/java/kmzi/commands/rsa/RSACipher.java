package kmzi.commands.rsa;

import picocli.CommandLine;

@CommandLine.Command(name = "rsa", subcommands = {
        RSAEncrypt.class,
        RSADecrypt.class
})
public class RSACipher {
    // перевод из 2 системы в 10
    public static int BiTen(int[] bi) {
        int ten=0;
        if (bi[0]==0) { //число положительное
            for (int i = 0; i < bi.length; i++) {
                ten = ten + bi[i] * (int) (Math.pow(2, bi.length - i - 1));
            }
        }
        if (bi[0]==1) { //число отрицательное
            for (int i = 0; i < bi.length; i++) {
                ten = ten + bi[i] * (int) (Math.pow(2, bi.length - i - 1));
            }
        }
        return ten;
    }
    //из массива битов в массив байтов
    public static byte[] inByte(int[] rez){
        byte[] mass=new byte[rez.length/8]; //обрабатываем блоки по 8
        for (int i=0; i<mass.length; i++ ) {
            int[] m = new int[8];
            for (int j=0; j<8; j++) {
                m[j]=rez[i*8+j];
            }
            mass[i]=(byte)BiTen(m);
        }
        return mass;
    }

    //строку битов в массив битов
    public static int[] str_arr2(String bi)  {
        String[] items = bi.split(""); //массив items 0,1 текста
        int[] results = new int[items.length];
        for (int i = 0; i < items.length; i++) {  //заполнили массив int битами
            results[i] = Integer.parseInt(items[i]);
        }
        return results;
    }

    public static int nod(int a,int b) { //функция нахождения НОД
        while (b !=0) {
            int tmp = a%b;
            a = b;
            b = tmp;
        }
        return a;
    }

    public static int e(int p, int q, int e){ //функция проверки е условию: НОД(e,(p-1)(q-1))=1.
        if (nod(e, ((p-1)*(q-1)))==1) return e;
        else return -1;
    }

    public static int d(int p, int q, int e){ //функция нахождения d
        int d=inv(e,(p-1)*(q-1))%((p-1)*(q-1));
        return d;
    }

    public  static int inv(int a, int m){ //функция нахождения обратного по модулю
        int inv=-1;
        for (int i=0; i<m; i++){
            if (i*a%m==1) inv=i;
        }
        return inv;
    }

}
