package sic.vista.swing.administracion;

import java.beans.PropertyVetoException;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javax.persistence.PersistenceException;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import org.apache.log4j.Logger;
import sic.modelo.BusquedaClienteCriteria;
import sic.modelo.Cliente;
import sic.modelo.Localidad;
import sic.modelo.Pais;
import sic.modelo.Provincia;
import sic.service.*;
import sic.util.Utilidades;
import sic.vista.swing.ModeloTabla;

public class GUI_Clientes extends JInternalFrame {

    private List<Cliente> clientes;
    private ModeloTabla modeloTablaDeResultados = new ModeloTabla();
    private final ProvinciaService provinciaService = new ProvinciaService();
    private final PaisService paisService = new PaisService();
    private final LocalidadService localidadService = new LocalidadService();
    private final ClienteService clienteService = new ClienteService();
    private final EmpresaService empresaService = new EmpresaService();
    private static final Logger log = Logger.getLogger(GUI_Clientes.class.getPackage().getName());

    public GUI_Clientes() {
        this.initComponents();
        this.setSize(750, 450);
    }

    private void cargarComboBoxPaises() {
        cmb_Pais.removeAllItems();
        List<Pais> paises;
        paises = paisService.getPaises();
        Pais paisTodos = new Pais();
        paisTodos.setNombre("Todos");
        cmb_Pais.addItem(paisTodos);
        for (Pais pais : paises) {
            cmb_Pais.addItem(pais);
        }
    }

    public void cargarComboBoxProvinciasDelPais(Pais paisSeleccionado) {
        cmb_Provincia.removeAllItems();
        List<Provincia> provincias = provinciaService.getProvinciasDelPais(paisSeleccionado);
        Provincia provinciaTodas = new Provincia();
        provinciaTodas.setNombre("Todas");
        cmb_Provincia.addItem(provinciaTodas);
        for (Provincia prov : provincias) {
            cmb_Provincia.addItem(prov);
        }
    }

    public void cargarComboBoxLocalidadesDeLaProvincia(Provincia provSeleccionada) {
        cmb_Localidad.removeAllItems();
        List<Localidad> Localidades = localidadService.getLocalidadesDeLaProvincia(provSeleccionada);
        Localidad localidadTodas = new Localidad();
        localidadTodas.setNombre("Todas");
        cmb_Localidad.addItem(localidadTodas);
        for (Localidad loc : Localidades) {
            cmb_Localidad.addItem(loc);
        }
    }

    private void setColumnas() {
        //sorting
        tbl_Resultados.setAutoCreateRowSorter(true);

        //nombres de columnas
        String[] encabezados = new String[14];
        encabezados[0] = "Predeterminado";
        encabezados[1] = "ID Fiscal";
        encabezados[2] = "Razon Social";
        encabezados[3] = "Nombre Fantasia";        
        encabezados[4] = "Direccion";
        encabezados[5] = "Condicion IVA";
        encabezados[6] = "Tel. Primario";
        encabezados[7] = "Tel. Secundario";
        encabezados[8] = "Contacto";
        encabezados[9] = "Email";
        encabezados[10] = "Fecha Alta";
        encabezados[11] = "Localidad";
        encabezados[12] = "Provincia";
        encabezados[13] = "Pais";
        modeloTablaDeResultados.setColumnIdentifiers(encabezados);
        tbl_Resultados.setModel(modeloTablaDeResultados);

        //tipo de dato columnas
        Class[] tipos = new Class[modeloTablaDeResultados.getColumnCount()];
        tipos[0] = Boolean.class;
        tipos[1] = String.class;
        tipos[2] = String.class;
        tipos[3] = String.class;
        tipos[4] = String.class;
        tipos[5] = String.class;
        tipos[6] = String.class;
        tipos[7] = String.class;
        tipos[8] = String.class;
        tipos[9] = String.class;
        tipos[10] = Date.class;
        tipos[11] = String.class;
        tipos[12] = String.class;
        tipos[13] = String.class;
        modeloTablaDeResultados.setClaseColumnas(tipos);
        tbl_Resultados.getTableHeader().setReorderingAllowed(false);
        tbl_Resultados.getTableHeader().setResizingAllowed(true);

        //Tamanios de columnas
        tbl_Resultados.getColumnModel().getColumn(0).setPreferredWidth(120);
        tbl_Resultados.getColumnModel().getColumn(1).setPreferredWidth(100);
        tbl_Resultados.getColumnModel().getColumn(2).setPreferredWidth(250);
        tbl_Resultados.getColumnModel().getColumn(3).setPreferredWidth(250);
        tbl_Resultados.getColumnModel().getColumn(4).setPreferredWidth(250);
        tbl_Resultados.getColumnModel().getColumn(5).setPreferredWidth(250);
        tbl_Resultados.getColumnModel().getColumn(6).setPreferredWidth(150);
        tbl_Resultados.getColumnModel().getColumn(7).setPreferredWidth(200);
        tbl_Resultados.getColumnModel().getColumn(8).setPreferredWidth(200);
        tbl_Resultados.getColumnModel().getColumn(9).setPreferredWidth(250);
        tbl_Resultados.getColumnModel().getColumn(10).setPreferredWidth(100);
        tbl_Resultados.getColumnModel().getColumn(11).setPreferredWidth(200);
        tbl_Resultados.getColumnModel().getColumn(12).setPreferredWidth(200);
        tbl_Resultados.getColumnModel().getColumn(13).setPreferredWidth(200);
    }

