package lk.zeamac.app.powermartadmin.Entity;

public class UserEntity {

    private String id;
    private String name;
    private String email;
    private String type;
    private String mobile;

    public UserEntity() {
    }

    public UserEntity(String id, String name, String email, String type, String mobile) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.type = type;
        this.mobile = mobile;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
