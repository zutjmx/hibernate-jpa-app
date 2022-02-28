package org.zutjmx.hibernate.app;

import jakarta.persistence.EntityManager;
import org.zutjmx.hibernate.app.dominio.ClienteDto;
import org.zutjmx.hibernate.app.entity.Cliente;
import org.zutjmx.hibernate.app.util.JpaUtil;

import java.util.List;

public class HibernateQL {

    private static final String consultaTodos = "select c from Cliente c";
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
                .setParameter("id",13L)
                .getSingleResult();
        System.out.println(cliente);

        System.out.println("::::: Consulta campo Nombre por ID :::::");
        String nombreCliente = entityManager
                .createQuery(consultaNombre.concat(wherePorId),String.class)
                .setParameter("id",15L)
                .getSingleResult();
        System.out.println("Nombre del cliente; "+nombreCliente);

        System.out.println("::::: Consulta varios campos (con Object[]) :::::");
        Object[] objectCliente = entityManager
                .createQuery(consultaIdNombreAp.concat(wherePorId),Object[].class)
                .setParameter("id",16L)
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

        entityManager.close();

    }
}
