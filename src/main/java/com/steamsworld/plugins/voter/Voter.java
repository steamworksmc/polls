package com.steamsworld.plugins.voter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

/**
 * @author Steamworks (Steamworks#1127)
 * Saturday 15 2022 (5:07 PM)
 * polls (com.steamsworld.plugins.object)
 */
@AllArgsConstructor
@Getter
public class Voter {

    private final UUID uuid;
    private final String answer;

}
