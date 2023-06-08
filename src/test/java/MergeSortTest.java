import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MergeSortTest {
    public String leftMessage1Text = "[2023-04-19 17:09:34] #channel1 user2: This is the first test message.";
    public String leftMessage2Text = "[2023-04-19 19:59:51] #channel1 user3: This is the second test message.";
    public String leftMessage3Text = "[2023-04-19 20:30:40] #channel1 user3: This is the third test message.";
    public String leftMessage4Text = "[2023-04-19 21:14:26] #channel1 user3: This is the fourth test message.";
    public String leftMessage5Text = "[2023-04-19 22:06:22] #channel1 user2: This is the fifth test message.";

    public String rightMessage1Text = "[2023-04-19 18:21:59] #channel1 user2: This is the sixth test message.";
    public String rightMessage2Text = "[2023-04-19 19:42:13] #channel1 user1: This is the seventh test message.";
    public String rightMessage3Text = "[2023-04-19 20:05:07] #channel1 user1: This is the eight test message.";
    public String rightMessage4Text = "[2023-04-19 21:58:57] #channel1 user1: This is the ninth test message.";
    public String rightMessage5Text = "[2023-04-19 23:15:05] #channel1 user2: This is the tenth test message.";

    // Test that the merge method works as expected. It should be private
    // in the final implementation however.
    @Test
    public void testMergeIntegers()
    {
        MergeSort<List<Integer>> sorter = new MergeSort<>();

        Integer[] leftArray = {1, 2, 4, 5, 7, 9};
        Integer[] rightArray = {1, 2, 3, 5, 6, 8};
        Integer[] expecetedArray = {1, 1, 2, 2, 3, 4, 5, 5, 6, 7, 8, 9};

        List<Integer> left = new ArrayList<>(Arrays.asList(leftArray));
        List<Integer> right = new ArrayList<>(Arrays.asList(rightArray));
        List<Integer> expected = new ArrayList<>(Arrays.asList(expecetedArray));

        List<Integer> actual = sorter.merge(left, right);
        Assert.assertEquals(expected, actual);
    } // end testMergeIntegers

    @Test
    public void testMergeChatMessages()
    {
        MergeSort<List<ChatMessage>> sorter = new MergeSort<>();

        ChatMessage leftMessage1 = new ChatMessage(leftMessage1Text);
        ChatMessage leftMessage2 = new ChatMessage(leftMessage2Text);
        ChatMessage leftMessage3 = new ChatMessage(leftMessage3Text);
        ChatMessage leftMessage4 = new ChatMessage(leftMessage4Text);
        ChatMessage leftMessage5 = new ChatMessage(leftMessage5Text);

        ChatMessage rightMessage1 = new ChatMessage(rightMessage1Text);
        ChatMessage rightMessage2 = new ChatMessage(rightMessage2Text);
        ChatMessage rightMessage3 = new ChatMessage(rightMessage3Text);
        ChatMessage rightMessage4 = new ChatMessage(rightMessage4Text);
        ChatMessage rightMessage5 = new ChatMessage(rightMessage5Text);

        List<ChatMessage> left = new ArrayList<>();
        left.add(leftMessage1);
        left.add(leftMessage2);
        left.add(leftMessage3);
        left.add(leftMessage4);
        left.add(leftMessage5);

        List<ChatMessage> right = new ArrayList<>();
        right.add(rightMessage1);
        right.add(rightMessage2);
        right.add(rightMessage3);
        right.add(rightMessage4);
        right.add(rightMessage5);

        List<ChatMessage> expected = new ArrayList<>();
        expected.add(leftMessage1);
        expected.add(leftMessage2);
        expected.add(leftMessage3);
        expected.add(leftMessage4);
        expected.add(leftMessage5);
        expected.add(rightMessage1);
        expected.add(rightMessage2);
        expected.add(rightMessage3);
        expected.add(rightMessage4);
        expected.add(rightMessage5);

        List<ChatMessage> actual = sorter.merge(left, right);
        Assert.assertEquals(expected, actual);
    } // end testMergeChatMessages

    @Test
    public void testMergeSortIntegers()
    {
        MergeSort<List<Integer>> sorter = new MergeSort<>();

        Integer[] unorderedArray = {5, 8, 2, 1, 9, 4};
        List<Integer> elementsToSort = new ArrayList<>(Arrays.asList(unorderedArray));

        Integer[] expecetedArray = {1, 2, 4, 5, 8, 9};
        List<Integer> expected = new ArrayList<>(Arrays.asList(expecetedArray));
        
        elementsToSort = sorter.mergeSort(elementsToSort);
        Assert.assertEquals(expected, elementsToSort);
    } // end testMergeSortIntegers

    @Test
    public void testMergeSortChatMessages()
    {
        MergeSort<List<ChatMessage>> sorter = new MergeSort<>();

        ChatMessage message1 = new ChatMessage(leftMessage1Text);
        ChatMessage message2 = new ChatMessage(leftMessage2Text);
        ChatMessage message3 = new ChatMessage(leftMessage3Text);
        ChatMessage message4 = new ChatMessage(leftMessage4Text);
        ChatMessage message5 = new ChatMessage(leftMessage5Text);

        List<ChatMessage> chatMessagesToSort = new ArrayList<>();
        chatMessagesToSort.add(message4);
        chatMessagesToSort.add(message3);
        chatMessagesToSort.add(message1);
        chatMessagesToSort.add(message2);
        chatMessagesToSort.add(message5);

        List<ChatMessage> expected = new ArrayList<>();
        expected.add(message1);
        expected.add(message2);
        expected.add(message3);
        expected.add(message4);
        expected.add(message5);

        chatMessagesToSort = sorter.mergeSort(chatMessagesToSort);
        Assert.assertEquals(expected, chatMessagesToSort);
    } // end testMergeSortChatMessages
} // end MergeSortTest