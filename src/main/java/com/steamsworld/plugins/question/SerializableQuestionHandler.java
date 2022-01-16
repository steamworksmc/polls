package com.steamsworld.plugins.question;

import lombok.Getter;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * @author Steamworks (Steamworks#1127)
 * Saturday 15 2022 (5:36 PM)
 * polls (com.steamsworld.plugins.question)
 */
public class SerializableQuestionHandler {

    @Getter
    private static final Set<SerializableQuestion> questions = new HashSet<>();

    public static Optional<SerializableQuestion> getPossibleEqual(Question question) {
        return questions
                .parallelStream()
                .filter(s -> s.getId().toLowerCase().equalsIgnoreCase(question.getId()))
                .findFirst();
    }

    public static void register(SerializableQuestion question) {
        if(!questions.contains(question))
            questions.add(question);
    }

    public static void replace(SerializableQuestion oldQuestion, SerializableQuestion newQuestion) {
        questions.remove(oldQuestion);
        questions.add(newQuestion);
    }

}
