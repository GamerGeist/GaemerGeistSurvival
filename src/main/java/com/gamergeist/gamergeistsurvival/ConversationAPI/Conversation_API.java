package com.gamergeist.gamergeistsurvival.ConversationAPI;

import com.gamergeist.gamergeistsurvival.GamerGeistSurvival;
import org.bukkit.conversations.Conversable;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.entity.Player;

public class Conversation_API {
        public static ConversationFactory conv;
        private static GamerGeistSurvival plugin;

        public Conversation_API(GamerGeistSurvival plugin){
        conv = new ConversationFactory(plugin);
        Conversation_API.plugin = plugin;
        }

        public static void SendInputMessage(Player player){
            conv.withFirstPrompt(new FirstPrompt(plugin)).withEscapeSequence("cancel")
            .withLocalEcho(false).buildConversation((Conversable) player).begin();
        }

}
