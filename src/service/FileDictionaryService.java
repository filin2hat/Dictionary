package service;

import entry.DictionaryEntry;
import validator.DictionaryValidator;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс служит для работы со словарем в файле
 */
public class FileDictionaryService implements DictionaryService {
    private Path filePath;
    private DictionaryValidator validator;
    private List<DictionaryEntry> entries;

    public FileDictionaryService(Path filePath, DictionaryValidator validator) {
        this.filePath = filePath;
        this.validator = validator;
        this.entries = loadEntriesFromFile();
    }

    private List<DictionaryEntry> loadEntriesFromFile() {
        List<DictionaryEntry> loadedEntries = new ArrayList<>();
        if (Files.exists(filePath)) {

            try (BufferedReader reader = Files.newBufferedReader(filePath)) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(" - ", 2);
                    if (parts.length == 2) {
                        String key = parts[0].trim();
                        String value = parts[1].trim();
                        loadedEntries.add(new DictionaryEntry(key, value));
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
            for (DictionaryEntry entry : entries) {
                writer.write(entry.getKey() + " - " + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("\n!!!Ошибка при записи файла: " + e.getMessage());
        }
    }

    @Override
    public List<DictionaryEntry> readEntries() {
        return new ArrayList<>(entries);
    }

    @Override
    public void addEntry(String key, String value) {
        if (validator.isValidKey(key)) {
            entries.add(new DictionaryEntry(key, value));
            saveEntriesToFile();
        } else {
            throw new IllegalArgumentException("\n!!!Недопустимый формат ключа!!!");
        }
    }

    @Override
    public void deleteEntry(String key) {
        entries.removeIf(entry -> entry.getKey().equals(key));
        saveEntriesToFile();
    }

    @Override
    public DictionaryEntry searchEntry(String key) {
        return entries.stream()
                .filter(entry -> entry.getKey().equals(key))
                .findFirst()
                .orElse(null);
    }
}
