package org.zutjmx.hibernate.app;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.ParameterExpression;
import jakarta.persistence.criteria.Root;
import org.zutjmx.hibernate.app.entity.Cliente;
import org.zutjmx.hibernate.app.util.JpaUtil;

import java.util.List;

public class HibernateCriteria {
    public static void main(String[] args) {
        EntityManager entityManager = JpaUtil.getEntityManager();

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Cliente> criteriaQuery = criteriaBuilder.createQuery(Cliente.class);
        Root<Cliente> from = criteriaQuery.from(Cliente.class);
        criteriaQuery.select(from);

        List<Cliente> clientes = entityManager
                .createQuery(criteriaQuery)
                .getResultList();

        System.out.println("::::: Listado de todos los clientes :::::");
        clientes.forEach(System.out::println);

        System.out.println("::::: Usando where equal :::::");
        criteriaQuery = criteriaBuilder.createQuery(Cliente.class);
        from = criteriaQuery.from(Cliente.class);
        ParameterExpression<String> nombreParametro = criteriaBuilder.parameter(String.class,"nombre");
        criteriaQuery
                .select(from)
                .where(criteriaBuilder.equal(from.get("nombre"),nombreParametro));
        clientes = entityManager
                .createQuery(criteriaQuery)
                .setParameter("nombre","ana")
                .getResultList();
        clientes.forEach(System.out::println);

        System.out.println("::::: Usando where like :::::");
        criteriaQuery = criteriaBuilder.createQuery(Cliente.class);
        from = criteriaQuery.from(Cliente.class);
        ParameterExpression<String> nombreParametroLike = criteriaBuilder.parameter(String.class,"nombre");
        criteriaQuery
                .select(from)
                .where(criteriaBuilder
                        .like(criteriaBuilder.upper(from.get("nombre")), criteriaBuilder.upper(nombreParametroLike)));
        clientes = entityManager
                .createQuery(criteriaQuery)
                .setParameter("nombre","%am%")
                .getResultList();
        clientes.forEach(System.out::println);

        System.out.println("::::: Usando where between :::::");
        criteriaQuery = criteriaBuilder.createQuery(Cliente.class);
        from = criteriaQuery.from(Cliente.class);
        criteriaQuery
                .select(from)
                .where(criteriaBuilder.between(from.get("id"),6L,25L));
        clientes = entityManager
                .createQuery(criteriaQuery)
                .getResultList();
        clientes.forEach(System.out::println);

        entityManager.close();
    }
}
