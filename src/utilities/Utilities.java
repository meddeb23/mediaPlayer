package utilities;

public class Utilities {
    public static String getExtension(String fileName) {
        String fe = "";
        int i = fileName.lastIndexOf('.');
        if (i > 0) {
            fe = fileName.substring(i + 1);
        }
        return fe;
    }
}
