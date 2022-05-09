package utilities;

import java.io.File;
import java.io.FileInputStream;

public class Utilities {
    public static String getExtension(String fileName) {
        String fe = "";
        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            fe = fileName.substring(i + 1);
        }
        return fe;
    }

    public static String read_metadata(File song) {
        try {
            FileInputStream file = new FileInputStream(song);
            int size = (int) song.length();
            // file.skip(size - 128);
            byte[] last128 = new byte[size];
            file.read(last128);
            System.out.println(last128.toString());
            String id3 = new String(last128);
            String tag = id3.substring(0, 3);
            System.out.println(tag);
            if (tag.equals("ID3")) {
                System.out.println("Title: " + id3.substring(18, 48).replace("TPE", ""));
                System.out.println("Artist: " + id3.substring(33, 62));
                System.out.println("Album: " + id3.substring(63, 91));
                System.out.println("Year: " + id3.substring(93, 97));
            } else
                System.out.println(" does not contain" + " ID3 information.");
            file.close();
            return id3.substring(18, 48).replace("TPE", "");

        } catch (Exception e) {
            System.out.println("Error - " + e.toString());
        }
        return null;
    }
}
