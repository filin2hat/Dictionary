package app;

import service.DictionaryService;
import service.FileDictionaryService;
import validator.FiveDigitValidator;
import validator.FourLetterValidator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Scanner;

public class DictionaryApp {
    private static final Path SRC_DIRECTORY = Paths.get("src");
    private static final Path FOUR_LETTER_DICTIONARY_PATH = SRC_DIRECTORY.resolve("fourLetterDictionary.txt");
    private static final Path FIVE_DIGIT_DICTIONARY_PATH = SRC_DIRECTORY.resolve("fiveDigitDictionary.txt");

    private DictionaryService currentDictionary;
    private final DictionaryService fourLetterDictionary;
    private final DictionaryService fiveDigitDictionary;

    public DictionaryApp() {
        initializeDictionaryFiles();

        this.fourLetterDictionary = new FileDictionaryService(
                FOUR_LETTER_DICTIONARY_PATH,
                new FourLetterValidator()
        );

        this.fiveDigitDictionary = new FileDictionaryService(
                FIVE_DIGIT_DICTIONARY_PATH,
                new FiveDigitValidator()
        );
    }

    private void initializeDictionaryFiles() {
        try {
            if (Files.notExists(SRC_DIRECTORY)) {
                Files.createDirectory(SRC_DIRECTORY);
                System.out.println("\nСоздана директория для файлов словарей.");
            }

            if (Files.notExists(FOUR_LETTER_DICTIONARY_PATH)) {
                Files.createFile(FOUR_LETTER_DICTIONARY_PATH);
                System.out.println("\nСоздан файл словаря с 4-буквенными ключами.");
            }

            if (Files.notExists(FIVE_DIGIT_DICTIONARY_PATH)) {
                Files.createFile(FIVE_DIGIT_DICTIONARY_PATH);
                System.out.println("\nСоздан файл словаря с 5-цифровыми ключами.");
            }
        } catch (IOException e) {
            System.err.println("\nОшибка при создании директории или файлов словарей: " + e.getMessage());
        }
    }

    public void start() {
        var scanner = new Scanner(System.in);
        var running = true;

        while (running) {
            System.out.println("""
                    \nВыберите словарь:
                    1. Словарь с 4-буквенными ключами (пример: test - тест).
                    2. Словарь с 5-цифровыми ключами (пример: 12345 - один два три четыре пять).
                    0. Выход.
                    \nВаш выбор:
                    """);
            try {
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1 -> {
                        currentDictionary = fourLetterDictionary;
                        menu(scanner);
                    }
                    case 2 -> {
                        currentDictionary = fiveDigitDictionary;
                        menu(scanner);
                    }
                    case 0 -> running = false;
                    default -> System.out.println("\nНеверный выбор, попробуйте снова.");
                }
            } catch (Exception e) {
                System.out.println("\nОшибка ввода! Пожалуйста, введите число.");
                scanner.nextLine();
            }
        }
        scanner.close();
    }

    private void menu(Scanner scanner) {
        boolean inMenu = true;

        while (inMenu) {
            System.out.println("""
                    \nМеню:
                    1. Просмотреть содержимое словаря
                    2. Найти запись по ключу
                    3. Добавить запись
                    4. Удалить запись
                    0. Назад
                    \nВаш выбор:
                    """);
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> displayEntries();
                case 2 -> searchEntry(scanner);
                case 3 -> addEntry(scanner);
                case 4 -> deleteEntry(scanner);
                case 0 -> inMenu = false;
                default -> System.out.println("\nНеверный выбор, попробуйте снова.");
            }
        }
    }

    private void displayEntries() {
        Map<String, String> entries = currentDictionary.readEntries();
        if (entries.isEmpty()) {
            System.out.println("\nСловарь пуст.");
        } else {
            System.out.println("\nСодержимое словаря:");
            entries.forEach((key, value) -> System.out.println(key + " - " + value));
        }
    }

    private void searchEntry(Scanner scanner) {
        System.out.print("\nВведите ключ для поиска: ");
        String key = scanner.nextLine();
        String value = currentDictionary.searchEntry(key);
        if (value != null) {
            System.out.println("\nНайдена запись: " + key + " - " + value);
        } else {
            System.out.println("\nЗапись с ключом '" + key + "' не найдена.");
        }
    }

    private void addEntry(Scanner scanner) {
        System.out.print("\nВведите ключ: ");
        String key = scanner.nextLine();
        System.out.print("\nВведите значение: ");
        String value = scanner.nextLine();

        try {
            currentDictionary.addEntry(key, value);
            System.out.println("\nЗапись добавлена.");
        } catch (IllegalArgumentException e) {
            System.out.println("\nОшибка: " + e.getMessage());
        }
    }

    private void deleteEntry(Scanner scanner) {
        System.out.print("\nВведите ключ для удаления: ");
        String key = scanner.nextLine();
        if (currentDictionary.readEntries().containsKey(key)) {
            currentDictionary.deleteEntry(key);
            System.out.println("\nЗапись удалена.");
        } else {
            System.out.println("\nЗапись с таким ключом не найдена.");
        }
    }
}
