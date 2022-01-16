package com.steamsworld.plugins.question;

import com.steamsworld.plugins.voter.Voter;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.*;

/**
 * @author Steamworks (Steamworks#1127)
 * Saturday 15 2022 (5:05 PM)
 * polls (com.steamsworld.plugins.object)
 */
@AllArgsConstructor
@Getter
public class Question {

    private final String id, message;
    private final List<String> acceptedAnswers;
    private final Set<Voter> voters;

    public Question(String id, String message, List<String> acceptedAnswers) {
        this(id, message, acceptedAnswers, new HashSet<>());
    }

    public void addVoter(UUID uuid, String answer) {
        addVoter(new Voter(uuid, answer));
    }

    public void addVoter(Voter voter) {
        voters.add(voter);
    }

    public Optional<String> getAnswer(UUID uuid) {
        return voters.parallelStream()
                .filter(voter -> voter.getUuid().equals(uuid))
                .findFirst()
                .map(Voter::getAnswer);
    }

    public Question copy() {
        return new Question(id, message, acceptedAnswers, voters);
    }
}
