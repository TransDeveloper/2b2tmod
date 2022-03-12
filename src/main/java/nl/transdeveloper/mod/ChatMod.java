package nl.transdeveloper.mod;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.enums.ReadyState;

import java.net.URI;
import java.net.URISyntaxException;

@Mod(modid = ChatMod.MODID, name = ChatMod.NAME, version = ChatMod.VERSION)
public class ChatMod
{
    public static final String MODID = "chatmod";
    public static final String NAME = "ChatMod";
    public static final String VERSION = "1.0";
    public static WebSocketClient client;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) throws URISyntaxException {
        MinecraftForge.EVENT_BUS.register(this);
        client = new Client(new URI("ws://2b2t.ngrok.io/"));
        client.connect();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
    }

    @SubscribeEvent
    public void clientChat(ClientChatEvent event) throws URISyntaxException {
        if(Minecraft.getMinecraft().getCurrentServerData() != null) {
            if (event.getMessage().startsWith("\\")) {
                event.setCanceled(true);
                if (client.getReadyState() == ReadyState.OPEN) {
                    client.send(Minecraft.getMinecraft().getCurrentServerData().serverIP + "|<" + Minecraft.getMinecraft().player.getName() + "> " + event.getMessage().substring(1));
                } else {
                    try {
                        Minecraft.getMinecraft().player.sendMessage(new TextComponentString(TextFormatting.RED + "Lost connection to server, reconnecting..."));
                        client.reconnect();
                    } catch (Exception e) {
                        Minecraft.getMinecraft().player.sendMessage(new TextComponentString(TextFormatting.RED + e.getMessage()));
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
