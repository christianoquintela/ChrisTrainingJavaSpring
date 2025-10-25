package com.example.training.services;

import com.example.training.models.User;
import com.example.training.models.enums.ProfileENUM;
import com.example.training.repositories.UserRepository;
import com.example.training.services.exceptions.DataBindingViolationException;
import com.example.training.services.exceptions.ObjectNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserService {


    private final UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    /*
     * Busca todos os usuários, valida se a lista não está vazia e retorna
     * uma lista apenas com os IDs dos usuários.
     *
     * @return Uma lista de IDs de usuários. Se nenhum usuário for encontrado,
     * retorna uma lista vazia.
     * Gerado por IA
     */
    public List<Long> findAllUserIds() {
        // 1. Busca todos os usuários do banco de dados através do repositório
        List<User> users = userRepository.findAll();

        // 2. Valida se a lista de usuários não está nula ou vazia
        if (users.isEmpty()) return Collections.emptyList();

        // 3. Usa a Stream API para mapear a lista de User para uma lista de Long (IDs)
        return users.stream()
                .map(User::getId)
                .collect(Collectors.toList());
    }

    public User findById(Long id) {
        Optional<User> userOptional = this.userRepository.findById(id);
        return userOptional.orElseThrow(() -> new ObjectNotFoundException("Id não encontrado: " + id + ", Tipo: " + User.class.getName()));
    }

    //Tem melhor controle do que está acontecendo com a aplicação, ou faz tudo ou n faz nada.
    //Use em Create e Update
    @Transactional
    public User create(User obj) {
        obj.setId(null);
        obj.setPassword(this.bCryptPasswordEncoder.encode(obj.getPassword()));
        obj.setProfiles(Stream.of(ProfileENUM.USER.getCode()).collect(Collectors.toSet()));
        obj = this.userRepository.save(obj);
        return obj;
    }

    //Inserção no banco dados
    @Transactional
    public User update(User obj) {
        User newObj = findById(obj.getId());
        newObj.setPassword(this.bCryptPasswordEncoder.encode(obj.getPassword()));
        return this.userRepository.save(newObj);
    }

    public void delete(Long id) {
        findById(id);
        try {
            this.userRepository.deleteById(id);
        } catch (Exception e) {
            throw new DataBindingViolationException("Não é possível excluir pois há entidades relacionadas" + e.getMessage());
        }
    }
}
