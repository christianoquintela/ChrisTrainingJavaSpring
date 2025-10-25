package com.example.training.models;

import com.example.training.models.enums.ProfileENUM;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = User.TABLE_NAME)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class User {
    public static final String TABLE_NAME = "user";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true)
    private Long id;

    @Column(name = "username", nullable = false, length = 100, unique = true)
    @NotNull(groups = CreateUser.class)
    @NotEmpty(groups = CreateUser.class)
    @Size(groups = CreateUser.class, min = 2, max = 100)
    private String username;

    //    @JsonProperty garante que a senha seja só de escrita, e não poderá ser lida, não será enviado json com a senha para o front.
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "password", nullable = false, length = 60)
    @NotNull(groups = {CreateUser.class, UpdateUser.class})
    @NotEmpty(groups = {CreateUser.class, UpdateUser.class})
    @Size(groups = {CreateUser.class, UpdateUser.class}, min = 8, max = 60)
    private String password;

    //    Para facilitar o entendimento, lê-se one(um) to(para) Many(muitos) -> um User pode ter task(várias tarefas).
    @OneToMany(mappedBy = "user")
    private List<Task> tasks = new ArrayList<>();
    @ElementCollection(fetch = FetchType.EAGER)//Sempre que busca o usuário no DB busca tbm seus perfis
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    //    Garante que ñ retorna os perfis do usuário nas chamdas restfull
    @CollectionTable(name = "user_profile")
    @Column(name = "profile", nullable = false)
    private Set<Integer> profiles = new HashSet<>();

    //    Aqui o JsonIgnora está sendo usado para ignorar todas as tasks, e pegar só o usuário.(não entendi, vamos ver na prática)
    @JsonIgnore
    public List<Task> getTasks() {
        return tasks;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) return false;
        User user = (User) obj;
        return Objects.equals(id, user.id) && Objects.equals(username, user.username) && Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, password);
    }

    public Set<ProfileENUM> getProfiles() {
        return this.profiles.stream().map(enums -> ProfileENUM.toEnum(enums)).collect(Collectors.toSet());
    }

    public void addProfile(ProfileENUM profileENUM) {
        this.profiles.add(profileENUM.getCode());
    }

    public interface CreateUser {
    }

    public interface UpdateUser {
    }
}
