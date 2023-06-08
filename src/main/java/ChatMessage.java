import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ChatMessage implements Comparable<ChatMessage> {
    private LocalDateTime timestamp;
    private String channelName;
    private String username;
    private String message;

    public ChatMessage(LocalDateTime timestamp, String channelName, String username, String message)
    {
        this.timestamp = timestamp;
        this.channelName = channelName;
        this.username = username;
        this.message = message;
    } // end initializing construtor

    public LocalDateTime getTimestamp()
    {
        return this.timestamp;
    } // end getTimestamp

    public void setTimestamp(LocalDateTime time)
    {
        this.timestamp = time;
    } // end setTimestamp

    public String getChannelName()
    {
        return this.channelName;
    } // end getChannelName

    public void setChannelName(String channelName)
    {
        this.channelName = channelName;
    } // end setChannelName

    public String getUsername()
    {
        return this.username;
    } // end getUsername

    public void setUsername(String username)
    {
        this.username = username;
    } // end setUsername

    public String getMessage()
    {
        return this.message;
    } // end getMessage

    public void setMessage(String message)
    {
        this.message = message;
    } // end setMessage

    @Override
    public int compareTo(ChatMessage message) {
        return this.timestamp.compareTo(message.timestamp);
    } // end compareTo
} // end ChatMessage