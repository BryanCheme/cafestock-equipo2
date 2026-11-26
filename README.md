# CaféStock — Equipo 2 (Escenario A)

Aplicación de escritorio en Java para la gestión de inventario y ventas de la cafetería universitaria. Reemplaza el cuaderno de Rosa, administradora de la cafetería de la facultad: controla el stock y registra las ventas.

Proyecto IB — ISID432 Desarrollo y Mantenimiento de Software, Escuela Politécnica Nacional (Facultad de Ingeniería de Sistemas).

## Equipo y escenario

- **Nombre del equipo:** CaféStock — Equipo 2
- **Escenario:** A — CaféStock: inventario y ventas de la cafetería

## Integrantes y roles

| Integrante | Rol |
|---|---|
| Samara Salazar | Product Owner y Desarrolladora |
| Bryan Cheme | Scrum Master y Desarrollador |
| Kenneth Gaona | Desarrollador (Arquitectura y Persistencia) |
| Bryan Toapanta | Desarrollador (Interfaz Gráfica y UX) |

## Tablero de Trello

https://n9.cl/81q9f

## Tecnologías utilizadas

- **Lenguaje:** Java (JDK)
- **Interfaz gráfica:** Java Swing
- **Persistencia de datos:** archivos CSV (`productos.csv` y `ventas.csv`), sin librerías externas
- **Gestión ágil:** Scrum con tablero en Trello
- **Control de versiones:** Git y GitHub
- **IDE de desarrollo:** Apache NetBeans / IntelliJ IDEA

## Cómo ejecutar la aplicación

1. Descargar o clonar este repositorio:
   ```bash
   git clone https://github.com/BryanCheme/cafestock-equipo2.git
   ```
2. Entrar a la carpeta `CafeStock/dist/`, donde están el `.jar` y los datos de prueba (`productos.csv` y `ventas.csv`):
   ```bash
   cd cafestock-equipo2/CafeStock/dist
   ```
3. Ejecutar el `.jar` desde esa misma carpeta:
   ```bash
   java -jar cafestock-equipo2.jar
   ```

**Importante:**

- Ejecuta el comando desde `CafeStock/dist/`: la aplicación lee y guarda `productos.csv` y `ventas.csv` en la carpeta desde la que se lanza. Si se ejecuta desde otra carpeta, abrirá sin datos.
- Se necesita un JDK instalado. El `.jar` incluido fue compilado con Java 27; con una versión anterior de Java no abrirá.

## Historias de usuario del Sprint 1

| Historia | Descripción | Dónde |
|---|---|---|
| HU-A1 | Registrar productos (código, nombre, precio, stock y stock mínimo) con validación de campos vacíos, letras en campos numéricos y código repetido | Pestaña **Productos** |
| HU-A2 | Consultar la lista de productos, buscar por código o nombre y editar todos los datos excepto el código | Pestaña **Productos** |
| HU-A3 | Registrar ventas con total calculado antes de confirmar, fecha editable y descuento automático del stock | Pestaña **Ventas** |

### Reglas de negocio

- El código del producto es único y no se puede cambiar.
- El precio debe ser mayor que 0; el stock y el stock mínimo son enteros desde 0.
- Cada venta corresponde a un solo producto y su total es precio × cantidad.
- No se puede vender más unidades de las que hay en stock; cada venta descuenta el stock.
- La fecha de la venta es editable (por defecto, la de hoy), en formato `AAAA-MM-DD`.

## Estructura del repositorio

```
cafestock-equipo2/
├── README.md
└── CafeStock/
    ├── src/
    │   ├── modelo/         Producto, Venta
    │   ├── persistencia/   GestorCSV (lectura y escritura de CSV)
    │   ├── logica/         Validador (reglas de validación)
    │   └── vista/          VentanaPrincipal (interfaz Swing)
    └── dist/
        ├── cafestock-equipo2.jar
        ├── productos.csv   (datos de prueba)
        └── ventas.csv      (datos de prueba)
```

## Formato de los datos de prueba

Los archivos usan `;` como separador y no tienen encabezado.

- `productos.csv`: `código;nombre;precio;stock actual;stock mínimo`
- `ventas.csv`: `número;fecha;código del producto;nombre del producto;cantidad;total`

## Commits

Cada mensaje de commit empieza con el código de la historia, por ejemplo: `HU-A1: validar código repetido`.

