package com.A2plugin.lsreborn.addon;

import com.A2plugin.lsreborn.LSReborn;

public abstract class LSRebornAddon {
    private final AddonMetadata metadata;
    private final LSReborn mainPlugin;
    private boolean enabled = false;

    public LSRebornAddon(AddonMetadata metadata, LSReborn mainPlugin) {
        this.metadata = metadata;
        this.mainPlugin = mainPlugin;
    }

    public abstract void onEnable();
    public abstract void onDisable();

    public AddonMetadata getMetadata() {
        return metadata;
    }

    public LSReborn getMainPlugin() {
        return mainPlugin;
    }

    public boolean isEnabled() {
        return enabled;
    }

    void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
