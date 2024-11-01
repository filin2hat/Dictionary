package validator;

public class FourLetterValidator implements DictionaryValidator {
    @Override
    public boolean isValidKey(String key) {
        return key.length() == 4 && key.matches("[a-zA-Z]+");
    }
}
