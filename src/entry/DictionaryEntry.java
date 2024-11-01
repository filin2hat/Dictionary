package entry;

/**
 * Класс записи словаря (пара ключ-значение)
 */
public class DictionaryEntry {
    private String key;
    private String value;

    public DictionaryEntry(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return key + " - " + value;
    }
}
