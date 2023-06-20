import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ChatMessage implements Comparable<ChatMessage> {
    private LocalDateTime timestamp;
    private String channelName;
    private String username;
    private String message;

    public ChatMessage() {

    } // end default constructor 

    public ChatMessage(String data) {
        
        if(data == "") {
            return;
        } // end if

        String chatActionRegex = "^\\[\\d{4}-\\d{2}-\\d{1,2} \\d{2}:\\d{2}:\\d{2}\\] #[^\\s]+ [^:]+ .+$";
        String chatMessageRegex1 = "\\[\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}\\] #[^\\s]+ [^:]+: .+";
        String chatMessageRegex2 = "\\[\\d{4}-\\d{2}-\\d{1} \\d{2}:\\d{2}:\\d{2}\\] #[^\\s]+ [^:]+: .+";

        if(data.matches(chatActionRegex)) {   
            loadActionMessage(data);
            return;
        } // end if

        if(!data.matches(chatMessageRegex1) && !data.matches(chatMessageRegex2)) {   
            return;
        } // end if

        //System.out.println(data);

        String[] dataArray = data.split(" ", 5);
        String dateString = (dataArray[0] + " " + dataArray[1]).substring(1).split("]")[0];
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-d HH:mm:ss");

        this.timestamp = LocalDateTime.parse(dateString, formatter);
        this.channelName = dataArray[2].substring(1);
        this.username = dataArray[3].split(":")[0];
        this.message = dataArray[4];
    } // end initializing constructor

    public ChatMessage(LocalDateTime timestamp, String channelName, String username, String message) {
        this.timestamp = timestamp;
        this.channelName = channelName;
        this.username = username;
        this.message = message;
    } // end initializing constructor

    public LocalDateTime getTimestamp() {
        return timestamp;
    } // end getTimestamp

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    } // end setTimestamp

    public String getChannelName() {
        return channelName;
    } // end getChannelName

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    } // end setChannelName

    public String getUsername() {
        return username;
    } // end getUsername

    public void setUsername(String username) {
        this.username = username;
    } // end setUsername

    public String getMessage() {
        return message;
    } // end getMessage

    public void setMessage(String message) {
        this.message = message;
    } // end setMessage

    private void loadActionMessage(String actionMessage) {
        String[] dataArray = actionMessage.split(" ", 4);
        String dateString = (dataArray[0] + " " + dataArray[1]).substring(1).split("]")[0];
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-d HH:mm:ss");

        this.timestamp = LocalDateTime.parse(dateString, formatter);
        this.channelName = dataArray[2].substring(1);
        this.message = dataArray[3];
        this.username = null;
    } // end loadActionMessage

    @Override
    public int compareTo(ChatMessage message) {
        if(message.timestamp == null) {
            return -1;
        } // end if

        if(timestamp == null) {
            return 1;
        } // end if

        return timestamp.compareTo(message.timestamp);
    } // end compareTo

    @Override
    public boolean equals(Object obj) {
        if(this == obj) {
            return true;
        }

        if(obj == null || getClass() != obj.getClass()) {
            return false;
        }

        ChatMessage otherMessage = (ChatMessage) obj;
        return this.toString().equals(otherMessage.toString());
    } // end equals

    @Override
    public String toString() {
        if(timestamp == null || channelName == null || message == null) {
            return "";
        } // end if 

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedTimestamp = timestamp.format(formatter);

        if(username == null) {
            return String.format("[%s] #%s %s", formattedTimestamp, channelName, message);
        } // end if

        return String.format("[%s] #%s %s: %s", formattedTimestamp, channelName, username, message);
    } // end toString
} // end ChatMessage