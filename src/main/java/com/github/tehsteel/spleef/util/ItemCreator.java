package com.github.tehsteel.spleef.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public final class ItemCreator {
	private final ItemStack item;
	private final ItemMeta meta;


	private ItemCreator(final Material material) {
		this.item = new ItemStack(material);
		this.meta = item.getItemMeta();
	}


	public static ItemCreator of(final Material material) {
		return new ItemCreator(material);
	}

	public ItemCreator setDisplayName(final String text) {
		meta.displayName(MiniMessage.miniMessage().deserialize(text));
		return this;

	}

	public ItemCreator setDisplayName(final String text, final boolean italic) {
		meta.displayName(MiniMessage.miniMessage().deserialize(text).decoration(TextDecoration.ITALIC, italic));
		return this;
	}

	public ItemCreator setLore(final List<String> lore) {
		final List<Component> components = new ArrayList<>();


		lore.forEach(text -> components.add(MiniMessage.miniMessage().deserialize(text)));

		meta.lore(components);

		return this;
	}

	public ItemCreator addItemFlags(final ItemFlag... itemFlags) {
		item.addItemFlags(itemFlags);


		return this;
	}

	public ItemStack build() {
		item.setItemMeta(meta);

		return item;
	}

}
