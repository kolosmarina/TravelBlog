package by.training.kolos.entity;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class User extends Entity {
    private Long id;
    private UserRole userRole;
    private String email;
    private String password;
    private String nickname;
    private Long registrationDate;
    private boolean isActive;

    private User() {
    }

    public Long getId() {
        return id;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getNickname() {
        return nickname;
    }

    public Long getRegistrationDate() {
        return registrationDate;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public static Builder builder() {
        return new User().new Builder();
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }


    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setRegistrationDate(Long registrationDate) {
        this.registrationDate = registrationDate;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public class Builder {
        private Builder() {
        }

        public Builder id(Long id) {
            User.this.id = id;
            return this;
        }

        public Builder role(UserRole userRole) {
            User.this.userRole = userRole;
            return this;
        }


        public Builder email(String email) {
            User.this.email = email;
            return this;
        }

        public Builder password(String password) {
            User.this.password = password;
            return this;
        }

        public Builder nickname(String nickname) {
            User.this.nickname = nickname;
            return this;
        }

        public Builder registrationDate(Long registrationDate) {
            User.this.registrationDate = registrationDate;
            return this;
        }

        public Builder isActive(boolean isActive) {
            User.this.isActive = isActive;
            return this;
        }

        public User build() {
            return User.this;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (isActive != user.isActive) return false;
        if (id != null ? !id.equals(user.id) : user.id != null) return false;
        if (userRole != user.userRole) return false;
        if (email != null ? !email.equals(user.email) : user.email != null) return false;
        if (password != null ? !password.equals(user.password) : user.password != null) return false;
        if (nickname != null ? !nickname.equals(user.nickname) : user.nickname != null) return false;
        return registrationDate != null ? registrationDate.equals(user.registrationDate) : user.registrationDate == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (userRole != null ? userRole.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (nickname != null ? nickname.hashCode() : 0);
        result = 31 * result + (registrationDate != null ? registrationDate.hashCode() : 0);
        result = 31 * result + (isActive ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userRole=" + userRole +
                ", email=" + email +
                ", password=" + password +
                ", nickname=" + nickname +
                ", registrationDate=" + registrationDate +
                ", isActive=" + isActive +
                '}';
    }

    //использование временной зоны по умолчанию
    //long timestamp = Instant.now().toEpochMilli();
    public static LocalDateTime localDateTimeFirst(long milliseconds) {
        Instant instant = Instant.ofEpochMilli(milliseconds);
        return instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
        //return Instant.ofEpochMilli(milliseconds).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    // использование метода по получению даты в jsp?
    //получение даты в зависимости от временной зоны, разобраться с передачей зоны!!!
    public static LocalDateTime toLocalDateTimeSecond(long milliseconds, int timeZone) {
        return Instant.ofEpochMilli(milliseconds).atZone(ZoneId.of("UTC")).toLocalDateTime();
        /*return LocalDateTime.ofInstant(Instant.ofEpochMilli(milliseconds),
                ZoneId.ofOffset("", ZoneOffset.ofHours(timeZone)));*/
    }
}
