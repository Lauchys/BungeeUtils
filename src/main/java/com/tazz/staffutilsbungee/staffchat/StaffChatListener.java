package com.tazz.staffutilsbungee.staffchat;

import com.tazz.staffutilsbungee.StaffUtils;
import com.tazz.staffutilsbungee.utils.ServerUtils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class StaffChatListener implements Listener {

    @EventHandler
    public void onChat(ChatEvent e) {
        if (!(e.getSender() instanceof ProxiedPlayer)) return;
        if (e.getMessage().startsWith("/")) return;

        ProxiedPlayer p = (ProxiedPlayer) e.getSender();

        if (!p.hasPermission("seabot.basic.staff")) return;
        if (!StaffChatCommand.staffChat.contains(p)) return;

        e.setCancelled(true);
        StaffUtils.getInstance().getStaffChatManager().staffChatMessage(p, e.getMessage());
    }
}
