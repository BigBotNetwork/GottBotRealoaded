package GB.Handler;

import GB.GottBot;
import com.rethinkdb.RethinkDB;
import com.rethinkdb.model.MapObject;
import com.rethinkdb.net.Connection;
import com.rethinkdb.net.Cursor;
public class DB {
    private static RethinkDB r = RethinkDB.r;
    private static Connection conn;

    public Connection getConn() {
        return conn;
    }

    public RethinkDB getR() {
        return r;
    }

    public static void Connect() {
        conn = r.connection()
                .hostname(GottBot.getConfig().getDB().get("Hostname"))
                .db(GottBot.getConfig().getDB().get("Database"))
                .port(Integer.parseInt(GottBot.getConfig().getDB().get("Port")))
                .user(GottBot.getConfig().getDB().get("User"), GottBot.getConfig().getDB().get("Password"))
                .connect();
        System.out.println("CONNECTED! " + conn.isOpen());
    }

    public String getAll(String table, String field) {
        Cursor cursor = r.table(table).getField(field).run(conn);
        return cursor.next().toString();
    }

    public String get(String table, String where, String wherevalue, String field) {
        String jsonString = "";
        try {
            Cursor cursor = r.table(table).filter(row -> row.g(where).eq(wherevalue)).getField(field).run(conn);
            if (cursor.hasNext()) {
                jsonString = cursor.next().toString();
            } else jsonString = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonString;
    }

    public String insert(String table, MapObject Hashmap) {
        String result = r.table(table).insert(Hashmap).run(conn);
        return result;
    }

    public String insertUser(String id) {
        String result = r.table("user").insert(
                r.hashMap("userid", id)
        ).run(conn).toString();
        return result;
    }

    public String insertServer(String id) {
        String result = r.table("server").insert(
                r.hashMap("serverid", id)
                        .with("CommandPrefix", "gb.")
        ).run(conn).toString();
        return result;
    }

    public String insertServerUser(String userid, String serverid) {
        String defaultperms = get("server", "serverid", serverid, "defaultperms");
        if (defaultperms == null) {
            insertServer(serverid);
        }
        String result = r.table("serveruser").insert(
                r.hashMap("serveruserid", serverid+" "+userid)
                        .with("perms", defaultperms)
        ).run(conn).toString();
        return result;
    }

    public String update(MapObject hashmap) {
        r.table("info").update(hashmap).run(conn);
        return "";
    }

}