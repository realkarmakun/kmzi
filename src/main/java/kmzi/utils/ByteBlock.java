package kmzi.utils;

import java.util.ArrayList;

// Класс отвечающий объекту - Байтовый блок любого размера
public class ByteBlock {
    public int size;
    public byte[] blockData;

    public ByteBlock(int size) {
        this.size = size;
        this.blockData = new byte[(int) size];
    }


    public void setAt(int index, byte data) {
        this.blockData[index] = data;
    }

    public byte getAt(int index) { return this.blockData[index]; }

    public String toString() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < size; i++) {
            if (blockData[i] == (byte) 0) {
                result.append("_");
            } else {
                result.append((char) blockData[i]);
            }
        }
        return result.toString();
    }

    // Перемешивает блок согласно перестановке
    public void shuffle(ArrayList<Integer> key) {
        byte[] result = new byte[(int) size];
        for (int i = 0; i < size; i++) {
            int indexOfPermutation = key.get(i) - 1;
            result[i] = blockData[indexOfPermutation];
        }
        this.blockData = result;
    }

    // Возвращает блок в оригинальное состояние по перестановке
    public void unShuffle(ArrayList<Integer> key) {
        byte[] result = new byte[(int) size];
        for (int i = 0; i < size; i++) {
            int trueIndexOfPermutation = key.get(i) - 1;
            result[trueIndexOfPermutation] = blockData[i];
        }
        this.blockData = result;
        checkNull();
    }

    public byte[] getBlockData() {
        return this.blockData;
    }

    // Нормализуем текст (избавимся от нулевых чаров)
    public void checkNull() {
        char empty = ' ';
        for (int i = 0; i < size; i++) {
            if (blockData[i] == (byte) 0) {
                blockData[i] = (byte) empty;
            }
        }
    }











}
