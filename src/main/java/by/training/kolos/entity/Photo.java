package by.training.kolos.entity;

import java.util.List;

public class Photo extends Entity {
    private Long id;
    private String description;
    private Long postId;
    private boolean isMainPhoto;
    private String url;
    private Long likesNumber;
    private List<String> tags;

    private Photo() {
    }

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public Long getPostId() {
        return postId;
    }

    public boolean isMainPhoto() {
        return isMainPhoto;
    }

    public String getUrl() {
        return url;
    }

    public Long getLikesNumber() {
        return likesNumber;
    }

    public List<String> getTags() {
        return tags;
    }

    public static Builder builder() {
        return new Photo().new Builder();
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public void setIsMainPhoto(boolean mainPhoto) {
        isMainPhoto = mainPhoto;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setLikesNumber(Long likesNumber) {
        this.likesNumber = likesNumber;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public class Builder {

        private Builder() {
        }

        public Builder id(Long id) {
            Photo.this.id = id;
            return this;
        }

        public Builder description(String description) {
            Photo.this.description = description;
            return this;
        }

        public Builder postId(Long postId) {
            Photo.this.postId = postId;
            return this;
        }

        public Builder isMainPhoto(Boolean isMainPhoto) {
            Photo.this.isMainPhoto = isMainPhoto;
            return this;
        }

        public Builder url(String url) {
            Photo.this.url = url;
            return this;
        }

        public Builder tags(Long likesNumber) {
            Photo.this.likesNumber = likesNumber;
            return this;
        }

        public Builder tags(List<String> tags) {
            Photo.this.tags = tags;
            return this;
        }

        public Photo build() {
            return Photo.this;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Photo photo = (Photo) o;

        if (isMainPhoto != photo.isMainPhoto) return false;
        if (id != null ? !id.equals(photo.id) : photo.id != null) return false;
        if (description != null ? !description.equals(photo.description) : photo.description != null) return false;
        if (postId != null ? !postId.equals(photo.postId) : photo.postId != null) return false;
        if (url != null ? !url.equals(photo.url) : photo.url != null) return false;
        if (likesNumber != null ? !likesNumber.equals(photo.likesNumber) : photo.likesNumber != null)
            return false;
        return tags != null ? tags.equals(photo.tags) : photo.tags == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (postId != null ? postId.hashCode() : 0);
        result = 31 * result + (isMainPhoto ? 1 : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (likesNumber != null ? likesNumber.hashCode() : 0);
        result = 31 * result + (tags != null ? tags.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Photo{" +
                "id=" + id +
                ", description=" + description +
                ", postId=" + postId +
                ", isMainPhoto=" + isMainPhoto +
                ", url=" + url +
                ", amountOfLikes=" + likesNumber +
                ", tags=" + tags +
                '}';
    }
}
