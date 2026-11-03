# poblar-bd.ps1 - puebla la base de datos de desarrollo (PostgreSQL en Docker) con seed-datos.sql:
#   catalogo (categorias, marcas, 63 productos con imagenes), ubigeo, tiendas, inventario, personas,
#   usuarios, clientes, empleados, direcciones, facturacion, cupones, carritos, pedidos, historial,
#   pagos, envios, comprobantes y resenas.
# Requisitos: backend corriendo (.\mvnw) y seed-datos.sql en la raiz del proyecto.
# Se puede ejecutar varias veces.
# Opcional: -Imagenes "ruta\wwwroot\Image\Productos" carga ademas el catalogo de esas imagenes (cargar-datos.ps1).
param(
    [string]$Api = "http://localhost:9999",
    [string]$Imagenes = ""
)
Set-Location $PSScriptRoot
$ErrorActionPreference = "Stop"

if ($Imagenes) {
    & .\cargar-datos.ps1 -Api $Api -Imagenes $Imagenes
}

$c = docker ps --filter "name=commerceapp-postgresql" --format "{{.Names}}" | Select-Object -First 1
if (-not $c) { throw "No esta corriendo el contenedor de PostgreSQL. Levanta el backend con .\mvnw" }

docker cp .\seed-datos.sql "${c}:/tmp/seed-datos.sql"
if ($LASTEXITCODE -ne 0) { throw "No se pudo copiar seed-datos.sql al contenedor $c" }
docker exec $c psql -q -U commerceApp -d commerceApp -f /tmp/seed-datos.sql
if ($LASTEXITCODE -ne 0) { throw "Fallo la carga de seed-datos.sql" }

Write-Host ""
Write-Host "Base de datos poblada. Usuarios de prueba: contrasena 'user' (admin: 'admin')."
