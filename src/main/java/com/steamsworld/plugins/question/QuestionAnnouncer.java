package com.steamsworld.plugins.question;

import com.steamsworld.plugins.PollsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Steamworks (Steamworks#1127)
 * Saturday 15 2022 (6:01 PM)
 * polls (com.steamsworld.plugins.question)
 */
public class QuestionAnnouncer {

    private final PollsPlugin plugin;
    private BukkitTask task;
    private int count = 0;

    public QuestionAnnouncer(PollsPlugin plugin) {
        this.plugin = plugin;
    }

    public void load() {
        if(!plugin.getConfig().getBoolean("announcer.enabled"))
            return;

        int interval = plugin.getConfig().getInt("announcer.interval");
        if(interval > 0)
            task = plugin.getServer().getScheduler().runTaskTimer(plugin, () -> {
                if(!plugin.getConfig().getBoolean("announcer.in-order")) {
                    List<Question> questions = plugin.getQuestionHandler().get();
                    Question question = questions.get(ThreadLocalRandom.current().nextInt(0, questions.size()));

                    for(Player player : plugin.getServer().getOnlinePlayers()) {
                        if (question.getAnswer(player.getUniqueId()).isPresent())
                            continue;

                        plugin.getQuestionHandler().send(player, question);
                    }
                } else {
                    Question question = plugin.getQuestionHandler().get().get(count);
                    for(Player player : Bukkit.getServer().getOnlinePlayers()) {
                        if(question.getAnswer(player.getUniqueId()).isPresent())
                            continue;

                        plugin.getQuestionHandler().send(player, question);
                    }

                    count++;
                    if(count + 1 > plugin.getQuestionHandler().get().size())
                        count = 0;
                }
            }, 0, interval * 20L);
    }

    public void reload() {
        if(task != null)
            task.cancel();

        if(plugin.getConfig().getBoolean("announcer.enabled"))
            load();
    }

}
