package request;

import java.io.Serializable;

public class ChangePasswordRequest extends Request implements Serializable {
    private String oldPassword,newPassword;

    public ChangePasswordRequest(String oldPassword,String newPassword){
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }


    public String getOldPassword() {
        return oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }
}