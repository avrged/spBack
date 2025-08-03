# 📤 Registro de Restaurante con Archivos Reales

## 🎯 **Endpoint Principal**
**POST** `http://localhost:7070/api/registro-restaurante`

## 📋 **Configuración en Postman**

### **1. Configuración básica:**
- **Método:** `POST`
- **URL:** `http://localhost:7070/api/registro-restaurante`
- **Headers:** (NO agregar Content-Type, Postman lo detecta automáticamente)

### **2. Body → form-data:**

#### **📝 Campos de texto obligatorios:**
| Key | Type | Value (ejemplo) |
|-----|------|-----------------|
| `nombreRestaurante` | Text | `El Rincón Gourmet` |
| `propietario` | Text | `Carlos Mendoza` |
| `correoElectronico` | Text | `carlos@email.com` |
| `numeroCelular` | Text | `555-1234` |
| `direccion` | Text | `Av. Principal 456` |
| `horarios` | Text | `11:00-23:00` |
| `idRestaurantero` | Text | `2` |
| `idZona` | Text | `1` |

#### **📝 Campos de texto opcionales:**
| Key | Type | Value (ejemplo) |
|-----|------|-----------------|
| `facebook` | Text | `https://facebook.com/rincon-gourmet` |
| `instagram` | Text | `@rincongourmet` |

#### **📁 Campos de archivos (opcionales pero recomendados):**
| Key | Type | Archivos aceptados |
|-----|------|-------------------|
| `imagenPrincipal` | File | `.jpg, .jpeg, .png, .gif, .webp` |
| `imagenSecundaria` | File | `.jpg, .jpeg, .png, .gif, .webp` |
| `imagenPlatillo` | File | `.jpg, .jpeg, .png, .gif, .webp` |
| `comprobanteDomicilio` | File | `.pdf, .doc, .docx, .txt` |
| `menuRestaurante` | File | `.pdf, .jpg, .jpeg, .png` |

## ✅ **Respuesta Exitosa**
```json
{
  "success": true,
  "message": "Restaurante registrado exitosamente",
  "data": "Restaurante registrado con ID de solicitud: 1"
}
```

## ❌ **Respuestas de Error**

### **400 - Datos inválidos:**
```json
{
  "success": false,
  "message": "Datos inválidos: Nombre del restaurante es obligatorio"
}
```

### **500 - Error de servidor:**
```json
{
  "success": false,
  "message": "Error al registrar restaurante: [detalle del error]"
}
```

## 📁 **¿Dónde se guardan los archivos?**

Los archivos se almacenan automáticamente en:
- **Imágenes:** `./uploads/images/[uuid].jpg`
- **Documentos:** `./uploads/documents/[uuid].pdf`
- **Menús:** `./uploads/menus/[uuid].pdf`

## 🌐 **Acceso a los archivos**

Los archivos quedan disponibles vía HTTP en:
- `http://localhost:7070/uploads/images/[uuid].jpg`
- `http://localhost:7070/uploads/documents/[uuid].pdf`
- `http://localhost:7070/uploads/menus/[uuid].pdf`

## 🔄 **Distribución en Base de Datos**

Los datos se distribuyen automáticamente en:

1. **📋 `solicitud_registro`**: Datos principales de la solicitud
2. **🏪 `restaurante`**: Información del restaurante
3. **📸 `imagen`**: Cada imagen como registro separado
4. **📄 `comprobante`**: Comprobante de domicilio
5. **📋 `menu`**: Menú del restaurante

## 🧪 **Ejemplo de Test Completo**

### **Paso 1: Crear Restaurantero**
**POST** `http://localhost:7070/usuarios`
```json
{
  "nombre": "Carlos Mendoza",
  "correo": "carlos@email.com",
  "contrasena": "123456",
  "tipo": "restaurantero"
}
```

### **Paso 2: Registrar Restaurante con archivos**
**POST** `http://localhost:7070/api/registro-restaurante`
- Usar **form-data** en Postman
- Llenar todos los campos de texto
- Subir archivos reales en los campos de File

### **Paso 3: Verificar en Base de Datos**
```sql
-- Ver solicitud creada
SELECT * FROM solicitud_registro ORDER BY id_solicitud DESC LIMIT 1;

-- Ver restaurante creado
SELECT * FROM restaurante ORDER BY id_restaurante DESC LIMIT 1;

-- Ver imágenes guardadas
SELECT * FROM imagen ORDER BY id_imagen DESC LIMIT 3;

-- Ver comprobante guardado
SELECT * FROM comprobante ORDER BY id_comprobante DESC LIMIT 1;

-- Ver menú guardado
SELECT * FROM menu ORDER BY id_menu DESC LIMIT 1;
```

## 🎯 **Validaciones Implementadas**

- ✅ Campos obligatorios validados
- ✅ Extensiones de archivo verificadas
- ✅ Archivos guardados con nombres únicos (UUID)
- ✅ Directorios creados automáticamente si no existen
- ✅ Manejo de errores robusto
- ✅ Soporte para archivos opcionales
