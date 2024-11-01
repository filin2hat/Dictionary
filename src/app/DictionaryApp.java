package app;

import entry.DictionaryEntry;
import exception.MenuInputException;
import service.DictionaryService;
import service.FileDictionaryService;
import validator.FiveDigitValidator;
import validator.FourLetterValidator;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.Scanner;
import java.util.InputMismatchException;

public class DictionaryApp {
    private static final Path SRC_DIRECTORY = Paths.get("src"); // Папка src
    private static final Path FOUR_LETTER_DICTIONARY_PATH = SRC_DIRECTORY.resolve("fourLetterDictionary.txt");
    private static final Path FIVE_DIGIT_DICTIONARY_PATH = SRC_DIRECTORY.resolve("fiveDigitDictionary.txt");

    private DictionaryService currentDictionary;   // Текущий выбранный словарь
    private DictionaryService fourLetterDictionary;
    private DictionaryService fiveDigitDictionary;

    public DictionaryApp() {
        initializeDictionaryFiles(); // Создание файлов и директории, если их нет

        // Инициализация двух словарей
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
            // Проверяем существование директории src и создаем, если её нет
            if (Files.notExists(SRC_DIRECTORY)) {
                Files.createDirectory(SRC_DIRECTORY);
                System.out.println("Создана директория: " + SRC_DIRECTORY);
            }

            // Проверяем и создаем файл fourLetterDictionary.txt, если его нет
            if (Files.notExists(FOUR_LETTER_DICTIONARY_PATH)) {
                Files.createFile(FOUR_LETTER_DICTIONARY_PATH);
                System.out.println("Создан файл словаря: " + FOUR_LETTER_DICTIONARY_PATH);
            }

            // Проверяем и создаем файл fiveDigitDictionary.txt, если его нет
            if (Files.notExists(FIVE_DIGIT_DICTIONARY_PATH)) {
                Files.createFile(FIVE_DIGIT_DICTIONARY_PATH);
                System.out.println("Создан файл словаря: " + FIVE_DIGIT_DICTIONARY_PATH);
            }
        } catch (IOException e) {
            System.err.println("Ошибка при создании директории или файлов словарей: " + e.getMessage());
        }
    }

    public void start() {
        var scanner = new Scanner(System.in);
        var running = true;

        while (running) {
            try {
                System.out.println("Выберите словарь:");
                System.out.println("1. Словарь с 4-буквенными ключами");
                System.out.println("2. Словарь с 5-цифровыми ключами");
                System.out.println("0. Выход");
                System.out.print("Ваш выбор: ");
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
                    default -> throw new MenuInputException("Неверный выбор, попробуйте снова.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Ошибка: введите число (0, 1 или 2).");
                scanner.nextLine(); // Очистка неверного ввода
            } catch (Exception e) {
                System.out.println("Произошла ошибка: " + e.getMessage());
            }
        }
        scanner.close();
    }

    private void menu(Scanner scanner) {
        boolean inMenu = true;

        while (inMenu) {
            try {
                System.out.println("\nМеню:");
                System.out.println("1. Просмотреть содержимое словаря");
                System.out.println("2. Найти запись по ключу");
                System.out.println("3. Добавить запись");
                System.out.println("4. Удалить запись");
                System.out.println("0. Назад");
                System.out.print("Ваш выбор: ");
                int choice = scanner.nextInt();
                scanner.nextLine();  // Очистка буфера

                switch (choice) {
                    case 1 -> displayEntries();
                    case 2 -> searchEntry(scanner);
                    case 3 -> addEntry(scanner);
                    case 4 -> deleteEntry(scanner);
                    case 0 -> inMenu = false;
                    default -> throw new MenuInputException("Ошибка: введите число от 0 до 4.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Ошибка: введите число от 0 до 4.");
                scanner.nextLine(); // Очистка неверного ввода
            } catch (Exception e) {
                System.out.println("Произошла ошибка: " + e.getMessage());
            }
        }
    }

    private void displayEntries() {
        List<DictionaryEntry> entries = currentDictionary.readEntries();
        if (entries.isEmpty()) {
            System.out.println("Словарь пуст.");
        } else {
            System.out.println("Содержимое словаря:");
            entries.forEach(System.out::println);
        }
    }

    private void searchEntry(Scanner scanner) {
        System.out.print("Введите ключ для поиска: ");
        String key = scanner.nextLine();
        DictionaryEntry entry = currentDictionary.searchEntry(key);
        if (entry != null) {
            System.out.println("Найдена запись: " + entry);
        } else {
            System.out.println("Запись с ключом '" + key + "' не найдена.");
        }
    }

    private void addEntry(Scanner scanner) {
        System.out.print("Введите ключ: ");
        String key = scanner.nextLine();
        System.out.print("Введите значение: ");
        String value = scanner.nextLine();

        try {
            currentDictionary.addEntry(key, value);
            System.out.println("Запись добавлена.");
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    private void deleteEntry(Scanner scanner) {
        System.out.print("Введите ключ для удаления: ");
        String key = scanner.nextLine();
        currentDictionary.deleteEntry(key);
        System.out.println("Запись удалена.");
    }
}
