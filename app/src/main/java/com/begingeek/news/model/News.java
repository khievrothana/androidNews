package com.begingeek.news.model;

/**
 * Created by itrot on 9/8/2016.
 */
public class News {
    private String Id;
    private String Title;
    private String FeatureImage;
    private String Content;
    private String CategoryId;
    private String UserId;
    private String Status;

    public News(String id, String title, String featureImage, String dateCreated) {
        Id = id;
        Title = title;
        FeatureImage = featureImage;
        DateCreated = dateCreated;
    }

    public News(String id, String title, String featureImage, String dateCreated, String content, String userId, String categoryId, String status, String resoure, String isFeature, String viewCount, String version, String lastUpdated) {

        Id = id;
        Title = title;
        FeatureImage = featureImage;
        DateCreated = dateCreated;
        Content = content;
        UserId = userId;
        CategoryId = categoryId;
        Status = status;
        Resoure = resoure;
        IsFeature = isFeature;
        ViewCount = viewCount;
        Version = version;
        LastUpdated = lastUpdated;
    }

    private String Resoure;
    private String IsFeature;
    private String ViewCount;
    private String Version;
    private String DateCreated;
    private String LastUpdated;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getFeatureImage() {
        return FeatureImage;
    }

    public void setFeatureImage(String featureImage) {
        FeatureImage = featureImage;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(String categoryId) {
        CategoryId = categoryId;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getResoure() {
        return Resoure;
    }

    public void setResoure(String resoure) {
        Resoure = resoure;
    }

    public String getIsFeature() {
        return IsFeature;
    }

    public void setIsFeature(String isFeature) {
        IsFeature = isFeature;
    }

    public String getViewCount() {
        return ViewCount;
    }

    public void setViewCount(String viewCount) {
        ViewCount = viewCount;
    }

    public String getVersion() {
        return Version;
    }

    public void setVersion(String version) {
        Version = version;
    }

    public String getDateCreated() {
        return DateCreated;
    }

    public void setDateCreated(String dateCreated) {
        DateCreated = dateCreated;
    }

    public String getLastUpdated() {
        return LastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        LastUpdated = lastUpdated;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }


}
