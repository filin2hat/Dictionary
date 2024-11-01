package service;

import java.util.Map;

/**
 * Интерфейс сервиса словаря
 */
public interface DictionaryService {

    Map<String, String> readEntries();

    void addEntry(String key, String value);

    void deleteEntry(String key);

    String searchEntry(String key);
}
