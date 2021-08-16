package com.gamergeist.gamergeistsurvival.ConversationAPI;

import com.gamergeist.gamergeistsurvival.GamerGeistSurvival;
import org.bukkit.conversations.Conversable;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.NumericPrompt;
import org.bukkit.conversations.Prompt;

public class FirstPrompt extends NumericPrompt {

    GamerGeistSurvival plugin;

    public FirstPrompt(GamerGeistSurvival plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getPromptText(ConversationContext context) {
        return "Please type the amount you want to withdraw:\nIf you want to cancel, type §4cancel";
    }


    @Override
    protected Prompt acceptValidatedInput(ConversationContext context, Number input) {
        Conversable c = context.getForWhom();
        if (input.intValue() >= 0) {
            //giving stuff here
            c.sendRawMessage("you have withdrawn: " + "int | Resource name");  //I don't know what else to reply XD
        } else {
            c.sendRawMessage("Input cannot be a negative number");
        }
        return END_OF_CONVERSATION;
    }

    @Override
    protected String getFailedValidationText(ConversationContext context, Number invalidInput) {
        return "&c Only use integer values";
    }

    protected String getInputNotNumericText(ConversationContext context, Number invalidInput) {
        return "&c Only use integer values";
    }

    @Override
    protected boolean isInputValid(ConversationContext context, String input) {
        if (input == null) {
            return false;
        }
        try {
            int i = Integer.parseInt(input);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}