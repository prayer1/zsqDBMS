import java.io.*;

public class User implements Serializable {
    private String name;
    private String password;
    private int level;//1代表只能使用select 2代表能使用全部权限
    public final static int READ_ONLY = 1;//只读权限
    public final static int ADMIN = 2;//所有权限

    private User() {
        name = null;
        password = null;
        level = 1;//1代表只能使用select 2代表能使用全部权限
    }
    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }

    /**
     * 从用户信息文件中读取用户对象并验证密码
     * @param userName
     * @param password
     * @return
     */
    public static User getUser(String userName, String password) {
        User user = null;
        File file = new File("dir/" + userName, "user.info");
        if (!file.exists()) {
            System.out.println("用户不存在");
            return null;
        }
        try (
                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);
                ) {
                user = (User)ois.readObject();
                if (user == null) {
                    System.out.println("此用户不存在");
                } else if (!password.equals(user.password)) {
                    user = null;//如果密码不正确，返回null
                    System.out.println("密码错误");
                }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return user;
    }

    /**
     * 从用户信息中读取用户对象
     * @param userName
     * @return
     */
    public static User getUser(String userName) {
        User user = null;
        File file = new File("dir:/"+userName, "user.info");
        if (!file.exists()) {
            System.out.println("用户不存在");
            return null;
        }
        try (
                FileInputStream fis = new FileInputStream(file);
                ObjectInputStream ois = new ObjectInputStream(fis);
                ) {
                user = (User)ois.readObject();
                if (user == null) {
                    System.out.println("此用户不存在");
                }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return user;
    }

    /**
     * 对用户授权并写入文件
     * @param level
     */
    public void grant(int level) {
        setLevel(level);
        writerUser(this);
    }

    /**
     * 将修改后的user覆盖原文件 更新文件信息
     * @param user
     */
    private static void writerUser(User user) {
        File file = new File("dir:" + user.getName(), "user.info");
        try (
                FileOutputStream fos = new FileOutputStream(file);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                ) {
            oos.writeObject(user);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public static int getReadOnly() {
        return READ_ONLY;
    }

    public static int getADMIN() {
        return ADMIN;
    }
}
