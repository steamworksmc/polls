package com.steamsworld.plugins.question;

import com.steamsworld.plugins.Constants;
import com.steamsworld.plugins.Locale;
import com.steamsworld.plugins.PollsPlugin;
import com.steamsworld.plugins.voter.Voter;
import lombok.AllArgsConstructor;
import net.md_5.bungee.api.chat.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * @author Steamworks (Steamworks#1127)
 * Saturday 15 2022 (5:32 PM)
 * polls (com.steamsworld.plugins.question)
 */
@AllArgsConstructor
public class QuestionHandler {

    private final PollsPlugin plugin;

    public List<Question> get() {
        List<Question> questions = new ArrayList<>();
        FileConfiguration config = plugin.getConfig();

        for(String id : config.getConfigurationSection("questions").getKeys(false)) {
            ConfigurationSection section = config.getConfigurationSection("questions." + id);
            String message = section.getString("message");
            List<String> answers = section.getStringList("answers");
            Question question = new Question(id, message, answers);
            Set<SerializableQuestion> serializableQuestions = SerializableQuestionHandler.getQuestions();

            if(!serializableQuestions.isEmpty())
                for(SerializableQuestion serializableQuestion : serializableQuestions)
                    if(serializableQuestion.getId().equalsIgnoreCase(question.getId()))
                        for(Voter voter : serializableQuestion.getVoters())
                            question.addVoter(voter);
            else SerializableQuestionHandler.register(new SerializableQuestion(question.getId(), question.getVoters()));
            questions.add(question);
        }

        return questions;
    }

    public void modify(Question question) {
        Optional<SerializableQuestion> serializableQuestion = SerializableQuestionHandler.getPossibleEqual(question);
        if(!serializableQuestion.isPresent()) {
            SerializableQuestionHandler.register(new SerializableQuestion(question.getId(), question.getVoters()));
            return;
        }

        SerializableQuestion serializable = serializableQuestion.get();
        SerializableQuestion copy = serializable.copy();

        copy.setVoters(question.getVoters());
        SerializableQuestionHandler.replace(serializable, copy);
    }

    private List<BaseComponent[]> getAnswerComponent(Question question) {
        List<BaseComponent[]> components = new ArrayList<>();
        for(String validAnswer : question.getAcceptedAnswers()) {
            String transformed = Constants.ANSWERS.apply(validAnswer);
            BaseComponent[] component = new ComponentBuilder(Constants.COLORS.apply(validAnswer))
                    .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, String.format("/poll vote %s %s", question.getId(), transformed)))
                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(Constants.COLORS.apply(Locale.HOVER_MESSAGE.get()))))
                    .create();
            components.add(component);
        }
        return components;
    }

    public void send(Player player, Question question) {
        player.sendMessage(Constants.COLORS.apply(question.getMessage()));
        player.sendMessage(" ");

        for(BaseComponent[] component : this.getAnswerComponent(question))
            player.spigot().sendMessage(component);
    }

    public Optional<Question> get(String id) {
        return this.get()
                .parallelStream()
                .filter(question -> question.getId().toLowerCase().equalsIgnoreCase(id.toLowerCase()))
                .findFirst();
    }

}
