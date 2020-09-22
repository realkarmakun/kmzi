package kmzi.commands.affine;

import picocli.CommandLine;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Vector;
import java.util.concurrent.Callable;


@CommandLine.Command(name = "encrypt", aliases = "e")
public class AFEncrypt implements Callable<Integer> {

    @CommandLine.Parameters()
    public File file;

    @CommandLine.Option(names = {"-o", "--output"})
    public String outputFileName;

    @CommandLine.Option(names = {"-k", "--key"}, split = ",", description = "Ключ для шифрования. " +
            "Например для передачи ключа (1,2) введите -k 1,2", required = true)
    public ArrayList<Integer> keys;


    @Override
    public Integer call() throws Exception {

        if (keys.size() != 2) {
            throw new Exception("ОШИБКА: Неправильный ключ! Он должен состоять из пары символов (a,b). " +
                    "Для подробностей используйте --help");
        }
        if (keys.get(0) <= 0 || keys.get(1) <= 0) {
            throw new Exception("ОШИБКА: Неправильные значения ключа! Ключ должен быть положительным.");
        }
        int a = keys.get(0);
        int b = keys.get(1);

        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] bInputFile = new byte[(int) file.length()];
        fileInputStream.read(bInputFile);
        fileInputStream.close();


        byte[] bOutFile = new byte[(int) file.length()];
        for (int i = 0; i < bInputFile.length; i++) {
            bOutFile[i] = (byte) (a * bInputFile[i] + b % 256);
        }

        FileOutputStream fileOutputStream = new FileOutputStream(outputFileName);
        fileOutputStream.write(bOutFile);

        return 0;
    }
}
