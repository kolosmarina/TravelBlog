package by.training.kolos.entity;

public class Comment extends Entity {
    private Long id;
    private Long publishDate;
    private String value;
    private Long photoId;
    private Long userId;
    private String userNickname;

    private Comment() {
    }

    public Long getId() {
        return id;
    }

    public Long getPublishDate() {
        return publishDate;
    }

    public String getValue() {
        return value;
    }

    public Long getPhotoId() {
        return photoId;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public static Builder builder() {
        return new Comment().new Builder();
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPublishDate(Long publishDate) {
        this.publishDate = publishDate;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setPhotoId(Long photoId) {
        this.photoId = photoId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }

    public class Builder {
        private Builder() {
        }

        public Builder id(Long id) {
            Comment.this.id = id;
            return this;
        }

        public Builder publishDate(Long publishDate) {
            Comment.this.publishDate = publishDate;
            return this;
        }

        public Builder value(String value) {
            Comment.this.value = value;
            return this;
        }

        public Builder photoId(Long photoId) {
            Comment.this.photoId = photoId;
            return this;
        }

        public Builder userId(Long userId) {
            Comment.this.userId = userId;
            return this;
        }

        public Builder userNickname(String userNickname) {
            Comment.this.userNickname = userNickname;
            return this;
        }

        public Comment build() {
            return Comment.this;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Comment comment = (Comment) o;

        if (id != null ? !id.equals(comment.id) : comment.id != null) return false;
        if (publishDate != null ? !publishDate.equals(comment.publishDate) : comment.publishDate != null) return false;
        if (value != null ? !value.equals(comment.value) : comment.value != null) return false;
        if (photoId != null ? !photoId.equals(comment.photoId) : comment.photoId != null) return false;
        if (userId != null ? !userId.equals(comment.userId) : comment.userId != null) return false;
        return userNickname != null ? userNickname.equals(comment.userNickname) : comment.userNickname == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (publishDate != null ? publishDate.hashCode() : 0);
        result = 31 * result + (value != null ? value.hashCode() : 0);
        result = 31 * result + (photoId != null ? photoId.hashCode() : 0);
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (userNickname != null ? userNickname.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", publishDate=" + publishDate +
                ", value=" + value +
                ", photoId=" + photoId +
                ", userId=" + userId +
                ", userNickname=" + userNickname +
                '}';
    }
}
