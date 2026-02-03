package lymesque.givemeaheart;

import net.fabricmc.api.ModInitializer;

public class GiveMeAHeart implements ModInitializer {
	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		System.out.println("'Give me a heart' loaded.");
	}
}
