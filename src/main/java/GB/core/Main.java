package GB.core;

import GB.Handler;
import GB.commands.botowner.*;
import GB.commands.moderation.*;
import GB.commands.music.*;
import GB.commands.tools.*;
import GB.commands.usercommands.*;
import GB.listener.*;
import GB.stuff.SECRETS;
import net.dv8tion.jda.bot.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.bot.sharding.ShardManager;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.managers.AudioManager;
import net.dv8tion.jda.core.utils.SessionController;
import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;

import static GB.core.commandHandler.commands;

public class Main {
    private static DefaultShardManagerBuilder builder = new DefaultShardManagerBuilder();
    private static Logger logger = LoggerFactory.getLogger(Main.class);
    public static JDA jda;
    public static String[] args;
    private static boolean dev = true;
    private static SessionController sessionController;
    public static ShardManager shardManager;
    public static boolean uploaderrors;
    public static AudioManager audioManager;

    public static void main(String[] args2) {
        try {
            logger.info("------------------start Bot----------------------");
            //if (!new File("Gott.log").exists()) { new File("Gott.log").createNewFile();logger.info("created File Gott.log"); }
            if (!dev) {
                uploaderrors=true;
                FTPClient client = new FTPClient();
                FileInputStream fis;
                client.connect("ftp.bigbotnetwork.de");
                client.login(SECRETS.FTPUSER, SECRETS.FTPPW);
                String filename = "Gott.log";
                fis = new FileInputStream(filename);
                client.storeFile(filename, fis);
                client.logout();
                builder.addEventListeners(new BotList());
                builder.setShardsTotal(2);
            } else {
                uploaderrors=false;
                builder.setShardsTotal(1);
                logger.info("Dev Mode activated - Don't load Botlist listener - Don't upload the Log file - Don't upload errors");
                builder.setAutoReconnect(true);
            }
            logger.info("read Token and logins");
            new Handler().getMySQL().connect();
            builder.setToken(SECRETS.TOKEN);
            builder.addEventListeners(
                new commandListener(),
                new Guildjoin(),
                new Message(),
                new Memberjoin(),
                new Reaction(),
                new PrivateMessage(),
                new Channel(),
                    new READY()
             );
            logger.info("loaded all listeners");
            commands.put("builder", new CommandBuilder());
            commands.put("language", new CommandLanguage());
            commands.put("test", new CommandTest());
            commands.put("prefix", new CommandPrefix());
            commands.put("bug", new CommandBug());
            commands.put("profile", new CommandProfile());
            commands.put("givehashes", new CommandGiveHashes());
            commands.put("registeruser", new CommandRegisterUser());
            commands.put("registerserver", new CommandRegisterServer());
            commands.put("invite", new CommandInvite());
            commands.put("eval", new CommandEval());
            commands.put("ban", new CommandBan());
            commands.put("kick", new CommandKick());
            commands.put("github", new CommandGitHub());
            commands.put("stop", new CommandStop());
            commands.put("setlvl", new CommandSetLevel());
            commands.put("setxp", new CommandSetXP());
            commands.put("clyde", new CommandClyde());
            commands.put("ping", new CommandPing());
            commands.put("guildleave", new CommandGuildLeave());
            commands.put("stats", new CommandStats());
            commands.put("verification", new CommandVerification());
            commands.put("say", new CommandSay());
            commands.put("blacklist", new CommandBlacklist());
            commands.put("guilds", new CommandGuilds());
            commands.put("levelmessage", new CommandLevelMessage());
            commands.put("guild", new CommandGuild());
            commands.put("help", new CommandHelp());
            commands.put("info", new CommandInfo());
            commands.put("token", new CommandToken());
            commands.put("log", new CommandLog());
            commands.put("gameplay", new CommandGame());
            commands.put("dm", new CommandDM());
            commands.put("miner", new CommandMiner());
            commands.put("premium", new CommandPremium());
            commands.put("setpremium", new CommandSetPremium());
            commands.put("clear", new CommandClear());
            commands.put("s", new CommandShard());
            commands.put("shard", new CommandShard());
            commands.put("uptime", new CommandUptime());
            commands.put("role", new CommandRole());
            commands.put("botinfo", new CommandBotInfo());
            commands.put("privatechannel", new CommandPrivatechannel());
            commands.put("clan", new CommandClan());
            commands.put("upvoted", new CommandUpvoted());
            commands.put("setPremium", new CommandSetPremium());
            /*MUSIC*/
            commands.put("join", new CommandJoin());
            commands.put("leave", new CommandLeave());
            commands.put("play", new CommandPlay());
            args = args2;
            logger.info("loaded all commands");
            logger.info("Starting the Bot...");
            builder.setSessionController(sessionController);
            shardManager = builder.build();

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}