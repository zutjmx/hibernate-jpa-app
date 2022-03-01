package org.zutjmx.hibernate.app;

import jakarta.persistence.EntityManager;
import org.zutjmx.hibernate.app.dominio.ClienteDto;
import org.zutjmx.hibernate.app.entity.Cliente;
import org.zutjmx.hibernate.app.util.JpaUtil;

import java.util.Arrays;
import java.util.List;

public class HibernateQL {

    private static final String consultaTodos = "select c from Cliente c";
    private static final String consultaNombreMasCorto = "select min(LENGTH(c.nombre)) from Cliente c";
    private static final String consultaEstadistica = "select min(c.id), max(c.id), sum(c.id), count(c), avg(LENGTH(c.nombre)) from Cliente c";
    private static final String consultaNombreMasLargo = "select max(LENGTH(c.nombre)) from Cliente c";
    private static final String consultaCountTodos = "select count(c) from Cliente c";
    private static final String consultaNombreYLongitud = "select c.nombre, length(c.nombre) from Cliente c";
    private static final String consultaMinId = "select min(c.id) from Cliente c";
    private static final String consultaMaxId = "select max(c.id) from Cliente c";
    private static final String consultaNombre = "select c.nombre from Cliente c";
    private static final String consultaConcatNombreApellido = "select concat(c.nombre, ' ', c.apellido) from Cliente c";
    private static final String consultaDoblePipeNombreApellido = "select c.nombre || ' ' || c.apellido from Cliente c";
    private static final String consultaNombreApellidoMayusculas = "select upper(concat(c.nombre, ' ', c.apellido)) from Cliente c";
    private static final String consultaNombreDistinct = "select distinct(c.nombre) from Cliente c";
    private static final String consultaFormaPagoDistinct = "select distinct(c.formaPago) from Cliente c";
    private static final String consultaFormaPagoCountDistinct = "select count(distinct(c.formaPago)) from Cliente c";
    private static final String consultaIdNombreAp = "select c.id, c.nombre, c.apellido from Cliente c";
    private static final String consultaClienteFormaPago = "select c, c.formaPago from Cliente c";
    private static final String consultaObjetoPersonalizado = "select new Cliente(c.nombre, c.apellido) from Cliente c";
    private static final String consultaObjetoPersonalizadoDto = "select new org.zutjmx.hibernate.app.dominio.ClienteDto(c.nombre, c.apellido) from Cliente c";
    private static final String wherePorId = " where c.id =: id";
    private static final String whereNombreLike = " where c.nombre like :paramNombre";
    private static final String whereIdBetween = " where c.id between 3 and 8";
    private static final String whereIdBetweenChar = " where c.nombre between 'J' and 'R'";
    private static final String orderByNombre = " order by c.nombre, c.apellido";
    private static final String orderByNombreDesc = " order by c.nombre desc, c.apellido desc";
    private static final String subQuery = "SELECT c.nombre, LENGTH(c.nombre) " +
            "FROM Cliente c WHERE LENGTH(c.nombre) = (SELECT MIN(LENGTH(d.nombre)) FROM Cliente d )";
    private static final String subQueryNombreMasLargo = "SELECT c.nombre, LENGTH(c.nombre) " +
            "FROM Cliente c WHERE LENGTH(c.nombre) = (SELECT MAX(LENGTH(d.nombre)) FROM Cliente d )";

    private static final String subQueryUltimoIdRegistrado = "SELECT c FROM Cliente c " +
            "WHERE c.id = (SELECT MAX(d.id) FROM Cliente d )";

    private static final String consultaConIn = "SELECT c FROM Cliente c " +
            "WHERE c.id in :listaIds";

