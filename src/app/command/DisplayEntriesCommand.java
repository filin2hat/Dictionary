package app.command;

import service.DictionaryService;
import java.util.Map;
import java.util.Scanner;

public class DisplayEntriesCommand implements DictionaryCommand {
    private final DictionaryService dictionary;

    public DisplayEntriesCommand(DictionaryService dictionary) {
        this.dictionary = dictionary;
    }

    @Override
    public void execute(Scanner scanner) {
        Map<String, String> entries = dictionary.readEntries();
        if (entries.isEmpty()) {
            System.out.println("\nСловарь пуст.");
        } else {
            System.out.println("\nСодержимое словаря:");
            entries.forEach((key, value) -> System.out.println(key + " - " + value));
        }
    }
}
