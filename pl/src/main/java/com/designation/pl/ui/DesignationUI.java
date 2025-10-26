package com.designation.pl.ui;
import com.designation.pl.model.*;
import com.designation.bl.exceptions.*;
import com.designation.bl.interfaces.pojo.*;
import com.designation.bl.pojo.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;
import java.io.*;
public class DesignationUI extends JFrame implements DocumentListener,ListSelectionListener
{
private JLabel titleLabel;
private JLabel searchLabel;
private JTextField searchTextField;
private JLabel searchErrorLabel;
private JButton clearSearchTextFieldButton;
private JTable designationTable;
private JScrollPane scrollPane;
private DesignationModel designationModel;
private Container container;
private DesignationPanel designationPanel;
private enum MODE{VIEW,ADD,EDIT,DELETE,EXPORT_TO_PDF};
private  MODE mode;
private ImageIcon logoIcon;
private ImageIcon clearIcon;
private ImageIcon addIcon;
private ImageIcon editIcon;
private ImageIcon cancelIcon;
private ImageIcon deleteIcon;
private ImageIcon saveIcon;
private ImageIcon crossIcon;
private ImageIcon pdfIcon;
private ImageIcon updateIcon;
private JLabel messageLabel;
private Font messageFont;
public DesignationUI()
{
initComponents();
setAppearance();
addListeners();
setViewMode();
designationPanel.setViewMode();
}
private void initComponents()
{
messageFont = new Font("Courier New",Font.BOLD,15);
logoIcon = new ImageIcon(this.getClass().getResource("/icons/logo.png"));
addIcon = new ImageIcon(this.getClass().getResource("/icons/add.png"));
editIcon = new ImageIcon(this.getClass().getResource("/icons/edit.png"));
updateIcon = new ImageIcon(this.getClass().getResource("/icons/update.png"));
cancelIcon = new ImageIcon(this.getClass().getResource("/icons/cancel.png"));
deleteIcon = new ImageIcon(this.getClass().getResource("/icons/delete.png"));
saveIcon = new ImageIcon(this.getClass().getResource("/icons/save.png"));
pdfIcon = new ImageIcon(this.getClass().getResource("/icons/pdf.png"));
crossIcon = new ImageIcon(this.getClass().getResource("/icons/cross.png"));
clearIcon = new ImageIcon(this.getClass().getResource("/icons/clear.png"));
setIconImage(logoIcon.getImage());
designationModel = new DesignationModel();
titleLabel = new JLabel("Designations");
searchLabel = new JLabel("Search");
searchTextField = new JTextField();
clearSearchTextFieldButton = new JButton(clearIcon);
searchErrorLabel = new JLabel("");
designationTable = new JTable(designationModel);
scrollPane = new JScrollPane(designationTable,ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
container = getContentPane();
setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
}
private void setAppearance()
{
Font titleFont = new Font("Verdana",Font.BOLD,18);
Font captionFont = new Font("Verdana",Font.BOLD,16);
Font dataFont = new Font("Verdana",Font.PLAIN,16);
Font searchErrorFont = new Font("Verdana",Font.BOLD,12);
Font columnHeaderFont = new Font("Verdana",Font.BOLD,16);
titleLabel.setFont(titleFont);
searchLabel.setFont(captionFont);
searchTextField.setFont(dataFont);
searchErrorLabel.setFont(searchErrorFont);
searchErrorLabel.setForeground(Color.red);
designationTable.setFont(dataFont);
designationTable.setRowHeight(35);
designationTable.getColumnModel().getColumn(0).setPreferredWidth(20);
designationTable.getColumnModel().getColumn(1).setPreferredWidth(400);
JTableHeader header = designationTable.getTableHeader();
header.setFont(columnHeaderFont);
header.setReorderingAllowed(false);
header.setResizingAllowed(false);
designationTable.setRowSelectionAllowed(true);
designationTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
designationPanel = new DesignationPanel();
container.setLayout(null);
int lm,tm;
lm =0;
tm =0;
searchErrorLabel.setBounds(lm+10+100+400+10-75,tm+10+20+10,100,20);
titleLabel.setBounds(lm+10,tm+10,200,40);
searchLabel.setBounds(lm+10,tm+10+40+10,100,30);
searchTextField.setBounds(lm+10+100+5,tm+10+40+10,400,30);
clearSearchTextFieldButton.setBounds(lm+10+100+400+10,tm+10+40+10,30,30);
scrollPane.setBounds(lm+10,tm+10+40+10+30+10,565,300);
designationPanel.setBounds(lm+10,tm+10+40+10+30+10+300+10,565,200);
container.add(titleLabel);
container.add(searchErrorLabel);
container.add(searchLabel);
container.add(searchTextField);
container.add(clearSearchTextFieldButton);
container.add(scrollPane);
container.add(designationPanel);
int w,h;
w = 600;
h = 660;
setSize(w,h);
Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
setLocation((d.width/2)-(w/2),(d.height/2)-(h/2));
}
private void addListeners()
{
searchTextField.getDocument().addDocumentListener(this);
clearSearchTextFieldButton.addActionListener(new ActionListener()
{
public void actionPerformed(ActionEvent ev)
{
searchTextField.setText(" ");
}
});
designationTable.getSelectionModel().addListSelectionListener(this);
}
public void changedUpdate(DocumentEvent de)
{
searchDesignation();
}
public void removeUpdate(DocumentEvent de)
{
searchDesignation();
}
public void insertUpdate(DocumentEvent de)
{
searchDesignation();
}
public void valueChanged(ListSelectionEvent le)
{
int selectedRowIndex = designationTable.getSelectedRow();
try
{
DesignationInterface designation = designationModel.getDesignationAt(selectedRowIndex);
designationPanel.setDesignation(designation);
}catch(BLException blException)
{
designationPanel.clearDesignation();
}
}
public void searchDesignation()
{
searchErrorLabel.setText("");
String title = searchTextField.getText().trim();
if(title.length()==0) return;
int rowIndex;
try
{
rowIndex = designationModel.indexOfTitle(title,true);
}catch(BLException blException)
{
searchErrorLabel.setText("Not Found");
return;
}
designationTable.setRowSelectionInterval(rowIndex,rowIndex);
Rectangle rectangle = designationTable.getCellRect(rowIndex,0,true);
designationTable.scrollRectToVisible(rectangle);
}
private void setViewMode()
{
this.mode = MODE.VIEW;
if(designationModel.getRowCount()==0)
{
searchTextField.setEnabled(false);
clearSearchTextFieldButton.setEnabled(false);
designationTable.setEnabled(false);
}
else
{
searchTextField.setEnabled(true);
clearSearchTextFieldButton.setEnabled(true);
designationTable.setEnabled(true);
}
}
private void setAddMode()
{
this.mode = MODE.ADD;
searchTextField.setEnabled(false);
clearSearchTextFieldButton.setEnabled(false);
designationTable.setEnabled(false);
}
private void setEditMode()
{
this.mode = MODE.EDIT;
searchTextField.setEnabled(false);
clearSearchTextFieldButton.setEnabled(false);
designationTable.setEnabled(false);
}
private void setDeleteMode()
{
this.mode = MODE.DELETE;
searchTextField.setEnabled(false);
clearSearchTextFieldButton.setEnabled(false);
designationTable.setEnabled(false);
}
private void setExportToPDFMode()
{
this.mode = MODE.EXPORT_TO_PDF;
searchTextField.setEnabled(false);
clearSearchTextFieldButton.setEnabled(false);
designationTable.setEnabled(false); 
}
//inner class starts
class DesignationPanel extends JPanel
{
private JLabel titleCaptionLabel;
private JLabel  titleLabel;
private JTextField titleTextField;
private JButton clearTitleTextFieldButton;
private JButton addButton;
private JButton editButton;
private JButton cancelButton;
private JButton deleteButton;
private JButton exportToPDFButton;
private JPanel buttonsPanel;
private DesignationInterface designation;
DesignationPanel()
{
setBorder(BorderFactory.createLineBorder(new Color(165,165,175)));
initComponents();
setAppearance();
addListeners();
}
public void setDesignation(DesignationInterface designation)
{
this.designation = designation;
titleLabel.setText(designation.getTitle());
}
public void clearDesignation()
{
this.designation = null;
titleLabel.setText(" ");
}
private void initComponents()
{
designation = null;
titleCaptionLabel = new JLabel("Designation");
titleLabel = new JLabel(" ");
titleTextField = new JTextField();
buttonsPanel = new JPanel();
clearTitleTextFieldButton = new JButton(crossIcon);
addButton = new JButton(addIcon);
editButton = new JButton(editIcon);
cancelButton = new JButton(cancelIcon);
deleteButton = new JButton(deleteIcon);
exportToPDFButton = new JButton(pdfIcon);
}
private void setAppearance()
{
Font captionFont = new Font("Verdana",Font.BOLD,16);
Font dataFont = new Font("Verdana",Font.PLAIN,16);
titleCaptionLabel.setFont(captionFont);
titleLabel.setFont(dataFont);
titleTextField.setFont(dataFont);
setLayout(null);
int lm,tm;
lm = 0;
tm =0;
titleCaptionLabel.setBounds(lm+10,tm+20,110,30);
titleLabel.setBounds(lm+110+10,tm+20,400,30);
titleTextField.setBounds(lm+10+110,tm+20,350,30);
clearTitleTextFieldButton.setBounds(lm+10+110+5+350+5,lm+20,30,30);
buttonsPanel.setBounds(50,tm+20+30+30,465,75);
buttonsPanel.setBorder(BorderFactory.createLineBorder(new Color(165,165,165)));
addButton.setBounds(70,12,50,50);
editButton.setBounds(70+50+20,12,50,50);
cancelButton.setBounds(70+50+20+50+20,12,50,50);
deleteButton.setBounds(70+50+20+50+20+50+20,12,50,50);
exportToPDFButton.setBounds(70+50+20+50+20+50+20+50+20,12,50,50);
buttonsPanel.setLayout(null);
buttonsPanel.add(addButton);
buttonsPanel.add(editButton);
buttonsPanel.add(cancelButton);
buttonsPanel.add(deleteButton);
buttonsPanel.add(exportToPDFButton);
add(titleCaptionLabel);
add(titleTextField);
add(titleLabel);
add(clearTitleTextFieldButton);
add(buttonsPanel);
}
private boolean addDesignation()
{
String title = titleTextField.getText().trim();
if(title.length()==0)
{
messageLabel = new JLabel("Designation required");
messageLabel.setFont(messageFont);
JOptionPane.showMessageDialog(null,messageLabel);
titleTextField.requestFocus();
return false;
}
DesignationInterface d = new Designation();
d.setTitle(title);
int rowIndex =0;
try
{
designationModel.add(d);
try
{
rowIndex = designationModel.indexOfDesignation(d);
}catch(BLException blException)
{
// do nothing
}
designationTable.setRowSelectionInterval(rowIndex,rowIndex);
Rectangle rectangle = designationTable.getCellRect(rowIndex,0,true);
designationTable.scrollRectToVisible(rectangle);
return true;
}catch(BLException blException)
{
if(blException.hasGenericException())
{
messageLabel = new JLabel(blException.getGenericException());
messageLabel.setFont(messageFont);
JOptionPane.showMessageDialog(null,messageLabel);
return false;
}
else
{
if(blException.hasException("title"))
{
messageLabel = new JLabel(blException.getException("title"));
messageLabel.setFont(messageFont);
JOptionPane.showMessageDialog(null,messageLabel);
}
}
titleTextField.requestFocus();
return false;
}
} 
private boolean updateDesignation()
{
String title = titleTextField.getText().trim();
if(title.length()==0)
{
messageLabel = new JLabel("Designation required");
messageLabel.setFont(messageFont);
JOptionPane.showMessageDialog(null,messageLabel);
titleTextField.requestFocus();
return false;
}
DesignationInterface d = new Designation();
d.setCode(this.designation.getCode());
d.setTitle(title);
int rowIndex =0;
try
{
designationModel.update(d);
try
{
rowIndex = designationModel.indexOfDesignation(d);
}catch(BLException blException)
{
// do nothing
}
designationTable.setRowSelectionInterval(rowIndex,rowIndex);
Rectangle rectangle = designationTable.getCellRect(rowIndex,0,true);
designationTable.scrollRectToVisible(rectangle);
return true;
}catch(BLException blException)
{
if(blException.hasGenericException())
{
messageLabel = new JLabel(blException.getGenericException());
messageLabel.setFont(messageFont);
JOptionPane.showMessageDialog(null,messageLabel);
}
else
{
if(blException.hasException("title"))
{
messageLabel  = new JLabel(blException.getException("title"));
messageLabel.setFont(messageFont);
JOptionPane.showMessageDialog(null,messageLabel);
}
}
titleTextField.requestFocus();
return false;
}
}
private void removeDesignation()
{
try
{
String title = this.designation.getTitle();
messageLabel = new JLabel("Delete Designation :"+title);
messageLabel.setFont(messageFont);
int selectedOption  = JOptionPane.showConfirmDialog(null
,messageLabel,"Confirmation",JOptionPane.YES_NO_OPTION);
if(selectedOption==JOptionPane.NO_OPTION) return;
if(selectedOption==JOptionPane.CLOSED_OPTION) return;
designationModel.remove(this.designation.getCode());
messageLabel = new JLabel("Designation :"+title+" deleted");
messageLabel.setFont(messageFont);
JOptionPane.showMessageDialog(this,messageLabel);
}catch(BLException blException)
{
if(blException.hasGenericException())
{
messageLabel = new JLabel(blException.getGenericException());
messageLabel.setFont(messageFont);
JOptionPane.showMessageDialog(null,messageLabel);
}
else
{
if(blException.hasException("title"))
{
JOptionPane.showMessageDialog(null,blException.getException("title"));
messageLabel = new JLabel(blException.getException("title"));
messageLabel.setFont(messageFont);
JOptionPane.showMessageDialog(null,messageLabel);
}
}
titleTextField.requestFocus();
}
}

private void addListeners()
{
this.exportToPDFButton.addActionListener(new ActionListener()
{
public void actionPerformed(ActionEvent ae)
{
JFileChooser jfc = new JFileChooser();
jfc.setCurrentDirectory(new File("."));
int selectedOption = jfc.showSaveDialog(DesignationUI.this);
if(selectedOption==JFileChooser.APPROVE_OPTION)
{
try
{
File selectedFile = jfc.getSelectedFile();
String pdfFile = selectedFile.getAbsolutePath();
if(pdfFile.endsWith(".")) pdfFile = pdfFile+"pdf";
if(pdfFile.endsWith(".pdf")==false) pdfFile = pdfFile+".pdf";
File file = new File(pdfFile);
File parent = new File(file.getParent());
if(parent.exists()==false|| parent.isDirectory()==false)
{
messageLabel = new JLabel("Incorrect path "+file.getAbsolutePath());
messageLabel.setFont(messageFont);
JOptionPane.showMessageDialog(null,messageLabel);
return;
}
designationModel.exportToPDF(file);
messageLabel = new JLabel("<html><body width='300px'>PDF Created<br> Location: "+file.getAbsolutePath()+"<body></html>");
messageLabel.setFont(messageFont);
JOptionPane.showMessageDialog(null,messageLabel); 
}catch(BLException blException)
{
if(blException.hasGenericException())
{
messageLabel = new JLabel(blException.getGenericException());
messageLabel.setFont(messageFont);
JOptionPane.showMessageDialog(null,messageLabel);
}
}
}
}
});
this.addButton.addActionListener(new ActionListener(){
public void actionPerformed(ActionEvent ae)
{
if(mode== MODE.VIEW)
{
setAddMode();
}
else
{
if(addDesignation())
{
setViewMode();
}
}
}
});
this.editButton.addActionListener(new ActionListener()
{
public void actionPerformed(ActionEvent ae)
{
if(mode==MODE.VIEW)
{
setEditMode();
}
else
{
if(updateDesignation())
{
setViewMode();
}
}
}
});
this.deleteButton.addActionListener(new ActionListener()
{
public void actionPerformed(ActionEvent ae)
{
setDeleteMode();
}
});
this.cancelButton.addActionListener(new ActionListener()
{
public void actionPerformed(ActionEvent ae)
{
setViewMode();
}
});
this.exportToPDFButton.addActionListener(new ActionListener()
{
public void actionPerformed(ActionEvent ae)
{
setViewMode();
}
});
}
void setViewMode()
{
DesignationUI.this.setViewMode();
this.titleTextField.setVisible(false);
this.addButton.setIcon(addIcon);
this.editButton.setIcon(editIcon);
this.clearTitleTextFieldButton.setVisible(false);
this.titleLabel.setVisible(true);
this.addButton.setEnabled(true);
this.cancelButton.setEnabled(false);
if(designationModel.getRowCount()>0)
{
this.editButton.setEnabled(true);
this.deleteButton.setEnabled(true);
this.exportToPDFButton.setEnabled(true);
}
else
{
this.editButton.setEnabled(false);
this.deleteButton.setEnabled(false);
this.exportToPDFButton.setEnabled(false);
}
}
void setAddMode()
{
DesignationUI.this.setAddMode();
this.clearTitleTextFieldButton.setVisible(true);
this.titleTextField.setText("");
this.titleLabel.setVisible(true);
this.titleTextField.setVisible(true);
addButton.setIcon(saveIcon);
editButton.setEnabled(false);
cancelButton.setEnabled(true);
deleteButton.setEnabled(false);
exportToPDFButton.setEnabled(false);
}
void setEditMode()
{
if(designationTable.getSelectedRow()<0 || designationTable.getSelectedRow()>=designationModel.getRowCount())
{
messageLabel = new JLabel("Select Designation to delete");
messageLabel.setFont(messageFont);
JOptionPane.showMessageDialog(null,messageLabel);
return;
}
DesignationUI.this.setEditMode();
this.titleTextField.setText(this.designation.getTitle());
this.titleLabel.setVisible(false);
this.titleTextField.setVisible(true);
addButton.setEnabled(false);
cancelButton.setEnabled(true);
deleteButton.setEnabled(false);
exportToPDFButton.setEnabled(false);
editButton.setEnabled(true);
editButton.setIcon(updateIcon);
}
void setDeleteMode()
{
if(designationTable.getSelectedRow()<0 || designationTable.getSelectedRow()>=designationModel.getRowCount())
{
messageLabel = new JLabel("Select Designation to delete");
messageLabel.setFont(messageFont);
JOptionPane.showMessageDialog(null,messageLabel);
return;
}
DesignationUI.this.setDeleteMode();
this.titleLabel.setVisible(false);
this.titleTextField.setVisible(true);
addButton.setEnabled(false);
editButton.setEnabled(false);
cancelButton.setEnabled(false);
deleteButton.setEnabled(false);
exportToPDFButton.setEnabled(false);
removeDesignation();
DesignationUI.this.setViewMode();
this.setViewMode();
}
void setExportToPDFMode()
{
DesignationUI.this.setExportToPDFMode();
addButton.setEnabled(false);
editButton.setEnabled(false);
cancelButton.setEnabled(false);
deleteButton.setEnabled(false);
exportToPDFButton.setEnabled(false);
}
}
}