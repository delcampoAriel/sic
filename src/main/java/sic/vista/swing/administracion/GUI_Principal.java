package sic.vista.swing.administracion;

import java.awt.Dimension;
import java.beans.PropertyVetoException;
import java.util.ResourceBundle;
import javax.persistence.PersistenceException;
import javax.swing.*;
import org.apache.log4j.Logger;
import sic.modelo.Empresa;
import sic.service.EmpresaService;
import sic.service.UsuarioService;
import sic.util.Utilidades;
import sic.vista.swing.GUI_LogIn;
import sic.vista.swing.tpv.GUI_PrincipalTPV;

public class GUI_Principal extends JFrame {

    private final JLabel lbl_Fondo;
    private final EmpresaService empresaService = new EmpresaService();
    private final UsuarioService usuarioService = new UsuarioService();
    private static final Logger log = Logger.getLogger(GUI_Principal.class.getPackage().getName());

    public GUI_Principal() {
        this.initComponents();
        ImageIcon iconoVentana = new ImageIcon(GUI_Principal.class.getResource("/sic/icons/SIC_24_square.png"));
        this.setIconImage(iconoVentana.getImage());
        lbl_Fondo = new JLabel();
        dp_Escritorio.add(lbl_Fondo);
        this.setTitle("S.I.C. Sistema de Información Comercial "
                + ResourceBundle.getBundle("Mensajes").getString("version"));
        this.setSize(new Dimension(1000, 720));
        this.setExtendedState(MAXIMIZED_BOTH);
        this.setLocationRelativeTo(null);
    }

    public JDesktopPane getDesktopPane() {
        return dp_Escritorio;
    }

