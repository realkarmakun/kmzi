package kmzi.commands.permutation;

import kmzi.utils.ByteBlock;
import picocli.CommandLine;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.concurrent.Callable;

@CommandLine.Command(name = "encrypt", aliases = "e", description = "Шифрование шифром подстановки")
public class PREncrypt implements Callable<Integer> {

    @CommandLine.Parameters(description = "Файл, содержимое которого будет шифроваться.")
    public File inputFile;

    @CommandLine.Option(names = {"-k", "--key"}, split = ",", description = "Перестановка, " +
            "которая будет использоватся как ключ.", required = true)
    public ArrayList<Integer> keyPermutation;

    @CommandLine.Option(names = {"-o", "--output"}, description = "Имя результирующего файла")
    public String outputFileName = "output.txt";


    @Override
    public Integer call() throws Exception {

        // Получим размер ключа
        int keySize = keyPermutation.size();

        // Читаем файл по байтово и сохраним в массив
        FileInputStream fileInputStream = new FileInputStream(inputFile);
        byte[] byteInputFile = new byte[(int) inputFile.length()];
        fileInputStream.read(byteInputFile);
        fileInputStream.close();

        // Создадим массив из блоков байтов
        ArrayList<ByteBlock> openTextBlocks = new ArrayList<>();

        // Создадим первый блок байтов (размером с ключ)
        ByteBlock byteBlock = new ByteBlock(keySize);


        // Теперь пройдемся по ОТ, разделим весь текст на блоки по 6 байтов
        for (int i = 0; i < (byteInputFile.length); i++) {
            if (i % keySize == 0) {
                byteBlock = new ByteBlock(keySize);
                byteBlock.setAt(i % keySize,byteInputFile[i]);
                openTextBlocks.add(byteBlock);
            } else {
                byteBlock.setAt(i % keySize, byteInputFile[i]);
            }
        }

        // Теперь перемешаем каждый блок согласно ключу (см. методы ByteBlock)
        // И сразу подготовим байтовый поток, чтобы записать данные из блоков обратно в файл
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        for (ByteBlock openTextBlock : openTextBlocks) {
            openTextBlock.shuffle(keyPermutation);
            outputStream.write(openTextBlock.getBlockData());
        }

        byte[] byteOutputFile = outputStream.toByteArray();

        FileOutputStream fileOutputStream = new FileOutputStream(outputFileName);
        fileOutputStream.write(byteOutputFile);


        return 0;
    }


}
