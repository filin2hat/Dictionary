package util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class FileUtils {

    /**
     * Чтение записей из файла в Map.
     *
     * @param filePath путь к файлу словаря.
     * @return Map, содержащий записи словаря.
     */
    public static Map<String, String> readEntriesFromFile(Path filePath) {
        Map<String, String> entries = new HashMap<>();
        if (Files.exists(filePath)) {
            try (BufferedReader reader = Files.newBufferedReader(filePath)) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(" - ", 2);
                    if (parts.length == 2) {
                        String key = parts[0].trim();
                        String value = parts[1].trim();
                        entries.put(key, value);
                    }
                }
            } catch (IOException e) {
                System.err.println("Ошибка при чтении файла: " + e.getMessage());
            }
        }
        return entries;
    }

    /**
     * Запись записей из Map в файл.
     *
     * @param filePath путь к файлу словаря.
     * @param entries Map с записями для записи.
     */
    public static void writeEntriesToFile(Path filePath, Map<String, String> entries) {
        try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
            for (Map.Entry<String, String> entry : entries.entrySet()) {
                writer.write(entry.getKey() + " - " + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Ошибка при записи файла: " + e.getMessage());
        }
    }
}
