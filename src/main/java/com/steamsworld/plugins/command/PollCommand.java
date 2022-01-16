package com.steamsworld.plugins.command;

import com.google.common.base.Joiner;
import com.steamsworld.plugins.Constants;
import com.steamsworld.plugins.Locale;
import com.steamsworld.plugins.PollsPlugin;
import com.steamsworld.plugins.api.PlayerVotedEvent;
import com.steamsworld.plugins.question.Question;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Steamworks (Steamworks#1127)
 * Saturday 15 2022 (6:17 PM)
 * polls (com.steamsworld.plugins.command)
 */
public class PollCommand implements TabExecutor {

    private final PollsPlugin plugin;

    public PollCommand(PollsPlugin plugin) {
        this.plugin = plugin;

        plugin.getCommand("poll").setExecutor(this);
        plugin.getCommand("poll").setTabCompleter(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(args.length == 0 || args.length == 2) {
            sender.sendMessage(Locale.POLL_USAGE.get());
            return true;
        }

        if(args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            if(!sender.hasPermission("poll.reload")) {
                sender.sendMessage(Locale.NO_PERMISSION.get());
                return true;
            }
            plugin.reload();
            sender.sendMessage(Locale.RELOADED.get());
            return true;
        }

        if(args.length == 1 && args[0].equalsIgnoreCase("vote")) {
            sender.sendMessage(Locale.POLL_USAGE.get());
            return true;
        }

        if(args.length >= 3 && args[0].equalsIgnoreCase("vote")) {
            if(!(sender instanceof Player)) {
                sender.sendMessage(Locale.NO_CONSOLE.get());
                return true;
            }

            Player player = (Player) sender;
            String id = args[1];
            String answer = String.join(" ", Arrays.copyOfRange(args, 2, args.length));
            Optional<Question> optional = plugin.getQuestionHandler().get(id);
            if(!optional.isPresent()) {
                player.sendMessage(Locale.QUESTION_NOT_FOUND.get().replace("{id}", id));
                return true;
            }

            Question question = optional.get();
            if(question.getAnswer(player.getUniqueId()).isPresent()) {
                player.sendMessage(Locale.ALREADY_VOTED.get());
                return true;
            }

            Question copy = question.copy();
            List<String> validAnswers = question.getAcceptedAnswers().parallelStream().map(v -> Constants.ANSWERS.apply(v)).collect(Collectors.toList());
            if(!validAnswers.contains(answer)) {
                player.sendMessage(Constants.COLORS.apply(Locale.INVALID_ANSWER.get().replace("{options}", Joiner.on(", ").join(question.getAcceptedAnswers()))));
                return true;
            }

            copy.addVoter(player.getUniqueId(), answer);
            plugin.getQuestionHandler().modify(copy);
            player.getServer()
                    .getPluginManager()
                    .callEvent(new PlayerVotedEvent(player, copy, answer));

            player.sendMessage(Locale.VOTED.get());
            return true;
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if(args.length == 1) {
            return Arrays.asList("vote", (sender.hasPermission("poll.reload") ? "reload" : ""))
                    .parallelStream()
                    .filter(match -> match.toLowerCase().startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        } else if(args.length == 2 && args[0].equalsIgnoreCase("vote")) {
            return plugin.getQuestionHandler()
                    .get()
                    .parallelStream()
                    .map(Question::getId)
                    .filter(id -> id.toLowerCase().startsWith(args[1].toLowerCase()))
                    .collect(Collectors.toList());
        } else if(args.length == 3 && args[0].equalsIgnoreCase("vote")) {
            Optional<Question> optional = plugin.getQuestionHandler().get(args[1]);
            if(optional.isPresent())
                return optional.get()
                    .getAcceptedAnswers()
                    .parallelStream()
                    .map(answer -> Constants.ANSWERS.apply(answer))
                    .filter(answer -> answer.toLowerCase().startsWith(args[2].toLowerCase()))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
