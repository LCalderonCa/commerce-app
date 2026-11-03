# cargar-datos.ps1  (version 2)
# Crea categorias, marcas, productos e imagenes en la API de commerce-app
# a partir de las carpetas de imagenes.
# Estructura esperada:  Image\Productos\{Categoria}\[{Marca}\]archivo.ext
# Se puede ejecutar varias veces: reutiliza categorias y marcas, y omite los productos que ya existen (por SKU).
param(
    [string]$Api = "http://localhost:9999",
    [string]$Imagenes = "D:\TiendaTech\TiendaTech\wwwroot\Image\Productos"
)
$ErrorActionPreference = "Stop"

if (-not (Test-Path $Imagenes)) { throw "No existe la carpeta de imagenes: $Imagenes" }

# --- Login como admin ---
$login = Invoke-RestMethod -Method Post -Uri "$Api/api/authenticate" -ContentType "application/json" -Body '{"username":"admin","password":"admin"}'
$h = @{ Authorization = "Bearer $($login.id_token)" }

function Enviar($metodo, $ruta, $objeto) {
    $json = $objeto | ConvertTo-Json -Depth 6 -Compress
    Invoke-RestMethod -Method $metodo -Uri "$Api$ruta" -Headers $h -ContentType "application/json; charset=utf-8" -Body ([System.Text.Encoding]::UTF8.GetBytes($json))
}

# En Windows PowerShell 5.1, Invoke-RestMethod entrega un arreglo JSON como UN solo objeto,
# y por eso @(...).Count daba siempre 1. Esta funcion lo normaliza.
function Lista($ruta) {
    $r = Invoke-RestMethod -Uri "$Api$ruta" -Headers $h
    if ($null -eq $r) { return ,@() }
    return ,@($r)
}

function Slug($texto) {
    $n = $texto.Normalize([Text.NormalizationForm]::FormD)
    $sb = New-Object System.Text.StringBuilder
    foreach ($c in $n.ToCharArray()) {
        if ([Globalization.CharUnicodeInfo]::GetUnicodeCategory($c) -ne [Globalization.UnicodeCategory]::NonSpacingMark) { [void]$sb.Append($c) }
    }
    ($sb.ToString().ToLower() -replace '[^a-z0-9]+', '-').Trim('-')
}

# Carpeta -> categoria, nombre singular y rango de precios (S/) provisional
$mapa = @{
    "audifonos"   = @{ Nombre = "Audífonos";   Singular = "Audífonos";  Min = 79;   Max = 899 }
    "celulares"   = @{ Nombre = "Celulares";   Singular = "Celular";    Min = 499;  Max = 4999 }
    "tablets"     = @{ Nombre = "Tablets";     Singular = "Tablet";     Min = 399;  Max = 3999 }
    "tv"          = @{ Nombre = "Televisores"; Singular = "Smart TV";   Min = 899;  Max = 4999 }
    "televisores" = @{ Nombre = "Televisores"; Singular = "Smart TV";   Min = 899;  Max = 4999 }
    "laptops"     = @{ Nombre = "Laptops";     Singular = "Laptop";     Min = 1499; Max = 7999 }
    "parlantes"   = @{ Nombre = "Parlantes";   Singular = "Parlante";   Min = 99;   Max = 999 }
    "smartwatch"  = @{ Nombre = "Smartwatch";  Singular = "Smartwatch"; Min = 199;  Max = 1999 }
    "consola"     = @{ Nombre = "Consolas";    Singular = "Consola";    Min = 299;  Max = 2999 }
    "consolas"    = @{ Nombre = "Consolas";    Singular = "Consola";    Min = 299;  Max = 2999 }
}

# Lo que ya existe en la API
$categorias = @{}
foreach ($c in (Lista "/api/categorias")) { if ($c.slug) { $categorias[$c.slug] = $c.id } }
$marcas = @{}
foreach ($m in (Lista "/api/marcas")) { if ($m.nombre) { $marcas[$m.nombre] = $m.id } }
Write-Host "Ya existen en la API: $($categorias.Count) categorias y $($marcas.Count) marcas"

