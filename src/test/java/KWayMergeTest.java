import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class KWayMergeTest {
    private String[] messagesChunk1 = {
        "[2023-06-06 09:15:00] #channel1 user1: Hello, everyone!",
        "[2023-06-06 10:30:12] #channel1 user2: How's everyone doing today?",
        "[2023-06-06 11:45:23] #channel1 user3: I have an important announcement to make.",
        "[2023-06-06 12:50:57] #channel1 user4: Has anyone seen my pen?",
        "[2023-06-06 14:20:36] #channel1 user2: @user4 I think I saw your pen on the table.",
        "[2023-06-06 15:10:08] #channel1 user5: Can someone help me with this coding problem?",
        "[2023-06-06 16:25:44] #channel1 user3: Sure, I can assist you with that.",
        "[2023-06-06 17:40:19] #channel1 user1: Remember, tomorrow is the team meeting.",
        "[2023-06-06 18:55:32] #channel1 user4: Thanks for the reminder!",
        "[2023-06-06 19:45:09] #channel1 user2: @user5 Let's discuss it in the coding-help channel.",
        "[2023-06-06 20:35:51] #channel1 user3: Good idea, user2!",
        "[2023-06-06 21:10:15] #channel1 user1: Have a great evening, everyone!",
        "[2023-06-07 08:05:30] #channel1 user5: Good morning, channel!",
        "[2023-06-07 09:20:18] #channel1 user2: Is the project deadline still the same?",
        "[2023-06-07 10:40:59] #channel1 user3: Yes, the deadline hasn't changed.",
        "[2023-06-07 12:15:24] #channel1 user4: Let's prioritize the remaining tasks.",
        "[2023-06-07 13:30:45] #channel1 user1: I've created a new branch for the feature implementation.",
        "[2023-06-07 14:55:03] #channel1 user2: @user1 Please share the branch name with us.",
        "[2023-06-07 16:10:27] #channel1 user1: It's called feature-xyz-branch.",
        "[2023-06-07 17:25:54] #channel1 user4: Let's review the pull requests before merging.",
        "[2023-06-07 18:40:17] #channel1 user3: @user5 Can you review my changes?",
        "[2023-06-07 19:55:39] #channel1 user5: Sure, I'll take a look.",
        "[2023-06-07 20:45:02] #channel1 user2: I'm excited about the upcoming release!",
        "[2023-06-07 21:30:19] #channel1 user1: We've made great progress. Keep it up, team!",
        "[2023-06-07 22:15:42] #channel1 user3: Thanks, user1! Let's keep pushing forward."
    };

    private String[] messagesChunk2 = {
        "[2023-06-06 09:15:30] #channel1 user4: Good morning, everyone!",
        "[2023-06-06 10:35:21] #channel1 user2: Any plans for the weekend?",
        "[2023-06-06 11:50:48] #channel1 user1: I'm looking forward to the team outing.",
        "[2023-06-06 13:05:16] #channel1 user3: @user2 Let's discuss the project updates.",
        "[2023-06-06 14:30:57] #channel1 user5: I need help with a database query.",
        "[2023-06-06 15:40:24] #channel1 user1: Sure, I can assist you with that.",
        "[2023-06-06 16:55:10] #channel1 user4: Let's schedule a meeting for tomorrow.",
        "[2023-06-06 18:20:06] #channel1 user3: @user5 We can discuss it in the database-help channel.",
        "[2023-06-06 19:30:42] #channel1 user2: Sounds good, user3!",
        "[2023-06-06 20:50:15] #channel1 user5: Thank you, user1, for your assistance.",
        "[2023-06-06 22:05:59] #channel1 user1: You're welcome! Have a great evening.",
        "[2023-06-07 08:30:12] #channel1 user3: Good morning, channel!",
        "[2023-06-07 09:40:55] #channel1 user2: How's the progress on the bug fixing?",
        "[2023-06-07 11:00:23] #channel1 user4: We've resolved most of the reported bugs.",
        "[2023-06-07 12:25:39] #channel1 user5: Let's do some testing to ensure everything is working correctly.",
        "[2023-06-07 13:45:17] #channel1 user1: I'll update the test cases with the latest changes.",
        "[2023-06-07 15:05:52] #channel1 user2: @user4 Please review my pull request.",
        "[2023-06-07 16:25:40] #channel1 user4: I'll take a look and provide feedback.",
        "[2023-06-07 17:40:08] #channel1 user3: @user1 Can you check the deployment process?",
        "[2023-06-07 19:00:33] #channel1 user1: Sure, I'll verify the deployment steps.",
        "[2023-06-07 20:20:15] #channel1 user5: We're almost there. Great job, everyone!",
        "[2023-06-07 21:35:59] #channel1 user2: Let's celebrate once we complete the release!",
        "[2023-06-07 22:55:42] #channel1 user3: Absolutely! Looking forward to it."
    };

    private String[] sortedMessagesChunks1And2 = {
        "[2023-06-06 09:15:00] #channel1 user1: Hello, everyone!", 
        "[2023-06-06 09:15:30] #channel1 user4: Good morning, everyone!", 
        "[2023-06-06 10:30:12] #channel1 user2: How's everyone doing today?", 
        "[2023-06-06 10:35:21] #channel1 user2: Any plans for the weekend?", 
        "[2023-06-06 11:45:23] #channel1 user3: I have an important announcement to make.", 
        "[2023-06-06 11:50:48] #channel1 user1: I'm looking forward to the team outing.", 
        "[2023-06-06 12:50:57] #channel1 user4: Has anyone seen my pen?", 
        "[2023-06-06 13:05:16] #channel1 user3: @user2 Let's discuss the project updates.", 
        "[2023-06-06 14:20:36] #channel1 user2: @user4 I think I saw your pen on the table.",
        "[2023-06-06 14:30:57] #channel1 user5: I need help with a database query.", 
        "[2023-06-06 15:10:08] #channel1 user5: Can someone help me with this coding problem?", 
        "[2023-06-06 15:40:24] #channel1 user1: Sure, I can assist you with that.", 
        "[2023-06-06 16:25:44] #channel1 user3: Sure, I can assist you with that.",
        "[2023-06-06 16:55:10] #channel1 user4: Let's schedule a meeting for tomorrow.", 
        "[2023-06-06 17:40:19] #channel1 user1: Remember, tomorrow is the team meeting.", 
        "[2023-06-06 18:20:06] #channel1 user3: @user5 We can discuss it in the database-help channel.", 
        "[2023-06-06 18:55:32] #channel1 user4: Thanks for the reminder!", 
        "[2023-06-06 19:30:42] #channel1 user2: Sounds good, user3!", 
        "[2023-06-06 19:45:09] #channel1 user2: @user5 Let's discuss it in the coding-help channel.", 
        "[2023-06-06 20:35:51] #channel1 user3: Good idea, user2!", 
        "[2023-06-06 20:50:15] #channel1 user5: Thank you, user1, for your assistance.", 
        "[2023-06-06 21:10:15] #channel1 user1: Have a great evening, everyone!", 
        "[2023-06-06 22:05:59] #channel1 user1: You're welcome! Have a great evening.", 
        "[2023-06-07 08:05:30] #channel1 user5: Good morning, channel!", 
        "[2023-06-07 08:30:12] #channel1 user3: Good morning, channel!", 
        "[2023-06-07 09:20:18] #channel1 user2: Is the project deadline still the same?", 
        "[2023-06-07 09:40:55] #channel1 user2: How's the progress on the bug fixing?",
        "[2023-06-07 10:40:59] #channel1 user3: Yes, the deadline hasn't changed.", 
        "[2023-06-07 11:00:23] #channel1 user4: We've resolved most of the reported bugs.", 
        "[2023-06-07 12:15:24] #channel1 user4: Let's prioritize the remaining tasks.", 
        "[2023-06-07 12:25:39] #channel1 user5: Let's do some testing to ensure everything is working correctly.", 
        "[2023-06-07 13:30:45] #channel1 user1: I've created a new branch for the feature implementation.", 
        "[2023-06-07 13:45:17] #channel1 user1: I'll update the test cases with the latest changes.", 
        "[2023-06-07 14:55:03] #channel1 user2: @user1 Please share the branch name with us.", 
        "[2023-06-07 15:05:52] #channel1 user2: @user4 Please review my pull request.", 
        "[2023-06-07 16:10:27] #channel1 user1: It's called feature-xyz-branch.", 
        "[2023-06-07 16:25:40] #channel1 user4: I'll take a look and provide feedback.", 
        "[2023-06-07 17:25:54] #channel1 user4: Let's review the pull requests before merging.", 
        "[2023-06-07 17:40:08] #channel1 user3: @user1 Can you check the deployment process?", 
        "[2023-06-07 18:40:17] #channel1 user3: @user5 Can you review my changes?", 
        "[2023-06-07 19:00:33] #channel1 user1: Sure, I'll verify the deployment steps.", 
        "[2023-06-07 19:55:39] #channel1 user5: Sure, I'll take a look.", 
        "[2023-06-07 20:20:15] #channel1 user5: We're almost there. Great job, everyone!", 
        "[2023-06-07 20:45:02] #channel1 user2: I'm excited about the upcoming release!", 
        "[2023-06-07 21:30:19] #channel1 user1: We've made great progress. Keep it up, team!", 
        "[2023-06-07 21:35:59] #channel1 user2: Let's celebrate once we complete the release!", 
        "[2023-06-07 22:15:42] #channel1 user3: Thanks, user1! Let's keep pushing forward.", 
        "[2023-06-07 22:55:42] #channel1 user3: Absolutely! Looking forward to it."
    };

    private String[] messagesChunk3 = {
        "[2023-06-06 09:16:12] #channel1 user2: Good morning, channel!",
        "[2023-06-06 10:40:39] #channel1 user1: Any updates on the customer feedback?",
        "[2023-06-06 11:55:08] #channel1 user4: We received positive responses from most of the customers.",
        "[2023-06-06 13:10:33] #channel1 user3: @user5 Let's discuss the marketing campaign.",
        "[2023-06-06 14:35:21] #channel1 user2: I have an idea for improving user engagement.",
        "[2023-06-06 15:50:56] #channel1 user1: Share it with us, user2.",
        "[2023-06-06 17:10:29] #channel1 user5: Can we allocate resources for the new feature development?",
        "[2023-06-06 18:30:14] #channel1 user3: We need to evaluate the impact on the project timeline.",
        "[2023-06-06 19:50:47] #channel1 user2: Let's have a discussion and make a decision.",
        "[2023-06-06 21:15:22] #channel1 user4: @user1 Please review my changes in the feature branch.",
        "[2023-06-06 22:35:04] #channel1 user1: I'll review it and provide feedback.",
        "[2023-06-07 08:50:17] #channel1 user3: Good morning, everyone!",
        "[2023-06-07 10:10:59] #channel1 user2: How's the performance of the latest release?",
        "[2023-06-07 11:30:35] #channel1 user4: It's running smoothly with no major issues.",
        "[2023-06-07 12:50:13] #channel1 user5: @user2 Can you assist me with the UI design?",
        "[2023-06-07 14:10:45] #channel1 user2: Of course, let's discuss it in the design-help channel.",
        "[2023-06-07 15:30:21] #channel1 user3: Sounds like a plan, user2!",
        "[2023-06-07 16:55:04] #channel1 user5: Thank you, user4, for your feedback on my changes.",
        "[2023-06-07 18:15:41] #channel1 user4: You're welcome! Keep up the good work.",
        "[2023-06-07 19:35:22] #channel1 user1: We're almost ready for the release. Just a few more tasks.",
        "[2023-06-07 20:55:09] #channel1 user2: Let's make sure everything is thoroughly tested.",
        "[2023-06-07 22:20:03] #channel1 user3: We're doing great! Keep up the momentum."
    };

    private String[] messagesChunk4 = {
        "[2023-06-06 09:17:00] #channel1 user4: Good morning, channel members!",
        "[2023-06-06 10:45:12] #channel1 user2: Any updates on the project timeline?",
        "[2023-06-06 12:00:36] #channel1 user1: We're on track to meet the deadlines.",
        "[2023-06-06 13:20:57] #channel1 user3: @user4 Let's discuss the budget allocation.",
        "[2023-06-06 14:45:24] #channel1 user5: I have a suggestion for optimizing the code.",
        "[2023-06-06 16:00:46] #channel1 user1: Share it with us, user5.",
        "[2023-06-06 17:25:17] #channel1 user4: Let's analyze the impact on performance before implementing it.",
        "[2023-06-06 18:40:52] #channel1 user3: @user2 Can you review my pull request?",
        "[2023-06-06 20:00:30] #channel1 user2: Sure, I'll take a look and provide feedback.",
        "[2023-06-06 21:25:09] #channel1 user5: Thanks, user2, for your prompt response.",
        "[2023-06-06 22:45:43] #channel1 user1: You're welcome! Have a great night, everyone.",
        "[2023-06-07 09:00:56] #channel1 user3: Good morning, channel!",
        "[2023-06-07 10:25:40] #channel1 user2: Let's finalize the documentation today.",
        "[2023-06-07 11:45:23] #channel1 user4: I'll work on the final touches and formatting.",
        "[2023-06-07 13:05:08] #channel1 user5: @user1 Can you assist me with the test cases?",
        "[2023-06-07 14:25:51] #channel1 user1: Of course, let's discuss it in the testing-help channel.",
        "[2023-06-07 15:50:39] #channel1 user3: Sounds good, user1!",
        "[2023-06-07 17:10:21] #channel1 user5: Thank you, user4, for reviewing my changes.",
        "[2023-06-07 18:35:09] #channel1 user4: You're welcome! The code looks good now.",
        "[2023-06-07 19:55:55] #channel1 user1: We're ready for the final testing phase.",
        "[2023-06-07 21:15:42] #channel1 user2: Let's ensure everything is thoroughly tested.",
        "[2023-06-07 22:40:36] #channel1 user3: We're almost there. Great job, team!"
    };

    private Integer[] intChunk1 = {2, 4, 6, 8, 10};
    private Integer[] intChunk2 = {1, 3, 5, 7, 9};
    private Integer[] intChunk3 = {12, 14, 16, 18, 20};
    private Integer[] intChunk4 = {11, 13, 15, 17, 19};

    private static final String CHUNKS_DIRECTORY = "tmp/chunks";
    private static final String TEST_CHUNKS_DIRECTORY = "tmp/chunks/testChunks";
    private FileHandler fileHandler = new FileHandler();
    private ChatMessageConverter chatMessageConverter = new ChatMessageConverter();
    private IntegerConverter integerConverter = new IntegerConverter();

    @Test
    public void testMergeTwoSortedChunks_ChatMessages() {
        KWayMerge<ChatMessage> merger = new KWayMerge<>(this.chatMessageConverter);
        this.fileHandler.writeChunkToFile(TEST_CHUNKS_DIRECTORY + "/chunk1.txt", messagesChunk1);
        this.fileHandler.writeChunkToFile(TEST_CHUNKS_DIRECTORY + "/chunk2.txt", messagesChunk2);

        List<ChatMessage> expected = new ArrayList<>();

        for(String messageText : this.sortedMessagesChunks1And2)
        {
            ChatMessage message = new ChatMessage(messageText);
            expected.add(message);
        } // end for

        List<String> chunkLocations = new ArrayList<>(Arrays.asList(TEST_CHUNKS_DIRECTORY + "/chunk1.txt", TEST_CHUNKS_DIRECTORY + "/chunk2.txt"));

        merger.mergeAllChunks(chunkLocations);
        List<ChatMessage> mergedChunk = this.fileHandler.loadChunkFromFile("finalMergedChunks.txt", this.chatMessageConverter);

        Assert.assertEquals(expected, mergedChunk);
        Assert.assertEquals(expected.size(), mergedChunk.size());
        Assert.assertEquals(expected.get(0), mergedChunk.get(0));
        Assert.assertEquals(expected.get(expected.size() - 1), mergedChunk.get(mergedChunk.size() - 1));

        this.fileHandler.deleteChunkFile("finalMergedChunks.txt");
        this.fileHandler.deleteChunkFile(TEST_CHUNKS_DIRECTORY + "/chunk1.txt");
        this.fileHandler.deleteChunkFile(TEST_CHUNKS_DIRECTORY + "/chunk2.txt");
    } // end testMergeTwoSortedChunks_ChatMessages

    @Test
    public void testMergeTwoSortedChunks_Integers() {
        KWayMerge<Integer> merger = new KWayMerge<>(this.integerConverter);
        this.fileHandler.writeChunkToFile(TEST_CHUNKS_DIRECTORY + "/chunk1.txt", intChunk1);
        this.fileHandler.writeChunkToFile(TEST_CHUNKS_DIRECTORY + "/chunk2.txt", intChunk2);

        List<Integer> expected = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));

        List<String> chunkLocations = new ArrayList<>(Arrays.asList(TEST_CHUNKS_DIRECTORY + "/chunk1.txt", TEST_CHUNKS_DIRECTORY + "/chunk2.txt"));

        merger.mergeAllChunks(chunkLocations);
        List<Integer> mergedChunk = this.fileHandler.loadChunkFromFile("finalMergedChunks.txt", this.integerConverter);

        Assert.assertEquals(expected, mergedChunk);
        Assert.assertEquals(expected.size(), mergedChunk.size());
        Assert.assertEquals(expected.get(0), mergedChunk.get(0));
        Assert.assertEquals(expected.get(expected.size() - 1), mergedChunk.get(mergedChunk.size() - 1));

        this.fileHandler.deleteChunkFile("finalMergedChunks.txt");
        this.fileHandler.deleteChunkFile(TEST_CHUNKS_DIRECTORY + "/chunk1.txt");
        this.fileHandler.deleteChunkFile(TEST_CHUNKS_DIRECTORY + "/chunk2.txt");
    } // end testMergeTwoSortedChunks_Integers

    @Test
    public void testMergeTwoSortedChunks_OneEmptyChunk_ChatMessages() {
        KWayMerge<ChatMessage> merger = new KWayMerge<>(this.chatMessageConverter);
        ChatMessage emptyMessage = new ChatMessage();
        this.fileHandler.writeChunkToFile(TEST_CHUNKS_DIRECTORY + "/chunk1.txt", messagesChunk3);
        this.fileHandler.writeChunkToFile(TEST_CHUNKS_DIRECTORY + "/chunk2.txt", Arrays.asList(emptyMessage.toString()).toArray());

        List<ChatMessage> expected = new ArrayList<>();

        for(String messageText : this.messagesChunk3)
        {
            ChatMessage message = new ChatMessage(messageText);
            expected.add(message);
        } // end for

        List<String> chunkLocations = new ArrayList<>(Arrays.asList(TEST_CHUNKS_DIRECTORY + "/chunk1.txt", TEST_CHUNKS_DIRECTORY + "/chunk2.txt"));

        merger.mergeAllChunks(chunkLocations);
        List<ChatMessage> mergedChunk = this.fileHandler.loadChunkFromFile("finalMergedChunks.txt", this.chatMessageConverter);

        Assert.assertEquals(expected, mergedChunk);
        Assert.assertEquals(expected.size(), mergedChunk.size());
        Assert.assertEquals(expected.get(0), mergedChunk.get(0));
        Assert.assertEquals(expected.get(expected.size() - 1), mergedChunk.get(mergedChunk.size() - 1));
        
        this.fileHandler.deleteChunkFile(TEST_CHUNKS_DIRECTORY + "/chunk1.txt");
        this.fileHandler.deleteChunkFile(TEST_CHUNKS_DIRECTORY + "/chunk2.txt");
        this.fileHandler.deleteChunkFile("finalMergedChunks.txt");
    } // end testMergeTwoSortedChunks_OneEmptyChunk_ChatMessages

    @Test
    public void testMergeTwoSortedChunks_OneEmptyChunk_Integers() {
        KWayMerge<Integer> merger = new KWayMerge<>(this.integerConverter);
        Integer[] emptyChunk = {};
        this.fileHandler.writeChunkToFile(TEST_CHUNKS_DIRECTORY + "/chunk1.txt", intChunk3);
        this.fileHandler.writeChunkToFile(TEST_CHUNKS_DIRECTORY + "/chunk2.txt", emptyChunk);

        List<Integer> expected = new ArrayList<>(Arrays.asList(12, 14, 16, 18, 20));

        List<String> chunkLocations = new ArrayList<>(Arrays.asList(TEST_CHUNKS_DIRECTORY + "/chunk1.txt", TEST_CHUNKS_DIRECTORY + "/chunk2.txt"));

        merger.mergeAllChunks(chunkLocations);
        List<Integer> mergedChunk = this.fileHandler.loadChunkFromFile("finalMergedChunks.txt", this.integerConverter);

        Assert.assertEquals(expected, mergedChunk);
        Assert.assertEquals(expected.size(), mergedChunk.size());
        Assert.assertEquals(expected.get(0), mergedChunk.get(0));
        Assert.assertEquals(expected.get(expected.size() - 1), mergedChunk.get(mergedChunk.size() - 1));

        this.fileHandler.deleteChunkFile(TEST_CHUNKS_DIRECTORY + "/chunk1.txt");
        this.fileHandler.deleteChunkFile(TEST_CHUNKS_DIRECTORY + "/chunk2.txt");
        this.fileHandler.deleteChunkFile("finalMergedChunks.txt");
    } // end testMergeTwoSortedChunks_OneEmptyChunk_Integers

    @Test
    public void testMergeSortedChunks_SingleChunk_ChatMessages() {
        KWayMerge<ChatMessage> merger = new KWayMerge<>(this.chatMessageConverter);
        this.fileHandler.writeChunkToFile(TEST_CHUNKS_DIRECTORY + "/chunk1.txt", this.messagesChunk4);

        List<ChatMessage> expected = new ArrayList<>();

        for(String messageText : this.messagesChunk4)
        {
            ChatMessage message = new ChatMessage(messageText);
            expected.add(message);
        } // end for

        List<String> chunkLocations = new ArrayList<>(Arrays.asList(TEST_CHUNKS_DIRECTORY + "/chunk1.txt"));

        merger.mergeAllChunks(chunkLocations);
        List<ChatMessage> mergedChunk = this.fileHandler.loadChunkFromFile("finalMergedChunks.txt", this.chatMessageConverter);

        Assert.assertEquals(expected, mergedChunk);
        Assert.assertEquals(expected.size(), mergedChunk.size());
        Assert.assertEquals(expected.get(0), mergedChunk.get(0));
        Assert.assertEquals(expected.get(expected.size() - 1), mergedChunk.get(mergedChunk.size() - 1));

        this.fileHandler.deleteChunkFile("finalMergedChunks.txt");
        this.fileHandler.deleteChunkFile(TEST_CHUNKS_DIRECTORY + "/chunk1.txt");
    } // end testMergeSortedChunks_SingleChunk_ChatMessages

    @Test
    public void testMergeSortedChunks_SingleChunk_Integers() {
        KWayMerge<Integer> merger = new KWayMerge<>(this.integerConverter);
        this.fileHandler.writeChunkToFile(TEST_CHUNKS_DIRECTORY + "/chunk1.txt", intChunk4);

        List<Integer> expected = new ArrayList<>(Arrays.asList(11, 13, 15, 17, 19));

        List<String> chunkLocations = new ArrayList<>(Arrays.asList(TEST_CHUNKS_DIRECTORY + "/chunk1.txt"));

        merger.mergeAllChunks(chunkLocations);
        List<Integer> mergedChunk = this.fileHandler.loadChunkFromFile("finalMergedChunks.txt", this.integerConverter);

        Assert.assertEquals(expected, mergedChunk);
        Assert.assertEquals(expected.size(), mergedChunk.size());
        Assert.assertEquals(expected.get(0), mergedChunk.get(0));
        Assert.assertEquals(expected.get(expected.size() - 1), mergedChunk.get(mergedChunk.size() - 1));

        this.fileHandler.deleteChunkFile("finalMergedChunks.txt");
        this.fileHandler.deleteChunkFile(TEST_CHUNKS_DIRECTORY + "/chunk1.txt");
    } // end testMergeSortedChunks_SingleChunk_Integers

    @Test
    public void testMergeSortedChunks_LargeChunks_Integers() {
        // The chunks should have a lot of entires that cannot directly be loaded into memory.
        KWayMerge<Integer> merger = new KWayMerge<>(this.integerConverter);
        List<String> chunkLocations = new ArrayList<>(Arrays.asList(TEST_CHUNKS_DIRECTORY + "/CHUNK_0.txt", TEST_CHUNKS_DIRECTORY + "/CHUNK_1.txt", TEST_CHUNKS_DIRECTORY + "/CHUNK_2.txt", TEST_CHUNKS_DIRECTORY + "/CHUNK_3.txt", TEST_CHUNKS_DIRECTORY + "/CHUNK_4.txt"));

        List<Integer> expected = this.fileHandler.loadChunkFromFile(TEST_CHUNKS_DIRECTORY + "/CHUNKS_SORTED.txt", this.integerConverter);

        merger.mergeAllChunks(chunkLocations);
        List<Integer> mergedChunk = this.fileHandler.loadChunkFromFile("finalMergedChunks.txt", this.integerConverter);

        Assert.assertEquals(expected, mergedChunk);

        this.fileHandler.deleteChunkFile("finalMergedChunks.txt");
    } // end testMergeSortedChunksWithLargeChunks
} // end KWayMergeTest