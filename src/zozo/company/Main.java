package zozo.company;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {

    public static void main(String[] args) {
        String path = "Q:\\Games\\savegames\\";

        List<GameProgress> gameProgresses = Arrays.asList(
                new GameProgress(6, 2, 3, 2.5),
                new GameProgress(60, 20, 20, 42.5),
                new GameProgress(30, 20, 45, 20)
        );

        List<String> saved = new ArrayList<>();
        for (int i = 0; i < gameProgresses.size(); i++) {
            String datPath = path  + "save" + i + ".dat";
            if (saveGame(datPath, gameProgresses.get(i)))
                saved.add(datPath);
        }

        zip(path + "zip.zip", saved);
        delete(saved);
    }

    public static Boolean saveGame(String path, GameProgress gp) {
        try (FileOutputStream stream = new FileOutputStream(path);
             ObjectOutputStream outputStream = new ObjectOutputStream(stream)) {
            outputStream.writeObject(gp);
            System.out.println("Done: " + path);
            return true;
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
            return false;
        }
    }

    private static void delete(List<String> savedFiles) {
        for (String sf : savedFiles) {
            File fileToDel = new File(sf);
            if (fileToDel.delete()) {
                System.out.println(sf + " удален!");
            } else {
                System.out.println(sf + " не был удален!");
            }
        }
    }

    public static void zip(String path, List<String> savedFiles) {
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(path))) {
            System.out.println("Архив: " + path);
            for (String sf : savedFiles) {
                try (FileInputStream fileInputStream = new FileInputStream(sf)) {
                    ZipEntry entry = new ZipEntry(new File(sf).getName());
                    zipOutputStream.putNextEntry(entry);
                    byte[] buffer = new byte[fileInputStream.available()];
                    fileInputStream.read(buffer);
                    zipOutputStream.write(buffer);
                    zipOutputStream.closeEntry();
                    System.out.println(sf + " добавлен в архив!");
                } catch (IOException ex) {
                    System.out.println("Error: " + ex.getMessage());
                }
            }
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }


}
