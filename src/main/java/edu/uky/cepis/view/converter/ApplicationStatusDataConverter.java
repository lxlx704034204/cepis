/**
 * 
 */
package edu.uky.cepis.view.converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;

import org.apache.log4j.Logger;
import org.springframework.web.jsf.FacesContextUtils;

import edu.uky.cepis.domain.ApplicationStatus;
import edu.uky.cepis.service.ApplicationStatusService;

/**
 * @author  cawalk4
 */
public class ApplicationStatusDataConverter implements Converter {

	private static Logger log = Logger.getLogger(ApplicationStatusDataConverter.class);
	
	public ApplicationStatusDataConverter() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.faces.convert.Converter#getAsObject(javax.faces.context.FacesContext
	 * , javax.faces.component.UIComponent, java.lang.String)
	 */
	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		if (log.isDebugEnabled()) {
			System.out.println(ApplicationStatusDataConverter.class
					+ ": Calling getAsObject Function with argument " + arg2);
		}
		if (arg0 == null) {
			if (log.isDebugEnabled())
				System.out.println(ApplicationStatusDataConverter.class
						+ ": FacesContext Empty");
		}
		if (arg1 == null) {
			if (log.isDebugEnabled())
				System.out.println(ApplicationStatusDataConverter.class
						+ ": UIComponent is empty");
		}
		if (arg2 == null) {
			if (log.isDebugEnabled())
				System.out.println(ApplicationStatusDataConverter.class
						+ ": String argument is empty");
			return null;
		}
		if (arg2.equals("NA")|| arg2.isEmpty()) {
			return null;
		}

		if (log.isDebugEnabled()) {
			if (arg0.isValidationFailed()) {
				System.out.println(ApplicationStatusDataConverter.class
						+ ": Validation Failed");
			}
		}
		ApplicationStatus applicationStatus = new ApplicationStatus();
		ApplicationStatusService applicationStatusService =  (ApplicationStatusService) FacesContextUtils
				.getWebApplicationContext(arg0).getBean("applicationStatusService");
		applicationStatus = applicationStatusService.findApplicationStatusByCode(new String(arg2));
		return applicationStatus;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.faces.convert.Converter#getAsString(javax.faces.context.FacesContext
	 * , javax.faces.component.UIComponent, java.lang.Object)
	 */
	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
		if (log.isDebugEnabled()) {
			System.out.println(ApplicationStatusDataConverter.class
					+ ": Calling getAsString Function with argument " + arg2);
		}
		if (arg2 == null) {
			if (log.isDebugEnabled())
				System.out.println(ApplicationStatusDataConverter.class
						+ ": String argument is empty");
			return null;
		}
		if (arg2 instanceof String) {
			return arg2.toString();
		}
		ApplicationStatus applicationStatus = (ApplicationStatus) arg2;
		return applicationStatus.getApplicationStatusCode();

	}

	

}
