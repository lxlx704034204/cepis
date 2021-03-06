<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<link href="${facesContext.externalContext.requestContextPath}/css/font.css" media="screen"
	rel="stylesheet" type="text/css"/>
<link href="${facesContext.externalContext.requestContextPath}/css/template.css"
	media="screen" rel="stylesheet" type="text/css"/>
<f:view afterPhase="#{userDetailController.initApplicationComponent}">
	<h:message for="delete_emailaddresstype_manager">
	</h:message>
	<h:form id="delete_emailaddresstype_manager">
		<br />
		<h:panelGrid columns="2">
			<h:graphicImage url="/images/warning.gif"
				rendered="#{userDetailController.userDetailWarnStatus}" alt="!"
				title="Warning!" />
			<font class="ws10" color="#062E8A" face="Tahoma"><h:outputText
				value="#{userDetailController.userDetailStatusMessage}" /></font>
		</h:panelGrid>
		<hr />
		<h:panelGrid columns="2">
			<h:graphicImage url="/images/info.png" alt="i" title="Info" />
			<h:panelGroup>
				<font class="ws10" color="#062E8A" face="Tahoma"> <h:outputText
					value="Delete existing email address type" /></font>
			</h:panelGroup>
		</h:panelGrid>
		<br />
		<br />
		<h:panelGrid columns="2"
			style="padding-right: 100px; padding-left: 100px">
			<h:panelGroup>
				<font class="ws10" color="#062E8A" face="Tahoma">Select
				Email AddressType:</font>
			</h:panelGroup>
			<h:selectOneListbox id="emailaddresstype_list_id"
				value="#{userDetailController.selectedEmailAddressType}" size="1"
				converter="#{EmailAddressTypeDataConverter}" style="width:200px">
				<f:selectItem itemLabel="Select One" itemValue="NA" />
				<f:selectItems
					value="#{cepisGlobalComponentConfigurator.emailAddressTypeList}"
					var="emailaddresstype" itemLabel="#{emailaddresstype.desc}" />
				<f:validator validatorId="EmailAddressTypeDataValidator" />
			</h:selectOneListbox>
			<h:message for="delete_emailaddresstype_button"></h:message>
			<h:commandButton id="delete_emailaddresstype_button" value="Delete"
				action="#{userDetailController.deleteEmailAddressType}"></h:commandButton>
		</h:panelGrid>
	</h:form>
</f:view>