    private void llamarGUI_SeleccionEmpresa() {
        GUI_SeleccionEmpresa gui_seleccionEmpresa = new GUI_SeleccionEmpresa(this, true);
        gui_seleccionEmpresa.setLocationRelativeTo(this);
        gui_seleccionEmpresa.setVisible(true);
        Empresa empresa = empresaService.getEmpresaActiva().getEmpresa();
        if (empresa == null) {
            lbl_EmpresaActiva.setText("Empresa: (sin empresa)");
        } else {
            lbl_EmpresaActiva.setText("Empresa: " + empresa.getNombre());
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lbl_EmpresaActiva = new javax.swing.JLabel();
        lbl_UsuarioActivo = new javax.swing.JLabel();
        lbl_Separador = new javax.swing.JLabel();
        dp_Escritorio = new javax.swing.JDesktopPane();
        mb_BarraMenues = new javax.swing.JMenuBar();
        mnu_Sistema = new javax.swing.JMenu();
        mnuItm_IrTPV = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        mnuItm_Empresas = new javax.swing.JMenuItem();
        mnuItm_CambiarEmpresa = new javax.swing.JMenuItem();
        Separador1 = new javax.swing.JPopupMenu.Separator();
        mnuItm_Usuarios = new javax.swing.JMenuItem();
        mnuItm_CambiarUser = new javax.swing.JMenuItem();
        Separador2 = new javax.swing.JPopupMenu.Separator();
        mnuItm_Configuracion = new javax.swing.JMenuItem();
        mnuItm_Salir = new javax.swing.JMenuItem();
        mnu_Compras = new javax.swing.JMenu();
        mnuItm_FacturasCompra = new javax.swing.JMenuItem();
        mnuItm_Proveedores = new javax.swing.JMenuItem();
        mnu_Ventas = new javax.swing.JMenu();
        mnuItm_FacturasVenta = new javax.swing.JMenuItem();
        mnuItm_Clientes = new javax.swing.JMenuItem();
        mnu_Administracion = new javax.swing.JMenu();
        mnuItm_Transportistas = new javax.swing.JMenuItem();
        mnuItm_FormasDePago = new javax.swing.JMenuItem();
        mnu_Stock = new javax.swing.JMenu();
        mnuItm_Productos = new javax.swing.JMenuItem();
        mnu_Estadisticas = new javax.swing.JMenu();
        mnuItm_TopProductos = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        lbl_EmpresaActiva.setFont(new java.awt.Font("DejaVu Sans", 1, 13)); // NOI18N
        lbl_EmpresaActiva.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sic/icons/Empresa_16x16.png"))); // NOI18N
        lbl_EmpresaActiva.setText("Empresa: (sin empresa)");
        lbl_EmpresaActiva.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lbl_UsuarioActivo.setFont(new java.awt.Font("DejaVu Sans", 1, 13)); // NOI18N
        lbl_UsuarioActivo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sic/icons/Group_16x16.png"))); // NOI18N
        lbl_UsuarioActivo.setText("Usuario: (sin usuario)");
        lbl_UsuarioActivo.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lbl_Separador.setFont(new java.awt.Font("DejaVu Sans", 1, 13)); // NOI18N
        lbl_Separador.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        dp_Escritorio.setBackground(new java.awt.Color(204, 204, 204));
        dp_Escritorio.setToolTipText("");

        mnu_Sistema.setText("Sistema");

        mnuItm_IrTPV.setText("Ir a Punto de Venta");
        mnuItm_IrTPV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuItm_IrTPVActionPerformed(evt);
            }
        });
        mnu_Sistema.add(mnuItm_IrTPV);
        mnu_Sistema.add(jSeparator1);

        mnuItm_Empresas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sic/icons/Empresa_16x16.png"))); // NOI18N
        mnuItm_Empresas.setText("Empresas");
        mnuItm_Empresas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuItm_EmpresasActionPerformed(evt);
            }
        });
        mnu_Sistema.add(mnuItm_Empresas);

        mnuItm_CambiarEmpresa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sic/icons/EmpresaGo_16x16.png"))); // NOI18N
        mnuItm_CambiarEmpresa.setText("Cambiar Empresa");
        mnuItm_CambiarEmpresa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuItm_CambiarEmpresaActionPerformed(evt);
            }
        });
        mnu_Sistema.add(mnuItm_CambiarEmpresa);
        mnu_Sistema.add(Separador1);

        mnuItm_Usuarios.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sic/icons/Group_16x16.png"))); // NOI18N
        mnuItm_Usuarios.setText("Usuarios");
        mnuItm_Usuarios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuItm_UsuariosActionPerformed(evt);
            }
        });
        mnu_Sistema.add(mnuItm_Usuarios);

        mnuItm_CambiarUser.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sic/icons/GroupArrow_16x16.png"))); // NOI18N
        mnuItm_CambiarUser.setText("Cambiar Usuario");
        mnuItm_CambiarUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuItm_CambiarUserActionPerformed(evt);
            }
        });
        mnu_Sistema.add(mnuItm_CambiarUser);
        mnu_Sistema.add(Separador2);

        mnuItm_Configuracion.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sic/icons/Gears_16x16.png"))); // NOI18N
        mnuItm_Configuracion.setText("Configuración");
        mnuItm_Configuracion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuItm_ConfiguracionActionPerformed(evt);
            }
        });
        mnu_Sistema.add(mnuItm_Configuracion);

        mnuItm_Salir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sic/icons/LogOut_16x16.png"))); // NOI18N
        mnuItm_Salir.setText("Salir");
        mnuItm_Salir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuItm_SalirActionPerformed(evt);
            }
        });
        mnu_Sistema.add(mnuItm_Salir);

        mb_BarraMenues.add(mnu_Sistema);

        mnu_Compras.setText("Compras");

        mnuItm_FacturasCompra.setText("Facturas");
        mnuItm_FacturasCompra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuItm_FacturasCompraActionPerformed(evt);
            }
        });
        mnu_Compras.add(mnuItm_FacturasCompra);

        mnuItm_Proveedores.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sic/icons/ProviderBag_16x16.png"))); // NOI18N
        mnuItm_Proveedores.setText("Proveedores");
        mnuItm_Proveedores.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuItm_ProveedoresActionPerformed(evt);
            }
        });
        mnu_Compras.add(mnuItm_Proveedores);

        mb_BarraMenues.add(mnu_Compras);

        mnu_Ventas.setText("Ventas");

        mnuItm_FacturasVenta.setText("Facturas");
        mnuItm_FacturasVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuItm_FacturasVentaActionPerformed(evt);
            }
        });
        mnu_Ventas.add(mnuItm_FacturasVenta);

        mnuItm_Clientes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sic/icons/Client_16x16.png"))); // NOI18N
        mnuItm_Clientes.setText("Clientes");
        mnuItm_Clientes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuItm_ClientesActionPerformed(evt);
            }
        });
        mnu_Ventas.add(mnuItm_Clientes);

        mb_BarraMenues.add(mnu_Ventas);

        mnu_Administracion.setText("Administración");

        mnuItm_Transportistas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sic/icons/Truck_16x16.png"))); // NOI18N
        mnuItm_Transportistas.setText("Transportistas");
        mnuItm_Transportistas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuItm_TransportistasActionPerformed(evt);
            }
        });
        mnu_Administracion.add(mnuItm_Transportistas);

        mnuItm_FormasDePago.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sic/icons/Wallet_16x16.png"))); // NOI18N
        mnuItm_FormasDePago.setText("Formas de Pago");
        mnuItm_FormasDePago.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuItm_FormasDePagoActionPerformed(evt);
            }
        });
        mnu_Administracion.add(mnuItm_FormasDePago);

        mb_BarraMenues.add(mnu_Administracion);

        mnu_Stock.setText("Stock");

        mnuItm_Productos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sic/icons/Product_16x16.png"))); // NOI18N
        mnuItm_Productos.setText("Productos");
        mnuItm_Productos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuItm_ProductosActionPerformed(evt);
            }
        });
        mnu_Stock.add(mnuItm_Productos);

        mb_BarraMenues.add(mnu_Stock);

        mnu_Estadisticas.setText("Estadísticas");

        mnuItm_TopProductos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sic/icons/Chart_16x16.png"))); // NOI18N
        mnuItm_TopProductos.setText("Top 5 de productos mas vendidos");
        mnuItm_TopProductos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuItm_TopProductosActionPerformed(evt);
            }
        });
        mnu_Estadisticas.add(mnuItm_TopProductos);

        mb_BarraMenues.add(mnu_Estadisticas);

        setJMenuBar(mb_BarraMenues);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(lbl_UsuarioActivo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbl_EmpresaActiva)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbl_Separador, javax.swing.GroupLayout.DEFAULT_SIZE, 456, Short.MAX_VALUE))
            .addComponent(dp_Escritorio, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 851, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(dp_Escritorio, javax.swing.GroupLayout.DEFAULT_SIZE, 597, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbl_UsuarioActivo)
                    .addComponent(lbl_Separador, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbl_EmpresaActiva)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void mnuItm_SalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuItm_SalirActionPerformed
        this.formWindowClosing(null);
    }//GEN-LAST:event_mnuItm_SalirActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        int respuesta = JOptionPane.showConfirmDialog(this,
                "¿Esta seguro que desea salir del sistema?",
                "Salir", JOptionPane.YES_NO_OPTION);
        if (respuesta == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }//GEN-LAST:event_formWindowClosing

    private void mnuItm_EmpresasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuItm_EmpresasActionPerformed
        GUI_Empresas gui_Empresas = new GUI_Empresas();
        gui_Empresas.setModal(true);
        gui_Empresas.setLocationRelativeTo(this);
        gui_Empresas.setVisible(true);
        Utilidades.cerrarTodasVentanas(dp_Escritorio);
        this.llamarGUI_SeleccionEmpresa();
    }//GEN-LAST:event_mnuItm_EmpresasActionPerformed

    private void mnuItm_CambiarUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuItm_CambiarUserActionPerformed
        this.dispose();
        new GUI_LogIn().setVisible(true);
    }//GEN-LAST:event_mnuItm_CambiarUserActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        lbl_UsuarioActivo.setText("Usuario: " + usuarioService.getUsuarioActivo().getUsuario().getNombre());
        try {
            if (empresaService.getEmpresas().isEmpty()) {
                this.mnuItm_EmpresasActionPerformed(null);
            } else {
                this.llamarGUI_SeleccionEmpresa();
            }

        } catch (PersistenceException ex) {
            log.error(ResourceBundle.getBundle("Mensajes").getString("mensaje_error_acceso_a_datos") + " - " + ex.getMessage());
            JOptionPane.showInternalMessageDialog(this, ResourceBundle.getBundle("Mensajes").getString("mensaje_error_acceso_a_datos"), "Error", JOptionPane.ERROR_MESSAGE);
            this.dispose();
        }
    }//GEN-LAST:event_formWindowOpened

    private void mnuItm_UsuariosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuItm_UsuariosActionPerformed
        GUI_Usuarios gui_Usuarios = new GUI_Usuarios();
        gui_Usuarios.setModal(true);
        gui_Usuarios.setLocationRelativeTo(this);
        gui_Usuarios.setVisible(true);
    }//GEN-LAST:event_mnuItm_UsuariosActionPerformed

    private void mnuItm_ProveedoresActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuItm_ProveedoresActionPerformed
        JInternalFrame gui = Utilidades.estaEnDesktop(getDesktopPane(), GUI_Proveedores.class);
        if (gui == null) {
            gui = new GUI_Proveedores();
            gui.setLocation(getDesktopPane().getWidth() / 2 - gui.getWidth() / 2,
                    getDesktopPane().getHeight() / 2 - gui.getHeight() / 2);
            getDesktopPane().add(gui);
            gui.setVisible(true);
        } else {
            //selecciona y trae al frente el internalframe
            try {
                gui.setSelected(true);

            } catch (PropertyVetoException ex) {
                String msjError = "No se pudo seleccionar la ventana requerida.";
                log.error(msjError + " - " + ex.getMessage());
                JOptionPane.showInternalMessageDialog(this.getDesktopPane(), msjError, "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_mnuItm_ProveedoresActionPerformed

    private void mnuItm_FacturasCompraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuItm_FacturasCompraActionPerformed
        JInternalFrame gui = Utilidades.estaEnDesktop(getDesktopPane(), GUI_FacturasCompra.class);
        if (gui == null) {
            gui = new GUI_FacturasCompra();
            gui.setLocation(getDesktopPane().getWidth() / 2 - gui.getWidth() / 2,
                    getDesktopPane().getHeight() / 2 - gui.getHeight() / 2);
            getDesktopPane().add(gui);
            gui.setVisible(true);
        } else {
            //selecciona y trae al frente el internalframe
            try {
                gui.setSelected(true);

            } catch (PropertyVetoException ex) {
                String msjError = "No se pudo seleccionar la ventana requerida.";
                log.error(msjError + " - " + ex.getMessage());
                JOptionPane.showInternalMessageDialog(this.getDesktopPane(), msjError, "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_mnuItm_FacturasCompraActionPerformed

    private void mnuItm_TransportistasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuItm_TransportistasActionPerformed
        JInternalFrame gui = Utilidades.estaEnDesktop(getDesktopPane(), GUI_Transportistas.class);
        if (gui == null) {
            gui = new GUI_Transportistas();
            gui.setLocation(getDesktopPane().getWidth() / 2 - gui.getWidth() / 2,
                    getDesktopPane().getHeight() / 2 - gui.getHeight() / 2);
            getDesktopPane().add(gui);
            gui.setVisible(true);
        } else {
            //selecciona y trae al frente el internalframe
            try {
                gui.setSelected(true);

            } catch (PropertyVetoException ex) {
                String msjError = "No se pudo seleccionar la ventana requerida.";
                log.error(msjError + " - " + ex.getMessage());
                JOptionPane.showInternalMessageDialog(this.getDesktopPane(), msjError, "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_mnuItm_TransportistasActionPerformed

    private void mnuItm_ProductosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuItm_ProductosActionPerformed
        JInternalFrame gui = Utilidades.estaEnDesktop(getDesktopPane(), GUI_Productos.class);
        if (gui == null) {
            GUI_Productos productos = new GUI_Productos();
            productos.setLocation(getDesktopPane().getWidth() / 2 - productos.getWidth() / 2,
                    getDesktopPane().getHeight() / 2 - productos.getHeight() / 2);
            getDesktopPane().add(productos);
            productos.setVisible(true);
        } else {
            //selecciona y trae al frente el internalframe
            try {
                gui.setSelected(true);

            } catch (PropertyVetoException ex) {
                String msjError = "No se pudo seleccionar la ventana requerida.";
                log.error(msjError + " - " + ex.getMessage());
                JOptionPane.showInternalMessageDialog(this.getDesktopPane(), msjError, "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_mnuItm_ProductosActionPerformed

    private void mnuItm_ClientesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuItm_ClientesActionPerformed
        JInternalFrame gui = Utilidades.estaEnDesktop(getDesktopPane(), GUI_Clientes.class);
        if (gui == null) {
            gui = new GUI_Clientes();
            gui.setLocation(getDesktopPane().getWidth() / 2 - gui.getWidth() / 2,
                    getDesktopPane().getHeight() / 2 - gui.getHeight() / 2);
            getDesktopPane().add(gui);
            gui.setVisible(true);
        } else {
            //selecciona y trae al frente el internalframe
            try {
                gui.setSelected(true);

            } catch (PropertyVetoException ex) {
                String msjError = "No se pudo seleccionar la ventana requerida.";
                log.error(msjError + " - " + ex.getMessage());
                JOptionPane.showInternalMessageDialog(this.getDesktopPane(), msjError, "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_mnuItm_ClientesActionPerformed

    private void mnuItm_CambiarEmpresaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuItm_CambiarEmpresaActionPerformed
        Utilidades.cerrarTodasVentanas(dp_Escritorio);
        this.llamarGUI_SeleccionEmpresa();
    }//GEN-LAST:event_mnuItm_CambiarEmpresaActionPerformed

    private void mnuItm_FormasDePagoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuItm_FormasDePagoActionPerformed
        GUI_FormasDePago gui_FormasDePago = new GUI_FormasDePago();
        gui_FormasDePago.setModal(true);
        gui_FormasDePago.setLocationRelativeTo(this);
        gui_FormasDePago.setVisible(true);
    }//GEN-LAST:event_mnuItm_FormasDePagoActionPerformed

    private void mnuItm_IrTPVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuItm_IrTPVActionPerformed
        this.dispose();
        GUI_PrincipalTPV gui_tpv = new GUI_PrincipalTPV();
        gui_tpv.setVisible(true);
    }//GEN-LAST:event_mnuItm_IrTPVActionPerformed

    private void mnuItm_FacturasVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuItm_FacturasVentaActionPerformed
        JInternalFrame gui = Utilidades.estaEnDesktop(getDesktopPane(), GUI_FacturasVenta.class);
        if (gui == null) {
            gui = new GUI_FacturasVenta();
            gui.setLocation(getDesktopPane().getWidth() / 2 - gui.getWidth() / 2,
                    getDesktopPane().getHeight() / 2 - gui.getHeight() / 2);
            getDesktopPane().add(gui);
            gui.setVisible(true);
        } else {
            //selecciona y trae al frente el internalframe
            try {
                gui.setSelected(true);

            } catch (PropertyVetoException ex) {
                String msjError = "No se pudo seleccionar la ventana requerida.";
                log.error(msjError + " - " + ex.getMessage());
                JOptionPane.showInternalMessageDialog(this.getDesktopPane(), msjError, "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_mnuItm_FacturasVentaActionPerformed

    private void mnuItm_TopProductosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuItm_TopProductosActionPerformed
        JInternalFrame gui = Utilidades.estaEnDesktop(getDesktopPane(), GUI_TopProductos.class);
        if (gui == null) {
            gui = new GUI_TopProductos();
            gui.setLocation(getDesktopPane().getWidth() / 2 - gui.getWidth() / 2,
                    getDesktopPane().getHeight() / 2 - gui.getHeight() / 2);
            getDesktopPane().add(gui);
            gui.setVisible(true);
        } else {
            //selecciona y trae al frente el internalframe
            try {
                gui.setSelected(true);

            } catch (PropertyVetoException ex) {
                String msjError = "No se pudo seleccionar la ventana requerida.";
                log.error(msjError + " - " + ex.getMessage());
                JOptionPane.showInternalMessageDialog(this.getDesktopPane(), msjError, "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_mnuItm_TopProductosActionPerformed

    private void mnuItm_ConfiguracionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuItm_ConfiguracionActionPerformed
        GUI_ConfiguracionDelSistema gui_ConfiguracionDelSistema = new GUI_ConfiguracionDelSistema();
        gui_ConfiguracionDelSistema.setModal(true);
        gui_ConfiguracionDelSistema.setLocationRelativeTo(this);
        gui_ConfiguracionDelSistema.setVisible(true);
        Utilidades.cerrarTodasVentanas(dp_Escritorio);
    }//GEN-LAST:event_mnuItm_ConfiguracionActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPopupMenu.Separator Separador1;
    private javax.swing.JPopupMenu.Separator Separador2;
    private javax.swing.JDesktopPane dp_Escritorio;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JLabel lbl_EmpresaActiva;
    private javax.swing.JLabel lbl_Separador;
    private javax.swing.JLabel lbl_UsuarioActivo;
    private javax.swing.JMenuBar mb_BarraMenues;
    private javax.swing.JMenuItem mnuItm_CambiarEmpresa;
    private javax.swing.JMenuItem mnuItm_CambiarUser;
    private javax.swing.JMenuItem mnuItm_Clientes;
    private javax.swing.JMenuItem mnuItm_Configuracion;
    private javax.swing.JMenuItem mnuItm_Empresas;
    private javax.swing.JMenuItem mnuItm_FacturasCompra;
    private javax.swing.JMenuItem mnuItm_FacturasVenta;
    private javax.swing.JMenuItem mnuItm_FormasDePago;
    private javax.swing.JMenuItem mnuItm_IrTPV;
    private javax.swing.JMenuItem mnuItm_Productos;
    private javax.swing.JMenuItem mnuItm_Proveedores;
    private javax.swing.JMenuItem mnuItm_Salir;
    private javax.swing.JMenuItem mnuItm_TopProductos;
    private javax.swing.JMenuItem mnuItm_Transportistas;
    private javax.swing.JMenuItem mnuItm_Usuarios;
    private javax.swing.JMenu mnu_Administracion;
    private javax.swing.JMenu mnu_Compras;
    private javax.swing.JMenu mnu_Estadisticas;
    private javax.swing.JMenu mnu_Sistema;
    private javax.swing.JMenu mnu_Stock;
    private javax.swing.JMenu mnu_Ventas;
    // End of variables declaration//GEN-END:variables
}
