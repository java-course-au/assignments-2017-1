package ru.spbau.mit;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Cp {
    public static void main(String[] args) {
        final int SIZE_MAX = 8;
        byte[] buffer = new byte[SIZE_MAX];
        try (FileInputStream f1 = new FileInputStream(args[0]);
             FileOutputStream f2 = new FileOutputStream(args[1]))
        {
            int len;
            while((len = f1.read(buffer)) != -1) {
                f2.write(buffer, 0, len);
            }
        } catch (IOException e) {
            System.out.println("I/O ERROR!");
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("ERROR: command take two parameters");
        }
    }
}
