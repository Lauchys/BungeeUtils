package com.tazz.staffutilsbungee.messages;

import com.tazz.staffutilsbungee.StaffUtils;
import com.tazz.staffutilsbungee.utils.Utils;
import litebans.api.Database;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.UUID;

public class MsgCommand extends Command {

    public MessageManager replyCommandManager;

    public MsgCommand(StaffUtils utils) {
        super("msg", "", "tell", "say");
        this.replyCommandManager = utils.messageManager;
    }

    public void execute(CommandSender sender, String[] args) {
        if (sender instanceof ProxiedPlayer) {
            ProxiedPlayer messageSender = (ProxiedPlayer) sender;

            UUID uuid = (messageSender).getUniqueId();
            String ip = (messageSender).getAddress().toString();
            boolean muted = Database.get().isPlayerMuted(uuid, ip);

            if (muted) {
                sender.sendMessage(Utils.c("&cCannot send a message while muted!"));
                return;
            }

            if(args.length < 2){
                messageSender.sendMessage(Utils.c("&cUsage: /msg (player) (message)"));
                return;
            }

            ProxiedPlayer messageReceiver = StaffUtils.getInstance().getProxy().getPlayer(args[0]);
            if (messageReceiver == null) {
                // Target player offline, send a message back to message sender
                messageSender.sendMessage(Utils.c("&c" + args[0] + " is not online."));
                return;
            }

            if(replyCommandManager.disabled.contains(messageReceiver.getUniqueId())){
                messageSender.sendMessage(Utils.c("&cThis player has turned off private messaging."));
                return;
            }

            // If player trying to message themselves
            if (messageSender.equals(messageReceiver)) {
                messageSender.sendMessage(Utils.c("&cYou cannot message yourself!"));
                return;
            }

            StringBuilder msg = new StringBuilder();
            for (int i = 1; i < args.length; i++) msg.append(args[i]).append(" ");

            replyCommandManager.message(messageReceiver, messageSender, msg.toString());
        } else {
            // Command only for players
            sender.sendMessage(Utils.c("&cThis command is only for players."));
        }
    }
}
