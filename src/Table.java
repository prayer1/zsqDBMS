public class Table {
    private static String userName;//用户姓名，切换或修改用户时修改
    private static String dbName;//数据库名，切换时修改



    public static void init(String userName, String dbName) {
        Table.userName = userName;
        Table.dbName = dbName;
    }
}
