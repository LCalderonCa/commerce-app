package pe.commerce.app.tienda;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Cada minuto pasa a EXPIRADO los pedidos PENDIENTE_PAGO cuyo tiempo de pago vencio,
 * libera el stock reservado y devuelve los productos al carrito del cliente.
 */
@Component
public class ExpiracionPedidosJob {

    private static final Logger LOG = LoggerFactory.getLogger(ExpiracionPedidosJob.class);

    private final VentaService ventaService;

    public ExpiracionPedidosJob(VentaService ventaService) {
        this.ventaService = ventaService;
    }

    @Scheduled(fixedDelay = 60_000, initialDelay = 30_000)
    public void expirarPedidos() {
        int n = ventaService.expirarPendientes();
        if (n > 0) {
            LOG.info("Pedidos expirados por falta de pago: {}", n);
        }
    }
}
