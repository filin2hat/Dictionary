package service;

import util.FileUtils;
import validator.DictionaryValidator;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * Класс для работы со словарем в файле.
 */
public class FileDictionaryService implements DictionaryService {
    private final Path filePath;
    private final DictionaryValidator validator;
    private final Map<String, String> entries;

    public FileDictionaryService(Path filePath, DictionaryValidator validator) {
        this.filePath = filePath;
        this.validator = validator;
        this.entries = FileUtils.readEntriesFromFile(filePath);
    }

    @Override
    public Map<String, String> readEntries() {
        return new HashMap<>(entries);
    }

    @Override
    public void addEntry(String key, String value) {
        if (validator.isValidKey(key)) {
            entries.put(key, value);
            FileUtils.writeEntriesToFile(filePath, entries);
            System.out.println("Запись добавлена.");
        } else {
            throw new IllegalArgumentException("\n!!!Недопустимый формат ключа!!!");
        }
    }

    @Override
    public void deleteEntry(String key) {
        if (entries.containsKey(key)) {
            entries.remove(key);
            FileUtils.writeEntriesToFile(filePath, entries);
            System.out.println("Запись удалена.");
        } else {
            System.out.println("Запись с таким ключом не найдена.");
        }
    }

    @Override
    public String searchEntry(String key) {
        return entries.get(key);
    }
}
