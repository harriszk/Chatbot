import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ChatMessageTest {
    private ChatMessage message;
    private String messageText = "[2023-04-19 22:06:22] #channel1 user1: This is a test message";

    @Before
    public void setUp()
    {
        this.message = new ChatMessage(this.messageText);
    } // end setUp

    @Test()
    public void testGetTimestamp()
    {
        String dateString = "2023-04-19 22:06:22";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime expected = LocalDateTime.parse(dateString, formatter);
        LocalDateTime actual = this.message.getTimestamp();
        Assert.assertEquals(expected, actual);
    } // end getTimestamp

    @Test 
    public void testSetTimestamp()
    {
        String newDateString = "2022-09-04 13:25:00";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime expected = LocalDateTime.parse(newDateString, formatter);
        this.message.setTimestamp(expected);
        LocalDateTime actual = this.message.getTimestamp();
        Assert.assertEquals(expected, actual);
    } // end setTimestamp

    @Test
    public void testGetChannelName()
    {
        String expected =  "channel1";
        String actual = this.message.getChannelName();
        Assert.assertEquals(expected, actual);
    } // end getChannelName

    @Test 
    public void testSetChannelName()
    {
        String expected = "channel2";
        this.message.setChannelName(expected);
        String actual = this.message.getChannelName();
        Assert.assertEquals(expected, actual);
    } // end setChannelName

    @Test
    public void testGetUsername()
    {
        String expected =  "user1";
        String actual = this.message.getUsername();
        Assert.assertEquals(expected, actual);
    } // end getUsername

    @Test 
    public void testSetUsername()
    {
        String expected = "user2";
        this.message.setUsername(expected);
        String actual = this.message.getUsername();
        Assert.assertEquals(expected, actual);
    } // end setUsername

    @Test
    public void testGetMessage()
    {
        String expected =  "This is a test message";
        String actual = this.message.getMessage();
        Assert.assertEquals(expected, actual);
    } // end getMessage

    @Test 
    public void testSetMessage()
    {
        String expected = "This is a changed test message";
        this.message.setMessage(expected);
        String actual = this.message.getMessage();
        Assert.assertEquals(expected, actual);
    } // end setMessage
} // end ChatMessageTest