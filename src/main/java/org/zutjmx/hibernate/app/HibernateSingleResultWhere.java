package org.zutjmx.hibernate.app;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.zutjmx.hibernate.app.entity.Cliente;
import org.zutjmx.hibernate.app.util.JpaUtil;

import java.util.Scanner;

public class HibernateSingleResultWhere {
    private static final String selectJpa = "select c from Cliente c";
    private static final String clausulaWhere = " where c.formaPago = ?1";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        EntityManager entityManager = JpaUtil.getEntityManager();
        Query query = entityManager.createQuery(selectJpa + clausulaWhere,Cliente.class);
        System.out.println("Indique la forma de pago:");
        String formaDePago = scanner.next();
        query.setParameter(1,formaDePago);
        query.setMaxResults(1);
        /*List<Cliente> clientes = (List<Cliente>) query.getResultList();
        clientes.forEach(System.out::println);*/
        Cliente cliente = (Cliente) query.getSingleResult();
        System.out.println(cliente);
        entityManager.close();
    }

}
