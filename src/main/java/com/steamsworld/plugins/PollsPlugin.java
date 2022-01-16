package com.steamsworld.plugins;

import com.steamsworld.plugins.command.PollCommand;
import com.steamsworld.plugins.command.SendPollCommand;
import com.steamsworld.plugins.command.VotesCommand;
import com.steamsworld.plugins.question.QuestionAnnouncer;
import com.steamsworld.plugins.question.QuestionHandler;
import com.steamsworld.plugins.question.SerializableQuestion;
import com.steamsworld.plugins.question.SerializableQuestionHandler;
import com.steamsworld.plugins.voter.VoteFile;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Set;

/**
 * @author Steamworks (Steamworks#1127)
 * Saturday 15 2022 (5:01 PM)
 * polls (com.steamsworld.plugins)
 */
public class PollsPlugin extends JavaPlugin {

    @Getter
    private static PollsPlugin instance;

    @Getter private QuestionHandler questionHandler;
    private VoteFile file;
    private QuestionAnnouncer announcer;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();

        file = new VoteFile(this.getDataFolder());
        Set<SerializableQuestion> questions = file.deserialize();
        if(!questions.isEmpty())
            for(SerializableQuestion question : questions)
                SerializableQuestionHandler.register(question);


        questionHandler = new QuestionHandler(this);
        (announcer = new QuestionAnnouncer(this)).load();

        new PollCommand(this);
        new VotesCommand(this);
        new SendPollCommand(this);

        this.getServer().getScheduler().runTaskTimerAsynchronously(this, () -> file.serialize(SerializableQuestionHandler.getQuestions()), 300 * 20, 600 * 20 * 3);
    }

    public void reload() {
        reloadConfig();
        announcer.reload();
        file.serialize(SerializableQuestionHandler.getQuestions());
    }

    @Override
    public void onDisable() {
        instance = null; /* reload safe */
        file.serialize(SerializableQuestionHandler.getQuestions());
    }
}
