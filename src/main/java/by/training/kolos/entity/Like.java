package by.training.kolos.entity;

public class Like extends Entity {
    private Long photoId;
    private Long userId;

    private Like() {
    }

    public Long getPhotoId() {
        return photoId;
    }

    public Long getUserId() {
        return userId;
    }

    public static Builder builder() {
        return new Like().new Builder();
    }

    public void setPhotoId(Long photoId) {
        this.photoId = photoId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public class Builder {
        private Builder() {
        }

        public Builder photoId(Long photoId) {
            Like.this.photoId = photoId;
            return this;
        }

        public Builder userId(Long userId) {
            Like.this.userId = userId;
            return this;
        }

        public Like build() {
            return Like.this;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Like like = (Like) o;

        if (photoId != null ? !photoId.equals(like.photoId) : like.photoId != null) return false;
        return userId != null ? userId.equals(like.userId) : like.userId == null;
    }

    @Override
    public int hashCode() {
        int result = photoId != null ? photoId.hashCode() : 0;
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Like{" +
                "photoId=" + photoId +
                ", userId=" + userId +
                '}';
    }
}
