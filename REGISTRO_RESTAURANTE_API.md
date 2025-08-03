# API de Registro de Restaurante

## Endpoint Principal: POST /api/registro-restaurante

Este endpoint maneja todo el flujo de registro de un restaurante nuevo, incluyendo:

1. **Solicitud de registro** (tabla `solicitud_registro`)
2. **Creación del restaurante** (tabla `restaurante`)
3. **Subida de imágenes** (tabla `imagen`)
4. **Carga de comprobantes** (tabla `comprobante`)
5. **Registro del menú** (tabla `menu`)

### Flujo de Datos entre Tablas

```sql
-- 1. Se crea la solicitud
INSERT INTO solicitud_registro (fecha, estado, nombre_propuesto_restaurante, correo, nombre_propietario, horario_atencion, id_restaurantero) 
VALUES (NOW(), 'pendiente', 'El Rincón del Sabor', 'elrincondelsabor@email.com', 'María García', '10:00-22:00', 2);

-- 2. Se crea el restaurante usando el ID de la solicitud
INSERT INTO restaurante (nombre, horario, telefono, etiquetas, id_solicitud, id_restaurantero, id_zona) 
VALUES ('El Rincón del Sabor', '10:00-22:00', '123-456-7890', 'Latino, Vegetariano', LAST_INSERT_ID(), 2, 1);

-- 3. Se crean las imágenes vinculadas al restaurante
INSERT INTO imagen (ruta_imagen, fecha_subida, id_restaurante, id_solicitud, id_restaurantero) 
VALUES ('/uploads/images/principal.jpg', NOW(), 1, 2, 2);

-- 4. Se crean los comprobantes
INSERT INTO comprobante (tipo, ruta_archivo, fecha_subida, id_restaurante, id_solicitud, id_restaurantero, id_zona) 
VALUES ('comprobante_domicilio', '/uploads/documents/domicilio.pdf', NOW(), 1, 2, 2, 1);

-- 5. Se crea el menú
INSERT INTO menu (ruta_archivo, ruta_menu, estado, id_restaurante, id_solicitud, id_restaurantero) 
VALUES ('/uploads/menus/menu.pdf', '/uploads/menus/menu.pdf', 'activo', 1, 2, 2);
```

## Formato de Petición

### Content-Type: `application/x-www-form-urlencoded` o `multipart/form-data`

### Campos del Formulario:

#### Datos Obligatorios:
- `nombreRestaurante` (string): Nombre del restaurante
- `propietario` (string): Nombre del propietario 
- `correoElectronico` (string): Email de contacto
- `numeroCelular` (string): Teléfono celular
- `direccion` (string): Dirección física del restaurante
- `horarios` (string): Horarios de atención (ej: "10:00-22:00")
- `idRestaurantero` (int): ID del restaurantero registrado
- `idZona` (int): ID de la zona donde está ubicado

#### Datos Opcionales:
- `facebook` (string): URL del Facebook
- `instagram` (string): URL del Instagram

#### Archivos (al menos uno es obligatorio):
- `imagenPrincipal` (string): Ruta de la imagen principal
- `imagenSecundaria` (string): Ruta de la imagen secundaria  
- `imagenPlatillo` (string): Ruta de la imagen del platillo

#### Documentos PDF:
- `comprobanteDomicilio` (string): Ruta del comprobante de domicilio
- `menuRestaurante` (string): Ruta del menú en PDF

## Ejemplo de Petición

```bash
curl -X POST http://localhost:7070/api/registro-restaurante \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "nombreRestaurante=El Rincón del Sabor" \
  -d "propietario=María García" \
  -d "correoElectronico=maria@email.com" \
  -d "numeroCelular=123456789" \
  -d "facebook=https://facebook.com/elrincon" \
  -d "instagram=@elrincondelsabor" \
  -d "direccion=Calle Principal 123" \
  -d "horarios=10:00-22:00" \
  -d "imagenPrincipal=/uploads/images/principal.jpg" \
  -d "imagenSecundaria=/uploads/images/secundaria.jpg" \
  -d "imagenPlatillo=/uploads/images/platillo.jpg" \
  -d "comprobanteDomicilio=/uploads/documents/domicilio.pdf" \
  -d "menuRestaurante=/uploads/menus/menu.pdf" \
  -d "idRestaurantero=2" \
  -d "idZona=1"
```

## Respuestas

### Éxito (201 Created):
```json
{
  "success": true,
  "message": "Restaurante registrado exitosamente",
  "data": "Restaurante registrado exitosamente. ID: 1, Solicitud: 2. Imágenes: Principal, Secundaria, Platillo, Comprobantes: Domicilio, Menú: Menú creado"
}
```

### Error de Validación (400 Bad Request):
```json
{
  "success": false,
  "message": "Datos inválidos: Nombre del restaurante es obligatorio"
}
```

### Error del Servidor (500 Internal Server Error):
```json
{
  "success": false,
  "message": "Error al registrar restaurante: [detalle del error]"
}
```

## Endpoints Adicionales

### Consultar Estado de Solicitud
```http
GET /api/solicitud/{id}/estado
```

**Respuesta:**
```json
{
  "success": true,
  "data": {
    "estado": "pendiente"
  }
}
```

### Consultar Solicitudes de un Restaurantero
```http
GET /api/restaurantero/{id}/solicitudes
```

**Respuesta:**
```json
{
  "success": true,
  "data": [
    {
      "id_solicitud": 1,
      "fecha": "2025-08-02T14:30:00",
      "estado": "pendiente",
      "nombre_propuesto_restaurante": "El Rincón del Sabor",
      "correo": "maria@email.com",
      "nombre_propietario": "María García",
      "horario_atencion": "10:00-22:00",
      "id_restaurantero": 2
    }
  ]
}
```

## Validaciones Implementadas

1. **Campos obligatorios**: Nombre restaurante, propietario, email, teléfono, dirección, horarios
2. **IDs válidos**: restaurantero > 0, zona > 0
3. **Al menos una imagen**: Debe proporcionar mínimo una imagen (principal, secundaria o platillo)
4. **Restaurantero existente**: Valida que el ID del restaurantero exista en la base de datos

## Tipos de Comprobante Disponibles

- `comprobante_domicilio`: Comprobante de domicilio
- `menu_restaurante`: Menú del restaurante  
- `licencia_funcionamiento`: Licencia de funcionamiento
- `permiso_sanidad`: Permiso de sanidad
- `certificado_manipulacion`: Certificado de manipulación de alimentos
- `ruc`: RUC
- `otro`: Otros tipos de comprobantes
