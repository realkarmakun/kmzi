package kmzi.commands.rsa;

import picocli.CommandLine;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.util.Scanner;
import java.util.concurrent.Callable;

import static kmzi.commands.rsa.RSACipher.*;

@CommandLine.Command(name = "decrypt", aliases = "d", description = "Расшифровка RSA")
public class RSADecrypt implements Callable<Integer> {

    @CommandLine.Parameters(description = "Файл, содержимое которого расшифровываться.")
    public File inputFile;

    @CommandLine.Option(names = {"-o", "--output"}, description = "Имя результирующего файла.")
    public String outputFileName = "b.txt";

    @CommandLine.Option(names = {"-q"}, description = "Значение q.", required = true)
    public int q;

    @CommandLine.Option(names = {"-p"}, description = "Значение p.", required = true)
    public int p;

    @Override
    public Integer call() throws Exception {
        int ee;
        System.out.println("Варианты пар (открытый ключ, закрытый ключ) для данных параметров p и q:");
        // подбираем пары чисел e,d для данных q,p
        for (int i=2000; i<2020; i++){
            ee=e(p, q, i);
            if(ee!=-1) System.out.println("e="+ee+" d="+d(p, q, ee));
        }
        int N = p*q;
        Scanner in = new Scanner(System.in);
        System.out.print("Введите закрытый ключ: ");
        int d = in.nextInt();

        byte[] byteArray = Files.readAllBytes(inputFile.toPath()); //массив считанных байтов из текста

        String strB="";
        for (int i=0; i<byteArray.length; i++) {             //в цикле собирается строка битов в строку strB
            if(byteArray[i]>=0){
                strB =strB+String.format("%8s", Integer.toBinaryString(byteArray[i])).replace(' ', '0');}
            if(byteArray[i]<0){
                String str=Integer.toBinaryString(byteArray[i]);
                str=str.substring(24);
                strB =strB+str;
            }
        }

        int[] rez=str_arr2(strB);                             //строку битов в массив
        String rezultat="";
        String biN=Integer.toBinaryString(N);
        for(int i=0; i<rez.length; i++){
            int [] mass=new int[biN.length()];
            if(i<rez.length+1-biN.length()){
                for (int j=0; j<biN.length();j++){
                    mass[j]=rez[i+j];
                }
                int C=BiTen(mass);
                BigInteger n= new BigInteger(String.valueOf(N));
                BigInteger c= new BigInteger(String.valueOf(C));
                BigInteger r= (c.pow(d)).mod(n);
                String str= r.toString(2);
                if(i!=0){
                    while (str.length()<biN.length()-1) {
                        str="0"+str;
                    }}
                rezultat+=str;
                i+=biN.length()-1;}
        }
        int [] rezul=str_arr2(rezultat);                      //результирующую строку в массив
        byte[] mass=inByte(rezul);                           //массив битов в массив байтов
        FileOutputStream fos = new FileOutputStream(outputFileName);
        fos.write(mass);                                          //записали байты в файл
        fos.close();
        return 0;
    }
}
