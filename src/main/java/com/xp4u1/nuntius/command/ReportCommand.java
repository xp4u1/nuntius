package com.xp4u1.nuntius.command;

import com.xp4u1.nuntius.Nuntius;
import com.xp4u1.nuntius.api.event.ReportEvent;
import com.xp4u1.nuntius.api.report.Reason;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.ArrayList;

public class ReportCommand extends Command {
    public ReportCommand(Nuntius plugin) {
        super("report");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!(sender instanceof ProxiedPlayer)) return;

        ProxiedPlayer player = (ProxiedPlayer) sender;
        switch (args.length) {
            case 1:
                if (args[0].equalsIgnoreCase("toggle")) {
                    if (player.hasPermission(Nuntius.getInstance().getConfig().getTeamPermission())) {
                        if (Nuntius.getInstance().getTeamBlacklist().contains(player.getUniqueId().toString())) {
                            Nuntius.getInstance().getTeamBlacklist().remove(player.getUniqueId().toString());
                            sendMessage(player, Nuntius.getMessage("toggle.subscribed"));
                        } else {
                            Nuntius.getInstance().getTeamBlacklist().add(player.getUniqueId().toString());
                            sendMessage(player, Nuntius.getMessage("toggle.unsubscribed"));
                        }
                    } else {
                        sendMessage(player, Nuntius.getMessage("noPermission"));
                    }
                    return;
                }
                sendHelp(player);
                break;
            case 2:
                ProxiedPlayer reportedPlayer = Nuntius.getInstance().getProxy().getPlayer(args[0]);
                if (reportedPlayer == null) {
                    sendMessage(player, Nuntius.getMessage("error.invalidPlayer"));
                    return;
                }
                Reason reason = getReason(args[1]);
                if (reason == null) {
                    sendMessage(player, Nuntius.getMessage("error.invalidReason"));
                    return;
                }

                Nuntius.getInstance().getProxy().getPluginManager().callEvent(new ReportEvent(reportedPlayer, player, reason));
                sendMessage(player, Nuntius.getMessage("received").replaceAll("%player%", reportedPlayer.getDisplayName()));
                break;
            default:
                sendHelp(player);
                break;
        }
    }

    /**
     * Sends message with default prefix.
     * @param player receiver of the message
     * @param message message in legacy text format
     */
    private void sendMessage(ProxiedPlayer player, String message) {
        player.sendMessage(new TextComponent(TextComponent.fromLegacyText(
                Nuntius.getMessage("prefix") + message
        )));
    }

    private void sendHelp(ProxiedPlayer player) {
        player.sendMessage(new TextComponent(TextComponent.fromLegacyText(Nuntius.getMessage("help.header"))));
        player.sendMessage(new TextComponent(TextComponent.fromLegacyText(
                Nuntius.getMessage("help.report")
        )));
        if (player.hasPermission(Nuntius.getInstance().getConfig().getTeamPermission())) {
            player.sendMessage(new TextComponent(TextComponent.fromLegacyText(
                    Nuntius.getMessage("help.reportToggle")
            )));
        }

        player.sendMessage(new TextComponent(TextComponent.fromLegacyText(
                "\n" + Nuntius.getMessage("help.reasons")
        )));
        ArrayList<Reason> reasons = Nuntius.getInstance().getConfig().getReasons();
        for (int i = 0; i < reasons.size(); i++) {
            Reason reason = reasons.get(i);

            TextComponent message = new TextComponent(TextComponent.fromLegacyText(
                    Nuntius.getMessage("help.reason")
                            .replaceAll("%index%", Integer.toString(i + 1))
                            .replaceAll("%reason%", reason.getTitle())
            ));
            // Do not set HoverEvent when message is empty.
            if (reason.getDescription() != null)
                message.setHoverEvent(new HoverEvent(
                        HoverEvent.Action.SHOW_TEXT,
                        new ComponentBuilder(reason.getDescription()).create()
                ));
            player.sendMessage(message);
        }
        player.sendMessage(new TextComponent(TextComponent.fromLegacyText(Nuntius.getMessage("help.footer"))));
    }

    /**
     * Gets the reason for a given index or title.
     * @param reason Can be the index (starting from 1) of the reason or the title.
     * @return Reason
     */
    private Reason getReason(String reason) {
        try {
            int index = Integer.parseInt(reason);
            index -= 1; // Convert to zero-based index.

            try {
                return Nuntius.getInstance().getConfig().getReasons().get(index);
            } catch (IndexOutOfBoundsException e) {
                return null;
            }
        } catch (NumberFormatException e) {
            // Reason was not the index.
            for (Reason entry : Nuntius.getInstance().getConfig().getReasons()) {
                if (entry.getTitle().equalsIgnoreCase(reason))
                    return entry;
            }
        }

        return null;
    }
}
