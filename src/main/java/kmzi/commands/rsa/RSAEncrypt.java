package kmzi.commands.rsa;

import picocli.CommandLine;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.util.Scanner;
import java.util.concurrent.Callable;

import static kmzi.utils.tools.*;

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

        int eCandidate;
        // Переберем пары числе e, d для исходных q и p
        System.out.println("Варианты пар (открытый ключ, закрытый ключ) для данных параметров p и q:");
        for (int i=2000; i<2020; i++){
            eCandidate= getE(p, q, i);
            if(eCandidate!=-1) {
                System.out.println("e=" + eCandidate + " d=" + getD(p, q, eCandidate));
            }
        }
        int N = p*q;
        Scanner in = new Scanner(System.in);
        System.out.print("Введите открытый ключ: ");
        int e = in.nextInt();
        
        byte[] inputFileAsByteArray = Files.readAllBytes(inputFile.toPath());
        
        // Создадим строку из байтов
        StringBuilder bitsAsString= new StringBuilder();
        for (byte b : inputFileAsByteArray) {
            if (b >= 0) {
                bitsAsString.append(String.format("%8s", Integer.toBinaryString(b)).replace(' ', '0'));
            }
            if (b < 0) {
                String str = Integer.toBinaryString(b);
                str = str.substring(24);
                bitsAsString.append(str);
            }
        }
        
        // N в бинарном виде
        StringBuilder resultString= new StringBuilder();
        String binaryN=Integer.toBinaryString(N);
        while ( bitsAsString.length() % (binaryN.length() - 1)!= 0 ) {
            bitsAsString.insert(0, "0");
        }

        // Строку битов в массив
        int[] bitsArray = stringToBitArray(bitsAsString.toString());
        for (int i=0; i < bitsArray.length; i++) {
            int [] mass=new int[binaryN.length()-1];
            if ( i < bitsArray.length+1-(binaryN.length()-1) ) {
                System.arraycopy(bitsArray, i, mass, 0, binaryN.length() - 1);
                int C= binaryToMandatory(mass);
                BigInteger n= new BigInteger(String.valueOf(N));
                BigInteger c= new BigInteger(String.valueOf(C));
                BigInteger r= (c.pow(e)).mod(n);
                StringBuilder str= new StringBuilder(r.toString(2));
                while (str.length()<binaryN.length()) {
                    str.insert(0, "0");
                }
                resultString.append(str);
                i+=binaryN.length()-2;}
        }
        int [] result = stringToBitArray(resultString.toString());
        // Массив битов в массив байтов
        byte[] mass = asByte(result);
        // Запишем в файл
        FileOutputStream fos = new FileOutputStream(outputFileName);
        fos.write(mass);
        fos.close();
        return 0;


    }
}
