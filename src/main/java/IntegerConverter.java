public class IntegerConverter implements ElementConverter<Integer> {
    @Override
    public Integer convert(String line) {
        return Integer.parseInt(line);
    } // end convert
} // end IntegerConverter
