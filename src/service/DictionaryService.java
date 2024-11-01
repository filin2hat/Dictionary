package service;

import entry.DictionaryEntry;

import java.util.List;

public interface DictionaryService {

    List<DictionaryEntry> readEntries();

    void addEntry(String key, String value);

    void deleteEntry(String key);

    DictionaryEntry searchEntry(String key);
}
