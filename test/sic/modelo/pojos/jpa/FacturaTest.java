package sic.modelo.pojos.jpa;

import sic.modelo.FacturaVenta;
import sic.modelo.PagoFacturaCompra;
import sic.modelo.FormaDePago;
import sic.modelo.RenglonFactura;
import sic.modelo.FacturaCompra;
import sic.modelo.Transportista;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class FacturaTest {

    private static EntityManagerFactory emf;
    private static EntityManager em;
    private static EntityTransaction tx;

    @BeforeClass
    public static void initEntityManager() {
        emf = Persistence.createEntityManagerFactory("SIC-Test-PU");
        em = emf.createEntityManager();

    }

    @AfterClass
    public static void closeEntityManager() {
        em.close();
        emf.close();
    }

    @Before
    public void initTransaction() {
        tx = em.getTransaction();
    }

    public FacturaCompra guardarUnaFacturaCompra() {
        FacturaCompra factura = new FacturaCompra();
        factura.setFecha(new Date());
        factura.setTipoFactura('A');

        //Forma de Pago
        FormaDePago formaDePago = new FormaDePago();
        formaDePago.setNombre("Contado");

        factura.setFormaPago(formaDePago);

        //Transportista
        Transportista transportista = new Transportista();
        transportista.setNombre("Demonte");
        transportista.setDireccion("Calle 123");
        transportista.setTelefono("");
        transportista.setWeb("");

        factura.setTransportista(transportista);

        //Renglones
        RenglonFactura renglon1 = new RenglonFactura();
        renglon1.setCodigoItem("123");
        renglon1.setDescripcionItem("Producto de pruebas");
        renglon1.setMedidaItem("Unidad");
        //factura.addRenglonFactura(renglon1);

        RenglonFactura renglon2 = new RenglonFactura();
        renglon2.setCodigoItem("321");
        renglon2.setDescripcionItem("Descripcion de prueba");
        renglon2.setMedidaItem("Docena");
        factura.setObservaciones("");
        //factura.addRenglonFactura(renglon2);

        PagoFacturaCompra pago1 = new PagoFacturaCompra();
        pago1.setFecha(new Date());
        pago1.setNota("");
        pago1.setMonto(122.15);
        //factura.addPagoFacturaCompra(pago1);

        PagoFacturaCompra pago2 = new PagoFacturaCompra();
        pago2.setFecha(new Date());
        pago2.setNota("");
        pago2.setMonto(122.15);
        //factura.addPagoFacturaCompra(pago2);

        tx.begin();
        em.persist(factura);
        tx.commit();
        return factura;
    }

    public FacturaVenta guardarUnaFacturaVenta() {
        FacturaVenta factura = new FacturaVenta();
        factura.setFecha(new Date());
        factura.setTipoFactura('A');
        factura.setNumSerie(123L);
        factura.setNumFactura(3L);

        //Forma de Pago
        FormaDePago formaDePago = new FormaDePago();
        formaDePago.setNombre("Contado");
        factura.setFormaPago(formaDePago);

        //Transportista
        Transportista transportista = new Transportista();
        transportista.setNombre("Demonte");
        transportista.setDireccion("Calle 123");
        transportista.setTelefono("");
        transportista.setWeb("");

        factura.setTransportista(transportista);

        //Renglones
        RenglonFactura renglon1 = new RenglonFactura();
        renglon1.setCodigoItem("123");
        renglon1.setDescripcionItem("Producto de pruebas");
        renglon1.setMedidaItem("Unidad");
        //factura.addRenglonFactura(renglon1);

        RenglonFactura renglon2 = new RenglonFactura();
        renglon2.setCodigoItem("321");
        renglon2.setDescripcionItem("Descripcion de prueba");
        renglon2.setMedidaItem("Docena");
        factura.setObservaciones("");
        //factura.addRenglonFactura(renglon2);       

        tx.begin();
        em.persist(factura);
        tx.commit();
        return factura;
    }

    @Test
    public void ejecutarConsultaTopMasVendidos() {
        this.guardarUnaFacturaVenta();
        TypedQuery<Object[]> query = (TypedQuery<Object[]>) em.createNamedQuery("Factura.buscarTopProductosMasVendidosPorAnio");
        query.setParameter("anio", 2013);
        List<Object[]> resul = query.getResultList();
        assertNotNull(resul);
    }
}