    private void cargarResultadosAlTable() {
        this.limpiarJTable();
        for (Cliente cliente : clientes) {
            Object[] fila = new Object[14];
            fila[0] = cliente.isPredeterminado();
            fila[1] = cliente.getId_Fiscal();
            fila[2] = cliente.getRazonSocial();
            fila[3] = cliente.getNombreFantasia();            
            fila[4] = cliente.getDireccion();
            fila[5] = cliente.getCondicionIVA().getNombre();
            fila[6] = cliente.getTelPrimario();
            fila[7] = cliente.getTelSecundario();            
            fila[8] = cliente.getContacto();
            fila[9] = cliente.getEmail();
            fila[10] = cliente.getFechaAlta();
            fila[11] = cliente.getLocalidad().getNombre();
            fila[12] = cliente.getLocalidad().getProvincia().getNombre();
            fila[13] = cliente.getLocalidad().getProvincia().getPais().getNombre();
            modeloTablaDeResultados.addRow(fila);
        }
        tbl_Resultados.setModel(modeloTablaDeResultados);
        //contador de registros
        String mensaje = clientes.size() + " clientes encontrados.";
        lbl_cantResultados.setText(mensaje);
    }

    private void limpiarJTable() {
        modeloTablaDeResultados = new ModeloTabla();
        tbl_Resultados.setModel(modeloTablaDeResultados);
        this.setColumnas();
    }

