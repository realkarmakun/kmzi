package kmzi.commands.rsa;

import picocli.CommandLine;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.util.Scanner;
import java.util.concurrent.Callable;

import static kmzi.commands.rsa.RSACipher.*;

@CommandLine.Command(name = "encrypt", aliases = "e", description = "Шифрование через RSA")
public class RSAEncrypt implements Callable<Integer> {

    @CommandLine.Parameters(description = "Файл, содержимое которого будет шифроваться.")
    public File inputFile;

    @CommandLine.Option(names = {"-o", "--output"}, description = "Имя результирующего файла.")
    public String outputFileName = "b.txt";

    @CommandLine.Option(names = {"-q"}, description = "Значение q." , required = true)
    public int q;

    @CommandLine.Option(names = {"-p"}, description = "Значение p." , required = true)
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
        System.out.print("Введите открытый ключ: ");
        int e = in.nextInt();

        byte[] byteArray = Files.readAllBytes(inputFile.toPath());

        StringBuilder strB= new StringBuilder();
        // в цикле собирается строка битов в строку strB
        for (byte b : byteArray) {
            if (b >= 0) {
                strB.append(String.format("%8s", Integer.toBinaryString(b)).replace(' ', '0'));
            }
            if (b < 0) {
                String str = Integer.toBinaryString(b);
                str = str.substring(24);
                strB.append(str);
            }
        }

        StringBuilder resultString= new StringBuilder();
        String biN=Integer.toBinaryString(N);
        while (strB.length()%(biN.length()-1)!=0){
            strB.insert(0, "0");
        }
        // строку битов в массив
        int[] rez=str_arr2(strB.toString());
        for(int i=0; i<rez.length; i++){
            int [] mass=new int[biN.length()-1];
            if(i<rez.length+1-(biN.length()-1)){
                System.arraycopy(rez, i, mass, 0, biN.length() - 1);
                int C=BiTen(mass);
                BigInteger n= new BigInteger(String.valueOf(N));
                BigInteger c= new BigInteger(String.valueOf(C));
                BigInteger r= (c.pow(e)).mod(n);
                StringBuilder str= new StringBuilder(r.toString(2));
                while (str.length()<biN.length()) {
                    str.insert(0, "0");
                }
                resultString.append(str);
                i+=biN.length()-2;}
        }
        // результирующую строку в массив
        int [] result = str_arr2(resultString.toString());
        // массив битов в массив байтов
        byte[] mass = inByte(result);
        // запись в файл
        FileOutputStream fos = new FileOutputStream(outputFileName);
        fos.write(mass);
        fos.close();
        return 0;


    }
}
