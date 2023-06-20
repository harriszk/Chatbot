public class ChatMessageConverter implements ElementConverter<ChatMessage> {
    @Override
    public ChatMessage convert(String line) {
        return new ChatMessage(line);
    } // end convert
} // end IntegerConverter
