package com.app.server.models;

import java.util.Date;

public class Photo {
    String id=null;
    String url;
    Date createDate;
    Date editDate;
    String userId;

    public String getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public Date getEditDate() {
        return editDate;
    }

    public String getUserId() {
        return userId;
    }

    public Photo(String url, Date createDate, Date editDate, String userId) {
        this.url = url;
        this.createDate = createDate;
        this.editDate = editDate;
        this.userId = userId;
    }

    public void setId(String id) {
        this.id = id;
    }
}
