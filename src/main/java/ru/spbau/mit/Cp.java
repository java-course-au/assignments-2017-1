package ru.spbau.mit;

import java.io.*;

public final class Cp {

    private Cp() {

    }

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("cp take two parameters!");
        } else {
            try (FileInputStream inStream = new FileInputStream(new File(args[0]));
                FileOutputStream outStream = new FileOutputStream(new File(args[1]))) {
                final int bufSize = 1024;
                byte[] buff = new byte[bufSize];
                int res = 0;
                while ((res = inStream.read(buff, 0, bufSize)) != -1) {
                    outStream.write(buff, 0, res);
                }

            } catch (FileNotFoundException e) {
                System.out.println("Cannot find file, please input path to file again");
                System.exit(-1);
            } catch (IOException e) {
                System.out.println("Cannot read file!");
                System.exit(-1);
            }
        }
    }
}
