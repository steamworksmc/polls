package com.steamsworld.plugins;

import lombok.AllArgsConstructor;
import org.bukkit.ChatColor;

import java.text.MessageFormat;

/**
 * @author Steamworks (Steamworks#1127)
 * Saturday 15 2022 (5:29 PM)
 * polls (com.steamsworld.plugins)
 */
@AllArgsConstructor
public enum Locale {

    /* usages */
    VOTES_USAGE("vote-usage"),
    SEND_USAGE("send-usage"),
    POLL_USAGE("poll-usage"),


    HOVER_MESSAGE("hover"),
    QUESTION_NOT_FOUND("question-not-found"),
    VOTE_FOR("vote-for"),
    ANSWER("answer"),
    NO_PERMISSION("no-permission"),
    PLAYER_NOT_FOUND("player-not-found"),
    SENT_POLL("sent-poll"),
    RELOADED("reloaded"),
    NO_CONSOLE("no-console"),
    ALREADY_VOTED("already-voted"),
    INVALID_ANSWER("invalid-answer"),
    VOTED("voted");

    String path;

    public String get() {
        return Constants.COLORS.apply(PollsPlugin.getInstance().getConfig().getString("messages." + this.path));
    }

}
