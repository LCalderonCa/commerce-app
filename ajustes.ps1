# ajustes.ps1 (version 3) - reaplica los ajustes propios despues de regenerar con JHipster
# Uso: copiar en la raiz del proyecto y ejecutar  .\ajustes.ps1
# Se puede ejecutar varias veces (no duplica cambios).
# Configuracion del equipo: PostgreSQL en Docker (puerto 15432 en el host), API en 9999.
# Si antes se aplico la version para PostgreSQL local, la revierte.
Set-Location $PSScriptRoot
$ErrorActionPreference = "Stop"
$utf8 = New-Object System.Text.UTF8Encoding($false)
function Leer($p) { [IO.File]::ReadAllText((Resolve-Path $p).Path, $utf8) }
function Escribir($p, $t) { [IO.File]::WriteAllText((Resolve-Path $p).Path, $t, $utf8) }

# 1. PostgreSQL en Docker (15432), API en 9999 y sin datos de relleno (faker)
$f = "src\main\docker\postgresql.yml"
Escribir $f ((Leer $f) -replace '127\.0\.0\.1:5432:5432', '127.0.0.1:15432:5432')

$f = "src\main\resources\config\application.yml"
Escribir $f ((Leer $f) -replace '(compose:\s*\r?\n\s*enabled:) false', '$1 true')

$f = "src\main\resources\config\application-secret-samples.yml"
Escribir $f ((Leer $f) -replace '(?m)^([ \t]+password:)[^\r\n]*', '$1')

$f = "src\main\resources\config\application-dev.yml"
Escribir $f ((Leer $f) `
    -replace 'localhost:5432/', 'localhost:15432/' `
    -replace 'port: 8080', 'port: 9999' `
    -replace 'base-url: http://127\.0\.0\.1:8080', 'base-url: http://127.0.0.1:9999' `
    -replace 'contexts: dev, faker', 'contexts: dev')
Write-Host "1. Docker (15432), puerto 9999 y faker: OK"

# 2. Catalogo publico (lectura abierta, escritura solo ADMIN)
$f = "src\main\java\pe\commerce\app\config\SecurityConfiguration.java"
$t = Leer $f
if ($t -notmatch '/api/productos') {
    $rutas = '"/api/productos", "/api/productos/**", "/api/categorias", "/api/categorias/**", "/api/marcas", "/api/marcas/**", "/api/producto-imagens", "/api/producto-imagens/**"'
    $nuevo = "                    .requestMatchers(HttpMethod.GET, $rutas).permitAll()`n" +
             "                    .requestMatchers($rutas).hasAuthority(AuthoritiesConstants.ADMIN)`n"
    $t = $t -replace '(?m)^(\s*\.requestMatchers\("/api/\*\*"\)\.authenticated\(\))', ($nuevo + '$1')
}
if ($t -notmatch '/api/tienda/publico') {
    $t = $t -replace '(?m)^(\s*\.requestMatchers\("/api/\*\*"\)\.authenticated\(\))', ('                    .requestMatchers("/api/tienda/publico/**").permitAll()' + "`n" + '$1')
}
Escribir $f $t
Write-Host "2. Catalogo publico y API de tienda: OK"

# 3. Roles SUPERVISOR y OPERADOR
$f = "src\main\resources\config\liquibase\data\authority.csv"
$t = (Leer $f).TrimEnd()
foreach ($r in "ROLE_SUPERVISOR", "ROLE_OPERADOR") { if ($t -notmatch $r) { $t += "`n$r" } }
Escribir $f ($t + "`n")

$f = "src\main\java\pe\commerce\app\security\AuthoritiesConstants.java"
$t = Leer $f
if ($t -notmatch 'SUPERVISOR') {
    $t = $t -replace '(public static final String USER = "ROLE_USER";)',
        "`$1`n`n    public static final String SUPERVISOR = `"ROLE_SUPERVISOR`";`n`n    public static final String OPERADOR = `"ROLE_OPERADOR`";"
    Escribir $f $t
}
Write-Host "3. Roles: OK"

# 4. Auditoria automatica (createdBy, createdDate, lastModifiedBy, lastModifiedDate)
#    Usa el @EnableJpaAuditing que JHipster ya trae en DatabaseConfiguration.
$imports = "import org.springframework.data.annotation.CreatedBy;`n" +
           "import org.springframework.data.annotation.CreatedDate;`n" +
           "import org.springframework.data.annotation.LastModifiedBy;`n" +
           "import org.springframework.data.annotation.LastModifiedDate;`n" +
           "import org.springframework.data.jpa.domain.support.AuditingEntityListener;`n"
$n = 0
foreach ($e in Get-ChildItem "src\main\java\pe\commerce\app\domain\*.java") {
    $t = [IO.File]::ReadAllText($e.FullName, $utf8)
    if ($t -notmatch '@Column\(name = "created_by"' -or $t -match '@CreatedBy') { continue }
    $t = $t -replace '(?m)^(package [^;]+;\r?\n)', ('$1' + "`n" + $imports)
    $t = $t -replace '(?m)^@Entity\r?$', "@Entity`n@EntityListeners(AuditingEntityListener.class)"
    $t = $t -replace '@Column\(name = "created_by", length = 50\)', "@CreatedBy`n    @Column(name = `"created_by`", length = 50, updatable = false)"
    $t = $t -replace '@Column\(name = "created_date"\)', "@CreatedDate`n    @Column(name = `"created_date`", updatable = false)"
    $t = $t -replace '@Column\(name = "last_modified_by", length = 50\)', "@LastModifiedBy`n    @Column(name = `"last_modified_by`", length = 50)"
    $t = $t -replace '@Column\(name = "last_modified_date"\)', "@LastModifiedDate`n    @Column(name = `"last_modified_date`")"
    [IO.File]::WriteAllText($e.FullName, $t, $utf8)
    $n++
}
Write-Host "4. Auditoria: $n entidades actualizadas"

Write-Host "Ajustes aplicados"
