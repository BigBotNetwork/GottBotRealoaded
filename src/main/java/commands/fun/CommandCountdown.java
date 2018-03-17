package commands.fun;

import commands.Command;
import core.Constants;
import core.MySQL;
import listener.Message;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.util.Timer;
import java.util.TimerTask;


public class CommandCountdown implements Command{
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        if (!MySQL.get("user", "id", event.getAuthor().getId(), "countdown").equals("none")) {
            if (args.length != 1) return;
            final int[] Timer = {Integer.parseInt(args[0])};
            MySQL.update("user", "countdown", "yes", "id", event.getAuthor().getId());
            Long Message = event.getTextChannel().sendMessage(new EmbedBuilder().setTitle("Countdown ").setDescription(String.valueOf(Timer[0])).setFooter(event.getAuthor().getAsMention(), event.getAuthor().getAvatarUrl()).build()).complete().getIdLong();
            net.dv8tion.jda.core.entities.Message message = event.getTextChannel().getMessageById(Message).complete();
            Timer[0]--;
            final int[] finalTimer = {Timer[0]};
            Timer t = new Timer();
            t.schedule(new TimerTask() {
                @Override
                public void run() {
                    message.editMessage(new EmbedBuilder().setTitle("Countdown ").setDescription(String.valueOf(finalTimer[0])).setFooter(event.getAuthor().getAsMention(), event.getAuthor().getAvatarUrl()).build()).queue();
                    if (finalTimer[0] == 0) {t.cancel(); MySQL.update("user", "countdown", "none", "id", event.getAuthor().getId());}
                    else finalTimer[0]--;
                }
            }, 1000, 1000);
        }
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {

    }

    @Override
    public String help() {
        return null;
    }
}
