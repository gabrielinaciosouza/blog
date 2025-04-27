package com.gabriel.blog.fixtures;

import com.gabriel.blog.domain.entities.User;
import com.gabriel.blog.domain.valueobjects.Email;
import com.gabriel.blog.domain.valueobjects.Id;
import com.gabriel.blog.domain.valueobjects.Image;
import com.gabriel.blog.domain.valueobjects.Name;

public class UserFixture {
    public static final User DEFAULT_USER = UserBuilder.anUser().build();

    public static class UserBuilder {
        private Id id = new Id("default-id");
        private Email email = new Email("default@example.com");
        private User.Role role = User.Role.USER;
        private Name name = new Name("Default User");
        private Image image = new Image("http://default.img");

        public static UserBuilder anUser() {
            return new UserBuilder();
        }

        public UserBuilder withId(Id id) {
            this.id = id;
            return this;
        }

        public UserBuilder withEmail(Email email) {
            this.email = email;
            return this;
        }

        public UserBuilder withRole(User.Role role) {
            this.role = role;
            return this;
        }

        public UserBuilder withName(Name name) {
            this.name = name;
            return this;
        }

        public UserBuilder withImage(Image image) {
            this.image = image;
            return this;
        }

        public User build() {
            return new User(id, email, role, name, image);
        }
    }
}
