package sic.vista.swing.tpv;

import sic.modelo.ResultadosFactura;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.persistence.PersistenceException;
import javax.swing.*;
import javax.swing.table.TableModel;
import org.apache.log4j.Logger;
import sic.modelo.Cliente;
import sic.modelo.Empresa;
import sic.modelo.FormaDePago;
import sic.modelo.Producto;
import sic.modelo.RenglonFactura;
import sic.service.*;
import sic.util.RenderTabla;
import sic.vista.swing.GUI_LogIn;
import sic.vista.swing.ModeloTabla;
import sic.vista.swing.administracion.GUI_Principal;
import sic.vista.swing.administracion.GUI_SeleccionEmpresa;

public class GUI_PrincipalTPV extends JFrame {

    private Empresa empresa;
    private char tipoDeFactura;
    private Cliente cliente;
    private List<RenglonFactura> renglones;
    private ModeloTabla modeloTablaResultados;
    private final EmpresaService empresaService = new EmpresaService();
    private final ClienteService clienteService = new ClienteService();
    private final FormaDePagoService formaDePagoService = new FormaDePagoService();
    private final TransportistaService transportistaService = new TransportistaService();
    private final RenglonDeFacturaService renglonDeFacturaService = new RenglonDeFacturaService();
    private final ProductoService productoService = new ProductoService();
    private final FacturaService facturaService = new FacturaService();
    private final UsuarioService usuarioService = new UsuarioService();
    private final HotKeysHandler keyHandler = new HotKeysHandler();
    private static final Logger log = Logger.getLogger(GUI_PrincipalTPV.class.getPackage().getName());

    public GUI_PrincipalTPV() {
        this.initComponents();
        modeloTablaResultados = new ModeloTabla();
        renglones = new ArrayList<>();
        this.setSize(new Dimension(1050, 645));
        this.setLocationRelativeTo(null);
        this.setExtendedState(MAXIMIZED_BOTH);
        ImageIcon iconoVentana = new ImageIcon(GUI_PrincipalTPV.class.getResource("/sic/icons/SIC_24_square.png"));
        this.setIconImage(iconoVentana.getImage());
        ImageIcon iconoNoMarcado = new ImageIcon(getClass().getResource("/sic/icons/chkNoMarcado_16x16.png"));
        this.tbtn_marcarDesmarcar.setIcon(iconoNoMarcado);

        //listeners        
        cmb_TipoFactura.addKeyListener(keyHandler);
        btn_CambiarUserEmpresa.addKeyListener(keyHandler);
        btn_NuevoCliente.addKeyListener(keyHandler);
        btn_BuscarCliente.addKeyListener(keyHandler);
        btn_BuscarProductos.addKeyListener(keyHandler);
        btn_EliminarEntradaProducto.addKeyListener(keyHandler);
        tbl_Resultado.addKeyListener(keyHandler);
        txt_CodigoProducto.addKeyListener(keyHandler);
        btn_BuscarPorCodigoProducto.addKeyListener(keyHandler);
        txt_Recargo_porcentaje.addKeyListener(keyHandler);
        btn_Continuar.addKeyListener(keyHandler);
        tbtn_marcarDesmarcar.addKeyListener(keyHandler);
    }

