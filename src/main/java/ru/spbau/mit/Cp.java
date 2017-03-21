package ru.spbau.mit;

import java.io.*;

public final class Cp {
    private static final int MAX_BUFFER_SIZE = 128;

    private Cp() {}
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Invalid number of arguments");
            return;
        }

        final String input = args[0];
        final String output = args[1];
        byte[] buffer = new byte[MAX_BUFFER_SIZE];
        try (BufferedInputStream bufferedInputStream = new BufferedInputStream(
                    new FileInputStream(input), MAX_BUFFER_SIZE);
             BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(
                     new FileOutputStream(output), MAX_BUFFER_SIZE)) {
            int numBytes;
            while ((numBytes = bufferedInputStream.read(buffer)) != -1) {
                bufferedOutputStream.write(buffer, 0, numBytes);
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println("Can not read from file.");
            System.out.println(e.getMessage());
        }
    }
}
