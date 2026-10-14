\# Inventario Service — Jakarta EE 11 + SQL Server Express 2025



Servicio REST de inventario desarrollado para la PA2 del curso Desarrollo de Aplicaciones Empresariales Avanzado.



\## 1. Tecnologías



\- Java 21

\- Maven

\- Jakarta EE 11

\- WildFly 41

\- Jakarta REST (JAX-RS)

\- Jakarta Enterprise Beans (EJB)

\- Jakarta Persistence (JPA)

\- Jakarta Transactions (JTA)

\- Bean Validation

\- SQL Server Express 2025



\## 2. Arquitectura



```text

Sistema de Pedidos / Postman

&#x20;         |

&#x20;         | HTTP + JSON

&#x20;         v

ProductoResource (Jakarta REST)

&#x20;         |

&#x20;         | @EJB

&#x20;         v

InventarioService (@Stateless)

&#x20;         |

&#x20;         | JPA / EntityManager

&#x20;         v

ProductoInventario

&#x20;         |

&#x20;         v

SQL Server Express

```



\## 3. Requisitos previos



\- JDK 21.

\- Maven disponible o integrado en IntelliJ IDEA.

\- SQL Server Express en ejecución.

\- Base de datos de inventario creada y configurada.

\- WildFly configurado para el proyecto.



\## 4. Preparación de la base de datos



1\. Habilitar TCP/IP en SQL Server Express.

2\. Configurar la autenticación necesaria para el usuario académico.

3\. Ejecutar `database/01\_crear\_inventario.sql` desde SQL Server Management Studio.

4\. Verificar el DataSource y la unidad de persistencia `InventarioPU`.



\## 5. Ejecución



Ejecutar las pruebas unitarias:



```bash

mvn test

```



Para iniciar WildFly mediante Maven, siempre que el aprovisionamiento esté correctamente configurado:



```bash

mvn wildfly:run

```



El proyecto utiliza un desplazamiento de puertos de 2. HTTP se publica en el puerto 8082 y administración en el 9992, según la configuración del plugin.



\## 6. URL base



`http://localhost:8082/inventario-service`



\## 7. Endpoints REST



\### Consultar productos



`GET /inventario-service/api/inventario/productos`



URL completa:



`http://localhost:8082/inventario-service/api/inventario/productos`



\### Consultar stock por ID



`GET /inventario-service/api/inventario/productos/1/stock`



\### Reservar stock por código



`POST /inventario-service/api/inventario/productos/codigo/LAP-001/reservas`



Body JSON:



```json

{

&#x20; "cantidad": 2

}

```



\### Devolver stock por código



`POST /inventario-service/api/inventario/productos/codigo/LAP-001/devoluciones`



Body JSON:



```json

{

&#x20; "cantidad": 2

}

```



La respuesta de una devolución válida incluye el código del producto, la cantidad devuelta y el stock restante. Las cantidades cero o negativas deben rechazarse.



\## 8. Productos de demostración



| Código | Producto |

|---|---|

| LAP-001 | Laptop |

| MON-001 | Monitor |

| TEC-001 | Teclado |



El stock cambia según las reservas y devoluciones realizadas durante las pruebas.



\## 9. Pruebas unitarias



Archivo: `src/test/java/pe/edu/isil/inventario/domain/ProductoInventarioTest.java`



Los cinco casos ejecutados comprueban:



\- Descuento de stock por reserva.

\- Rechazo de una reserva superior al stock disponible.

\- Devolución válida de stock.

\- Rechazo de devolución con cantidad cero o negativa.

\- Conservación del stock ante operaciones inválidas.



Resultado observado en IntelliJ: 5 pruebas aprobadas, 0 fallos y 0 errores.



\## 10. Material de referencia



\- `docs/GUIA\_IMPLEMENTACION\_PASO\_A\_PASO.md`

\- `docs/INTEGRACION\_CON\_SISTEMA\_PEDIDOS.md`

\- `requests/inventario-service.http`

\- `openapi/inventario-service.yaml`

\- `database/02\_reset\_stock\_demo.sql`



\## 11. Consideraciones de seguridad



Las credenciales reales no deben almacenarse en el repositorio. Para producción, deben utilizarse mecanismos seguros de gestión de secretos y configuración del DataSource. La configuración de certificados debe revisarse antes de utilizar el servicio en un entorno productivo.