$extensiones = @(".webp", ".jpg", ".jpeg", ".png")
$archivos = @(Get-ChildItem $Imagenes -Recurse -File | Where-Object { $extensiones -contains $_.Extension.ToLower() } | Sort-Object FullName)
$base = $Imagenes.TrimEnd('\')

$creados = 0; $existentes = 0; $omitidos = 0; $errores = 0
Write-Host "Imagenes encontradas: $($archivos.Count)"

foreach ($f in $archivos) {
    $rel = $f.FullName.Substring($base.Length + 1)
    $partes = $rel -split '\\'
    try {
        if ($partes.Length -lt 2 -or -not $mapa.ContainsKey($partes[0])) {
            Write-Host "OMITIDO (carpeta desconocida): $rel" -ForegroundColor Yellow
            $omitidos++
            continue
        }
        $info = $mapa[$partes[0]]

        # Categoria
        $catSlug = Slug $info.Nombre
        if (-not $categorias.ContainsKey($catSlug)) {
            $nueva = Enviar "Post" "/api/categorias" @{ nombre = $info.Nombre; slug = $catSlug; orden = ($categorias.Count + 1); activo = $true }
            $categorias[$catSlug] = $nueva.id
            Write-Host "Categoria creada: $($info.Nombre)" -ForegroundColor Cyan
        }

        # Marca (subcarpeta; si no hay, "Generica")
        $marca = if ($partes.Length -ge 3) { $partes[1] } else { "Generica" }
        if (-not $marcas.ContainsKey($marca)) {
            $nueva = Enviar "Post" "/api/marcas" @{ nombre = $marca; activo = $true }
            $marcas[$marca] = $nueva.id
            Write-Host "Marca creada: $marca" -ForegroundColor Cyan
        }

        # Producto
        $codigo = ($f.BaseName -split '-')[0]
        $sku = "SKU-$codigo"
        $existe = Lista "/api/productos?sku.equals=$sku"
        if ($existe.Count -gt 0) { $existentes++; continue }

        $n = 0
        if (-not [int]::TryParse($codigo, [ref]$n)) {
            $n = ($codigo.ToCharArray() | ForEach-Object { [int]$_ } | Measure-Object -Sum).Sum
        }
        $fraccion = ($n % 100) / 100.0
        $lista = [decimal]([math]::Round(($info.Min + ($info.Max - $info.Min) * $fraccion) / 10) * 10 - 1)
        $oferta = $null
        if ($n % 2 -eq 0) { $oferta = [decimal]([math]::Round($lista * 0.85 / 10) * 10 - 1) }

        $nombre = if ($marca -eq "Generica") { "$($info.Singular) $codigo" } else { "$($info.Singular) $marca $codigo" }

        $producto = Enviar "Post" "/api/productos" @{
            sku           = $sku
            nombre        = $nombre
            slug          = (Slug $nombre)
            modelo        = $codigo
            descripcion   = "Producto nuevo y sellado, con garantía del fabricante. Edita esta descripción más adelante."
            precioLista   = $lista
            precioOferta  = $oferta
            garantiaMeses = 12
            activo        = $true
            categoria     = @{ id = $categorias[$catSlug] }
            marca         = @{ id = $marcas[$marca] }
        }

        # Imagen principal
        $url = "/Image/Productos/" + ($partes -join "/")
        Enviar "Post" "/api/producto-imagens" @{ url = $url; orden = 1; producto = @{ id = $producto.id } } | Out-Null

        $creados++
        Write-Host "OK  $nombre  (S/ $lista)"
    }
    catch {
        $errores++
        $detalle = $_.Exception.Message
        if ($_.ErrorDetails -and $_.ErrorDetails.Message) { $detalle += " | " + $_.ErrorDetails.Message }
        Write-Host "ERROR en $rel : $detalle" -ForegroundColor Red
    }
}

Write-Host ""
Write-Host "Terminado. Creados: $creados | Ya existian: $existentes | Omitidos: $omitidos | Errores: $errores"
