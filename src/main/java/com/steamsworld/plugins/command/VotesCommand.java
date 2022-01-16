package com.steamsworld.plugins.command;

import com.steamsworld.plugins.Constants;
import com.steamsworld.plugins.Locale;
import com.steamsworld.plugins.PollsPlugin;
import com.steamsworld.plugins.question.Question;
import lombok.AllArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Steamworks (Steamworks#1127)
 * Saturday 15 2022 (5:27 PM)
 * polls (com.steamsworld.plugins.command)
 */
public class VotesCommand implements TabExecutor {

    private final PollsPlugin plugin;

    public VotesCommand(PollsPlugin plugin) {
        this.plugin = plugin;

        plugin.getCommand("votes").setExecutor(this);
        plugin.getCommand("votes").setTabCompleter(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if(args.length == 0) {
            sender.sendMessage(Locale.VOTES_USAGE.get());
            return true;
        }

        Optional<Question> optional = plugin.getQuestionHandler().get(args[0]);
        if(!optional.isPresent()) {
            sender.sendMessage(Locale.QUESTION_NOT_FOUND.get().replace("{id}", args[0]));
            return true;
        }

        Question question = optional.get();
        sender.sendMessage(Locale.VOTE_FOR.get().replace("{question}", args[0]));

        for(String answer : question.getAcceptedAnswers()) {
            String transformed = Constants.ANSWERS.apply(answer);
            long votes = question.getVoters().parallelStream().filter(v -> v.getAnswer().equalsIgnoreCase(transformed)).count();
            sender.sendMessage(Constants.COLORS.apply(Locale.ANSWER.get().replace("{answer}", answer).replace("{votes}", "" + votes)));
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        return
                args.length == 1 ?
                        plugin.getQuestionHandler()
                                .get()
                                .parallelStream()
                                .map(Question::getId)
                                .filter(id -> id.toLowerCase().startsWith(args[0].toLowerCase()))
                                .collect(Collectors.toList()) :
                        Collections.emptyList();
    }
}
