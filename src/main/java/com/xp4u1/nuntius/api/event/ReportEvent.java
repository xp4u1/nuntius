package com.xp4u1.nuntius.api.event;

import com.xp4u1.nuntius.api.report.Reason;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Event;

public class ReportEvent extends Event {
    private final ProxiedPlayer reportedPlayer;
    private final ProxiedPlayer sender;
    private final Reason reason;

    public ReportEvent(ProxiedPlayer reportedPlayer, ProxiedPlayer sender, Reason reason) {
        this.reportedPlayer = reportedPlayer;
        this.sender = sender;
        this.reason = reason;
    }

    public ProxiedPlayer getReportedPlayer() {
        return reportedPlayer;
    }

    public ProxiedPlayer getSender() {
        return sender;
    }

    public Reason getReason() {
        return reason;
    }
}
