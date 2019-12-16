package by.training.kolos.entity;

public class Tag extends Entity {
    private Long id;
    private String value;

    private Tag() {
    }

    public Long getId() {
        return id;
    }

    public String getValue() {
        return value;
    }

    public static Builder builder() {
        return new Tag().new Builder();
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public class Builder {
        private Builder() {
        }

        public Builder id(Long id) {
            Tag.this.id = id;
            return this;
        }

        public Builder value(String value) {
            Tag.this.value = value;
            return this;
        }

        public Tag build() {
            return Tag.this;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tag tag = (Tag) o;

        if (id != null ? !id.equals(tag.id) : tag.id != null) return false;
        return value != null ? value.equals(tag.value) : tag.value == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Tag{" +
                "id=" + id +
                ", value=" + value +
                '}';
    }
}
