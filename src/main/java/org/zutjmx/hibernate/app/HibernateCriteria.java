package org.zutjmx.hibernate.app;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import org.zutjmx.hibernate.app.entity.Cliente;
import org.zutjmx.hibernate.app.util.JpaUtil;

import java.lang.reflect.Array;
import java.util.Arrays;
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

        System.out.println("::::: Usando where in :::::");
        criteriaQuery = criteriaBuilder.createQuery(Cliente.class);
        from = criteriaQuery.from(Cliente.class);
        ParameterExpression<List> parameterExpression = criteriaBuilder.parameter(List.class,"nombres");
        criteriaQuery
                .select(from)
                .where(from.get("nombre").in(parameterExpression));
        clientes = entityManager
                .createQuery(criteriaQuery)
                .setParameter("nombres",Arrays.asList("jesus","ana","ada","amber"))
                .getResultList();
        clientes.forEach(System.out::println);

        System.out.println("::::: Usando mayor que o mayor o igual :::::");
        criteriaQuery = criteriaBuilder.createQuery(Cliente.class);
        from = criteriaQuery.from(Cliente.class);
        criteriaQuery
                .select(from)
                .where(criteriaBuilder.ge(from.get("id"),5L));
        clientes = entityManager
                .createQuery(criteriaQuery)
                .getResultList();
        clientes.forEach(System.out::println);

        System.out.println("::::: Nombres con longitud mayor a 3 :::::");
        criteriaQuery = criteriaBuilder.createQuery(Cliente.class);
        from = criteriaQuery.from(Cliente.class);
        criteriaQuery
                .select(from)
                .where(criteriaBuilder.gt(criteriaBuilder.length(from.get("nombre")),3L));
        clientes = entityManager
                .createQuery(criteriaQuery)
                .getResultList();
        clientes.forEach(System.out::println);

        System.out.println("::::: Usando predicados 'and' y 'or' :::::");
        criteriaQuery = criteriaBuilder.createQuery(Cliente.class);
        from = criteriaQuery.from(Cliente.class);

        Predicate porNombre = criteriaBuilder.equal(from.get("nombre"),"amber");
        Predicate porFormaPago = criteriaBuilder.equal(from.get("formaPago"),"credito");
        Predicate porIdMayorOIgual = criteriaBuilder.ge(from.get("id"),1L);

        criteriaQuery
                .select(from)
                //.where(criteriaBuilder.and(porNombre,porFormaPago));
                //.where(criteriaBuilder.or(porNombre,porFormaPago));
                .where(criteriaBuilder.and(porIdMayorOIgual,criteriaBuilder.or(porNombre,porFormaPago)));
        clientes = entityManager
                .createQuery(criteriaQuery)
                .getResultList();
        clientes.forEach(System.out::println);

        System.out.println("::::: Usando order by :::::");
        criteriaQuery = criteriaBuilder.createQuery(Cliente.class);
        from = criteriaQuery.from(Cliente.class);
        criteriaQuery
                .select(from)
                .orderBy(criteriaBuilder.asc(from.get("nombre"))
                        ,criteriaBuilder.desc(from.get("apellido")));
        clientes = entityManager
                .createQuery(criteriaQuery)
                .getResultList();
        clientes.forEach(System.out::println);

        System.out.println("::::: Consulta por ID (Un sólo registro) :::::");
        criteriaQuery = criteriaBuilder.createQuery(Cliente.class);
        from = criteriaQuery.from(Cliente.class);
        ParameterExpression<Long> idParam = criteriaBuilder.parameter(Long.class,"id");
        criteriaQuery
                .select(from)
                .where(criteriaBuilder.equal(from.get("id"),idParam));
        Cliente cliente = entityManager
                .createQuery(criteriaQuery)
                .setParameter("id",10L)
                .getSingleResult();
        System.out.println(cliente);

        System.out.println("::::: Listado de nombres de empleados :::::");
        CriteriaQuery<String> queryString = criteriaBuilder.createQuery(String.class);
        from = queryString.from(Cliente.class);
        queryString
                .select(from.get("nombre"));
        List<String> nombres = entityManager
                .createQuery(queryString)
                .getResultList();
        nombres.forEach(System.out::println);

        System.out.println("::::: Listado de nombres de empleados (con Distinct) :::::");
        queryString = criteriaBuilder.createQuery(String.class);
        from = queryString.from(Cliente.class);
        queryString
                .select(criteriaBuilder.upper(from.get("nombre")))
                .distinct(true);
        nombres = entityManager
                .createQuery(queryString)
                .getResultList();
        nombres.forEach(System.out::println);

        entityManager.close();
    }
}
