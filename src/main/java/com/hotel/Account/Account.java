package com.hotel.Account;

/*Account Class:
 *	Holds getter, setter, and default constructor
 * */
public class Account {

    private String email_id;
    private String user_name;
    private String password;
    private String type;
  
    public void setEmailID(String email_id) {
        this.email_id = email_id;
    }
    public String getEmailID() {
        return email_id;
    }

    public String getUserName() {
        return user_name;
    }

    public void setUserName(String user_name) {
        this.user_name = user_name;
    }
    
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public Account() {}
}
