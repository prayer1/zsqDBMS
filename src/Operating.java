import java.io.File;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Operating {
    private static final Pattern PATTERN_INSERT = Pattern.compile("insert\\s+into\\s+(\\w+)(\\(((\\w+,?)+)\\))?\\s+\\w+\\((([^\\)]+,?)+)\\);?");
    private static final Pattern PATTERN_CREATE_TABLE = Pattern.compile("create\\stable\\s(\\w+)\\s?\\(((?:\\s?\\w+\\s\\w+,?)+)\\)\\s?;");
    private static final Pattern PATTERN_ALTER_TABLE_ADD = Pattern.compile("alter\\stable\\s(\\w+)\\sadd\\s(\\w+\\s\\w+)\\s?;");
    private static final Pattern PATTERN_DELETE = Pattern.compile("delete\\sfrom\\s(\\w+)(?:\\swhere\\s(\\w+\\s?[<=>]\\s?[^\\s\\;]+(?:\\sand\\s(?:\\w+)\\s?(?:[<=>])\\s?(?:[^\\s\\;]+))*))?\\s?;");
    private static final Pattern PATTERN_UPDATE = Pattern.compile("update\\s(\\w+)\\sset\\s(\\w+\\s?=\\s?[^,\\s]+(?:\\s?,\\s?\\w+\\s?=\\s?[^,\\s]+)*)(?:\\swhere\\s(\\w+\\s?[<=>]\\s?[^\\s\\;]+(?:\\sand\\s(?:\\w+)\\s?(?:[<=>])\\s?(?:[^\\s\\;]+))*))?\\s?;");
    private static final Pattern PATTERN_DROP_TABLE = Pattern.compile("drop\\stable\\s(\\w+);");
    private static final Pattern PATTERN_SELECT = Pattern.compile("select\\s(\\*|(?:(?:\\w+(?:\\.\\w+)?)+(?:\\s?,\\s?\\w+(?:\\.\\w+)?)*))\\sfrom\\s(\\w+(?:\\s?,\\s?\\w+)*)(?:\\swhere\\s([^\\;]+\\s?;))?");
    private static final Pattern PATTERN_DELETE_INDEX = Pattern.compile("delete\\sindex\\s(\\w+)\\s?;");
    private static final Pattern PATTERN_GRANT_ADMIN = Pattern.compile("grant\\sadmin\\sto\\s([^;\\s]+)\\s?;");
    private static final Pattern PATTERN_REVOKE_ADMIN = Pattern.compile("revoke\\sadmin\\sfrom\\s([^;\\s]+)\\s?;");

    public  void dbms() {
        User user = User.getUser("user1", "abc");
        if (null == user) {
            System.out.println("已推出dbms");
            return;
        } else {
            System.out.println(user.getName() + "登陆成功");
        }
        //默认进入user1用户文件夹
        File userFolder  = new File("dir", user.getName());
        //默认进入user1的默认数据库db1
        File dbFolder = new File(userFolder, "db1");

        Table.init(user.getName(), dbFolder.getName());

        Scanner sc = new Scanner(System.in);
        String cmd;
        while (!"exit".equals(cmd = sc.nextLine())) {
            Matcher matcherGrantAdmin = PATTERN_GRANT_ADMIN.matcher(cmd);
            Matcher matcherRevokeAdmin = PATTERN_REVOKE_ADMIN.matcher(cmd);
            Matcher matcherInsert = PATTERN_INSERT.matcher(cmd);
            Matcher matcherCreateTable = PATTERN_CREATE_TABLE.matcher(cmd);
            Matcher matcherAlterTable_add = PATTERN_ALTER_TABLE_ADD.matcher(cmd);
            Matcher matcherDelete = PATTERN_DELETE.matcher(cmd);
            Matcher matcherUpdate = PATTERN_UPDATE.matcher(cmd);
            Matcher matcherDropTable = PATTERN_DROP_TABLE.matcher(cmd);
            Matcher matcherSelect = PATTERN_SELECT.matcher(cmd);
            Matcher matcherDeleteIndex = PATTERN_DELETE_INDEX.matcher(cmd);

            while (matcherGrantAdmin.find()) {
                User grantUser = User.getUser(matcherRevokeAdmin.group(1));
                if (grantUser == null) {
                    System.out.println("授权失败");
                } else if (user.getName().equals(grantUser.getName())) {
                    //如果是当前操作的用户，就直接更改当前用户权限
                    user.grant(User.ADMIN);
                    System.out.println("用户:" + user.getName() + "授权成功");
                } else {
                    grantUser.grant(User.ADMIN);
                    System.out.println("用户:" + grantUser.getName() + "授权成功");
                }
            }

            while (matcherRevokeAdmin.find()) {
                User grantUser = User.getUser(matcherRevokeAdmin.group(1));
                if (null == grantUser) {
                    System.out.println("取消授权失败");
                } else if (user.getName().equals(grantUser.getName())) {
                    user.grant(User.READ_ONLY);
                    System.out.println("用户:" + user.getName() + "已取消授权");
                } else {
                    grantUser.grant(User.READ_ONLY);
                    System.out.println("用户:" + grantUser.getName() + "已取消授权");
                }
            }

            while (matcherAlterTable_add.find()) {
                if (user.getLevel() != User.ADMIN) {
                    System.out.println("用户:" + user.getName() + "权限不够，无法完成此操作！");
                    break;
                }
                alterTableAdd(matcherAlterTable_add);
            }
        }
    }
    private void alterTableAdd(Matcher matcherAlterTable_add) {
        String tableName = matcherAlterTable_add.group(1);
    }
}
