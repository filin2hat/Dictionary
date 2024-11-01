package exception;

/**
 * Класс исключений для обработки ошибок ввода пользователем
 */
public class MenuInputException extends Exception {
    public MenuInputException(String message) {
        super(message);
    }
}
