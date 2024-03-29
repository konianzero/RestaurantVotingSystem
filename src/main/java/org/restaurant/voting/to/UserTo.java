package org.restaurant.voting.to;

import org.restaurant.voting.HasIdAndEmail;
import org.restaurant.voting.util.validation.SafeHtml;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Objects;

public class UserTo extends BaseTo implements HasIdAndEmail, Serializable {
    private static final long serialVersionUID = 1L;

    @Size(min = 2, max = 100)
    @NotBlank
    @SafeHtml
    private String name;

    @Email
    @NotBlank
    @Size(min = 5, max = 50)
    @SafeHtml
    private String email;

    @NotBlank
    @Size(min = 5, max = 100)
    private String password;

    public UserTo() {
    }

    public UserTo(Integer id, String name, String email, String password) {
        super(id);
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserTo userTo = (UserTo) o;
        return Objects.equals(id, userTo.id) &&
                Objects.equals(name, userTo.name) &&
                Objects.equals(email, userTo.email) &&
                Objects.equals(password, userTo.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email, password);
    }

    @Override
    public String toString() {
        return "UserTo{" +
                "id=" + id +
                ", name='" + name +
                ", email='" + email +
                '}';
    }
}
