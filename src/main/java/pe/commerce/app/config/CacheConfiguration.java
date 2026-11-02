package pe.commerce.app.config;

import java.time.Duration;
import org.ehcache.config.builders.*;
import org.ehcache.jsr107.Eh107Configuration;
import org.hibernate.cache.jcache.ConfigSettings;
import org.springframework.boot.cache.autoconfigure.JCacheManagerCustomizer;
import org.springframework.boot.hibernate.autoconfigure.HibernatePropertiesCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tech.jhipster.config.JHipsterProperties;

@Configuration
@EnableCaching
public class CacheConfiguration {

    private final javax.cache.configuration.Configuration<Object, Object> jcacheConfiguration;

    public CacheConfiguration(JHipsterProperties jHipsterProperties) {
        var ehcache = jHipsterProperties.getCache().getEhcache();

        jcacheConfiguration = Eh107Configuration.fromEhcacheCacheConfiguration(
            CacheConfigurationBuilder.newCacheConfigurationBuilder(
                Object.class,
                Object.class,
                ResourcePoolsBuilder.heap(ehcache.getMaxEntries())
            )
                .withExpiry(ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofSeconds(ehcache.getTimeToLiveSeconds())))
                .build()
        );
    }

    @Bean
    public HibernatePropertiesCustomizer hibernatePropertiesCustomizer(javax.cache.CacheManager cacheManager) {
        return hibernateProperties -> hibernateProperties.put(ConfigSettings.CACHE_MANAGER, cacheManager);
    }

    @Bean
    public JCacheManagerCustomizer cacheManagerCustomizer() {
        return cm -> {
            createCache(cm, pe.commerce.app.repository.UserRepository.USERS_BY_LOGIN_CACHE);
            createCache(cm, pe.commerce.app.repository.UserRepository.USERS_BY_EMAIL_CACHE);
            createCache(cm, pe.commerce.app.domain.User.class.getName());
            createCache(cm, pe.commerce.app.domain.Authority.class.getName());
            createCache(cm, pe.commerce.app.domain.User.class.getName() + ".authorities");
            createCache(cm, pe.commerce.app.domain.Ubigeo.class.getName());
            createCache(cm, pe.commerce.app.domain.Persona.class.getName());
            createCache(cm, pe.commerce.app.domain.Persona.class.getName() + ".documentoses");
            createCache(cm, pe.commerce.app.domain.Persona.class.getName() + ".mediosContactos");
            createCache(cm, pe.commerce.app.domain.DocumentoIdentidad.class.getName());
            createCache(cm, pe.commerce.app.domain.MedioContacto.class.getName());
            createCache(cm, pe.commerce.app.domain.Cliente.class.getName());
            createCache(cm, pe.commerce.app.domain.Cliente.class.getName() + ".direccioneses");
            createCache(cm, pe.commerce.app.domain.Cliente.class.getName() + ".datosFacturacions");
            createCache(cm, pe.commerce.app.domain.Cliente.class.getName() + ".pedidoses");
            createCache(cm, pe.commerce.app.domain.Cliente.class.getName() + ".resenases");
            createCache(cm, pe.commerce.app.domain.Empleado.class.getName());
            createCache(cm, pe.commerce.app.domain.Direccion.class.getName());
            createCache(cm, pe.commerce.app.domain.DatoFacturacion.class.getName());
            createCache(cm, pe.commerce.app.domain.Categoria.class.getName());
            createCache(cm, pe.commerce.app.domain.Categoria.class.getName() + ".subcategoriases");
            createCache(cm, pe.commerce.app.domain.Categoria.class.getName() + ".productoses");
            createCache(cm, pe.commerce.app.domain.Marca.class.getName());
            createCache(cm, pe.commerce.app.domain.Marca.class.getName() + ".productoses");
            createCache(cm, pe.commerce.app.domain.Producto.class.getName());
            createCache(cm, pe.commerce.app.domain.Producto.class.getName() + ".imageneses");
            createCache(cm, pe.commerce.app.domain.Producto.class.getName() + ".inventarioses");
            createCache(cm, pe.commerce.app.domain.Producto.class.getName() + ".resenases");
            createCache(cm, pe.commerce.app.domain.ProductoImagen.class.getName());
            createCache(cm, pe.commerce.app.domain.Tienda.class.getName());
            createCache(cm, pe.commerce.app.domain.Tienda.class.getName() + ".inventarioses");
            createCache(cm, pe.commerce.app.domain.Tienda.class.getName() + ".empleadoses");
            createCache(cm, pe.commerce.app.domain.Inventario.class.getName());
            createCache(cm, pe.commerce.app.domain.Carrito.class.getName());
            createCache(cm, pe.commerce.app.domain.Carrito.class.getName() + ".itemses");
            createCache(cm, pe.commerce.app.domain.CarritoItem.class.getName());
            createCache(cm, pe.commerce.app.domain.Cupon.class.getName());
            createCache(cm, pe.commerce.app.domain.Pedido.class.getName());
            createCache(cm, pe.commerce.app.domain.Pedido.class.getName() + ".detalleses");
            createCache(cm, pe.commerce.app.domain.Pedido.class.getName() + ".historialEstadoses");
            createCache(cm, pe.commerce.app.domain.Pedido.class.getName() + ".pagoses");
            createCache(cm, pe.commerce.app.domain.Pedido.class.getName() + ".comprobanteses");
            createCache(cm, pe.commerce.app.domain.PedidoDetalle.class.getName());
            createCache(cm, pe.commerce.app.domain.PedidoEstadoHistorial.class.getName());
            createCache(cm, pe.commerce.app.domain.Pago.class.getName());
            createCache(cm, pe.commerce.app.domain.Envio.class.getName());
            createCache(cm, pe.commerce.app.domain.Comprobante.class.getName());
            createCache(cm, pe.commerce.app.domain.Resena.class.getName());
            // jhipster-needle-ehcache-add-entry
        };
    }

    private void createCache(javax.cache.CacheManager cm, String cacheName) {
        javax.cache.Cache<Object, Object> cache = cm.getCache(cacheName);
        if (cache != null) {
            cache.clear();
        } else {
            cm.createCache(cacheName, jcacheConfiguration);
        }
    }
}
