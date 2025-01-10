package org.eu.hanana.reimu.mc.lcr.quilt;

import net.fabricmc.api.ModInitializer;
import org.quiltmc.loader.api.ModContainer;
import org.eu.hanana.reimu.mc.lcr.fabriclike.ExampleModFabricLike;

public final class ExampleModQuilt implements ModInitializer {
    @Override
    public void onInitialize() {
        // Run the Fabric-like setup.
        ExampleModFabricLike.init();
    }
}
