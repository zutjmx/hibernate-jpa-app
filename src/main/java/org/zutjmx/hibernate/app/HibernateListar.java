package org.zutjmx.hibernate.app;

import jakarta.persistence.EntityManager;
import org.zutjmx.hibernate.app.entity.Cliente;
import org.zutjmx.hibernate.app.util.JpaUtil;

import java.util.List;

public class HibernateListar {

    private static final String selectJpa = "select c from Cliente c";

    public static void main(String[] args) {
        EntityManager entityManager = JpaUtil.getEntityManager();
        List<Cliente> clientes = entityManager.createQuery(selectJpa,Cliente.class).getResultList();
        clientes.forEach(
                cliente -> System.out.println(cliente.toString())
                //System.out::println
        );
        entityManager.close();
    }
}
