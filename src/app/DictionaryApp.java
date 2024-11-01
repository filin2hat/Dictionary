package app;

import app.command.*;
import service.DictionaryService;
import service.FileDictionaryService;
import validator.FiveDigitValidator;
import validator.FourLetterValidator;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class DictionaryApp {
    private static final Path SRC_DIRECTORY = Paths.get("src");
    private static final Path FOUR_LETTER_DICTIONARY_PATH = SRC_DIRECTORY.resolve("fourLetterDictionary.txt");
    private static final Path FIVE_DIGIT_DICTIONARY_PATH = SRC_DIRECTORY.resolve("fiveDigitDictionary.txt");

    private final Map<Integer, DictionaryService> dictionaryMap = new HashMap<>();
    private final Map<Integer, DictionaryCommand> commandMap = new HashMap<>();
    private DictionaryService currentDictionary;

    public DictionaryApp() {
        initializeDictionaries();
    }

    private void initializeDictionaries() {
        dictionaryMap.put(1, new FileDictionaryService(FOUR_LETTER_DICTIONARY_PATH, new FourLetterValidator()));
        dictionaryMap.put(2, new FileDictionaryService(FIVE_DIGIT_DICTIONARY_PATH, new FiveDigitValidator()));
    }

    private void initializeCommands() {
        commandMap.put(1, new DisplayEntriesCommand(currentDictionary));
        commandMap.put(2, new SearchEntryCommand(currentDictionary));
        commandMap.put(3, new AddEntryCommand(currentDictionary));
        commandMap.put(4, new DeleteEntryCommand(currentDictionary));
    }

    public void start() {
        var scanner = new Scanner(System.in);
        while (true) {
            System.out.println("""
                    \nВыберите словарь:
                    1. Словарь с 4-буквенными ключами (пример: test - тест).
                    2. Словарь с 5-цифровыми ключами (пример: 12345 - один два три четыре пять).
                    0. Выход.
                    \nВаш выбор:
                    """);
            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 0) break;

            currentDictionary = dictionaryMap.get(choice);
            if (currentDictionary == null) {
                System.out.println("\nНеверный выбор, попробуйте снова.");
            } else {
                initializeCommands();
                displayMenu(scanner);
            }
        }
        scanner.close();
    }

    private void displayMenu(Scanner scanner) {
        while (true) {
            System.out.println("""
                    \nМеню:
                    1. Просмотреть содержимое словаря
                    2. Найти запись по ключу
                    3. Добавить запись
                    4. Удалить запись
                    0. Назад
                    \nВаш выбор:
                    """);
            int action = scanner.nextInt();
            scanner.nextLine();

            if (action == 0) break;

            DictionaryCommand command = commandMap.get(action);
            if (command != null) {
                command.execute(scanner);
            } else {
                System.out.println("\nНеверный выбор, попробуйте снова.");
            }
        }
    }
}
