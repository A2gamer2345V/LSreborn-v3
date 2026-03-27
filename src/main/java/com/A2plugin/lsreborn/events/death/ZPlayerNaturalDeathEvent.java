package com.A2plugin.lsreborn.events.death;

import com.A2plugin.lsreborn.events.ZPlayerDeathEventBase;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.entity.PlayerDeathEvent;

public class ZPlayerNaturalDeathEvent extends ZPlayerDeathEventBase {
    @Getter @Setter
    private double heartsToLose;

    @Getter @Setter
    private boolean shouldDropHearts;

    @Getter @Setter
    private String deathMessage;

    public ZPlayerNaturalDeathEvent(PlayerDeathEvent originalEvent, double heartsToLose) {
        super(originalEvent);
        this.heartsToLose = heartsToLose;
        this.shouldDropHearts = false;
        this.deathMessage = originalEvent.getDeathMessage();
    }
}