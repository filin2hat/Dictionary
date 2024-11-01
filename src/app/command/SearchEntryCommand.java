package app.command;

import service.DictionaryService;
import java.util.Scanner;

public class SearchEntryCommand implements DictionaryCommand {
    private final DictionaryService dictionary;

    public SearchEntryCommand(DictionaryService dictionary) {
        this.dictionary = dictionary;
    }

    @Override
    public void execute(Scanner scanner) {
        System.out.print("\nВведите ключ для поиска: ");
        String key = scanner.nextLine();
        String value = dictionary.searchEntry(key);
        if (value != null) {
            System.out.println("\nНайдена запись: " + key + " - " + value);
        } else {
            System.out.println("\nЗапись с ключом '" + key + "' не найдена.");
        }
    }
}
