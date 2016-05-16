package es.deusto.model.services.database.dao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table "RSS".
 */
public class RSS {

    private Long id;
    private String name;
    private String imageUri;
    private String url;

    public RSS() {
    }

    public RSS(Long id) {
        this.id = id;
    }

    public RSS(Long id, String name, String imageUri, String url) {
        this.id = id;
        this.name = name;
        this.imageUri = imageUri;
        this.url = url;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
