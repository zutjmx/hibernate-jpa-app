package org.zutjmx.hibernate.app;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.zutjmx.hibernate.app.entity.Cliente;
import org.zutjmx.hibernate.app.util.JpaUtil;

import java.util.List;
import java.util.Scanner;

public class HibernatePorId {
    private static final String selectJpa = "select c from Cliente c";
    private static final String clausulaWhere = " where c.id = ?1";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        EntityManager entityManager = JpaUtil.getEntityManager();

        System.out.println("Indique el ID:");
        Long id = scanner.nextLong();

        /*Query query = entityManager.createQuery(selectJpa + clausulaWhere,Cliente.class);
        query.setParameter(1,id);
        Cliente cliente = (Cliente) query.getSingleResult();*/

        Cliente cliente = entityManager.find(Cliente.class,id);

        System.out.println(cliente);
        entityManager.close();
    }

}
