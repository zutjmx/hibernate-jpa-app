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
        System.out.println(":: Listado de Clientes ::");
        Long cuantos = clientes.stream().count();
        clientes.forEach(
                cliente -> System.out.println(cliente.toString())
                //System.out::println
        );
        System.out.println("Total de registros: " + cuantos);
        System.out.println(":: Fin de Listado ::");
        entityManager.close();
    }
}
