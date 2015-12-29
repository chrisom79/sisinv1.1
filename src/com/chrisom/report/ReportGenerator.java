package com.chrisom.report;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.chrisom.sisinv.entity.NotaRemision;

import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;

public class ReportGenerator {
	 public void callingPedido(NotaRemision nr) {
	     try {
	    	 Map<String, Object> parameters = new HashMap<String, Object> ();
	    	 SimpleDateFormat dt = new SimpleDateFormat("dd/MMM/yyyy"); 
	    	 JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(nr.getNotaRemisionDetalles());
	    	 
	    	 parameters.put("FECHA", dt.format(new Date()));
	    	 parameters.put("CLIENTE_NOMBRE", nr.getNombre());
	    	 parameters.put("NUMERO", nr.getId());
	    	 parameters.put("VENDEDOR_NOMBRE", nr.getVendedor().getNombre());
	    	 parameters.put("PRODUCTOS", ds);
	    	 
	    	 /*String current = new java.io.File( "." ).getCanonicalPath();
			 System.out.println("Current dir:"+current);
			 String currentDir = System.getProperty("user.dir");
			 System.out.println("Current dir using System:" +currentDir);*/
			 JasperReport report = JasperCompileManager.compileReport("reports/pedido.jrxml");
			 JasperPrint print = JasperFillManager.fillReport(report, parameters, new JREmptyDataSource());
			 JasperExportManager.exportReportToPdfFile(print, "pedido.pdf");
		     
			 if (Desktop.isDesktopSupported()) {
		        File myFile = new File("pedido.pdf");
		        Desktop.getDesktop().open(myFile);
			}
	     } catch (JRException ex) {
	         ex.printStackTrace();
	     }  catch (IOException ex) {
		     ex.printStackTrace();
		 }
	 }
}
