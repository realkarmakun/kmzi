package kmzi.commands.pseudorandomgen;

import picocli.CommandLine;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.LongBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.concurrent.Callable;

@CommandLine.Command(name = "pseudorandom", aliases = "psdgen",
        description = "Линейный генератор псевдослучайной последовательности. x(n+1)=ax(n)+c mod m")
public class PseudorandomGen implements Callable<Integer> {

    @CommandLine.Option(names = {"-a"}, description = "Значение a", required = true)
    public Integer a;

    @CommandLine.Option(names = {"-c"}, description = "Значение c", required = true)
    public Integer c;

    @CommandLine.Option(names = {"-m", "--mod"}, description = "Значение модуля m", required = true)
    public Integer m;

    @CommandLine.Option(names = {"-x"}, description = "Значение x(0)", required = true)
    public Integer x;



    @Override
    public Integer call() throws Exception {
        // Счетчики четных нечетных
        int odd = 0;
        int even = 0;

        // Счетчик установленных битов
        int amountOfSetBits = 0;

        // Количество добавлений
        int iteration = 0;

        // x(n)
        int currentX = x;

        // x(0)
        BitSet x0AsBitSet = BitSet.valueOf(new byte[] {x.byteValue()});

        // Пустые биты
        BitSet sequence = new BitSet();
        boolean keepSearching = true;
        do {
            // Считаем итерации
            iteration++;
            // Добавляем в последовательность байт текущей итерации
            BitSet currentByteAsBitSet = BitSet.valueOf(new byte[] {(byte) currentX});
            sequence = addToBitSet(sequence, currentByteAsBitSet);

            // Количество установленных битов
            amountOfSetBits += currentByteAsBitSet.cardinality();

            // Четное или нечетное число
            if (currentByteAsBitSet.get(7)) {
                odd++;
            } else {
                even++;
            }

            // Если последний байт равен x(0), и это не первая итерация, заканчиваем цикл, нашли период
            if (currentByteAsBitSet.equals(x0AsBitSet) && iteration != 1) {
                keepSearching = false;
            } else {
                // Иначе подсчет x(n) и проходим цикл заново
                currentX = (a * currentX + c) % m;
            }

        } while (keepSearching);

        // Выводы
        System.out.println("Длина периода:" + sequence.size());
        System.out.println("Количество установленных бит (единицы): " + amountOfSetBits);
        System.out.println("Количетсво неустановленных бит (нули): " + (sequence.size() - amountOfSetBits));
        System.out.println("Нечетные числа: " + odd);
        System.out.println("Четные числа: " + even);

        System.out.println("Бинарный вид: ");
        //System.out.println(Arrays.toString(sequence.toByteArray()));
        printBitSetAsBinaryString(sequence);

        return 0;
    }

    // Метод для добавления BitSet к другому BitSet
    public BitSet addToBitSet(BitSet whereToAdd, BitSet whatToAdd) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(whereToAdd.toByteArray());
        outputStream.write(whatToAdd.toByteArray());
        outputStream.close();
        return BitSet.valueOf(outputStream.toByteArray());
    }

    // Печать BitSet как бинарной строки
    public void printBitSetAsBinaryString(BitSet bitSet) {
        byte[] bitSetAsByteArray = bitSet.toByteArray();
        for (int i = 0; i < bitSetAsByteArray.length; i++) {
            System.out.println(i + " байт: " +(bitSetAsByteArray[i]) + " | " + Integer.toBinaryString(bitSetAsByteArray[i] & 0xFF));
        }

    }
}