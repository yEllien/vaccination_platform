package View;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import View.GraphicsComponents.TextField;

class DataTable extends JScrollPane {
	
	Color backgroundColor;
	Color panelColor;
	
	DefaultTableModel model;
	JTable table;
	
	int maxHeight = 450;
	int minWidth = 650;
	
	int CONFIRM = 4;
	
	String[] titles = new String [] {
			"SSN",
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
		//table.setBorder(BorderFactory.createLineBorder(panelColor, 2));
	
		
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
			        String columnName = model.getColumnName(column);
			        Boolean checked = (Boolean) model.getValueAt(row, column);
			        if (checked) {
			            System.out.println(columnName + ": " + true);
			        } else {
			            System.out.println(columnName + ": " + false);
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
	
}

public class MedicalUserPage extends UserPage {

	DataPanel dataPanel;
	
	String lastSearch;
	
	MedicalUserPage(VaccinationPlatformGUI frame) {
		super(frame);
		//loadAccountInfo();
		//loadOptions();
	}
	
	void loadAccountInfo () {
		//TODO get name
		leftPanel.accountName.setText("Employee name");
		dataPanel.loadData();
	}

	@Override
	void setUp() {
		
		createAccountTab();

		//createOptionAndPage("Appointments");
		createAppointmentsTab();
	}
	
	void createAccountTab () {
		createOptionAndPage("Account");
		selectOption(0);
		dataPanel = new DataPanel (
				new String[] {
						"First Name",
						"Last name",
						"Employee ID",
						"Employer ID",
						"e-mail",
						"Phone number"
				},
				panelColor, backgroundColor);
		mainPanel.addToContentPanel(0, dataPanel);
		dataPanel.init();
	}
	
	void createAppointmentsTab () {
		createOptionAndPage("Appointments");
		
		JPanel searchPanel = new JPanel();
		searchPanel.setBackground(panelColor);
		searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.X_AXIS));
			
		TextField search = new TextField();
		search.setPlaceholder("");
		
		String [] searchModes = {"SSN", "Date"};
		JComboBox <String> searchMode = new JComboBox <String> (new DefaultComboBoxModel<String>(searchModes));
		searchMode.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String s = (String) searchMode.getSelectedItem();
				
				switch(s) {
				case "SSN" :
					search.setPlaceholder("e.g. 18010112345");
					break;
				case "Date" :
					search.setPlaceholder("e.g. 18-01-2001");
					break;
				}
			}
			
		});

		lastSearch = "";
		JButton go = new JButton("go");
		go.setBackground(backgroundColor);
		
		JButton cancel = new JButton ("cancel");
		cancel.setBackground(backgroundColor);
		
		go.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				
				String s = (String) searchMode.getSelectedItem();
				String target = search.getText();
				cancel.setVisible(true);
				go.setVisible(false);
				lastSearch = target;
				
				switch(s) {
				case "SSN":
					//lookup database by ssn
				case "Date":
					//lookup database by date
				}
			}
			
		});
		
		cancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				//reload all data, undo search
				cancel.setVisible(false);
				search.setText("");
				lastSearch = "";
			}
			
		});
		
		search.getDocument().addDocumentListener(new DocumentListener() {
			  public void changedUpdate(DocumentEvent e) {
				  updateButtons();
			  }
			  public void removeUpdate(DocumentEvent e) {
				  updateButtons();
			  }
			  public void insertUpdate(DocumentEvent e) {
				  updateButtons();
			  }
			  
			  public void updateButtons () {
				  if (search.getText().equals("")) {
					  go.setVisible(false);
					  return;
				  }
				  
				  if (!lastSearch.equals("")) {
						cancel.setVisible(true); 
				  }
				  
				  if (lastSearch.equals(search.getText())) {
					  go.setVisible(false);
				  }
				  else go.setVisible(true);
			  }
			});
		
		JLabel label = new JLabel();
		label.setText("Search by ");
		label.setBorder(BorderFactory.createEmptyBorder(0,20,0,10));
		
		searchMode.setBorder(BorderFactory.createEmptyBorder(0,0,0,0));
		
		search.setBorder(
				BorderFactory.createCompoundBorder(
						BorderFactory.createMatteBorder(0, 30, 0, 0, panelColor),
						BorderFactory.createMatteBorder(2,6,2,6, backgroundColor)
						));
		
		go.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		cancel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		searchPanel.setBorder(BorderFactory.createEmptyBorder(15,0,10,30));
		
		searchPanel.add(Box.createGlue());
		searchPanel.add(label);
		searchPanel.add(searchMode);
		searchPanel.add(Box.createGlue());
		searchPanel.add(search);
		searchPanel.add(cancel);
		searchPanel.add(go);
		searchPanel.add(Box.createGlue());
		
		searchMode.setSelectedIndex(0);
		
		
		//JTable table = new JTable(model);
		DataTable table = new DataTable(backgroundColor, panelColor);
		
		JPanel tmp = new JPanel();
		tmp.setBackground(backgroundColor);
		tmp.setLayout(new BoxLayout(tmp, BoxLayout.Y_AXIS));
		
		tmp.add(searchPanel);
		tmp.add(table);
		
		mainPanel.addToContentPanel(1, tmp);
		table.updateViewportSize();
		
		table.addRow(new Object[] {"1234", "Medical Center", "12-02-2022", "12:00", false});
		
		//	mainPanel.addToContentPanel(1, table);
		table.addRow(new Object[] {"2345", "Medical Center", "13-02-2022", "08:00", false});
		table.addRow(new Object[] {"2345", "Medical Center", "13-02-2022", "08:00", false});
		table.addRow(new Object[] {"2345", "Medical Center", "13-02-2022", "08:00", false});
		table.addRow(new Object[] {"2345", "Medical Center", "13-02-2022", "08:00", false});
		table.addRow(new Object[] {"2345", "Medical Center", "13-02-2022", "08:00", false});
		table.addRow(new Object[] {"2345", "Medical Center", "13-02-2022", "08:00", false});
		table.addRow(new Object[] {"2345", "Medical Center", "13-02-2022", "08:00", false});
		table.addRow(new Object[] {"2345", "Medical Center", "13-02-2022", "08:00", false});
		table.addRow(new Object[] {"2345", "Medical Center", "13-02-2022", "08:00", false});
		table.addRow(new Object[] {"2345", "Medical Center", "13-02-2022", "08:00", false});
		table.addRow(new Object[] {"2345", "Medical Center", "13-02-2022", "08:00", false});
		table.addRow(new Object[] {"2345", "Medical Center", "13-02-2022", "08:00", false});
		table.addRow(new Object[] {"2345", "Medical Center", "13-02-2022", "08:00", false});
		table.addRow(new Object[] {"2345", "Medical Center", "13-02-2022", "08:00", false});
		table.addRow(new Object[] {"2345", "Medical Center", "13-02-2022", "08:00", false});
		table.addRow(new Object[] {"2345", "Medical Center", "13-02-2022", "08:00", false});
		table.addRow(new Object[] {"2345", "Medical Center", "13-02-2022", "08:00", false});
		table.addRow(new Object[] {"2345", "Medical Center", "13-02-2022", "08:00", false});
		table.addRow(new Object[] {"2345", "Medical Center", "13-02-2022", "08:00", false});
		table.addRow(new Object[] {"2345", "Medical Center", "13-02-2022", "08:00", false});
		
		go.setVisible(false);
		cancel.setVisible(false);
		mainPanel.revalidate();
		mainPanel.repaint();
	}

	@Override
	void reload() {
		loadAccountInfo();
	}
}
