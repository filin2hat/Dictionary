package validator;

/**
 * Валидатор для ключей из 5 цифр
 */
public class FiveDigitValidator implements DictionaryValidator {
    @Override
    public boolean isValidKey(String key) {
        return key.length() == 5 && key.matches("[0-9]+");
    }
}
