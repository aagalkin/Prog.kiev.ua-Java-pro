package com.service;

import com.Tools;
import com.entity.Client;
import com.entity.Container;
import com.entity.Role;
import com.repo.ClientRepo;
import com.repo.ContainerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class ClientService {

    @Autowired
    private ContainerRepo containerRepo;

    @Autowired
    private ClientRepo clientRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public boolean addNewClient(String name, String surname, String email, String password, Integer phone, String city){
        Client client = clientRepo.findByEmail(email);
        if (client != null){
            return false;
        }
        client = new Client(name, surname, email, password, phone, city, Role.USER, new ArrayList<>());
        clientRepo.save(client);


        Container container = new Container();
        container.setClient(client);
        container.setProducts(new HashMap<>());
        container.setCount(0);
        client.setContainer(container);
        containerRepo.save(container);
        clientRepo.save(client);
        return true;
    }

    public List<Client> findAll(){
        return clientRepo.findAll();
    }

    @Transactional
    public Client getClientByEmail(String email){
        return clientRepo.findByEmail(email);
    }

    @Transactional
    public List<Client> findAllWhereContains(String name){
        if (name.isEmpty()) return clientRepo.findAll();
        name = name.toLowerCase();
        List<Client> clients = new ArrayList<>();
        List<Client> list = clientRepo.findAll();
        for (Client client : list){
            if (client.getName().toLowerCase().contains(name) || client.getSurname().toLowerCase().contains(name) ||
                    client.getEmail().toLowerCase().contains(name) || client.getCity().toLowerCase().contains(name)){
                clients.add(client);
            }else if (Tools.isDigit(name)){
                Long digit = Long.parseLong(name);
                if (client.getPhone().equals(digit) || client.getId().equals(digit)){
                    clients.add(client);
                }
            }
        }
        return clients;
    }

    @Transactional
    public Client getOne(Long id){
        return clientRepo.getOne(id);
    }

    public boolean update(Long id, String name, String surname, Integer phone, String city, String role){
        if (id == null || id < 1) return false;
        Client client = clientRepo.getOne(id);
        if (client == null) return false;
        if (name != null && !name.isEmpty()) client.setName(name);
        if (surname != null &&  !surname.isEmpty()) client.setSurname(surname);
        if (phone != null && phone < 1) client.setPhone(phone);
        if (city != null &&  !city.isEmpty()) client.setCity(city);
        if (role != null && !role.isEmpty()){
            switch (role){
                case "ADMIN":
                    client.setRole(Role.ADMIN);
                    break;
                case "USER":
                    client.setRole(Role.USER);
                    break;
            }
        }
        clientRepo.save(client);
        return true;
    }

    @Transactional
    public boolean editPassword(Long id, String oldPassword, String newPassword){
        if (id == null || oldPassword.isEmpty() || newPassword.isEmpty()) return false;
        Client client = clientRepo.getOne(id);
        oldPassword = passwordEncoder.encode(oldPassword);
        if (!oldPassword.equals(client.getPassword())) return false;
        client.setPassword(passwordEncoder.encode(newPassword));
        clientRepo.save(client);
        return true;
    }

    @Transactional
    public void save(Client client) {
        clientRepo.save(client);
    }
}
