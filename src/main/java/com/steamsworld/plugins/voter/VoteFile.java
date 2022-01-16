package com.steamsworld.plugins.voter;

import com.steamsworld.plugins.Constants;
import com.steamsworld.plugins.question.SerializableQuestion;
import lombok.Getter;
import lombok.Setter;

import java.io.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Steamworks (Steamworks#1127)
 * Saturday 15 2022 (5:55 PM)
 * polls (com.steamsworld.plugins.voter)
 */
@SuppressWarnings("ResultOfMethodCallIgnored")
public class VoteFile {

    private final File file;

    public VoteFile(File directory) {
        this.file = new File(directory + File.separator, "voters.json");
        create();
    }

    public Set<SerializableQuestion> deserialize() {
        try(BufferedReader reader = new BufferedReader(new FileReader(file))) {
            QuestionResult result = Constants.GSON.fromJson(reader, QuestionResult.class);
            return result == null ? Collections.emptySet() : result.getQuestions();
        } catch (IOException ignored) {
            return Collections.emptySet();
        }
    }

    public void serialize(Set<SerializableQuestion> questions) {
        file.delete();
        create();
        try(Writer writer = new FileWriter(file)) {
            QuestionResult result = new QuestionResult();
            result.setQuestions(questions);
            Constants.GSON.toJson(result, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void create() {
        if(!file.exists()) {
            if(!file.getParentFile().exists())
                file.getParentFile().mkdirs();

            try {
                file.createNewFile();
            } catch (IOException ignored) {}
        }
    }

    @Getter
    @Setter
    public static class QuestionResult {
        private Set<SerializableQuestion> questions = new HashSet<>();
    }

}
