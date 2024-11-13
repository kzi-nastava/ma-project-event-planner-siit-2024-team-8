package com.example.eventplanner.domain;

public class AssetDTO {
    private String name;
    private AssetType type;

    private String imageURL;

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AssetType getType() {
        return type;
    }

    public void setType(AssetType type) {
        this.type = type;
    }

    public AssetDTO(String name, AssetType type,String imageURL) {
        this.name = name;
        this.type = type;
        this.imageURL=imageURL;
    }
}
