package poppark.tech.mandi.com.bmob.statistics;

/**
 * Created by kingadmin on 2018/3/5.
 */

public class UserData  {

    private String userId;
    private String userSex;
    private String nickName;
    private String password;

    public String getUserId() {
        return userId;
    }

    public String getUserSex() {
        return userSex;
    }

    public String getNickName() {
        return nickName;
    }

    public String getPassword() {
        return password;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUserSex(String userSex) {
        this.userSex = userSex;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
