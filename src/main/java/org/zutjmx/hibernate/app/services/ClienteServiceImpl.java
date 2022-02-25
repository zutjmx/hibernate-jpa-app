package org.zutjmx.hibernate.app.services;

import jakarta.persistence.EntityManager;
import org.zutjmx.hibernate.app.entity.Cliente;
import org.zutjmx.hibernate.app.repositories.ClienteRepository;
import org.zutjmx.hibernate.app.repositories.CrudRepository;

import java.util.List;
import java.util.Optional;

public class ClienteServiceImpl implements ClienteService {

    private EntityManager entityManager;
    private CrudRepository<Cliente> clienteCrudRepository;

    public ClienteServiceImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.clienteCrudRepository = new ClienteRepository(entityManager);
    }

    @Override
    public List<Cliente> listar() {
        return clienteCrudRepository.listar();
    }

    @Override
    public Optional<Cliente> porId(Long id) {
        return Optional.ofNullable(clienteCrudRepository.porId(id));
    }

    @Override
    public void guardar(Cliente cliente) {
        try {
            entityManager.getTransaction().begin();
            clienteCrudRepository.guardar(cliente);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            e.printStackTrace();
        }
    }

    @Override
    public void eliminar(Long id) {
        try {
            entityManager.getTransaction().begin();
            clienteCrudRepository.eliminar(id);
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            entityManager.getTransaction().rollback();
            e.printStackTrace();
        }
    }
}
