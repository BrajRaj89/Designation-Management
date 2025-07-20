package com.designation.bl.interfaces.managers;
import com.designation.bl.exceptions.*;
import com.designation.bl.interfaces.pojo.*;
import java.util.*;
public interface DesignationManagerInterface
{
public void addDesignation(DesignationInterface designation) throws BLException;
public void updateDesignation(DesignationInterface designation) throws BLException;
public void removeDesignation(int code) throws BLException;
public  DesignationInterface getDesignationByCode(int code) throws BLException;
public DesignationInterface getDesignationByTitle(String title) throws BLException;
public int getDesignationCount();
public boolean designationCodeExists(int code);
public boolean designationTitleExists(String title);
public Set<DesignationInterface> getDesignations();

}
