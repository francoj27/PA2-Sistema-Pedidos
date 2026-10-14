# PA2 – Sistema de Pedidos e Inventario

**Curso:** Desarrollo de Aplicaciones Empresariales Avanzado  
**Institución:** ISIL

## Descripción

Este repositorio contiene los dos proyectos modificados para la Práctica Aplicada 2 (PA2). El objetivo es implementar la devolución de stock en el servicio de inventario e integrar esta funcionalidad con la anulación de pedidos.

## Proyectos

### 1. Inventario Service — Jakarta EE

**Ubicación:** `Sesión 4/inventario-service/`

Servicio REST desarrollado con Jakarta EE, JPA y WildFly. Incluye la operación de devolución de stock, validaciones y pruebas unitarias.

### 2. Sistema de Pedidos — Spring Boot

**Ubicación:** `Sesión 5/sistema-pedidos-springboot/`

Aplicación desarrollada con Spring Boot y Spring Data JPA. Incluye la anulación de pedidos, la comunicación REST con el servicio de inventario y el registro de las anulaciones.

## Tecnologías

- Java 21
- Jakarta EE
- Spring Boot
- Spring Data JPA
- Maven
- WildFly
- SQL Server
- H2
- REST API
- JUnit y Mockito

## Documentación

Cada proyecto contiene su propio `README.md` con información específica sobre requisitos, configuración, endpoints, ejecución y pruebas.

## Estructura del repositorio

```text
PA2-Sistema-Pedidos/
├── Sesión 4/
│   └── inventario-service/
└── Sesión 5/
    └── sistema-pedidos-springboot/
```

## Ejecución

Consulta el README de cada proyecto antes de ejecutarlo. El servicio de inventario debe estar disponible antes de probar las operaciones de anulación de pedidos que dependen de él.

## Autoría

Repositorio personal creado para la entrega académica de la PA2.
