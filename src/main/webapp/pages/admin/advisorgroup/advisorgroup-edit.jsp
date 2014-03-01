<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="https://ajax4jsf.dev.java.net/ajax" prefix="a4j"%>
<link href="${facesContext.externalContext.requestContextPath}/css/font.css" media="screen"
	rel="stylesheet" type="text/css"/>
<link href="${facesContext.externalContext.requestContextPath}/css/template.css"
	media="screen" rel="stylesheet" type="text/css"/>
<f:view afterPhase="#{appComponentController.initApplicationComponent}">
	<h:message for="edit_advisorgroup_manager">
	</h:message>
	<h:form id="edit_advisorgroup_manager">
		<br />
		<h:panelGrid columns="2">
			<h:graphicImage url="/images/warning.gif"
				rendered="#{appComponentController.appComponentWarnStatus}" alt="Warning!"
				title="Warning!" />
			<font class="ws10" color="#062E8A" face="Tahoma"><h:outputText
				value="#{appComponentController.appComponentStatusMessage}" /></font>
		</h:panelGrid>
		<hr />
		<h:panelGrid columns="2">
			<h:graphicImage url="/images/info.png" alt="i" title="Info" />
			<h:panelGroup>
				<font class="ws10" color="#062E8A" face="Tahoma"> <h:outputText
					value="Edit existing Advisor Group" /></font>
			</h:panelGroup>
		</h:panelGrid>
		<br />
		<br />
		<a4j:region selfRendered="true"
			ajaxListener="#{appComponentController.valueChangeEditAdvisorGroup}"
			id="advisorgrouplist_ajaxregion">
			<a4j:status startText="Loading ..." startStyleClass="loadingStatus" />
			<h:panelGrid columns="2"
				style="padding-right: 100px; padding-left: 100px">
				<h:panelGroup>
					<font class="ws10" color="#062E8A" face="Tahoma">Select
					Advisor Group:</font>
				</h:panelGroup>
				<h:selectOneListbox id="advisorgroup_list_id"
					value="#{appComponentController.selectedAdvisorGroup}" size="1"
					converter="#{AdvisorGroupDataConverter}" style="width:200px">
					<f:selectItem itemLabel="Select One" itemValue="NA" />
					<f:selectItems
						value="#{cepisGlobalComponentConfigurator.advisorGroupList}"
						var="advisorgroup" itemLabel="#{advisorgroup.name}" />
					<f:validator validatorId="AdvisorGroupDataValidator" />
					<a4j:support event="onchange"
						reRender="name,shortDesc" />
				</h:selectOneListbox>
				<h:panelGroup>
					<font class="ws10" color="#062E8A" face="Tahoma"> <h:outputText
						value="Name:"></h:outputText></font>
					<br />
					<h:outputText value="e.g., Advisor Group Name, Max 50 Characters." style="color:gray"></h:outputText>
				</h:panelGroup>
				<h:inputText id="name" value="#{appComponentController.name}"
					style="width:200px"></h:inputText>
				<h:panelGroup>
					<font class="ws10" color="#062E8A" face="Tahoma"> <h:outputText
						value="Short description:"></h:outputText></font>
					<br />
					<h:outputText
						value="Short description for the drop-down box. Max 255 characters"
						style="color:gray"></h:outputText>
				</h:panelGroup>
				<h:inputTextarea id="shortDesc"
					value="#{appComponentController.shortDesc}" style="width:200px"
					rows="2"></h:inputTextarea>
				
				<h:message for="edit_advisorgroup_button"></h:message>
				<h:commandButton id="edit_advisorgroup_button" value="Edit"
					action="#{appComponentController.editAdvisorGroup}"></h:commandButton>
			</h:panelGrid>
		</a4j:region>
	</h:form>
</f:view>