    public char getTipoDeFactura() {
        return tipoDeFactura;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public JTextArea getTxta_Observaciones() {
        return txta_Observaciones;
    }

    public List<RenglonFactura> getRenglones() {
        return renglones;
    }

    public ResultadosFactura getResultadosFactura() {
        ResultadosFactura resultados = new ResultadosFactura();
        resultados.setSubTotal(Double.parseDouble(txt_Subtotal.getValue().toString()));
        resultados.setRecargo_porcentaje(Double.parseDouble(txt_Recargo_porcentaje.getValue().toString()));
        resultados.setRecargo_neto(Double.parseDouble(txt_Recargo_neto.getValue().toString()));
        resultados.setDescuento_porcentaje(0);
        resultados.setDescuento_neto(0);
        resultados.setSubTotal_neto(Double.parseDouble(txt_SubTotalNeto.getValue().toString()));
        resultados.setIva_105_neto(Double.parseDouble(txt_IVA105_neto.getValue().toString()));
        resultados.setIva_21_neto(Double.parseDouble(txt_IVA21_neto.getValue().toString()));
        resultados.setImpuestoInterno_neto(Double.parseDouble(txt_ImpInterno_neto.getValue().toString()));
        resultados.setTotal(Double.parseDouble(txt_Total.getValue().toString()));
        return resultados;
    }

    public ModeloTabla getModeloTabla() {
        return this.modeloTablaResultados;
    }

    private void prepararComponentes() {
        txt_Subtotal.setValue(new Double("0.0"));
        txt_Recargo_porcentaje.setValue(new Double("0.0"));
        txt_Recargo_neto.setValue(new Double("0.0"));
        txt_SubTotalNeto.setValue(new Double("0.0"));
        txt_IVA105_neto.setValue(new Double("0.0"));
        txt_IVA21_neto.setValue(new Double("0.0"));
        txt_ImpInterno_neto.setValue(new Double("0.0"));
        txt_Total.setValue(new Double("0.0"));
        if (!usuarioService.getUsuarioActivo().getUsuario().getPermisosAdministrador()) {
            btn_VolverAdministracion.setEnabled(false);
        }
    }

    private void llamarGUI_SeleccionEmpresa() {
        GUI_SeleccionEmpresa GUI_Empresas = new GUI_SeleccionEmpresa(this, true);
        GUI_Empresas.setVisible(true);
        if (empresaService.getEmpresaActiva().getEmpresa() == null) {
            System.exit(0);
        } else {
            empresa = empresaService.getEmpresaActiva().getEmpresa();
            this.setTitle("S.I.C. Punto de Venta "
                    + ResourceBundle.getBundle("Mensajes").getString("version")
                    + " - " + empresa.getNombre());
        }
    }

    private boolean existeClientePredeterminado() {
        Cliente clientePredeterminado = clienteService.getClientePredeterminado(empresa);
        if (clientePredeterminado == null) {
            String mensaje = "No posee ningun Cliente Predeterminado con la empresa seleccionada.\n"
                    + "Debe establecer uno como Predeterminado o dar de alta uno nuevo\n"
                    + "para poder utilizar el T.P.V. (Terminal Punto de Venta)";
            JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        } else {
            this.cargarCliente(clientePredeterminado);
            return true;
        }
    }

    private boolean existeFormaDePagoPredeterminada() {
        FormaDePago fp = formaDePagoService.getFormaDePagoPredeterminada(empresa);
        if (fp == null) {
            String mensaje = "No posee ninguna Forma de Pago Predeterminada con la empresa seleccionada.\n"
                    + "Debe establecer una como Predeterminada o dar de alta una nueva\n"
                    + "para poder utilizar el T.P.V. (Terminal Punto de Venta)";
            JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        } else {
            return true;
        }
    }

    private boolean existeTransportistaCargado() {
        if (transportistaService.getTransportistas(empresa).isEmpty()) {
            String mensaje = "No posee ningun Transportista cargado en la empresa seleccionada.\n"
                    + "Debe dar de alta al menos uno para poder utilizar el T.P.V. (Terminal Punto de Venta)";
            JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        } else {
            return true;
        }
    }

    private void cargarCliente(Cliente cliente) {
        this.cliente = cliente;
        txt_NombreCliente.setText(cliente.getRazonSocial());
        txt_DomicilioCliente.setText(cliente.getDireccion());
        txt_CondicionIVACliente.setText(cliente.getCondicionIVA().toString());
        txt_IDFiscalCliente.setText(cliente.getId_Fiscal());
    }

    private void setColumnas() {
        //nombres de columnas
        String[] encabezados = new String[8];
        encabezados[0] = " ";
        encabezados[1] = "Codigo";
        encabezados[2] = "Descripcion";
        encabezados[3] = "Unidad";
        encabezados[4] = "Cantidad";
        encabezados[5] = "P. Unitario";
        encabezados[6] = "% Descuento";
        encabezados[7] = "Importe";
        modeloTablaResultados.setColumnIdentifiers(encabezados);
        tbl_Resultado.setModel(modeloTablaResultados);

        //tipo de dato columnas
        Class[] tipos = new Class[modeloTablaResultados.getColumnCount()];
        tipos[0] = Boolean.class;
        tipos[1] = String.class;
        tipos[2] = String.class;
        tipos[3] = String.class;
        tipos[4] = Double.class;
        tipos[5] = Double.class;
        tipos[6] = Double.class;
        tipos[7] = Double.class;
        modeloTablaResultados.setClaseColumnas(tipos);
        tbl_Resultado.getTableHeader().setReorderingAllowed(false);
        tbl_Resultado.getTableHeader().setResizingAllowed(true);

        //render para los tipos de datos
        tbl_Resultado.setDefaultRenderer(Double.class, new RenderTabla());

        //tamanios de columnas
        tbl_Resultado.getColumnModel().getColumn(0).setPreferredWidth(25);
        tbl_Resultado.getColumnModel().getColumn(1).setPreferredWidth(170);
        tbl_Resultado.getColumnModel().getColumn(2).setPreferredWidth(580);
        tbl_Resultado.getColumnModel().getColumn(3).setPreferredWidth(120);
        tbl_Resultado.getColumnModel().getColumn(4).setPreferredWidth(120);
        tbl_Resultado.getColumnModel().getColumn(5).setPreferredWidth(120);
        tbl_Resultado.getColumnModel().getColumn(6).setPreferredWidth(120);
        tbl_Resultado.getColumnModel().getColumn(7).setPreferredWidth(120);
    }

    private boolean existeStockDisponible(double cantRequerida, Producto producto) {
        double disponibilidad;
        if (producto.isIlimitado() == false) {
            disponibilidad = producto.getCantidad();
            for (RenglonFactura renglon : renglones) {
                if (renglon.getDescripcionItem().equals(producto.getDescripcion())) {
                    disponibilidad -= renglon.getCantidad();
                }
            }
            return disponibilidad >= cantRequerida;
        } else {
            return true;
        }
    }

    private void agregarRenglon(RenglonFactura renglon) {
        boolean agregado = false;
        //busca entre los renglones al producto, aumenta la cantidad y recalcula el descuento        
        for (int i = 0; i < renglones.size(); i++) {
            if (renglones.get(i).getId_ProductoItem() == renglon.getId_ProductoItem()) {
                Producto producto = productoService.getProductoPorId(renglon.getId_ProductoItem());
                renglones.set(i, renglonDeFacturaService.calcularRenglon(tipoDeFactura, Movimiento.VENTA, renglones.get(i).getCantidad() + renglon.getCantidad(), producto, renglon.getDescuento_porcentaje()));
                agregado = true;
            }
        }

        //si no encuentra el producto entre los renglones, carga un nuevo renglon        
        if (agregado == false) {
            renglones.add(renglon);
        }

        //para que baje solo el scroll vertical
        Point p = new Point(0, tbl_Resultado.getHeight());
        sp_Resultado.getViewport().setViewPosition(p);
    }

    private void cargarRenglonesAlTable(boolean eliminar) {
        TableModel copiaDatosJTable = tbl_Resultado.getModel(); // para copiar los datos del JTable
        int cantidadDeFilas = copiaDatosJTable.getRowCount();
        modeloTablaResultados = new ModeloTabla();
        this.setColumnas();
        int indiceParaMarcar = 0;
        for (RenglonFactura renglon : renglones) {
            Object[] fila = new Object[8];
            try {
                if (cantidadDeFilas != 0 && (eliminar == false)) {
                    boolean marcado = (boolean) copiaDatosJTable.getValueAt(indiceParaMarcar, 0);
                    fila[0] = marcado;
                } else {
                    fila[0] = false;
                }
            } catch (ArrayIndexOutOfBoundsException renglonNuevo) {
                fila[0] = false;
            }

            indiceParaMarcar++;
            fila[1] = renglon.getCodigoItem();
            fila[2] = renglon.getDescripcionItem();
            fila[3] = renglon.getMedidaItem();
            fila[4] = renglon.getCantidad();
            fila[5] = renglon.getPrecioUnitario();
            fila[6] = renglon.getDescuento_porcentaje();
            fila[7] = renglon.getImporte();
            modeloTablaResultados.addRow(fila);
        }
        tbl_Resultado.setModel(modeloTablaResultados);
    }

    private void limpiarYRecargarComponentes() {
        renglones = new ArrayList<>();
        modeloTablaResultados = new ModeloTabla();
        this.setColumnas();
        txt_CodigoProducto.setText("");
        txta_Observaciones.setText("");
        txt_Recargo_porcentaje.setValue(0.0);
        this.calcularResultados();
    }

    private void buscarProductoConVentanaAuxiliar() {
        if (facturaService.validarCantidadMaximaDeRenglones(renglones.size())) {
            GUI_BuscarProductos GUI_buscarProducto = new GUI_BuscarProductos(this, true, renglones);
            GUI_buscarProducto.setVisible(true);
            if (GUI_buscarProducto.debeCargarRenglon()) {
                this.agregarRenglon(GUI_buscarProducto.getRenglon());
                this.cargarRenglonesAlTable(false);
                this.calcularResultados();
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    ResourceBundle.getBundle("Mensajes").getString("mensaje_maxima_cantidad_de_renglones"),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscarProductoPorCodigo() throws ServiceException {
        Producto producto = productoService.getProductoPorCodigo(
                txt_CodigoProducto.getText().trim(), empresaService.getEmpresaActiva().getEmpresa());
        if (producto == null) {
            JOptionPane.showMessageDialog(this, "No se encontró ningun producto con el codigo ingresado!", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            if (this.existeStockDisponible(1, producto)) {
                RenglonFactura renglon = renglonDeFacturaService.calcularRenglon(tipoDeFactura, Movimiento.VENTA, 1, producto, 0);
                this.agregarRenglon(renglon);
                this.cargarRenglonesAlTable(false);
                this.calcularResultados();
                txt_CodigoProducto.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "No existe disponibilidad para el producto buscado.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void validarComponentesDeResultados() {
        try {
            txt_Recargo_porcentaje.commitEdit();

        } catch (ParseException ex) {
            String msjError = "Se produjo un error analizando los campos.";
            log.error(msjError + " - " + ex.getMessage());
        }
    }

    private void calcularResultados() {
        double subTotal;
        double recargo_porcentaje;
        double recargo_neto;
        double subTotalNeto;
        double iva_105_neto;
        double iva_21_neto;
        double impInterno_neto;
        double total;
        this.validarComponentesDeResultados();
        //SubTotal  
        subTotal = facturaService.calcularSubTotal(renglones);
        txt_Subtotal.setValue(subTotal);

        //Recargo
        recargo_porcentaje = Double.parseDouble(txt_Recargo_porcentaje.getValue().toString());
        recargo_neto = facturaService.calcularRecargo_neto(subTotal, recargo_porcentaje);
        txt_Recargo_neto.setValue(recargo_neto);

        //SubTotal neto
        subTotalNeto = facturaService.calcularSubTotal_neto(subTotal, recargo_neto, 0);
        txt_SubTotalNeto.setValue(subTotalNeto);

        //iva 10,5% neto
        iva_105_neto = facturaService.calcularIva_neto(tipoDeFactura, 0, recargo_porcentaje, renglones, 10.5);
        txt_IVA105_neto.setValue(iva_105_neto);

        //IVA 21% neto
        iva_21_neto = facturaService.calcularIva_neto(tipoDeFactura, 0, recargo_porcentaje, renglones, 21.0);
        txt_IVA21_neto.setValue(iva_21_neto);

        //Imp Interno neto
        impInterno_neto = facturaService.calcularImpInterno_neto(tipoDeFactura, 0, recargo_porcentaje, renglones);
        txt_ImpInterno_neto.setValue(impInterno_neto);

        //total
        total = facturaService.calcularTotal(subTotal, 0, recargo_neto, iva_105_neto, iva_21_neto, impInterno_neto);
        txt_Total.setValue(total);
    }

    private boolean cerrarVentana() {
        int respuesta = JOptionPane.showConfirmDialog(this,
                "¿Esta seguro que desea salir?\nSe perderá la Venta que esta realizando.",
                "Salir", JOptionPane.YES_NO_OPTION);
        return respuesta == JOptionPane.YES_OPTION;
    }

    private void cargarTiposDeFacturaDisponibles() {
        char[] tiposFactura = facturaService.getTipoFacturaVenta(empresaService.getEmpresaActiva().getEmpresa(), cliente);
        cmb_TipoFactura.removeAllItems();
        for (int i = 0; tiposFactura.length > i; i++) {
            cmb_TipoFactura.addItem(tiposFactura[i]);
        }
    }

    private void recargarRenglonesSegunTipoDeFactura() {
        //resguardo de renglones
        List<RenglonFactura> resguardoRenglones = renglones;
        renglones = new ArrayList<>();
        for (RenglonFactura renglonFactura : resguardoRenglones) {
            Producto producto = productoService.getProductoPorId(renglonFactura.getId_ProductoItem());
            RenglonFactura renglon = renglonDeFacturaService.calcularRenglon(tipoDeFactura, Movimiento.VENTA, renglonFactura.getCantidad(), producto, renglonFactura.getDescuento_porcentaje());
            this.agregarRenglon(renglon);
        }
        this.cargarRenglonesAlTable(false);
        this.calcularResultados();
    }

    /**
     * Clase interna para manejar las hotkeys del TPV
     */
    class HotKeysHandler extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent evt) {
            if (evt.getKeyCode() == KeyEvent.VK_F5) {
                btn_NuevoClienteActionPerformed(null);
            }

            if (evt.getKeyCode() == KeyEvent.VK_F2) {
                btn_BuscarClienteActionPerformed(null);
            }

            if (evt.getKeyCode() == KeyEvent.VK_F4) {
                btn_BuscarProductosActionPerformed(null);
            }

            if (evt.getKeyCode() == KeyEvent.VK_F9) {
                btn_ContinuarActionPerformed(null);
            }

            if (evt.getSource() == tbl_Resultado && evt.getKeyCode() == 127) {
                btn_EliminarEntradaProductoActionPerformed(null);
            }

            if (evt.getSource() == tbl_Resultado && evt.getKeyCode() == KeyEvent.VK_TAB) {
                txt_CodigoProducto.requestFocus();
            }
        }
    };

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelGeneral = new javax.swing.JPanel();
        panelCliente = new javax.swing.JPanel();
        lbl_NombreCliente = new javax.swing.JLabel();
        lbl_DomicilioCliente = new javax.swing.JLabel();
        lbl_IDFiscalCliente = new javax.swing.JLabel();
        lbl_CondicionIVACliente = new javax.swing.JLabel();
        txt_CondicionIVACliente = new javax.swing.JTextField();
        txt_IDFiscalCliente = new javax.swing.JTextField();
        txt_DomicilioCliente = new javax.swing.JTextField();
        txt_NombreCliente = new javax.swing.JTextField();
        btn_BuscarCliente = new javax.swing.JButton();
        btn_NuevoCliente = new javax.swing.JButton();
        panelEncabezado = new javax.swing.JPanel();
        lbl_TipoFactura = new javax.swing.JLabel();
        cmb_TipoFactura = new javax.swing.JComboBox();
        btn_CambiarUserEmpresa = new javax.swing.JButton();
        btn_VolverAdministracion = new javax.swing.JButton();
        panelRenglones = new javax.swing.JPanel();
        sp_Resultado = new javax.swing.JScrollPane();
        tbl_Resultado = new javax.swing.JTable();
        btn_BuscarProductos = new javax.swing.JButton();
        btn_EliminarEntradaProducto = new javax.swing.JButton();
        txt_CodigoProducto = new javax.swing.JTextField();
        btn_BuscarPorCodigoProducto = new javax.swing.JButton();
        tbtn_marcarDesmarcar = new javax.swing.JToggleButton();
        panelObservaciones = new javax.swing.JPanel();
        lbl_Observaciones = new javax.swing.JLabel();
        btn_AddComment = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        txta_Observaciones = new javax.swing.JTextArea();
        panelResultados = new javax.swing.JPanel();
        lbl_SubTotal = new javax.swing.JLabel();
        txt_Subtotal = new javax.swing.JFormattedTextField();
        lbl_IVA21 = new javax.swing.JLabel();
        txt_IVA21_neto = new javax.swing.JFormattedTextField();
        lbl_Total = new javax.swing.JLabel();
        txt_Total = new javax.swing.JFormattedTextField();
        txt_Recargo_porcentaje = new javax.swing.JFormattedTextField();
        txt_Recargo_neto = new javax.swing.JFormattedTextField();
        lbl_DescuentoRecargo = new javax.swing.JLabel();
        txt_ImpInterno_neto = new javax.swing.JFormattedTextField();
        lbl_ImpInterno = new javax.swing.JLabel();
        txt_SubTotalNeto = new javax.swing.JFormattedTextField();
        lbl_SubTotalNeto = new javax.swing.JLabel();
        txt_IVA105_neto = new javax.swing.JFormattedTextField();
        lbl_IVA105 = new javax.swing.JLabel();
        lbl_105 = new javax.swing.JLabel();
        lbl_21 = new javax.swing.JLabel();
        btn_Continuar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("S.I.C. Punto de Venta");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        panelGeneral.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lbl_NombreCliente.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbl_NombreCliente.setText("Nombre:");

        lbl_DomicilioCliente.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbl_DomicilioCliente.setText("Domicilio:");

        lbl_IDFiscalCliente.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbl_IDFiscalCliente.setText("ID Fiscal:");

        lbl_CondicionIVACliente.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbl_CondicionIVACliente.setText("Condición IVA:");

        txt_CondicionIVACliente.setEditable(false);
        txt_CondicionIVACliente.setFocusable(false);

        txt_IDFiscalCliente.setEditable(false);
        txt_IDFiscalCliente.setFocusable(false);

        txt_DomicilioCliente.setEditable(false);
        txt_DomicilioCliente.setFocusable(false);

        txt_NombreCliente.setEditable(false);
        txt_NombreCliente.setFocusable(false);

        btn_BuscarCliente.setForeground(java.awt.Color.blue);
        btn_BuscarCliente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sic/icons/Client_16x16.png"))); // NOI18N
        btn_BuscarCliente.setText("Buscar Cliente (F2)");
        btn_BuscarCliente.setFocusable(false);
        btn_BuscarCliente.setPreferredSize(new java.awt.Dimension(200, 30));
        btn_BuscarCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_BuscarClienteActionPerformed(evt);
            }
        });

        btn_NuevoCliente.setForeground(java.awt.Color.blue);
        btn_NuevoCliente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sic/icons/AddClient_16x16.png"))); // NOI18N
        btn_NuevoCliente.setText("Nuevo Cliente (F5)");
        btn_NuevoCliente.setFocusable(false);
        btn_NuevoCliente.setPreferredSize(new java.awt.Dimension(200, 30));
        btn_NuevoCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_NuevoClienteActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelClienteLayout = new javax.swing.GroupLayout(panelCliente);
        panelCliente.setLayout(panelClienteLayout);
        panelClienteLayout.setHorizontalGroup(
            panelClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelClienteLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelClienteLayout.createSequentialGroup()
                        .addComponent(btn_NuevoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(btn_BuscarCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelClienteLayout.createSequentialGroup()
                        .addGroup(panelClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lbl_NombreCliente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lbl_DomicilioCliente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lbl_CondicionIVACliente))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_DomicilioCliente, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txt_NombreCliente)
                            .addGroup(panelClienteLayout.createSequentialGroup()
                                .addComponent(txt_CondicionIVACliente, javax.swing.GroupLayout.PREFERRED_SIZE, 357, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lbl_IDFiscalCliente)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txt_IDFiscalCliente)))))
                .addContainerGap())
        );
        panelClienteLayout.setVerticalGroup(
            panelClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelClienteLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_BuscarCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_NuevoCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_NombreCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_NombreCliente))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_DomicilioCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_DomicilioCliente))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_CondicionIVACliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_CondicionIVACliente)
                    .addComponent(txt_IDFiscalCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_IDFiscalCliente))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        lbl_TipoFactura.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        lbl_TipoFactura.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lbl_TipoFactura.setText("Tipo de Factura:");

        cmb_TipoFactura.setFont(new java.awt.Font("Ubuntu", 1, 15)); // NOI18N
        cmb_TipoFactura.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "A", "B", "X" }));
        cmb_TipoFactura.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmb_TipoFacturaItemStateChanged(evt);
            }
        });
        cmb_TipoFactura.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmb_TipoFacturaActionPerformed(evt);
            }
        });

        btn_CambiarUserEmpresa.setForeground(java.awt.Color.blue);
        btn_CambiarUserEmpresa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sic/icons/GroupArrow_16x16.png"))); // NOI18N
        btn_CambiarUserEmpresa.setToolTipText("Cambiar usuario");
        btn_CambiarUserEmpresa.setFocusable(false);
        btn_CambiarUserEmpresa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_CambiarUserEmpresaActionPerformed(evt);
            }
        });

        btn_VolverAdministracion.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sic/icons/Administracion_16x16.png"))); // NOI18N
        btn_VolverAdministracion.setToolTipText("Ir al administrador");
        btn_VolverAdministracion.setFocusable(false);
        btn_VolverAdministracion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_VolverAdministracionActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelEncabezadoLayout = new javax.swing.GroupLayout(panelEncabezado);
        panelEncabezado.setLayout(panelEncabezadoLayout);
        panelEncabezadoLayout.setHorizontalGroup(
            panelEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelEncabezadoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbl_TipoFactura)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmb_TipoFactura, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btn_VolverAdministracion)
                .addGap(0, 0, 0)
                .addComponent(btn_CambiarUserEmpresa))
        );
        panelEncabezadoLayout.setVerticalGroup(
            panelEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelEncabezadoLayout.createSequentialGroup()
                .addGroup(panelEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelEncabezadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lbl_TipoFactura)
                        .addComponent(cmb_TipoFactura, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btn_VolverAdministracion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btn_CambiarUserEmpresa, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        panelEncabezadoLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btn_CambiarUserEmpresa, btn_VolverAdministracion});

        panelRenglones.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        tbl_Resultado.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tbl_Resultado.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tbl_Resultado.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                tbl_ResultadoFocusGained(evt);
            }
        });
        tbl_Resultado.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbl_ResultadoMouseClicked(evt);
            }
        });
        sp_Resultado.setViewportView(tbl_Resultado);

        btn_BuscarProductos.setForeground(java.awt.Color.blue);
        btn_BuscarProductos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sic/icons/Product_16x16.png"))); // NOI18N
        btn_BuscarProductos.setText("Buscar Producto (F4)");
        btn_BuscarProductos.setFocusable(false);
        btn_BuscarProductos.setPreferredSize(new java.awt.Dimension(200, 30));
        btn_BuscarProductos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_BuscarProductosActionPerformed(evt);
            }
        });

        btn_EliminarEntradaProducto.setForeground(java.awt.Color.blue);
        btn_EliminarEntradaProducto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sic/icons/DeleteProduct_16x16.png"))); // NOI18N
        btn_EliminarEntradaProducto.setText("Eliminar Producto (DEL)");
        btn_EliminarEntradaProducto.setFocusable(false);
        btn_EliminarEntradaProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_EliminarEntradaProductoActionPerformed(evt);
            }
        });

        txt_CodigoProducto.setFont(new java.awt.Font("DejaVu Sans", 0, 15)); // NOI18N
        txt_CodigoProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_CodigoProductoActionPerformed(evt);
            }
        });

        btn_BuscarPorCodigoProducto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sic/icons/16x16.png"))); // NOI18N
        btn_BuscarPorCodigoProducto.setFocusable(false);
        btn_BuscarPorCodigoProducto.setPreferredSize(new java.awt.Dimension(34, 28));
        btn_BuscarPorCodigoProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_BuscarPorCodigoProductoActionPerformed(evt);
            }
        });

        tbtn_marcarDesmarcar.setFocusable(false);
        tbtn_marcarDesmarcar.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                tbtn_marcarDesmarcarStateChanged(evt);
            }
        });

        javax.swing.GroupLayout panelRenglonesLayout = new javax.swing.GroupLayout(panelRenglones);
        panelRenglones.setLayout(panelRenglonesLayout);
        panelRenglonesLayout.setHorizontalGroup(
            panelRenglonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRenglonesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelRenglonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(sp_Resultado)
                    .addGroup(panelRenglonesLayout.createSequentialGroup()
                        .addComponent(tbtn_marcarDesmarcar, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txt_CodigoProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(btn_BuscarPorCodigoProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btn_BuscarProductos, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(btn_EliminarEntradaProducto)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        panelRenglonesLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btn_BuscarProductos, btn_EliminarEntradaProducto});

        panelRenglonesLayout.setVerticalGroup(
            panelRenglonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelRenglonesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelRenglonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(txt_CodigoProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_BuscarPorCodigoProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelRenglonesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btn_BuscarProductos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btn_EliminarEntradaProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(tbtn_marcarDesmarcar, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(sp_Resultado, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                .addContainerGap())
        );

        panelRenglonesLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btn_BuscarPorCodigoProducto, txt_CodigoProducto});

        lbl_Observaciones.setText("Observaciones:");

        btn_AddComment.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sic/icons/Comment_16x16.png"))); // NOI18N
        btn_AddComment.setFocusable(false);
        btn_AddComment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_AddCommentActionPerformed(evt);
            }
        });

        txta_Observaciones.setBackground(new java.awt.Color(220, 215, 215));
        txta_Observaciones.setColumns(20);
        txta_Observaciones.setEditable(false);
        txta_Observaciones.setRows(5);
        txta_Observaciones.setFocusable(false);
        jScrollPane1.setViewportView(txta_Observaciones);

        javax.swing.GroupLayout panelObservacionesLayout = new javax.swing.GroupLayout(panelObservaciones);
        panelObservaciones.setLayout(panelObservacionesLayout);
        panelObservacionesLayout.setHorizontalGroup(
            panelObservacionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelObservacionesLayout.createSequentialGroup()
                .addGroup(panelObservacionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelObservacionesLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lbl_Observaciones))
                    .addGroup(panelObservacionesLayout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 469, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btn_AddComment)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelObservacionesLayout.setVerticalGroup(
            panelObservacionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelObservacionesLayout.createSequentialGroup()
                .addComponent(lbl_Observaciones)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelObservacionesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btn_AddComment)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        lbl_SubTotal.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lbl_SubTotal.setText("SubTotal");

        txt_Subtotal.setEditable(false);
        txt_Subtotal.setForeground(new java.awt.Color(29, 156, 37));
        txt_Subtotal.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("$##,###,##0.00"))));
        txt_Subtotal.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_Subtotal.setFocusable(false);
        txt_Subtotal.setFont(new java.awt.Font("DejaVu Sans", 0, 17)); // NOI18N

        lbl_IVA21.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lbl_IVA21.setText("I.V.A.");

        txt_IVA21_neto.setEditable(false);
        txt_IVA21_neto.setForeground(new java.awt.Color(29, 156, 37));
        txt_IVA21_neto.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("$##,###,##0.00"))));
        txt_IVA21_neto.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_IVA21_neto.setFocusable(false);
        txt_IVA21_neto.setFont(new java.awt.Font("DejaVu Sans", 0, 17)); // NOI18N

        lbl_Total.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lbl_Total.setText("TOTAL");

        txt_Total.setEditable(false);
        txt_Total.setForeground(new java.awt.Color(29, 156, 37));
        txt_Total.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("$##,###,##0.00"))));
        txt_Total.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_Total.setFocusable(false);
        txt_Total.setFont(new java.awt.Font("DejaVu Sans", 1, 36)); // NOI18N

        txt_Recargo_porcentaje.setForeground(new java.awt.Color(29, 156, 37));
        txt_Recargo_porcentaje.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("##,###,##0.00"))));
        txt_Recargo_porcentaje.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_Recargo_porcentaje.setFont(new java.awt.Font("DejaVu Sans", 0, 17)); // NOI18N
        txt_Recargo_porcentaje.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txt_Recargo_porcentajeFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txt_Recargo_porcentajeFocusLost(evt);
            }
        });
        txt_Recargo_porcentaje.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_Recargo_porcentajeActionPerformed(evt);
            }
        });
        txt_Recargo_porcentaje.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_Recargo_porcentajeKeyTyped(evt);
            }
        });

        txt_Recargo_neto.setEditable(false);
        txt_Recargo_neto.setForeground(new java.awt.Color(29, 156, 37));
        txt_Recargo_neto.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("$##,###,##0.00"))));
        txt_Recargo_neto.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_Recargo_neto.setFocusable(false);
        txt_Recargo_neto.setFont(new java.awt.Font("DejaVu Sans", 0, 17)); // NOI18N

        lbl_DescuentoRecargo.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lbl_DescuentoRecargo.setText("Recargo (%)");

        txt_ImpInterno_neto.setEditable(false);
        txt_ImpInterno_neto.setForeground(new java.awt.Color(29, 156, 37));
        txt_ImpInterno_neto.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("$##,###,##0.00"))));
        txt_ImpInterno_neto.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_ImpInterno_neto.setFocusable(false);
        txt_ImpInterno_neto.setFont(new java.awt.Font("DejaVu Sans", 0, 17)); // NOI18N

        lbl_ImpInterno.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lbl_ImpInterno.setText("Imp. Interno");

        txt_SubTotalNeto.setEditable(false);
        txt_SubTotalNeto.setForeground(new java.awt.Color(29, 156, 37));
        txt_SubTotalNeto.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("$##,###,##0.00"))));
        txt_SubTotalNeto.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_SubTotalNeto.setFocusable(false);
        txt_SubTotalNeto.setFont(new java.awt.Font("DejaVu Sans", 0, 17)); // NOI18N

        lbl_SubTotalNeto.setText("SubTotal Neto");

        txt_IVA105_neto.setEditable(false);
        txt_IVA105_neto.setForeground(new java.awt.Color(29, 156, 37));
        txt_IVA105_neto.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("$##,###,##0.00"))));
        txt_IVA105_neto.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        txt_IVA105_neto.setFocusable(false);
        txt_IVA105_neto.setFont(new java.awt.Font("DejaVu Sans", 0, 17)); // NOI18N

        lbl_IVA105.setText("I.V.A.");

        lbl_105.setText("10.5 %");

        lbl_21.setText("21 %");

        javax.swing.GroupLayout panelResultadosLayout = new javax.swing.GroupLayout(panelResultados);
        panelResultados.setLayout(panelResultadosLayout);
        panelResultadosLayout.setHorizontalGroup(
            panelResultadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelResultadosLayout.createSequentialGroup()
                .addGroup(panelResultadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lbl_SubTotal, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                    .addComponent(txt_Subtotal))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelResultadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txt_Recargo_neto)
                    .addComponent(txt_Recargo_porcentaje)
                    .addComponent(lbl_DescuentoRecargo, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelResultadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txt_SubTotalNeto)
                    .addComponent(lbl_SubTotalNeto, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelResultadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lbl_105, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                    .addComponent(lbl_IVA105, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txt_IVA105_neto))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelResultadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(panelResultadosLayout.createSequentialGroup()
                        .addGroup(panelResultadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txt_IVA21_neto)
                            .addComponent(lbl_IVA21, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelResultadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txt_ImpInterno_neto)
                            .addComponent(lbl_ImpInterno, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)))
                    .addComponent(lbl_21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelResultadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txt_Total)
                    .addComponent(lbl_Total, javax.swing.GroupLayout.DEFAULT_SIZE, 241, Short.MAX_VALUE)))
        );
        panelResultadosLayout.setVerticalGroup(
            panelResultadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(panelResultadosLayout.createSequentialGroup()
                .addGroup(panelResultadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_IVA21)
                    .addComponent(lbl_ImpInterno))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbl_21, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelResultadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txt_ImpInterno_neto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txt_IVA21_neto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGroup(panelResultadosLayout.createSequentialGroup()
                .addGroup(panelResultadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_DescuentoRecargo)
                    .addComponent(lbl_SubTotal)
                    .addComponent(lbl_Total)
                    .addComponent(lbl_SubTotalNeto)
                    .addComponent(lbl_IVA105))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelResultadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelResultadosLayout.createSequentialGroup()
                        .addComponent(lbl_105, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(panelResultadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txt_SubTotalNeto, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_IVA105_neto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(panelResultadosLayout.createSequentialGroup()
                        .addComponent(txt_Recargo_porcentaje, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelResultadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txt_Subtotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txt_Recargo_neto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(txt_Total)))
        );

        panelResultadosLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {lbl_105, lbl_21, txt_IVA105_neto, txt_IVA21_neto, txt_ImpInterno_neto, txt_Recargo_neto, txt_Recargo_porcentaje, txt_SubTotalNeto, txt_Subtotal});

        btn_Continuar.setForeground(java.awt.Color.blue);
        btn_Continuar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sic/icons/22x22_FlechaGO.png"))); // NOI18N
        btn_Continuar.setText("Continuar (F9)");
        btn_Continuar.setFocusable(false);
        btn_Continuar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_ContinuarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelGeneralLayout = new javax.swing.GroupLayout(panelGeneral);
        panelGeneral.setLayout(panelGeneralLayout);
        panelGeneralLayout.setHorizontalGroup(
            panelGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelGeneralLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelCliente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelEncabezado, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelRenglones, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(panelGeneralLayout.createSequentialGroup()
                        .addGroup(panelGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(panelGeneralLayout.createSequentialGroup()
                                .addComponent(panelObservaciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btn_Continuar))
                            .addComponent(panelResultados, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panelGeneralLayout.setVerticalGroup(
            panelGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelGeneralLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelEncabezado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelRenglones, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelObservaciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btn_Continuar, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelResultados, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelGeneral, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelGeneral, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btn_BuscarClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_BuscarClienteActionPerformed
        GUI_BuscarClientes GUI_buscarCliente = new GUI_BuscarClientes(this, true);
        GUI_buscarCliente.setVisible(true);
        try {
            if (GUI_buscarCliente.getCliSeleccionado() != null) {
                this.cargarCliente(GUI_buscarCliente.getCliSeleccionado());
                this.cargarTiposDeFacturaDisponibles();
            }

        } catch (PersistenceException ex) {
            log.error(ResourceBundle.getBundle("Mensajes").getString("mensaje_error_acceso_a_datos") + " - " + ex.getMessage());
            JOptionPane.showMessageDialog(this, ResourceBundle.getBundle("Mensajes").getString("mensaje_error_acceso_a_datos"), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btn_BuscarClienteActionPerformed

    private void btn_NuevoClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_NuevoClienteActionPerformed
        GUI_NuevoCliente GUI_AltaCliente = new GUI_NuevoCliente(this, true);
        GUI_AltaCliente.setVisible(true);
        try {
            if (GUI_AltaCliente.getClienteDadoDeAlta() != null) {
                this.cargarCliente(GUI_AltaCliente.getClienteDadoDeAlta());
                this.cargarTiposDeFacturaDisponibles();
            }

        } catch (PersistenceException ex) {
            log.error(ResourceBundle.getBundle("Mensajes").getString("mensaje_error_acceso_a_datos") + " - " + ex.getMessage());
            JOptionPane.showMessageDialog(this, ResourceBundle.getBundle("Mensajes").getString("mensaje_error_acceso_a_datos"), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btn_NuevoClienteActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        try {
            btn_CambiarUserEmpresa.setText(usuarioService.getUsuarioActivo().getUsuario().getNombre());
            this.setColumnas();
            this.prepararComponentes();
            this.llamarGUI_SeleccionEmpresa();
            //verifica que exista un Cliente predeterminado, una Forma de Pago y un Transportista
            if (this.existeClientePredeterminado() && this.existeFormaDePagoPredeterminada() && this.existeTransportistaCargado()) {
                this.cargarTiposDeFacturaDisponibles();
            } else {
                this.dispose();
                new GUI_LogIn().setVisible(true);
            }

        } catch (PersistenceException ex) {
            log.error(ResourceBundle.getBundle("Mensajes").getString("mensaje_error_acceso_a_datos") + " - " + ex.getMessage());
            JOptionPane.showMessageDialog(this, ResourceBundle.getBundle("Mensajes").getString("mensaje_error_acceso_a_datos"), "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }//GEN-LAST:event_formWindowOpened

    private void btn_CambiarUserEmpresaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_CambiarUserEmpresaActionPerformed
        if (this.cerrarVentana()) {
            this.dispose();
            new GUI_LogIn().setVisible(true);
        }
    }//GEN-LAST:event_btn_CambiarUserEmpresaActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        if (this.cerrarVentana()) {
            System.exit(0);
        }
    }//GEN-LAST:event_formWindowClosing

    private void cmb_TipoFacturaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmb_TipoFacturaItemStateChanged
//para evitar que pase null cuando esta recargando el comboBox
        try {
            if (cmb_TipoFactura.getSelectedItem() != null) {
                tipoDeFactura = cmb_TipoFactura.getSelectedItem().toString().charAt(0);
                this.recargarRenglonesSegunTipoDeFactura();
            }

        } catch (PersistenceException ex) {
            log.error(ResourceBundle.getBundle("Mensajes").getString("mensaje_error_acceso_a_datos") + " - " + ex.getMessage());
            JOptionPane.showMessageDialog(this, ResourceBundle.getBundle("Mensajes").getString("mensaje_error_acceso_a_datos"), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_cmb_TipoFacturaItemStateChanged

    private void btn_VolverAdministracionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_VolverAdministracionActionPerformed
        if (this.cerrarVentana()) {
            this.dispose();
            new GUI_Principal().setVisible(true);
        }
    }//GEN-LAST:event_btn_VolverAdministracionActionPerformed

    private void btn_BuscarPorCodigoProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_BuscarPorCodigoProductoActionPerformed
        try {
            this.buscarProductoPorCodigo();

        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

        } catch (PersistenceException ex) {
            log.error(ResourceBundle.getBundle("Mensajes").getString("mensaje_error_acceso_a_datos") + " - " + ex.getMessage());
            JOptionPane.showMessageDialog(this, ResourceBundle.getBundle("Mensajes").getString("mensaje_error_acceso_a_datos"), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btn_BuscarPorCodigoProductoActionPerformed

    private void txt_CodigoProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_CodigoProductoActionPerformed
        try {
            this.buscarProductoPorCodigo();

        } catch (ServiceException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);

        } catch (PersistenceException ex) {
            log.error(ResourceBundle.getBundle("Mensajes").getString("mensaje_error_acceso_a_datos") + " - " + ex.getMessage());
            JOptionPane.showMessageDialog(this, ResourceBundle.getBundle("Mensajes").getString("mensaje_error_acceso_a_datos"), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_txt_CodigoProductoActionPerformed

    private void btn_AddCommentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_AddCommentActionPerformed
        GUI_Observaciones GUI_Observaciones = new GUI_Observaciones(this, true, txta_Observaciones.getText());
        GUI_Observaciones.setVisible(true);
        txta_Observaciones.setText(GUI_Observaciones.getTxta_Observaciones().getText());
    }//GEN-LAST:event_btn_AddCommentActionPerformed

    private void txt_Recargo_porcentajeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_Recargo_porcentajeFocusLost
        this.calcularResultados();
    }//GEN-LAST:event_txt_Recargo_porcentajeFocusLost

    private void txt_Recargo_porcentajeFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txt_Recargo_porcentajeFocusGained
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                txt_Recargo_porcentaje.selectAll();
            }
        });
    }//GEN-LAST:event_txt_Recargo_porcentajeFocusGained

    private void txt_Recargo_porcentajeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_Recargo_porcentajeActionPerformed
        this.calcularResultados();
    }//GEN-LAST:event_txt_Recargo_porcentajeActionPerformed

    private void btn_ContinuarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ContinuarActionPerformed
        GUI_CerrarVenta gui_CerrarVenta = new GUI_CerrarVenta(this, true);
        gui_CerrarVenta.setVisible(true);
        if (gui_CerrarVenta.isExito()) {
            this.limpiarYRecargarComponentes();
        }
    }//GEN-LAST:event_btn_ContinuarActionPerformed

    private void btn_EliminarEntradaProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_EliminarEntradaProductoActionPerformed
        int[] indicesParaEliminar = new int[tbl_Resultado.getRowCount()];
        int punteroIndices = 0;
        int cantidadAEliminar = 0;
        for (int i = 0; i < tbl_Resultado.getRowCount(); i++) {
            if ((boolean) tbl_Resultado.getValueAt(i, 0)) {
                indicesParaEliminar[punteroIndices] = i;
                punteroIndices++;
                cantidadAEliminar++;
            }
        }
        List<RenglonFactura> paraBorrar = new ArrayList<>();
        for (int i = 0; i < cantidadAEliminar; i++) {
            paraBorrar.add(renglones.get(indicesParaEliminar[i]));
        }
        for (RenglonFactura renglon : paraBorrar) {
            renglones.remove(renglon);
        }
        this.cargarRenglonesAlTable(true);
        this.calcularResultados();

    }//GEN-LAST:event_btn_EliminarEntradaProductoActionPerformed

    private void btn_BuscarProductosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_BuscarProductosActionPerformed
        try {
            this.buscarProductoConVentanaAuxiliar();

        } catch (PersistenceException ex) {
            log.error(ResourceBundle.getBundle("Mensajes").getString("mensaje_error_acceso_a_datos") + " - " + ex.getMessage());
            JOptionPane.showMessageDialog(this, ResourceBundle.getBundle("Mensajes").getString("mensaje_error_acceso_a_datos"), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btn_BuscarProductosActionPerformed

    private void tbl_ResultadoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tbl_ResultadoFocusGained
        //Si no hay nada seleccionado y NO esta vacio el table, selecciona la primer fila
        if ((tbl_Resultado.getSelectedRow() == -1) && (tbl_Resultado.getRowCount() != 0)) {
            tbl_Resultado.setRowSelectionInterval(0, 0);
        }
    }//GEN-LAST:event_tbl_ResultadoFocusGained

    private void txt_Recargo_porcentajeKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_Recargo_porcentajeKeyTyped
        char c = evt.getKeyChar();
        if ((c < '0' || c > '9') && (c != ',') && (c != '.')) {
            evt.consume();
        }
    }//GEN-LAST:event_txt_Recargo_porcentajeKeyTyped

    private void cmb_TipoFacturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmb_TipoFacturaActionPerformed
        for (int i = 0; i < tbl_Resultado.getRowCount(); i++) {
            tbl_Resultado.setValueAt((boolean) tbl_Resultado.getValueAt(i, 0), i, 0);
        }
    }//GEN-LAST:event_cmb_TipoFacturaActionPerformed

    private void tbl_ResultadoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbl_ResultadoMouseClicked
        int fila = tbl_Resultado.getSelectedRow();
        int columna = tbl_Resultado.getSelectedColumn();
        if (columna == 0) {
            tbl_Resultado.setValueAt(!(boolean) tbl_Resultado.getValueAt(fila, columna), fila, columna);
        }
    }//GEN-LAST:event_tbl_ResultadoMouseClicked

    private void tbtn_marcarDesmarcarStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_tbtn_marcarDesmarcarStateChanged
        int cantidadDeFilas = tbl_Resultado.getRowCount();
        if (this.tbtn_marcarDesmarcar.isSelected()) {
            ImageIcon iconoMarcado = new ImageIcon(getClass().getResource("/sic/icons/chkMarca_16x16.png"));
            this.tbtn_marcarDesmarcar.setIcon(iconoMarcado);
            for (int i = 0; i < cantidadDeFilas; i++) {
                tbl_Resultado.setValueAt(true, i, 0);
            }
        } else {
            ImageIcon iconoNoMarcado = new ImageIcon(getClass().getResource("/sic/icons/chkNoMarcado_16x16.png"));
            this.tbtn_marcarDesmarcar.setIcon(iconoNoMarcado);
            for (int i = 0; i < cantidadDeFilas; i++) {
                tbl_Resultado.setValueAt(false, i, 0);
            }
        }
    }//GEN-LAST:event_tbtn_marcarDesmarcarStateChanged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_AddComment;
    private javax.swing.JButton btn_BuscarCliente;
    private javax.swing.JButton btn_BuscarPorCodigoProducto;
    private javax.swing.JButton btn_BuscarProductos;
    private javax.swing.JButton btn_CambiarUserEmpresa;
    private javax.swing.JButton btn_Continuar;
    private javax.swing.JButton btn_EliminarEntradaProducto;
    private javax.swing.JButton btn_NuevoCliente;
    private javax.swing.JButton btn_VolverAdministracion;
    private javax.swing.JComboBox cmb_TipoFactura;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lbl_105;
    private javax.swing.JLabel lbl_21;
    private javax.swing.JLabel lbl_CondicionIVACliente;
    private javax.swing.JLabel lbl_DescuentoRecargo;
    private javax.swing.JLabel lbl_DomicilioCliente;
    private javax.swing.JLabel lbl_IDFiscalCliente;
    private javax.swing.JLabel lbl_IVA105;
    private javax.swing.JLabel lbl_IVA21;
    private javax.swing.JLabel lbl_ImpInterno;
    private javax.swing.JLabel lbl_NombreCliente;
    private javax.swing.JLabel lbl_Observaciones;
    private javax.swing.JLabel lbl_SubTotal;
    private javax.swing.JLabel lbl_SubTotalNeto;
    private javax.swing.JLabel lbl_TipoFactura;
    private javax.swing.JLabel lbl_Total;
    private javax.swing.JPanel panelCliente;
    private javax.swing.JPanel panelEncabezado;
    private javax.swing.JPanel panelGeneral;
    private javax.swing.JPanel panelObservaciones;
    private javax.swing.JPanel panelRenglones;
    private javax.swing.JPanel panelResultados;
    private javax.swing.JScrollPane sp_Resultado;
    private javax.swing.JTable tbl_Resultado;
    private javax.swing.JToggleButton tbtn_marcarDesmarcar;
    private javax.swing.JTextField txt_CodigoProducto;
    private javax.swing.JTextField txt_CondicionIVACliente;
    private javax.swing.JTextField txt_DomicilioCliente;
    private javax.swing.JTextField txt_IDFiscalCliente;
    private javax.swing.JFormattedTextField txt_IVA105_neto;
    private javax.swing.JFormattedTextField txt_IVA21_neto;
    private javax.swing.JFormattedTextField txt_ImpInterno_neto;
    private javax.swing.JTextField txt_NombreCliente;
    private javax.swing.JFormattedTextField txt_Recargo_neto;
    private javax.swing.JFormattedTextField txt_Recargo_porcentaje;
    private javax.swing.JFormattedTextField txt_SubTotalNeto;
    private javax.swing.JFormattedTextField txt_Subtotal;
    private javax.swing.JFormattedTextField txt_Total;
    private javax.swing.JTextArea txta_Observaciones;
    // End of variables declaration//GEN-END:variables
}
