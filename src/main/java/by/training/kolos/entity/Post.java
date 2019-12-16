package by.training.kolos.entity;

public class Post extends Entity {
    private Long id;
    private String name;
    private Long publishDate;
    private Long userId;
    private WorldPart worldPart;
    private Long mainPhotoId;

    private Post() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Long getPublishDate() {
        return publishDate;
    }

    public Long getUserId() {
        return userId;
    }

    public WorldPart getWorldPart() {
        return worldPart;
    }

    public Long getMainPhotoId() {
        return mainPhotoId;
    }

    public static Builder builder() {
        return new Post().new Builder();
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPublishDate(Long publishDate) {
        this.publishDate = publishDate;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setWorldPart(WorldPart worldPart) {
        this.worldPart = worldPart;
    }

    public void setMainPhotoId(Long mainPhotoId) {
        this.mainPhotoId = mainPhotoId;
    }

    public class Builder {
        private Builder() {
        }


        public Builder id(Long id) {
            Post.this.id = id;
            return this;
        }

        public Builder name(String name) {
            Post.this.name = name;
            return this;
        }

        public Builder publishDate(Long publishDate) {
            Post.this.publishDate = publishDate;
            return this;
        }

        public Builder userId(Long userId) {
            Post.this.userId = userId;
            return this;
        }

        public Builder mainPhotoId(Long mainPhotoId) {
            Post.this.mainPhotoId = mainPhotoId;
            return this;
        }

        public Builder worldPart(WorldPart worldPart) {
            Post.this.worldPart = worldPart;
            return this;
        }

        public Post build() {
            return Post.this;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Post post = (Post) o;

        if (id != null ? !id.equals(post.id) : post.id != null) return false;
        if (name != null ? !name.equals(post.name) : post.name != null) return false;
        if (publishDate != null ? !publishDate.equals(post.publishDate) : post.publishDate != null) return false;
        if (userId != null ? !userId.equals(post.userId) : post.userId != null) return false;
        if (worldPart != post.worldPart) return false;
        return mainPhotoId != null ? mainPhotoId.equals(post.mainPhotoId) : post.mainPhotoId == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (publishDate != null ? publishDate.hashCode() : 0);
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (worldPart != null ? worldPart.hashCode() : 0);
        result = 31 * result + (mainPhotoId != null ? mainPhotoId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", name=" + name +
                ", publishDate=" + publishDate +
                ", userId=" + userId +
                ", worldPart=" + worldPart +
                ", mainPhotoId=" + mainPhotoId +
                '}';
    }
}
