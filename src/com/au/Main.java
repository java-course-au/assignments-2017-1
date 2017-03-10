package com.au;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Main {
    private static final int MAX_SIZE = 1000;

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Not enough arguments");
            return;
        }

        String fileFrom = args[0];
        String fileTo = args[1];
        FileInputStream reader;
        FileOutputStream writer;
        byte[] buffer = new byte[MAX_SIZE];

        try {
            reader = new FileInputStream(fileFrom);
        } catch (FileNotFoundException e) {
            System.out.println("Input file not found");
            return;
        }

        try {
            writer = new FileOutputStream(fileTo, false);
        } catch (FileNotFoundException e) {
            System.out.println("Could not create output file");
            return;
        }

        int curOffsetRead = 0;
        try {
            boolean fileHasEnded = false;
            while (!fileHasEnded) {
                int bytesRead = reader.read(buffer, curOffsetRead, MAX_SIZE);
                if (bytesRead == -1) {
                    fileHasEnded = true;
                } else {
                    writer.write(buffer, 0, bytesRead);
                }
            }
        } catch (IOException e) {
            System.out.println("Some exception occured while reading file");
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