    public static void main(String[] args) {
        EntityManager entityManager = JpaUtil.getEntityManager();

        System.out.println("::::: Consulta Todos :::::");
        List<Cliente> clientes = entityManager
                .createQuery(consultaTodos, Cliente.class)
                .getResultList();
        clientes.forEach(System.out::println);

        System.out.println("::::: Consulta por ID :::::");

        Cliente cliente = entityManager
                .createQuery(consultaTodos.concat(wherePorId), Cliente.class)
                .setParameter("id",18L)
                .getSingleResult();
        System.out.println(cliente);

        System.out.println("::::: Consulta campo Nombre por ID :::::");
        String nombreCliente = entityManager
                .createQuery(consultaNombre.concat(wherePorId),String.class)
                .setParameter("id",19L)
                .getSingleResult();
        System.out.println("Nombre del cliente; "+nombreCliente);

        System.out.println("::::: Consulta varios campos (con Object[]) :::::");
        Object[] objectCliente = entityManager
                .createQuery(consultaIdNombreAp.concat(wherePorId),Object[].class)
                .setParameter("id",20L)
                .getSingleResult();
        Long id = (Long) objectCliente[0];
        String nombre = (String) objectCliente[1];
        String apellido = (String) objectCliente[2];
        System.out.println("Cliente :: {" + id + " " +nombre + " " + apellido + "}");

        System.out.println("::::: Consulta varios campos (con List<Object>) :::::");
        List<Object[]> registros = entityManager
                .createQuery(consultaIdNombreAp,Object[].class)
                .getResultList();
        /*for (Object[] reg: registros) {
            id = (Long) reg[0];
            nombre = (String) reg[1];
            apellido = (String) reg[2];
            System.out.println("Cliente :: {" + id + " " +nombre + " " + apellido + "}");
        }*/
        registros.forEach(reg -> {
            Long idCli = (Long) reg[0];
            String nombreCli = (String) reg[1];
            String apellidoCli = (String) reg[2];
            System.out.println("Cliente :: {" + idCli + " " +nombreCli + " " + apellidoCli + "}");
        });

        System.out.println("::::: Consulta por cliente y forma de pago :::::");
        registros = entityManager
                .createQuery(consultaClienteFormaPago,Object[].class)
                .getResultList();
        registros.forEach(reg -> {
            Cliente c = (Cliente) reg[0];
            String formaPago = (String) reg[1];
            System.out.println("Cliente == {" + c.toString() + " " +formaPago + "}");
        });

        System.out.println("::::: Consulta con objeto(Entity) de clase personalizada :::::");
        clientes = entityManager
                .createQuery(consultaObjetoPersonalizado, Cliente.class)
                .getResultList();
        clientes.forEach(System.out::println);

        System.out.println("::::: Consulta con objeto(Dto) de clase personalizada :::::");
        List<ClienteDto> clientesDto = entityManager
                .createQuery(consultaObjetoPersonalizadoDto, ClienteDto.class)
                .getResultList();
        clientesDto.forEach(System.out::println);

        System.out.println("::::: Listado de nombres de cliente :::::");
        List<String> nombres = entityManager
                .createQuery(consultaNombre, String.class)
                .getResultList();
        nombres.forEach(System.out::println);

        System.out.println("::::: Listado de nombres de cliente (usando Distinct) :::::");
        nombres = entityManager
                .createQuery(consultaNombreDistinct, String.class)
                .getResultList();
        nombres.forEach(System.out::println);

        System.out.println("::::: Listado de formas de pago (usando Distinct) :::::");
        List<String> formasPago = entityManager
                .createQuery(consultaFormaPagoDistinct, String.class)
                .getResultList();
        formasPago.forEach(System.out::println);

        System.out.println("::::: Listado de formas de pago (usando Count::Distinct) :::::");
        Long totalFormasPago = entityManager
                .createQuery(consultaFormaPagoCountDistinct, Long.class)
                .getSingleResult();
        System.out.println("Total de formas de pago: " + totalFormasPago);

        System.out.println("::::: Concatenar nombre y apellido (Usando Concat o doble || ) :::::");
        nombres = entityManager
                /*.createQuery(consultaConcatNombreApellido, String.class)*/
                .createQuery(consultaDoblePipeNombreApellido, String.class)
                .getResultList();
        nombres.forEach(System.out::println);

        System.out.println("::::: Concatenar nombre y apellido en mayúsculas :::::");
        nombres = entityManager
                .createQuery(consultaNombreApellidoMayusculas, String.class)
                .getResultList();
        nombres.forEach(System.out::println);

        System.out.println("::::: consulta usando Like :::::");
        String paramNombre = "AN";
        clientes = entityManager
                .createQuery(consultaTodos.concat(whereNombreLike), Cliente.class)
                .setParameter("paramNombre","%"+paramNombre+"%")
                .getResultList();
        clientes.forEach(System.out::println);

        System.out.println("::::: consulta usando between (rango númerico) :::::");
        clientes = entityManager
                .createQuery(consultaTodos.concat(whereIdBetween), Cliente.class)
                .getResultList();
        clientes.forEach(System.out::println);

        System.out.println("::::: consulta usando between (rango carácteres) :::::");
        clientes = entityManager
                .createQuery(consultaTodos.concat(whereIdBetweenChar), Cliente.class)
                .getResultList();
        clientes.forEach(System.out::println);

        System.out.println("::::: Consultas con Order By Asc (Default) :::::");
        clientes = entityManager
                .createQuery(consultaTodos.concat(orderByNombre), Cliente.class)
                .getResultList();
        clientes.forEach(System.out::println);

        System.out.println("::::: Consultas con Order By Desc :::::");
        clientes = entityManager
                .createQuery(consultaTodos.concat(orderByNombreDesc), Cliente.class)
                .getResultList();
        clientes.forEach(System.out::println);

        System.out.println("::::: Total de registros en la tabla :::::");
        Long total = entityManager
                .createQuery(consultaCountTodos, Long.class)
                .getSingleResult();
        System.out.println("Total de registros en la tabla Clientes: ".concat(total.toString()));

        System.out.println("::::: Consulta usando función min() sobre el ID del cliente :::::");
        Long minimoId = entityManager
                .createQuery(consultaMinId, Long.class)
                .getSingleResult();
        System.out.println("Valor mínimo de ID en la tabla Clientes: ".concat(minimoId.toString()));

        System.out.println("::::: Consulta usando función max() sobre el ID del cliente :::::");
        Long maxId = entityManager
                .createQuery(consultaMaxId, Long.class)
                .getSingleResult();
        System.out.println("Valor máximo de ID en la tabla Clientes: ".concat(maxId.toString()));

        System.out.println("::::: Consulta con nombre y longitud del nombre :::::");
        registros = entityManager
                .createQuery(consultaNombreYLongitud,Object[].class)
                .getResultList();
        registros.forEach(reg -> {
            String nombreCli = (String) reg[0];
            Integer longitudNombreCli = (Integer) reg[1];
            System.out.println("Nombre y longitud del nombre del Cliente :: {" + nombreCli + ", " + longitudNombreCli + "}");
        });

        System.out.println("::::: Consulta con nombre más corto :::::");
        Integer nombreMasCorto = entityManager
                .createQuery(consultaNombreMasCorto,Integer.class)
                .getSingleResult();
        System.out.println("Longitud del nombre más corto: ".concat(nombreMasCorto.toString()));

        System.out.println("::::: Consulta con nombre más largo :::::");
        Integer nombreMasLargo = entityManager
                .createQuery(consultaNombreMasLargo,Integer.class)
                .getSingleResult();
        System.out.println("Longitud del nombre más largo: ".concat(nombreMasLargo.toString()));

        System.out.println("::::: Consulta con count, min. max, avg y sum :::::");
        //select min(c.id), max(c.id), sum(c.id), count(c), avg(LENGTH(c.nombre)) from Cliente c
        Object[] estadistica = entityManager
                .createQuery(consultaEstadistica,Object[].class)
                .getSingleResult();
        System.out.println("Mínimo ID: " + estadistica[0]);
        System.out.println("Máximo ID: " + estadistica[1]);
        System.out.println("Suma de ID's: " + estadistica[2]);
        System.out.println("Total de registros: " + estadistica[3]);
        System.out.println("Promedio longitud nombre: " + estadistica[4]);

        System.out.println("::::: Consulta con nombre más corto y su longitud :::::");
        registros = entityManager
                .createQuery(subQuery,Object[].class)
                .getResultList();
        registros.forEach(reg ->{
            String nombreCli = (String) reg[0];
            Integer nombreLongitud = (Integer) reg[1];
            System.out.println("Nombre: " + nombreCli + ", Longitud nombre: " + nombreLongitud);
        });

        System.out.println("::::: Consulta con nombre más largo y su longitud :::::");
        registros = entityManager
                .createQuery(subQueryNombreMasLargo,Object[].class)
                .getResultList();
        registros.forEach(reg ->{
            String nombreCli = (String) reg[0];
            Integer nombreLongitud = (Integer) reg[1];
            System.out.println("Nombre: " + nombreCli + ", Longitud nombre: " + nombreLongitud);
        });

        System.out.println("::::: Consulta último ID registrado :::::");
        clientes = entityManager
                .createQuery(subQueryUltimoIdRegistrado,Cliente.class)
                .getResultList();
        clientes.forEach(System.out::println);

        System.out.println("::::: Consulta con IN :::::");
        clientes = entityManager
                .createQuery(consultaConIn,Cliente.class)
                .setParameter("listaIds", Arrays.asList(7L, 18L, 20L, 25L, 28L))
                .getResultList();
        clientes.forEach(System.out::println);

        entityManager.close();

    }
}
