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

        System.out.println("::::: Uso de concat :::::");
        queryString = criteriaBuilder.createQuery(String.class);
        from = queryString.from(Cliente.class);
        queryString
                .select(criteriaBuilder.concat(
                        criteriaBuilder.concat(from.get("nombre")," "),
                        from.get("apellido")
                ));
        nombres = entityManager
                .createQuery(queryString)
                .getResultList();
        nombres.forEach(System.out::println);

        System.out.println("::::: Uso de upper :::::");
        queryString = criteriaBuilder.createQuery(String.class);
        from = queryString.from(Cliente.class);
        queryString
                .select(criteriaBuilder.upper(
                            criteriaBuilder.concat(
                                    criteriaBuilder.concat(from.get("nombre")," "),
                                    from.get("apellido")
                            )
                        )
                );
        nombres = entityManager
                .createQuery(queryString)
                .getResultList();
        nombres.forEach(System.out::println);

        System.out.println("::::: Consulta de campos personalizados :::::");
        CriteriaQuery<Object[]> queryObject = criteriaBuilder.createQuery(Object[].class);
        from = queryObject.from(Cliente.class);
        queryObject.multiselect(from.get("id"),from.get("nombre"),from.get("apellido"),from.get("formaPago"));
        List<Object[]> registros = entityManager
                .createQuery(queryObject)
                .getResultList();
        registros.forEach(reg -> {
            Long id = (Long) reg[0];
            String nombre = (String) reg[1];
            String apellido = (String) reg[2];
            String formaPago = (String) reg[3];
            System.out.println("Id: "+id+", nombre: "+nombre+", apellido: "+apellido+", forma de pago: "+formaPago);
        });

        System.out.println("::::: Consulta de campos personalizados con Where:::::");
        queryObject = criteriaBuilder.createQuery(Object[].class);
        from = queryObject.from(Cliente.class);
        queryObject
                .multiselect(
                        from.get("id"),
                        from.get("nombre"),
                        from.get("apellido"),
                        from.get("formaPago")
                ).where(criteriaBuilder.like(from.get("nombre"),"a%"));
        registros = entityManager
                .createQuery(queryObject)
                .getResultList();
        registros.forEach(reg -> {
            Long id = (Long) reg[0];
            String nombre = (String) reg[1];
            String apellido = (String) reg[2];
            String formaPago = (String) reg[3];
            System.out.println("Id: "+id+", nombre: "+nombre+", apellido: "+apellido+", forma de pago: "+formaPago);
        });

        System.out.println("::::: Consulta de campos personalizados con Where ID (un sólo registro) :::::");
        queryObject = criteriaBuilder.createQuery(Object[].class);
        from = queryObject.from(Cliente.class);
        queryObject
                .multiselect(
                        from.get("id"),
                        from.get("nombre"),
                        from.get("apellido"),
                        from.get("formaPago")
                ).where(criteriaBuilder.equal(from.get("id"),15L));
        Object[] registro = entityManager
                .createQuery(queryObject)
                .getSingleResult();
        System.out.println("id: " + registro[0]+",nombre: "+registro[1]+",apellido: "+registro[2]+",forma de pago: "+registro[3]);

        System.out.println("::::: Usando count :::::");
        CriteriaQuery<Long> queryConteo = criteriaBuilder.createQuery(Long.class);
        from = queryConteo.from(Cliente.class);
        queryConteo
                .select(
                        criteriaBuilder.count(from.get("id"))
                );
        Long totalDeResgistros = entityManager
                .createQuery(queryConteo)
                .getSingleResult();
        System.out.println("Total de registros en la tabla clientes: "+totalDeResgistros);

        System.out.println("::::: Usando sum :::::");
        CriteriaQuery<Long> querySuma = criteriaBuilder.createQuery(Long.class);
        from = querySuma.from(Cliente.class);
        querySuma
                .select(
                        criteriaBuilder.sum(from.get("id"))
                );
        Long sumaId = entityManager
                .createQuery(querySuma)
                .getSingleResult();
        System.out.println("Suma de los id´s de la tabla clientes: "+sumaId);

        System.out.println("::::: Usando max :::::");
        CriteriaQuery<Long> queryMax = criteriaBuilder.createQuery(Long.class);
        from = queryMax.from(Cliente.class);
        queryMax
                .select(
                        criteriaBuilder.max(from.get("id"))
                );
        Long maxId = entityManager
                .createQuery(queryMax)
                .getSingleResult();
        System.out.println("Máximo de los id´s de la tabla clientes: "+maxId);

        System.out.println("::::: Usando min :::::");
        CriteriaQuery<Long> queryMin = criteriaBuilder.createQuery(Long.class);
        from = queryMin.from(Cliente.class);
        queryMin
                .select(
                        criteriaBuilder.min(from.get("id"))
                );
        Long minId = entityManager
                .createQuery(queryMin)
                .getSingleResult();
        System.out.println("Mínimo de los id´s de la tabla clientes: "+minId);


        System.out.println("::::: Usando count, sum, max, min y avg :::::");
        CriteriaQuery<Object[]> queryEstadistica = criteriaBuilder.createQuery(Object[].class);
        from = queryEstadistica.from(Cliente.class);
        queryEstadistica
                .multiselect(
                        criteriaBuilder.count(from.get("id")),
                        criteriaBuilder.sum(from.get("id")),
                        criteriaBuilder.max(from.get("id")),
                        criteriaBuilder.min(from.get("id")),
                        criteriaBuilder.avg(from.get("id"))
                );

        Object[] estadistica = entityManager
                .createQuery(queryEstadistica)
                .getSingleResult();
        System.out.println("conteo: " + estadistica[0]+", suma: "+estadistica[1]+", máximo: "+estadistica[2]+", mínimo: "+estadistica[3]+", promedio: "+estadistica[4]);

        entityManager.close();
    }
}
