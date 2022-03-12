package nl.transdeveloper.mod;

import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.stream.Collectors;

import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

public class Client extends WebSocketClient {
    public Client(URI serverURI) {
        super(serverURI);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        send("handshake");
        System.out.println("new connection opened");
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("closed with exit code " + code + " additional info: " + reason);
    }

    @Override
    public void onMessage(String message) {
        if(!message.contains("handshake")) {
            String[] cms = message.split("\\|");
            if(Minecraft.getMinecraft().getCurrentServerData().isOnLAN() == false) {
                if (cms[0].contains(Minecraft.getMinecraft().getCurrentServerData().serverIP)) {
                    if (cms[1].split(" ")[1].startsWith(">")) {
                        String prefix = TextFormatting.WHITE + cms[1].split(" ")[0];
                        String msm = "";
                        if(cms[1].split("> ").length == 2){
                            msm = cms[1].split("> ")[1].replace(">", "> ");
                        }else if(cms[1].split("> ").length == 3) {
                            msm = "> " + cms[1].split("> ")[2];
                        }
                        Minecraft.getMinecraft().player.sendMessage(new TextComponentString(prefix + TextFormatting.GREEN + " " + msm));
                    } else {
                        Minecraft.getMinecraft().player.sendMessage(new TextComponentString(cms[1]));
                    }
                }
            }
        }
    }

    @Override
    public void onMessage(ByteBuffer buffers) {
        byte[] buffer = new byte[buffers.remaining()];
        buffers.get(buffer);
        String message = new String(buffer, StandardCharsets.UTF_8);
        if(!message.contains("handshake")) {
            String[] cms = message.split("\\|");
            if(Minecraft.getMinecraft().getCurrentServerData().isOnLAN() == false) {
                if (cms[0].contains(Minecraft.getMinecraft().getCurrentServerData().serverIP)) {
                    if (cms[1].split(" ")[1].startsWith(">")) {
                        String prefix = TextFormatting.WHITE + cms[1].split(" ")[0];
                        String msm = "";
                        if(cms[1].split("> ").length == 2){
                            msm = cms[1].split("> ")[1].replace(">", "> ");
                        }else if(cms[1].split("> ").length == 3) {
                            msm = "> " + cms[1].split("> ")[2];
                        }
                        Minecraft.getMinecraft().player.sendMessage(new TextComponentString(prefix + TextFormatting.GREEN + " " + msm));
                    } else {
                        Minecraft.getMinecraft().player.sendMessage(new TextComponentString(cms[1]));
                    }
                }
            }
        }
    }

    @Override
    public void onError(Exception ex) {
        System.err.println("An error occurred:" + ex);
    }
}
