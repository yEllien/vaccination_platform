package View;

import java.awt.Color;
import java.awt.Dimension;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;

import Controller.db;

public 
class DataTable extends JScrollPane {
	
	Color backgroundColor;
	Color panelColor;
	
	DefaultTableModel model;
	JTable table;
	
	int maxHeight = 450;
	int minWidth = 650;
	
	int CONFIRM = 5;
	
	String[] titles = new String [] {
			"SSN",
			"Dose Number",
			"Name",
			"Date",
			"Time",
			"Confirm"
	};
	
	Class[] dataClasses = new Class[] {
			String.class,
			String.class,
			String.class,
			String.class,
			String.class,
			Boolean.class
	};
	
	DataTable (Color backgroundColor, Color panelColor) {
		
		this.backgroundColor = backgroundColor;
		this.panelColor = panelColor;
		
		model = new DefaultTableModel() {
			public Class getColumnClass (int columnIndex) {
				return dataClasses[columnIndex];
			}
		};
		
		model.setColumnIdentifiers(titles);
		
		table = new JTable(model);
		table.setShowGrid(false);
		table.setAlignmentX(SwingConstants.CENTER);
		table.setRowHeight(table.getRowHeight()+20);
		table.setBackground(backgroundColor);
		
		JTableHeader h = table.getTableHeader();
		h.setOpaque(false);
		h.setBackground(panelColor);
		h.setPreferredSize(new Dimension (
				h.getPreferredSize().width, 
				h.getPreferredSize().height+20));
		table.setTableHeader(h);

		this.getViewport().add(table);
		this.setBackground(backgroundColor);
		this.setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createLineBorder(panelColor, 2),
					BorderFactory.createEmptyBorder(0,10,0,0)
				));
		
		table.getModel().addTableModelListener(new TableModelListener() {

			@Override
			public void tableChanged(TableModelEvent e) {
				int row = e.getFirstRow();
			    int column = e.getColumn();
			    if (column == CONFIRM) {
			        TableModel model = (TableModel) e.getSource();
			        String columnName = model.getColumnName	(column);
			        Boolean checked = (Boolean) model.getValueAt(row, column);
			        if (checked) {
			        	String message = "Log vaccination for citizen with SSN:" + model.getValueAt(row, 0) + "?";
			        	int n = JOptionPane.showConfirmDialog(
			        			table, 
			        			message, 
			        			"Confirm Vaccination", 
			        			JOptionPane.YES_NO_OPTION);
			        	
			        	if (n == JOptionPane.YES_OPTION) {
			        		//TODO log citizen as vaccinated
			        		
			        		try {
			        			db database = new db();
			        			database.init();
								
			        			database.ConfirmVaccination(model.getValueAt(row, 0).toString(), Integer.parseInt((String) model.getValueAt(row, 1)));
			        			
			        			database.con.close();
							} catch (SQLException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
			        	}
			        	else {
			        		model.setValueAt(false, row, column);			        		
			        	}
			        	
			            //System.out.println(columnName + ": " + true);
			        } else {
			        	//TODO undo vaccination			        		
			        }
			    }
			}
		});
	}
	
	void updateViewportSize () {
		
		
		int height = table.getRowHeight()*table.getRowCount()
				+table.getTableHeader().getPreferredSize().height;
		this.setPreferredSize(
			new Dimension(minWidth, 
						  height<maxHeight?height:maxHeight));
		
	}
	
	void addRow (Object[] data) {
		model.addRow(data);
		updateViewportSize();
	}
	
	void loadArray(Object[][] data) {
		System.out.println("ROW COUNT BEFORE ADDING ANY DATA" + model.getRowCount());
		int size = model.getRowCount();
		
		for (int i=0; i<size; i++) {
			model.removeRow(0);
			System.out.println("REMOVING ROW " + i);
		}
		
		for (Object[] row : data) {
			model.addRow(row);
		}
	}
	
}