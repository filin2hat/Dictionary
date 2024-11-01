package app.command;

import service.DictionaryService;
import java.util.Scanner;

public class AddEntryCommand implements DictionaryCommand {
    private final DictionaryService dictionary;

    public AddEntryCommand(DictionaryService dictionary) {
        this.dictionary = dictionary;
    }

    @Override
    public void execute(Scanner scanner) {
        System.out.print("\nВведите ключ: ");
        String key = scanner.nextLine();
        System.out.print("\nВведите значение: ");
        String value = scanner.nextLine();

        try {
            dictionary.addEntry(key, value);
            System.out.println("\nЗапись добавлена.");
        } catch (IllegalArgumentException e) {
            System.out.println("\nОшибка: " + e.getMessage());
        }
    }
}
