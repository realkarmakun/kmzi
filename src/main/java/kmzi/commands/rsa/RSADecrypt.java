package kmzi.commands.rsa;

import picocli.CommandLine;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Scanner;
import java.util.concurrent.Callable;

import static kmzi.utils.tools.*;

@CommandLine.Command(name = "decrypt", aliases = "d", description = "Расшифровка RSA")
public class RSADecrypt implements Callable<Integer> {

    @CommandLine.Parameters(description = "Файл, содержимое которого нужно расшифровать.")
    public File inputFile;

    @CommandLine.Option(names = {"-o", "--output"}, description = "Имя результирующего файла.")
    public String outputFileName = "с.txt";

    @CommandLine.Option(names = {"-q"}, description = "Значение q.", required = true)
    public int q;

    @CommandLine.Option(names = {"-p"}, description = "Значение p.", required = true)
    public int p;

    @Override
    public Integer call() throws Exception {
        int N = p*q;
        Scanner in = new Scanner(System.in);
        System.out.print("Введите закрытый ключ: ");
        int d = in.nextInt();

        byte[] inputFileAsByteArray = Files.readAllBytes(inputFile.toPath());

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

        int[] rez=stringToBitArray(bitsAsString.toString());
        StringBuilder resultString= new StringBuilder();
        String biN=Integer.toBinaryString(N);
        for(int i=0; i<rez.length; i++){
            int [] mass=new int[biN.length()];
            if(i<rez.length+1-biN.length()){
                System.arraycopy(rez, i, mass, 0, biN.length());
                int C=binaryToMandatory(mass);
                BigInteger n= new BigInteger(String.valueOf(N));
                BigInteger c= new BigInteger(String.valueOf(C));
                BigInteger r= (c.pow(d)).mod(n);
                StringBuilder str= new StringBuilder(r.toString(2));
                if(i!=0){
                    while (str.length()<biN.length()-1) {
                        str.insert(0, "0");
                    }}
                resultString.append(str);
                i+=biN.length()-1;}
        }
        int [] result=stringToBitArray(resultString.toString());
        byte[] mass=asByte(result);
        FileOutputStream fos = new FileOutputStream(outputFileName);
        fos.write(mass);
        fos.close();
        return 0;
    }
}
