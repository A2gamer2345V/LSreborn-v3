package com.A2plugin.lsreborn.events.death;

import com.A2plugin.lsreborn.events.ZPlayerDeathEventBase;
import com.A2plugin.lsreborn.util.MessageUtils;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;

public class ZPlayerMaxHeartsReachedEvent extends ZPlayerDeathEventBase {
    @Getter
    private final Player killer;
    
    @Getter
    private final double maxHeartsLimit;
    
    @Getter @Setter
    private boolean shouldDropHeartsInstead;
    
    @Getter @Setter
    private Component maxHeartsMessage;

    public ZPlayerMaxHeartsReachedEvent(PlayerDeathEvent originalEvent, Player killer, double maxHearts) {
        super(originalEvent);
        this.killer = killer;
        this.maxHeartsLimit = maxHearts;
        this.shouldDropHeartsInstead = false;
        this.maxHeartsMessage = MessageUtils.getAndFormatMsg(
                false,
                "maxHeartLimitReached",
                "&cYou already reached the limit of %limit% hearts!",
                new MessageUtils.Replaceable("%limit%", String.valueOf((int) (maxHearts / 2)))
        );
    }
}