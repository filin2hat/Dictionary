package service;

import validator.DictionaryValidator;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * Класс служит для работы со словарем в файле
 */
public class FileDictionaryService implements DictionaryService {
    private final Path filePath;
    private final DictionaryValidator validator;
    private final Map<String, String> entries;

    public FileDictionaryService(Path filePath, DictionaryValidator validator) {
        this.filePath = filePath;
        this.validator = validator;
        this.entries = loadEntriesFromFile();
    }

    private Map<String, String> loadEntriesFromFile() {
        Map<String, String> loadedEntries = new HashMap<>();
        if (Files.exists(filePath)) {
            try (BufferedReader reader = Files.newBufferedReader(filePath)) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(" - ", 2);
                    if (parts.length == 2) {
                        String key = parts[0].trim();
                        String value = parts[1].trim();
                        loadedEntries.put(key, value);
                    }
                }
            } catch (IOException e) {
                System.err.println("\n!!!Ошибка при чтении файла: " + e.getMessage());
            }
        }
        return loadedEntries;
    }

    private void saveEntriesToFile() {
        try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
            for (Map.Entry<String, String> entry : entries.entrySet()) {
                writer.write(entry.getKey() + " - " + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("\n!!!Ошибка при записи файла: " + e.getMessage());
        }
    }

    @Override
    public Map<String, String> readEntries() {
        return new HashMap<>(entries);
    }

    @Override
    public void addEntry(String key, String value) {
        if (validator.isValidKey(key)) {
            entries.put(key, value);
            saveEntriesToFile();
        } else {
            throw new IllegalArgumentException("\n!!!Недопустимый формат ключа!!!");
        }
    }

    @Override
    public void deleteEntry(String key) {
        if (entries.containsKey(key)) {
            entries.remove(key);
            saveEntriesToFile();
        } else {
            System.out.println("Ключ не найден.");
        }
    }

    @Override
    public String searchEntry(String key) {
        return entries.get(key);
    }
}
