package com.steamsworld.plugins.command;

import com.steamsworld.plugins.Locale;
import com.steamsworld.plugins.PollsPlugin;
import com.steamsworld.plugins.question.Question;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Steamworks (Steamworks#1127)
 * Saturday 15 2022 (6:08 PM)
 * polls (com.steamsworld.plugins.command)
 */
public class SendPollCommand implements TabExecutor {

    private final PollsPlugin plugin;

    public SendPollCommand(PollsPlugin plugin) {
        this.plugin = plugin;

        plugin.getCommand("sendpoll").setExecutor(this);
        plugin.getCommand("sendpoll").setTabCompleter(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!sender.hasPermission("polls.commands.send")) {
            sender.sendMessage(Locale.NO_PERMISSION.get());
            return true;
        }

        if(args.length == 0 || args.length == 1) {
            sender.sendMessage(Locale.SEND_USAGE.get());
            return true;
        }

        Optional<Question> optional = plugin.getQuestionHandler().get(args[1]);
        List<Player> sendTo = new ArrayList<>();
        if(args[0].equalsIgnoreCase("all"))
            sendTo.addAll(plugin.getServer().getOnlinePlayers());
        else {
            Player player = plugin.getServer().getPlayer(args[0]);
            if(player == null) {
                sender.sendMessage(Locale.PLAYER_NOT_FOUND.get().replace("{name}", args[0]));
                return true;
            }
            sendTo.add(player);
        }

        if(!optional.isPresent()) {
            sender.sendMessage(Locale.QUESTION_NOT_FOUND.get().replace("{id}", args[1]));
            return true;
        }

        Question question = optional.get();
        for(Player player : sendTo)
            plugin.getQuestionHandler().send(player, question);

        sender.sendMessage(Locale.SENT_POLL.get());
        sendTo.clear();;
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if(!sender.hasPermission("polls.commands.send"))
            return Collections.emptyList();

        if(args.length == 1) {
            List<String> matches = plugin.getServer()
                    .getOnlinePlayers()
                    .parallelStream()
                    .map(HumanEntity::getName)
                    .collect(Collectors.toList());

            matches.add("all");
            return matches
                    .parallelStream()
                    .filter(name -> name.toLowerCase().startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        } else if(args.length == 2) {
            return plugin.getQuestionHandler()
                    .get()
                    .parallelStream()
                    .map(Question::getId)
                    .filter(id -> id.toLowerCase().startsWith(args[1].toLowerCase()))
                    .collect(Collectors.toList());
        }

        return Collections.emptyList();
    }
}