    private void buscar() {
        BusquedaClienteCriteria criteria = new BusquedaClienteCriteria(
                chk_RazonSocialNombreFantasia.isSelected(), txt_RazonSocialNombreFantasia.getText().trim(),
                chk_RazonSocialNombreFantasia.isSelected(), txt_RazonSocialNombreFantasia.getText().trim(),
                chk_Id_Fiscal.isSelected(), txt_Id_Fiscal.getText().trim(),
                chk_Ubicacion.isSelected(), (Pais) cmb_Pais.getSelectedItem(),
                chk_Ubicacion.isSelected(), (Provincia) cmb_Provincia.getSelectedItem(),
                chk_Ubicacion.isSelected(), (Localidad) cmb_Localidad.getSelectedItem(),
                empresaService.getEmpresaActiva().getEmpresa());

        try {
            clientes = clienteService.buscarClientes(criteria);
            this.cargarResultadosAlTable();
        } catch (PersistenceException ex) {
            log.error(ResourceBundle.getBundle("Mensajes").getString("mensaje_error_acceso_a_datos") + " - " + ex.getMessage());
            JOptionPane.showInternalMessageDialog(this, ResourceBundle.getBundle("Mensajes").getString("mensaje_error_acceso_a_datos"), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelFiltros = new javax.swing.JPanel();
        chk_RazonSocialNombreFantasia = new javax.swing.JCheckBox();
        txt_RazonSocialNombreFantasia = new javax.swing.JTextField();
        chk_Ubicacion = new javax.swing.JCheckBox();
        cmb_Provincia = new javax.swing.JComboBox();
        cmb_Pais = new javax.swing.JComboBox();
        cmb_Localidad = new javax.swing.JComboBox();
        btn_Buscar = new javax.swing.JButton();
        txt_Id_Fiscal = new javax.swing.JTextField();
        chk_Id_Fiscal = new javax.swing.JCheckBox();
        lbl_cantResultados = new javax.swing.JLabel();
        panelResultados = new javax.swing.JPanel();
        sp_Resultados = new javax.swing.JScrollPane();
        tbl_Resultados = new javax.swing.JTable();
        btn_Nuevo = new javax.swing.JButton();
        btn_Modificar = new javax.swing.JButton();
        btn_Eliminar = new javax.swing.JButton();
        btn_setPredeterminado = new javax.swing.JButton();

        setClosable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Administrar Clientes");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/sic/icons/Client_16x16.png"))); // NOI18N
        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameOpened(evt);
            }
        });

        panelFiltros.setBorder(javax.swing.BorderFactory.createTitledBorder("Filtros"));

        chk_RazonSocialNombreFantasia.setText("Razon Social / Nombre Fantasia:");
        chk_RazonSocialNombreFantasia.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chk_RazonSocialNombreFantasiaItemStateChanged(evt);
            }
        });

        txt_RazonSocialNombreFantasia.setEnabled(false);

        chk_Ubicacion.setText("Ubicación:");
        chk_Ubicacion.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chk_UbicacionItemStateChanged(evt);
            }
        });

        cmb_Provincia.setEnabled(false);
        cmb_Provincia.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmb_ProvinciaItemStateChanged(evt);
            }
        });

        cmb_Pais.setEnabled(false);
        cmb_Pais.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmb_PaisItemStateChanged(evt);
            }
        });

        cmb_Localidad.setEnabled(false);

        btn_Buscar.setForeground(java.awt.Color.blue);
        btn_Buscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sic/icons/Search_16x16.png"))); // NOI18N
        btn_Buscar.setText("Buscar");
        btn_Buscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_BuscarActionPerformed(evt);
            }
        });

        txt_Id_Fiscal.setEnabled(false);

        chk_Id_Fiscal.setText("ID Fiscal:");
        chk_Id_Fiscal.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chk_Id_FiscalItemStateChanged(evt);
            }
        });

        lbl_cantResultados.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        javax.swing.GroupLayout panelFiltrosLayout = new javax.swing.GroupLayout(panelFiltros);
        panelFiltros.setLayout(panelFiltrosLayout);
        panelFiltrosLayout.setHorizontalGroup(
            panelFiltrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFiltrosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelFiltrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelFiltrosLayout.createSequentialGroup()
                        .addComponent(btn_Buscar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lbl_cantResultados, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(panelFiltrosLayout.createSequentialGroup()
                        .addGroup(panelFiltrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(chk_Ubicacion, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(chk_RazonSocialNombreFantasia, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(chk_Id_Fiscal, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelFiltrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txt_RazonSocialNombreFantasia)
                            .addComponent(txt_Id_Fiscal)
                            .addComponent(cmb_Localidad, 0, 235, Short.MAX_VALUE)
                            .addComponent(cmb_Provincia, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cmb_Pais, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        panelFiltrosLayout.setVerticalGroup(
            panelFiltrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFiltrosLayout.createSequentialGroup()
                .addGroup(panelFiltrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(chk_RazonSocialNombreFantasia)
                    .addComponent(txt_RazonSocialNombreFantasia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelFiltrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(chk_Id_Fiscal)
                    .addComponent(txt_Id_Fiscal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelFiltrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(chk_Ubicacion)
                    .addGroup(panelFiltrosLayout.createSequentialGroup()
                        .addComponent(cmb_Pais, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmb_Provincia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmb_Localidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelFiltrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btn_Buscar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lbl_cantResultados, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        panelResultados.setBorder(javax.swing.BorderFactory.createTitledBorder("Resultados"));

        tbl_Resultados.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tbl_Resultados.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tbl_Resultados.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        sp_Resultados.setViewportView(tbl_Resultados);

        btn_Nuevo.setForeground(java.awt.Color.blue);
        btn_Nuevo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sic/icons/AddClient_16x16.png"))); // NOI18N
        btn_Nuevo.setText("Nuevo");
        btn_Nuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_NuevoActionPerformed(evt);
            }
        });

        btn_Modificar.setForeground(java.awt.Color.blue);
        btn_Modificar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sic/icons/EditClient_16x16.png"))); // NOI18N
        btn_Modificar.setText("Modificar");
        btn_Modificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_ModificarActionPerformed(evt);
            }
        });

        btn_Eliminar.setForeground(java.awt.Color.blue);
        btn_Eliminar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sic/icons/DeleteClient_16x16.png"))); // NOI18N
        btn_Eliminar.setText("Eliminar");
        btn_Eliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_EliminarActionPerformed(evt);
            }
        });

        btn_setPredeterminado.setForeground(java.awt.Color.blue);
        btn_setPredeterminado.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sic/icons/ClientArrow_16x16.png"))); // NOI18N
        btn_setPredeterminado.setText("Establecer como Predeterminado");
        btn_setPredeterminado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_setPredeterminadoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelResultadosLayout = new javax.swing.GroupLayout(panelResultados);
        panelResultados.setLayout(panelResultadosLayout);
        panelResultadosLayout.setHorizontalGroup(
            panelResultadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(sp_Resultados, javax.swing.GroupLayout.DEFAULT_SIZE, 655, Short.MAX_VALUE)
            .addGroup(panelResultadosLayout.createSequentialGroup()
                .addComponent(btn_Nuevo)
                .addGap(0, 0, 0)
                .addComponent(btn_Modificar)
                .addGap(0, 0, 0)
                .addComponent(btn_Eliminar)
                .addGap(0, 0, 0)
                .addComponent(btn_setPredeterminado)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        panelResultadosLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btn_Eliminar, btn_Modificar, btn_Nuevo});

        panelResultadosLayout.setVerticalGroup(
            panelResultadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelResultadosLayout.createSequentialGroup()
                .addComponent(sp_Resultados, javax.swing.GroupLayout.DEFAULT_SIZE, 188, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelResultadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btn_Nuevo)
                    .addComponent(btn_Modificar)
                    .addComponent(btn_Eliminar)
                    .addComponent(btn_setPredeterminado)))
        );

        panelResultadosLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btn_Eliminar, btn_Modificar, btn_Nuevo, btn_setPredeterminado});

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelResultados, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panelFiltros, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panelFiltros, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelResultados, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void chk_RazonSocialNombreFantasiaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chk_RazonSocialNombreFantasiaItemStateChanged
        //Pregunta el estado actual del checkBox
        if (chk_RazonSocialNombreFantasia.isSelected() == true) {
            txt_RazonSocialNombreFantasia.setEnabled(true);
            txt_RazonSocialNombreFantasia.requestFocus();
        } else {
            txt_RazonSocialNombreFantasia.setEnabled(false);
        }
    }//GEN-LAST:event_chk_RazonSocialNombreFantasiaItemStateChanged

    private void chk_UbicacionItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chk_UbicacionItemStateChanged
        //Pregunta el estado actual del checkBox
        if (chk_Ubicacion.isSelected() == true) {
            cmb_Pais.setEnabled(true);
            cmb_Provincia.setEnabled(true);
            cmb_Localidad.setEnabled(true);
            cmb_Pais.requestFocus();
        } else {
            cmb_Pais.setEnabled(false);
            cmb_Provincia.setEnabled(false);
            cmb_Localidad.setEnabled(false);
        }
    }//GEN-LAST:event_chk_UbicacionItemStateChanged

    private void cmb_PaisItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmb_PaisItemStateChanged
        if (cmb_Pais.getItemCount() > 0) {
            try {
                if (!cmb_Pais.getSelectedItem().toString().equals("Todos")) {
                    this.cargarComboBoxProvinciasDelPais((Pais) cmb_Pais.getSelectedItem());
                } else {
                    cmb_Provincia.removeAllItems();
                    Provincia provinciaTodas = new Provincia();
                    provinciaTodas.setNombre("Todas");
                    cmb_Provincia.addItem(provinciaTodas);
                    cmb_Localidad.removeAllItems();
                    Localidad localidadTodas = new Localidad();
                    localidadTodas.setNombre("Todas");
                    cmb_Localidad.addItem(localidadTodas);
                }
            } catch (PersistenceException ex) {
                log.error(ResourceBundle.getBundle("Mensajes").getString("mensaje_error_acceso_a_datos") + " - " + ex.getMessage());
                JOptionPane.showInternalMessageDialog(this, ResourceBundle.getBundle("Mensajes").getString("mensaje_error_acceso_a_datos"), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_cmb_PaisItemStateChanged

    private void cmb_ProvinciaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmb_ProvinciaItemStateChanged
        if (cmb_Provincia.getItemCount() > 0) {
            try {
                if (!cmb_Provincia.getSelectedItem().toString().equals("Todas")) {
                    cargarComboBoxLocalidadesDeLaProvincia((Provincia) cmb_Provincia.getSelectedItem());
                } else {
                    cmb_Localidad.removeAllItems();
                    Localidad localidadTodas = new Localidad();
                    localidadTodas.setNombre("Todas");
                    cmb_Localidad.addItem(localidadTodas);
                }

            } catch (PersistenceException ex) {
                log.error(ResourceBundle.getBundle("Mensajes").getString("mensaje_error_acceso_a_datos") + " - " + ex.getMessage());
                JOptionPane.showInternalMessageDialog(this, ResourceBundle.getBundle("Mensajes").getString("mensaje_error_acceso_a_datos"), "Error", JOptionPane.ERROR_MESSAGE);
            }

        } else {
            cmb_Localidad.removeAllItems();
        }
    }//GEN-LAST:event_cmb_ProvinciaItemStateChanged

    private void btn_BuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_BuscarActionPerformed
        this.buscar();
        if (clientes.isEmpty()) {
            JOptionPane.showMessageDialog(this, ResourceBundle.getBundle("Mensajes").getString("mensaje_busqueda_sin_resultados"),
                    "Aviso", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_btn_BuscarActionPerformed

    private void btn_NuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_NuevoActionPerformed
        GUI_DetalleCliente gui_DetalleCliente = new GUI_DetalleCliente();
        gui_DetalleCliente.setModal(true);
        gui_DetalleCliente.setLocationRelativeTo(this);
        gui_DetalleCliente.setVisible(true);
        try {
            this.cargarComboBoxPaises();
        } catch (PersistenceException ex) {
            log.error(ResourceBundle.getBundle("Mensajes").getString("mensaje_error_acceso_a_datos") + " - " + ex.getMessage());
            JOptionPane.showInternalMessageDialog(this, ResourceBundle.getBundle("Mensajes").getString("mensaje_error_acceso_a_datos"), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btn_NuevoActionPerformed

    private void btn_EliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_EliminarActionPerformed
        if (tbl_Resultados.getSelectedRow() != -1) {
            int indexFilaSeleccionada = Utilidades.getSelectedRowModelIndice(tbl_Resultados);
            int respuesta = JOptionPane.showConfirmDialog(this,
                    "¿Esta seguro que desea eliminar el cliente: "
                    + clientes.get(indexFilaSeleccionada) + "?",
                    "Eliminar", JOptionPane.YES_NO_OPTION);
            if (respuesta == JOptionPane.YES_OPTION) {
                try {
                    clienteService.eliminar(clientes.get(indexFilaSeleccionada));
                } catch (PersistenceException ex) {
                    log.error(ResourceBundle.getBundle("Mensajes").getString("mensaje_error_acceso_a_datos") + " - " + ex.getMessage());
                    JOptionPane.showInternalMessageDialog(this, ResourceBundle.getBundle("Mensajes").getString("mensaje_error_acceso_a_datos"), "Error", JOptionPane.ERROR_MESSAGE);
                }
                //actualiza JTable
                this.buscar();
            }
        }
    }//GEN-LAST:event_btn_EliminarActionPerformed

    private void btn_ModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ModificarActionPerformed
        if (tbl_Resultados.getSelectedRow() != -1) {
            int indexFilaSeleccionada = Utilidades.getSelectedRowModelIndice(tbl_Resultados);
            GUI_DetalleCliente gui_DetalleCliente = new GUI_DetalleCliente(clientes.get(indexFilaSeleccionada));
            gui_DetalleCliente.setModal(true);
            gui_DetalleCliente.setLocationRelativeTo(this);
            gui_DetalleCliente.setVisible(true);
            this.buscar();
            try {
                this.cargarComboBoxPaises();
            } catch (PersistenceException ex) {
                log.error(ResourceBundle.getBundle("Mensajes").getString("mensaje_error_acceso_a_datos") + " - " + ex.getMessage());
                JOptionPane.showInternalMessageDialog(this, ResourceBundle.getBundle("Mensajes").getString("mensaje_error_acceso_a_datos"), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_btn_ModificarActionPerformed

    private void chk_Id_FiscalItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chk_Id_FiscalItemStateChanged
        if (chk_Id_Fiscal.isSelected() == true) {
            txt_Id_Fiscal.setEnabled(true);
            txt_Id_Fiscal.requestFocus();
        } else {
            txt_Id_Fiscal.setEnabled(false);
        }
    }//GEN-LAST:event_chk_Id_FiscalItemStateChanged

    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened
        try {
            this.cargarComboBoxPaises();
            this.setColumnas();
            this.setMaximum(true);
            String mensaje = "No existe ningun Cliente establecido como Predeterminado,\ndebe "
                    + "establecer uno para poder utilizar el T.P.V. (Terminal Punto de Venta).";
            if (!clienteService.getClientes(empresaService.getEmpresaActiva().getEmpresa()).isEmpty()) {
                if (clienteService.getClientePredeterminado(empresaService.getEmpresaActiva().getEmpresa()) == null) {
                    JOptionPane.showInternalMessageDialog(this, mensaje, "Aviso", JOptionPane.WARNING_MESSAGE);
                }
            } else {
                JOptionPane.showInternalMessageDialog(this, mensaje, "Aviso", JOptionPane.WARNING_MESSAGE);
            }

        } catch (PersistenceException ex) {
            log.error(ResourceBundle.getBundle("Mensajes").getString("mensaje_error_acceso_a_datos") + " - " + ex.getMessage());
            JOptionPane.showInternalMessageDialog(this, ResourceBundle.getBundle("Mensajes").getString("mensaje_error_acceso_a_datos"), "Error", JOptionPane.ERROR_MESSAGE);


        } catch (PropertyVetoException ex) {
            String msjError = "Se produjo un error al intentar maximizar la ventana.";
            log.error(msjError + " - " + ex.getMessage());
            JOptionPane.showInternalMessageDialog(this, msjError, "Error", JOptionPane.ERROR_MESSAGE);
            this.dispose();
        }
    }//GEN-LAST:event_formInternalFrameOpened

    private void btn_setPredeterminadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_setPredeterminadoActionPerformed
        if (tbl_Resultados.getSelectedRow() != -1) {
            int indexFilaSeleccionada = Utilidades.getSelectedRowModelIndice(tbl_Resultados);

            try {
                if (clienteService.getClientePorId(clientes.get(indexFilaSeleccionada).getId_Cliente()) != null) {
                    clienteService.setClientePredeterminado(clientes.get(indexFilaSeleccionada));
                    btn_BuscarActionPerformed(evt);
                } else {
                    JOptionPane.showInternalMessageDialog(this, "No se encontro el Cliente"
                            + " seleccionado, puede que haya sido eliminado desde otro puesto"
                            + " de trabajo SIC.\nActualize la lista y vuelva a intentarlo.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }

            } catch (PersistenceException ex) {
                log.error(ResourceBundle.getBundle("Mensajes").getString("mensaje_error_acceso_a_datos") + " - " + ex.getMessage());
                JOptionPane.showInternalMessageDialog(this, ResourceBundle.getBundle("Mensajes").getString("mensaje_error_acceso_a_datos"), "Error", JOptionPane.ERROR_MESSAGE);
            }

        } else {
            JOptionPane.showInternalMessageDialog(this, "Debe seleccionar un Cliente antes de realizar la operacion.",
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btn_setPredeterminadoActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_Buscar;
    private javax.swing.JButton btn_Eliminar;
    private javax.swing.JButton btn_Modificar;
    private javax.swing.JButton btn_Nuevo;
    private javax.swing.JButton btn_setPredeterminado;
    private javax.swing.JCheckBox chk_Id_Fiscal;
    private javax.swing.JCheckBox chk_RazonSocialNombreFantasia;
    private javax.swing.JCheckBox chk_Ubicacion;
    private javax.swing.JComboBox cmb_Localidad;
    private javax.swing.JComboBox cmb_Pais;
    private javax.swing.JComboBox cmb_Provincia;
    private javax.swing.JLabel lbl_cantResultados;
    private javax.swing.JPanel panelFiltros;
    private javax.swing.JPanel panelResultados;
    private javax.swing.JScrollPane sp_Resultados;
    private javax.swing.JTable tbl_Resultados;
    private javax.swing.JTextField txt_Id_Fiscal;
    private javax.swing.JTextField txt_RazonSocialNombreFantasia;
    // End of variables declaration//GEN-END:variables
}
