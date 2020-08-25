package com.miyukideveloper.ide.discordrp;

import com.miyukideveloper.ide.compatibilites.Language;
import com.miyukideveloper.ide.editor.Editor;

import net.arikia.dev.drpc.DiscordEventHandlers;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;

public class DRichPresence {
	
	/*  c plus plus?  */
	private
		DiscordRPC rpc;
	private  
		DiscordEventHandlers handlers;
	private
		DiscordRichPresence presence;
	
	@SuppressWarnings("static-access")
	public DRichPresence() {
		rpc = new DiscordRPC();
		String id = "747666089995141220";
		handlers = new DiscordEventHandlers();
		rpc.discordInitialize(id, handlers, true);
		
		presence = new DiscordRichPresence();
		presence.smallImageText = "https://miyukinozomi.github.io/miyuki.github.io/";
		presence.largeImageKey = "icon";
		presence.startTimestamp = System.currentTimeMillis() / 1000;
		presence.state = "<status>";
		rpc.discordUpdatePresence(presence);
	}
	
	@SuppressWarnings("static-access")
	public void onUpdate(Editor currentEditor,String workspace) {
		if(currentEditor.getFile() != null) {
			presence.state = Language.getLangKey("current_file") + ": " + currentEditor.getFile().getName();
		}else {
			presence.state = Language.getLangKey("current_file") + ": " + Language.getLangKey("welcome_page");
		}
		presence.details =  Language.getLangKey("current_workspace") + ": " + workspace;		
		
		rpc.discordUpdatePresence(presence);
	}
	
	public DiscordRPC getRpc() {
		return rpc;
	}
	
	public DiscordEventHandlers getHandlers() {
		return handlers;
	}
	
	public DiscordRichPresence getPresence() {
		return presence;
	}
}