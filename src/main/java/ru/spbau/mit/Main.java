package ru.spbau.mit;

import java.io.*;

public final class Main {

    private static final int BUFFER_SIZE = 4 * 1024;

    private Main() {
    }

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Wrong argument number");
            return;
        }

        final String srcFilename = args[0];
        final String dstFilename = args[1];

        try (InputStream srcStream = new FileInputStream(srcFilename)) {
            try (OutputStream dstStream = new FileOutputStream(dstFilename)) {
                copy(srcStream, dstStream);
            } catch (FileNotFoundException e) {
                System.out.println("Can't open output file");
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            System.out.println("Can't open input file");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private static void copy(InputStream src, OutputStream dst) throws IOException {
        byte[] buffer = new byte[BUFFER_SIZE];
        int readCnt = src.read(buffer);

        while (readCnt != -1) {
            dst.write(buffer, 0, readCnt);
            readCnt = src.read(buffer);
        }
    }

}
