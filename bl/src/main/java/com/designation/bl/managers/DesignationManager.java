package com.designation.bl.managers;
import com.designation.bl.interfaces.pojo.*;
import com.designation.bl.interfaces.managers.*;
import com.designation.bl.exceptions.*;
import com.designation.bl.pojo.*;
import com.designation.dl.interfaces.dto.*;
import com.designation.dl.interfaces.dao.*;
import com.designation.dl.exceptions.*;
import com.designation.dl.dto.*;
import com.designation.dl.dao.*;
import java.util.*;

public class DesignationManager implements DesignationManagerInterface
{
private Map<Integer,DesignationInterface> codeWiseDesignationMap;
private Map<String,DesignationInterface> titleWiseDesignationMap;
private Set<DesignationInterface> designationsSet;
private static DesignationManagerInterface designationManager = null;
private DesignationManager() throws BLException
{
populateDataStructures();
}
private void populateDataStructures() throws BLException
{   
this.codeWiseDesignationMap = new HashMap<>();
this.titleWiseDesignationMap = new HashMap<>();
this.designationsSet = new TreeSet<>();
try
{
Set<DesignationDTOInterface> dlDesignations;
dlDesignations = new DesignationDAO().getAll();
DesignationInterface designation;
for(DesignationDTOInterface dlDesignation:dlDesignations)
{
designation = new Designation();
designation.setCode(dlDesignation.getCode());
designation.setTitle(dlDesignation.getTitle());
this.codeWiseDesignationMap.put(designation.getCode(),designation);
this.titleWiseDesignationMap.put(designation.getTitle().toUpperCase(),designation);
this.designationsSet.add(designation);
} 
}catch(DAOException daoException)
{
BLException blException = new BLException();
blException.setGenericException(daoException.getMessage());
throw blException;
}
}
public static DesignationManagerInterface getDesignationManager() throws BLException
{
if(designationManager==null) designationManager = new DesignationManager();
return designationManager;
}
public void addDesignation(DesignationInterface designation) throws BLException
{
BLException blException = new BLException();
if(designation==null)
{
blException.setGenericException("Designation required");
throw blException;
}
int code = designation.getCode();
String title = designation.getTitle();
if(code!=0)
{
blException.addException("code","code should be zero");
}
if(title==null)
{
blException.addException("title","title required");
title=" ";
}
else
{
title = title.trim();
if(title.length()==0)
{
blException.addException("title","Title required");
}
}
if(title.length()>0)
{
if(this.titleWiseDesignationMap.containsKey(title.toUpperCase()))
{
blException.addException("title","Designation: "+title+" exists");
}
}
if(blException.hasExceptions())
{
throw blException;
}
try
{
DesignationDTOInterface designationDTO;
designationDTO = new DesignationDTO();
designationDTO.setTitle(title);
DesignationDAOInterface designationDAO;
designationDAO = new DesignationDAO();
designationDAO.add(designationDTO);
code = designationDTO.getCode();
Designation dsDesignation = new Designation();
dsDesignation.setCode(code);
dsDesignation.setTitle(title);
codeWiseDesignationMap.put(code,dsDesignation);
titleWiseDesignationMap.put(title.toUpperCase(),dsDesignation);
designationsSet.add(dsDesignation);
designation.setCode(code);
}catch(DAOException daoException)
{
blException.setGenericException(daoException.getMessage());
throw blException;
}
}
public void updateDesignation(DesignationInterface designation) throws BLException
{
BLException blException = new BLException();
if(designation==null)
{
blException.setGenericException("Designation required");
throw blException;
}
int code = designation.getCode();
String title = designation.getTitle();
if(code<=0)
{
blException.addException("code","invalid code "+code);
}
if(code>0)
{
if(this.codeWiseDesignationMap.containsKey(code)==false)
{
blException.addException("code","Invalid code "+code);
throw blException;
}
}
if(title==null)
{
blException.addException("title","title required");
title=" ";
}
else
{
title = title.trim();
if(title.length()==0)
{
blException.addException("title","Title required");
}
}

if(title.length()>0)
{
if(this.titleWiseDesignationMap.containsKey(title.toUpperCase()))
{
blException.addException("title","Designation: "+title+" exists");
}
}
if(blException.hasExceptions())
{
throw blException;
}
try
{
DesignationInterface dsDesignation = codeWiseDesignationMap.get(code);
DesignationDTOInterface dlDesignation = new DesignationDTO();
dlDesignation.setCode(code);
dlDesignation.setTitle(title);
new DesignationDAO().update(dlDesignation);
//remove the old one from all ds
codeWiseDesignationMap.remove(code);
titleWiseDesignationMap.remove(dsDesignation.getTitle().toUpperCase());
designationsSet.remove(dsDesignation);
// update the DS object
dsDesignation.setTitle(title);
//update the DS
codeWiseDesignationMap.put(code,dsDesignation);
titleWiseDesignationMap.put(title.toUpperCase(),dsDesignation);
 designationsSet.add(dsDesignation);
}catch(DAOException daoException)
{
blException.setGenericException(daoException.getMessage());
throw blException;
}
}
public void removeDesignation(int code) throws BLException
{
BLException blException = new BLException();
if(code<=0)
{
blException.addException("code","Invalid code");
throw blException;
}
if(code>0)
{
if(codeWiseDesignationMap.containsKey(code)==false)
{
blException.addException("code","Invalid code");
throw blException;
}
}
try
{
DesignationInterface dsDesignation;
dsDesignation = codeWiseDesignationMap.get(code);
new DesignationDAO().delete(code);
//remove from DS
codeWiseDesignationMap.remove(code);
titleWiseDesignationMap.remove(dsDesignation.getTitle().toUpperCase());
//remove dsDesignation object
designationsSet.remove(dsDesignation);
}catch(DAOException daoException)
{
blException.setGenericException(daoException.getMessage());
throw blException;
}
}
public DesignationInterface getDSDesignationByCode(int code) 
{
DesignationInterface designation;
designation = codeWiseDesignationMap.get(code);
return designation;
}
public  DesignationInterface getDesignationByCode(int code) throws BLException
{
DesignationInterface designation;
designation = codeWiseDesignationMap.get(code);
if(designation==null)
{
BLException blException;
blException = new BLException();
blException.addException("code","invalid code "+code);
throw blException;
}
DesignationInterface d;
d = new Designation();
d.setCode(designation.getCode());
d.setTitle(designation.getTitle());
return d;
}
public DesignationInterface getDesignationByTitle(String title) throws BLException
{
DesignationInterface designation;
designation = titleWiseDesignationMap.get(title.toUpperCase());
if(designation==null)
{
BLException blException = new BLException();
blException.addException("title","Invalid title "+title);
throw blException;
}
DesignationInterface d;
d = new Designation();
d.setCode(designation.getCode());
d.setTitle(designation.getTitle());
return d;
}
public int getDesignationCount()
{
return designationsSet.size();
}
public boolean designationCodeExists(int code)
{
return codeWiseDesignationMap.containsKey(code);
}
public boolean designationTitleExists(String title)
{
return titleWiseDesignationMap.containsKey(title.toUpperCase());
}
public Set<DesignationInterface> getDesignations()
{
Set<DesignationInterface> designations;
designations = new TreeSet<>();
designationsSet.forEach((designation)->{
DesignationInterface d  =new Designation();
d.setCode(designation.getCode());
d.setTitle(designation.getTitle());
designations.add(d);
});
return designations;
}
}
