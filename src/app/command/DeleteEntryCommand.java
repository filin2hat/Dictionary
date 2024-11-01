package app.command;

import service.DictionaryService;
import java.util.Scanner;

public class DeleteEntryCommand implements DictionaryCommand {
    private final DictionaryService dictionary;

    public DeleteEntryCommand(DictionaryService dictionary) {
        this.dictionary = dictionary;
    }

    @Override
    public void execute(Scanner scanner) {
        System.out.print("\nВведите ключ для удаления: ");
        String key = scanner.nextLine();

        if (dictionary.readEntries().containsKey(key)) {
            dictionary.deleteEntry(key);
            System.out.println("\nЗапись удалена.");
        } else {
            System.out.println("\nЗапись с таким ключом не найдена.");
        }
    }
}
