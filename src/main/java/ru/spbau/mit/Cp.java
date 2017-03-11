package ru.spbau.mit;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Cp {
    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            System.out.println("Usage: file-source file-destination");
        }

        FileInputStream in = new FileInputStream(args[0]);
        FileOutputStream out = new FileOutputStream(args[1]);

        byte[] buf = new byte[1024];
        int r;
        while ((r = in.read(buf)) != -1) {
            out.write(buf, 0, r);
        }

        in.close();
        out.close();
    }
}
