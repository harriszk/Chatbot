public class ChatMessageConverter implements ElementConverter<ChatMessage> {
    @Override
    public ChatMessage convert(String line) {
        if(line.equals(""))
        {
            return new ChatMessage();
        } // end if

        return new ChatMessage(line);
    } // end convert
} // end IntegerConverter
