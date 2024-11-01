package service;

import entry.DictionaryEntry;
import validator.DictionaryValidator;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class FileDictionaryService implements DictionaryService {
    private Path filePath;                      // Путь к файлу словаря
    private DictionaryValidator validator;      // Валидация для ключей словаря
    private List<DictionaryEntry> entries;      // Список всех записей в словаре

    public FileDictionaryService(Path filePath, DictionaryValidator validator) {
        this.filePath = filePath;
        this.validator = validator;

        createFileIfNotExists();
        this.entries = loadEntriesFromFile();
    }

    private void createFileIfNotExists() {
        try {
            if (Files.notExists(filePath)) {
                Files.createFile(filePath);
                System.out.println("Создан файл словаря: " + filePath);
            }
        } catch (IOException e) {
            System.err.println("Ошибка при создании файла: " + e.getMessage());
        }
    }

    private List<DictionaryEntry> loadEntriesFromFile() {
        List<DictionaryEntry> loadedEntries = new ArrayList<>();

        // Проверка существования файла и его чтение
        if (Files.exists(filePath)) {

            try (BufferedReader reader = Files.newBufferedReader(filePath)) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(" - ", 2); // Разделяем строку на ключ и значение
                    if (parts.length == 2) {
                        String key = parts[0].trim();
                        String value = parts[1].trim();
                        loadedEntries.add(new DictionaryEntry(key, value));
                    }
                }
            } catch (IOException e) {
                System.err.println("Ошибка при чтении файла: " + e.getMessage());
            }
        }
        return loadedEntries;
    }

    // Метод для сохранения всех записей в файл
    private void saveEntriesToFile() {
        // Используем try-with-resources для автоматического закрытия потока
        try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
            for (DictionaryEntry entry : entries) {
                writer.write(entry.getKey() + " - " + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Ошибка при записи файла: " + e.getMessage());
        }
    }

    // Чтение записей из списка
    @Override
    public List<DictionaryEntry> readEntries() {
        return new ArrayList<>(entries); // Возвращаем копию списка
    }

    // Добавление записи, если ключ соответствует правилам валидации
    @Override
    public void addEntry(String key, String value) {
        if (validator.isValidKey(key)) {
            entries.add(new DictionaryEntry(key, value));
            saveEntriesToFile();
        } else {
            throw new IllegalArgumentException("Недопустимый формат ключа");
        }
    }

    // Удаление записи по ключу
    @Override
    public void deleteEntry(String key) {
        entries.removeIf(entry -> entry.getKey().equals(key));
        saveEntriesToFile();
    }

    // Поиск записи по ключу
    @Override
    public DictionaryEntry searchEntry(String key) {
        return entries.stream()
                .filter(entry -> entry.getKey().equals(key))
                .findFirst()
                .orElse(null);
    }
}
