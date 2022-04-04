package com.xp4u1.nuntius.listener;

import com.xp4u1.nuntius.Nuntius;
import com.xp4u1.nuntius.api.event.ReportEvent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ReportListener implements Listener {
    @EventHandler
    public void onReport(ReportEvent reportEvent) {
        TextComponent jumpMessage = new TextComponent(TextComponent.fromLegacyText(
                "\n" + Nuntius.getMessage("team.jumpMessage")
                        .replaceAll("%player%", reportEvent.getReportedPlayer().getDisplayName())
        ));
        String servername = reportEvent.getReportedPlayer().getServer().getInfo().getName();
        jumpMessage.setHoverEvent(new HoverEvent(
                HoverEvent.Action.SHOW_TEXT,
                new ComponentBuilder(
                        Nuntius.getMessage("team.jumpHoverMessage").replaceAll("%servername%", servername)
                ).create())
        );
        jumpMessage.setClickEvent(new ClickEvent(
                ClickEvent.Action.RUN_COMMAND,
                Nuntius.getInstance().getConfig().getJumpCommand()
                        .replaceAll("%servername%", servername)
                        .replaceAll("%player%", reportEvent.getReportedPlayer().getDisplayName())
        ));

        TextComponent[] messages = {
                new TextComponent("\n"),
                new TextComponent(TextComponent.fromLegacyText(
                        Nuntius.getMessage("prefix") + Nuntius.getMessage("team.newReport")
                )),
                new TextComponent(TextComponent.fromLegacyText(
                        "\n\n" + Nuntius.getMessage("team.submitted")
                                .replaceAll("%player%", reportEvent.getSender().getDisplayName())
                )),
                new TextComponent(TextComponent.fromLegacyText(
                        "\n" + Nuntius.getMessage("team.reported")
                                .replaceAll("%player%", reportEvent.getReportedPlayer().getDisplayName())
                )),
                new TextComponent(TextComponent.fromLegacyText(
                        "\n\n" + Nuntius.getMessage("team.reason")
                                .replaceAll("%reason%", reportEvent.getReason().getTitle())
                )),
                jumpMessage,
                new TextComponent("\n")
        };

        Nuntius.getInstance().getProxy().getPlayers().stream()
                .filter(proxiedPlayer -> proxiedPlayer.hasPermission(Nuntius.getInstance().getConfig().getTeamPermission()))
                .filter(proxiedPlayer -> !Nuntius.getInstance().getTeamBlacklist().contains(proxiedPlayer.getUniqueId().toString()))
                .forEach(proxiedPlayer -> proxiedPlayer.sendMessage(messages));
    }
}
