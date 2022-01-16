package com.steamsworld.plugins.question;

import com.steamsworld.plugins.voter.Voter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

/**
 * @author Steamworks (Steamworks#1127)
 * Saturday 15 2022 (5:35 PM)
 * polls (com.steamsworld.plugins.question)
 */
@AllArgsConstructor
@Getter
@Setter
public class SerializableQuestion {

    private final String id;
    private Set<Voter> voters;

    public SerializableQuestion copy() {
        return new SerializableQuestion(id, voters);
    }
}
