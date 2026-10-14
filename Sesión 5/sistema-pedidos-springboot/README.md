# Sistema de Pedidos — Spring Boot

Proyecto desarrollado para la PA2 del curso Desarrollo de Aplicaciones Empresariales Avanzado.

## 1. Tecnologías

- Java 21
- Spring Boot
- Spring Data JPA
- H2 Database
- Maven
- REST Client
- JUnit y Mockito

## 2. Descripción

La aplicación permite registrar y consultar pedidos. También permite anular un pedido registrado, solicitar la devolución del stock al servicio de inventario y guardar el registro de la anulación.

La aplicación utiliza una base de datos H2 en memoria para los pedidos y el registro de anulaciones. La devolución de stock se comunica mediante HTTP con el servicio `inventario-service`.

## 3. Configuración

La configuración se encuentra en `src/main/resources/application.properties`.

Propiedades principales:

- Puerto de la aplicación: `8081`
- Contexto: `/sistema-pedidos`
- Base de datos: H2 en memoria
- URL base del inventario: `http://localhost:8082/inventario-service/api/inventario`

No se deben incluir contraseñas reales ni credenciales sensibles en el repositorio.

## 4. Ejecución

Requisitos: Java 21 y acceso al servicio de inventario desplegado en WildFly.

Desde la carpeta del proyecto, ejecutar las pruebas:

```bash
.\mvnw.cmd test
```

Para iniciar la aplicación:

```bash
.\mvnw.cmd spring-boot:run
```

Iniciar primero SQL Server y WildFly con el servicio de inventario disponible en el puerto `8082`. Después iniciar Spring Boot en el puerto `8081`.

## 5. Endpoints principales

### Registrar un pedido

Utilizar el endpoint de registro de pedidos existente en el proyecto.

### Consultar pedidos

Utilizar el endpoint de consulta de pedidos existente en el proyecto.

### Anular un pedido

`POST /sistema-pedidos/api/v1/anulaciones`

Ejemplo de cuerpo JSON:

```json
{
  "pedidoId": 1,
  "motivo": "Solicitud del cliente"
}
```

### Consultar anulaciones

`GET /sistema-pedidos/api/v1/anulaciones`

## 6. Flujo de anulación

1. Se verifica que el pedido exista y esté registrado.
2. Se valida que no haya sido anulado previamente.
3. Se solicita al servicio de inventario la devolución de la cantidad correspondiente.
4. Si la devolución remota tiene éxito, se actualiza el estado del pedido a `ANULADO` y se registra la anulación.
5. Si la devolución falla, no debe confirmarse la anulación local.

La base de datos H2 y el servicio de inventario tienen límites transaccionales independientes; no se implementa una transacción distribuida.

## 7. Pruebas

Las pruebas unitarias del servicio de anulación verifican escenarios como anulación exitosa, pedido inexistente, pedido ya anulado y fallo del servicio de inventario.

Ejecutar todas las pruebas con:

```bash
.\mvnw.cmd test
```

## 8. Limitaciones

La base H2 está configurada en memoria, por lo que los datos se pierden al reiniciar la aplicación. La disponibilidad de las operaciones de anulación depende de que el servicio de inventario esté accesible.
