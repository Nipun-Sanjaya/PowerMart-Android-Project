package lk.zeamac.app.powermartadmin.Entity;

public class CategoryEntity {

    private String id;
    private String name;
    private String imagePath;

    public CategoryEntity() {
    }

    public CategoryEntity(String id, String name, String imagePath) {
        this.id = id;
        this.name = name;
        this.imagePath = imagePath;
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

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
