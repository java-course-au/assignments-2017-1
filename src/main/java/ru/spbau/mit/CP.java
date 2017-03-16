package ru.spbau.mit;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class CP {
    private static final int MAX_SIZE = 1024;

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Not enough arguments provided.");
        } else {
            String from = args[0];
            String to = args[1];
            int bytesRead;
            byte[] buf = new byte[MAX_SIZE];

            try (FileInputStream fromStream = new FileInputStream(from);
                 FileOutputStream toStream = new FileOutputStream(to)) {
                while ((bytesRead = fromStream.read(buf)) != -1) {
                    toStream.write(buf, 0, bytesRead);
                }
            } catch (IOException e) {
                System.out.println("Mistake occured.");
            }
        }
    }
}
