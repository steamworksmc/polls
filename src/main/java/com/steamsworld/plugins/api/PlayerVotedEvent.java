package com.steamsworld.plugins.api;

import com.steamsworld.plugins.question.Question;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;

/**
 * @author Steamworks (Steamworks#1127)
 * Saturday 15 2022 (5:12 PM)
 * polls (com.steamsworld.plugins.api)
 */
@Getter
public class PlayerVotedEvent extends PlayerEvent {

    public static HandlerList HANDLERS = new HandlerList();

    private final Question question;
    private final String answer;

    public PlayerVotedEvent(Player player, Question question, String answer) {
        super(player);

        this.question = question;
        this.answer = answer;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
}
