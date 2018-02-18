package commands;

import listener.commandListener;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import stuff.SECRETS;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static stuff.DATA.url;

public class registeruser implements Command {
    private static Logger logger = LoggerFactory.getLogger(registeruser.class);
    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        return false;
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {

        if (event.getAuthor().getId().equals("261083609148948488")) {
            try {
                Connection con = DriverManager.getConnection(url + "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", SECRETS.user, SECRETS.password);
                PreparedStatement pst = con.prepareStatement("Select * FROM `server` WHERE ID=" + event.getGuild().getId());
                ResultSet rs = pst.executeQuery();
                if (!rs.next()) {
                    con = DriverManager.getConnection(url + "?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", SECRETS.user, SECRETS.password);
                    pst = con.prepareStatement("INSERT INTO `server` (`ID`) VALUES ('" + event.getGuild().getId() + "');");
                    pst.execute();
                    rs.close();
                    logger.info("neuer Server: Name: " + event.getGuild().getName() + " ID: " + event.getGuild().getId() + " Member: " + event.getGuild().getMembers().size());

                }
                rs.close();

                int i = 0;
                while (event.getGuild().getMembers().size() - 1 >= i) {
                    pst = con.prepareStatement("Select * FROM `user` WHERE ID=" + event.getGuild().getMembers().get(i).getUser().getId());
                    rs = pst.executeQuery();
                    if (!rs.next()) {
                        pst = con.prepareStatement("INSERT INTO `user` (`ID`) VALUES ('" + event.getGuild().getMembers().get(i).getUser().getId() + "');");
                        pst.execute();
                        pst.close();
                        logger.info("neuer User in database Name: " + event.getGuild().getMembers().get(i).getUser().getName() + " ID: " + event.getGuild().getMembers().get(i).getUser().getId() + " von " + event.getGuild().getName());
                    }
                    i++;
                }

                rs.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

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
