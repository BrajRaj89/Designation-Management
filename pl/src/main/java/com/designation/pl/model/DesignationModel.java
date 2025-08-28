package com.designation.pl.model;
import com.designation.bl.interfaces.managers.*;
import com.designation.bl.interfaces.pojo.*;
import com.designation.bl.managers.*;
import com.designation.bl.exceptions.*;
import java.util.*;
import javax.swing.table.*;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.*;
import com.itextpdf.io.font.constants.*;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.property.*;
import com.itextpdf.layout.borders.*;
import com.itextpdf.kernel.font.*;
import com.itextpdf.io.image.*;
import java.io.*;
public class DesignationModel extends AbstractTableModel
{
private java.util.List<DesignationInterface> designations;
DesignationManagerInterface designationManager;
Set<DesignationInterface> blDesignations;
private String []columnTitle;
public DesignationModel()
{
populateDataStructures();
}
private void populateDataStructures()
{
this.columnTitle = new String[2];
this.columnTitle[0] = "S.no";
this.columnTitle[1] = "Designation";
try
{
designationManager = DesignationManager.getDesignationManager();
blDesignations = designationManager.getDesignations();
}catch(BLException blException)
{
//do nothing
}
this.designations = new LinkedList<>();
for(DesignationInterface designation:blDesignations)
{
this.designations.add(designation);
}
Collections.sort(this.designations,new Comparator<DesignationInterface>()
{
public int compare(DesignationInterface left,DesignationInterface right)
{
return left.getTitle().toUpperCase().compareTo(right.getTitle().toUpperCase());
}
});
}
public int getRowCount()
{
return this.designations.size();
}
public int getColumnCount()
{
return this.columnTitle.length;
}
public String getColumnName(int columnIndex)
{
return columnTitle[columnIndex];
}
public Object getValueAt(int rowIndex,int columnIndex)
{
if(columnIndex==0) return rowIndex+1;
return this.designations.get(rowIndex).getTitle();
}
public Class<?> getColumnClass(int columnIndex)
{
if(columnIndex==0) return Integer.class;
return String.class;
}
public boolean isCellEditable(int rowIndex,int columnIndex)
{
return false;
}
// Application specific methods 
public void add(DesignationInterface designation) throws BLException
{
designationManager.addDesignation(designation);
this.designations.add(designation);
Collections.sort(this.designations,new Comparator<DesignationInterface>()
{
public int compare(DesignationInterface left,DesignationInterface right)
{
return left.getTitle().toUpperCase().compareTo(right.getTitle().toUpperCase());
}
});
fireTableDataChanged();
}
public int indexOfDesignation(DesignationInterface designation) throws BLException
{
int index =0;
DesignationInterface d;
Iterator<DesignationInterface> iterator = this.designations.iterator();
while(iterator.hasNext())
{
d = iterator.next();
if(d.equals(designation))
{
return index;
}
index++;
}
BLException blException = new BLException();
blException.setGenericException("invalid designation: "+designation.getTitle());
throw blException;
}
public int indexOfTitle(String title,boolean partialLeftSearch) throws BLException
{
int index =0;
Iterator<DesignationInterface> iterator = this.designations.iterator();
DesignationInterface d;
while(iterator.hasNext())
{
d = iterator.next();
if(partialLeftSearch)
{
if(d.getTitle().toUpperCase().startsWith(title.toUpperCase()))
{
return index;
}
}
else
{
if(d.getTitle().equals(title))
{
return index;
}
}
index++;
}
BLException blException = new BLException();
blException.setGenericException("invalid designation: "+title);
throw blException;
}
public void update(DesignationInterface designation) throws BLException
{
designationManager.updateDesignation(designation);
this.designations.remove(indexOfDesignation(designation));
this.designations.add(designation);
Collections.sort(this.designations,new Comparator<DesignationInterface>(){
public int compare(DesignationInterface left,DesignationInterface right)
{
return left.getTitle().toUpperCase().compareTo(right.getTitle().toUpperCase());
}
});
fireTableDataChanged();
}
public void remove(int code) throws BLException
{
designationManager.removeDesignation(code);
Iterator<DesignationInterface> iterator = this.designations.iterator();
int index = 0;
while(iterator.hasNext())
{
if(iterator.next().getCode()==code) break;
index++;
}
if(index==this.designations.size())
{
BLException blException = new BLException();
blException.setGenericException("invalid designation code: "+code);
throw blException;
}
this.designations.remove(index);
fireTableDataChanged();
}
public DesignationInterface getDesignationAt(int index) throws BLException
{
if(index<0 || index>=this.designations.size())
{
BLException blException;
blException = new BLException();
blException.setGenericException("Invalid index "+index);
throw blException;
}
return this.designations.get(index);
}
public void exportToPDF(File file) throws BLException
{
try
{
if(file.exists()) file.delete();
PdfWriter pdfWriter = new PdfWriter(file);
PdfDocument pdfDocument = new PdfDocument(pdfWriter);
Document doc = new Document(pdfDocument);
//create header
Image logo = new Image(ImageDataFactory.create(this.getClass().getResource("/icons/logo.png")));
Paragraph logoPara = new Paragraph();
logoPara.add(logo);
Paragraph companyNamePara = new Paragraph();
companyNamePara.add("ABCD corporation");
PdfFont companyNameFont = PdfFontFactory.createFont(StandardFonts.TIMES_BOLD);
companyNamePara.setFont(companyNameFont);
companyNamePara.setFontSize(16);
Paragraph reportTitlePara = new Paragraph("List of Designations");
PdfFont reportTitleFont = PdfFontFactory.createFont(StandardFonts.TIMES_BOLD);
reportTitlePara.setFont(reportTitleFont);
reportTitlePara.setFontSize(15);
PdfFont pageNumberFont = PdfFontFactory.createFont(StandardFonts.TIMES_BOLD);
PdfFont columnTitleFont  = PdfFontFactory.createFont(StandardFonts.TIMES_BOLD);
PdfFont dataFont = PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN);
Paragraph columnTitle1 = new Paragraph("S.No");
columnTitle1.setFont(columnTitleFont);
columnTitle1.setFontSize(14);
Paragraph columnTitle2 = new Paragraph("Designations");
columnTitle2.setFont(columnTitleFont);
columnTitle2.setFontSize(14);
Paragraph pageNumberParagraph;
Paragraph dataParagraph;
float topTableColumnWidths[] ={1,5};
float dataTableColumnWidths[] ={1,5};
int sno,x,pageSize;
pageSize =5;
boolean newPage = true;
Table pageNumberTable;
Table topTable;
Table dataTable = null;
Cell cell;
int numberOfPages = this.designations.size()/pageSize;
if((this.designations.size()%pageSize)!=0) numberOfPages++;
int pageNumber =0;
DesignationInterface designation;
sno =0;
x=0;
while(x<this.designations.size())
{
if(newPage==true)
{
//create new page header
pageNumber++;
topTable = new Table(UnitValue.createPercentArray(topTableColumnWidths));
cell = new Cell();
cell.setBorder(Border.NO_BORDER);
cell.add(logoPara);
topTable.addCell(cell);
cell = new Cell();
cell.add(companyNamePara);
cell.setBorder(Border.NO_BORDER);
cell.setVerticalAlignment(VerticalAlignment.MIDDLE);
topTable.addCell(cell);
doc.add(topTable);
pageNumberParagraph = new Paragraph("page: "+pageNumber+"/"+numberOfPages);
pageNumberParagraph.setFont(pageNumberFont);
pageNumberParagraph.setFontSize(13);
pageNumberTable = new Table(1);
pageNumberTable.setWidth(UnitValue.createPercentValue(100));
cell = new Cell();
cell.setBorder(Border.NO_BORDER);
cell.add(pageNumberParagraph);
cell.setTextAlignment(TextAlignment.RIGHT);
pageNumberTable.addCell(cell);
doc.add(pageNumberTable);
dataTable =new Table(UnitValue.createPercentArray(dataTableColumnWidths));
dataTable.setWidth(UnitValue.createPercentValue(100));
cell = new Cell(1,2);
cell.add(reportTitlePara);
cell.setTextAlignment(TextAlignment.CENTER);
dataTable.addHeaderCell(cell);
dataTable.addHeaderCell(columnTitle1);
dataTable.addHeaderCell(columnTitle2);
newPage = false;
}
designation  = this.designations.get(x);
sno++;
cell = new Cell();
dataParagraph  = new Paragraph(String.valueOf(sno));
dataParagraph.setFont(dataFont);
dataParagraph.setFontSize(14);
cell.add(dataParagraph);
cell.setTextAlignment(TextAlignment.RIGHT);
dataTable.addCell(cell);
cell  = new Cell();
dataParagraph = new Paragraph(designation.getTitle());
dataParagraph.setFont(dataFont);
dataParagraph.setFontSize(14);
cell.add(dataParagraph);
dataTable.addCell(cell);
x++;
if(sno%pageSize==0 || x==this.designations.size())
{
// create footer
doc.add(dataTable);
doc.add(new Paragraph("Software: by @ABC_Groups"));
if(x<this.designations.size())
{
// add new page to document
doc.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
newPage = true;
}
}
}
doc.close();
}catch(Exception exception)
{
BLException blException;
blException = new BLException();
blException.setGenericException(exception.getMessage());
throw blException;
}
}
}