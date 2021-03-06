<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h"%>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f"%>
<%@ taglib uri="http://richfaces.org/rich" prefix="rich"%>
<%@ taglib uri="http://richfaces.org/a4j" prefix="a4j"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="authz" %>

<f:subview id="praxisI">
	<h:form id="praxisI_manager_form" styleClass="styleish">
		<h:panelGroup>
			<h:outputLabel for="studentId" value="Student ID: "  styleClass="small" />
			<h:outputText id="studentId" value ="#{testScoreController.currentlySelectedUser.ukID}" />	
		</h:panelGroup>
		<h:panelGroup id="praxisI_list">
			<rich:extendedDataTable id="praxisI_Table" styleClass="basicTable"
			rowClasses="oddRow,evenRow" selectedClass="selectedRow"
			width="100%" var="praxisITestScore" height="200px"
			value="#{testScoreController.praxisITestScoreList}"
			binding="#{testScoreController.praxisITable}"
			noDataLabel="No Praxis I Scores Found.">
			
				<a4j:support event="onRowClick" action="#{testScoreController.takePraxisISelection}"/>
					
				<rich:column id="praxisITestCode_Col" width="10%" sortable="true"
				sortBy="#{praxisITestScore.praxisICode}">
					<f:facet name="header">
						<h:outputText value="Test Code" />
					</f:facet>
					<h:panelGroup>
						<h:outputText value="#{praxisITestScore.praxisICode}" />
					</h:panelGroup>
				</rich:column>
				<rich:column id="candidateId_Col" width="8%">
					<f:facet name="header">
						<h:outputText value="Candidate" />
					</f:facet>
					<h:panelGroup>
						<h:outputText value="#{praxisITestScore.candidateId}"/>
					</h:panelGroup>
				</rich:column>
				<rich:column id="praxisITestTitle_Col" width="25%" sortable="true"
				sortBy="#{praxisITestScore.praxisITest.title}">
					<f:facet name="header">
						<h:outputText value="Test Title" />
					</f:facet>
					<h:outputText value="#{praxisITestScore.praxisITest.title}" />
				</rich:column>
				<rich:column id="praxisITestDate_Col" width="9%" sortable="true"
				sortBy="#{praxisITestScore.testDate}">
					<f:facet name="header">
						<h:outputText value="Test Date" />
					</f:facet>
					<h:outputText value="#{praxisITestScore.testDate}" />
				</rich:column>
				<rich:column id="praxisIScore_Col" width="9%" sortable="true"
				sortBy="#{praxisITestScore.score}">
					<f:facet name="header">
						<h:outputText value="Score" />
					</f:facet>
					<h:panelGroup>
						<h:outputText value="#{praxisITestScore.scoreInt}" />
					</h:panelGroup>
				</rich:column>
				<rich:column id="praxisICutoff_Col" width="10%" sortable="true"
				sortBy="#{praxisITestScore.praxisITest.cutoff}">
					<f:facet name="header">
						<h:outputText value="Cutoff" />
					</f:facet>
					<h:outputText value="#{praxisITestScore.praxisITest.cutoff}" />
				</rich:column>
				<rich:column id="praxisIMadeCutoff_Col" width="9%" sortable="true"
				sortBy="#{praxisITestScore.cutOffString}">
					<f:facet name="header">
						<h:outputText value="Made Cutoff" />
					</f:facet>
					<h:outputText value="#{praxisITestScore.cutOffString}" />
				</rich:column>
				<rich:column id="praxisIReceivedDate_Col" width="10%" sortable="true"
				sortBy="#{praxisITestScore.dateRecieved}">
					<f:facet name="header">
						<h:outputText value="Received Date" />
					</f:facet>
					<h:panelGroup>
						<h:outputText value="#{praxisITestScore.dateRecieved}" />
					</h:panelGroup>
				</rich:column>
				<rich:column id="praxisIPrimary_Col" width="10%" sortable="true"
				sortBy="#{praxisITestScore.primarystring}">
					<f:facet name="header">
						<h:outputText value="Primary" />
					</f:facet>
					<h:outputText value="#{praxisITestScore.primaryString}" />
				</rich:column>				
			</rich:extendedDataTable>
		</h:panelGroup>
		<h:panelGrid columns="2">
		
			<authz:authorize ifAnyGranted="ROLE_ADMINISTRATOR,ROLE_DEVELOPER,ROLE_STUDENT_ACCOUNT_MANAGER_ADD_EDIT" >
				<a4j:commandButton id="edit_praxis" value="Edit Selected"
				styleClass="buttonStyle" reRender="selectedPraxisIScoreEditPanel"
				oncomplete="Richfaces.showModalPanel('selectedPraxisIScoreEditPanel')" 
				action="#{testScoreController.prepEditSelectedPraxisI}" />			
			</authz:authorize>
			
			<authz:authorize ifAnyGranted="ROLE_ADMINISTRATOR,ROLE_DEVELOPER,ROLE_STUDENT_ACCOUNT_MANAGER_DELETE">
				<h:commandButton id="delete_test" value="Delete Selected"
					styleClass="buttonStyle"
					action="#{testScoreController.deletePraxisITestScoreFromUser}">
					<a4j:support reRender="praxisI_manager_form:praxisI_list"
						limitToList="true" />
				</h:commandButton>
			</authz:authorize>			
			
		</h:panelGrid>
	</h:form>
	
	<authz:authorize ifAnyGranted="ROLE_ADMINISTRATOR,ROLE_DEVELOPER,ROLE_STUDENT_ACCOUNT_MANAGER_ADD_EDIT" >
		<h:form id="praxisI_entry_form" styleClass="styleish">
			<h:panelGroup id="praxisI_entry">
				<h:panelGrid columns="2" cellpadding="5" styleClass="highlightClass"
					width="100%">
					<h:outputLabel for="candidateId" styleClass="small" value="Candidate Id : *"/>
					<h:inputText id="candidateId" value="#{testScoreController.praxisICandidateId}" 
					required="true" requiredMessage="Candidate Id: Value is required.">
					</h:inputText>
					<h:outputLabel for="testDate" styleClass="small" value="Test Date : *" />
					<rich:calendar id="testDate"
						value="#{testScoreController.praxisITestDate}" cellWidth="24px"
						datePattern="MM/dd/yyyy" enableManualInput="true" cellHeight="22px"
						style="width:200px" required="true"
						requiredMessage="Test Date: Value is required.">
					</rich:calendar>
					<h:outputLabel for="receivedDate" styleClass="small" value="Received Date : *"/>
					<rich:calendar id="receivedDate"
						value="#{testScoreController.praxisIReceivedDate}" cellWidth="24px"
						datePattern="MM/dd/yyyy" enableManualInput="true" cellHeight="22px"
						style="width:200px" required="true"
						requiredMessage="Received Date: Value is required.">
					</rich:calendar>
					<h:outputLabel for="praxisICode" value="Test(Current) : *" styleClass="small"/>
					<h:selectOneMenu id="praxisICode"
						value="#{testScoreController.selectedPraxisITest}"
						converter="#{PraxisITestDataConverter}"
						disabled="#{testScoreController.praxisIOldTestCodeFlg}"
						required="true" requiredMessage="Test(Current): Value is required.">
						<f:selectItem itemValue="" itemLabel="-- Select --" />
						<f:selectItems
							value="#{testScoreController.currentPraxisITestList}"
							var="praxisITest" itemValue="#{praxisITest}"
							itemLabel="#{praxisITest.praxisICode}  |  #{praxisITest.title} | Begin: #{praxisITest.beginDateFormatted} |
							 End: #{praxisITest.endDateFormatted} | Cutoff: #{praxisITest.cutoff} | #{praxisITest.status} " />
					</h:selectOneMenu>
					<h:outputLabel for="isOldPraxisICode" styleClass="small" value="Old Praxis II Test : "/>
					<h:selectBooleanCheckbox id="isOldPraxisICode"
						value="#{testScoreController.praxisIOldTestCodeFlg}">
						<a4j:support event="onclick" ajaxSingle="true"
							reRender="praxisI_entry_form:praxisI_entry" />
					</h:selectBooleanCheckbox>
					<h:outputLabel for="oldPraxisICode"  styleClass="small" value="Test(Old) : "/>
					<h:selectOneMenu id="oldPraxisICode"
						value="#{testScoreController.selectedPraxisITest}"
						converter="#{PraxisITestDataConverter}"
						disabled="#{!testScoreController.praxisIOldTestCodeFlg}"
						required="true" requiredMessage="Test(Old): Value is required.">
						<f:selectItem itemValue="" itemLabel="-- Select --" />
						<f:selectItems value="#{testScoreController.oldPraxisITestList}"
							var="oldPraxisITest" itemValue="#{oldPraxisITest}"
							itemLabel="#{oldPraxisITest.praxisICode}  |  #{oldPraxisITest.title} | Begin: #{oldPraxisITest.beginDateFormatted} |
							 End: #{oldPraxisITest.endDateFormatted} | Cutoff: #{oldPraxisITest.cutoff} | #{oldPraxisITest.status} " />
					</h:selectOneMenu>
					<h:outputLabel for="score" styleClass="small" value="Exam Score : "/>
					<h:inputText id="score"
						value="#{testScoreController.praxisITestScore}" required="true"
						requiredMessage="Exam Score: Value is required.">
						<f:convertNumber />
					</h:inputText>
					<h:commandButton id="add_new" value="Add New"
						styleClass="buttonStyle"
						action="#{testScoreController.addPraxisITestScoreToUser}">
						<a4j:support reRender="praxisI_manager_form:praxisI_list"
							limitToList="true" />
					</h:commandButton>
				</h:panelGrid>
			</h:panelGroup>
		</h:form>
	</authz:authorize>
	<rich:modalPanel id="selectedPraxisIScoreEditPanel" height="450"
		width="650" autosized="true" domElementAttachment="parent">
		<f:facet name="header">
			<h:outputText value="Edit Praxis I Score" />
		</f:facet>
		<f:facet name="controls">
			<h:panelGroup>
				<h:graphicImage value="/images/icons/cancel.png" id="hideViewlink"
					styleClass="hidelink" alt="X" />
				<rich:componentControl for="selectedPraxisIScoreEditPanel"
					attachTo="hideViewlink" operation="hide" event="onclick" />
			</h:panelGroup>
		</f:facet>
		<h:form styleClass="styleish">
			<rich:messages style="color:red;" />
			<h:panelGrid columns="2" cellpadding="5" styleClass="highlightClass" width="100%">
				<h:outputLabel for="editCandidateId" styleClass="small" value="Candidate Id : *"/>
				<h:inputText id="editCandidateId" value="#{testScoreController.selectedPraxisITestScore.candidateId}" 
				required="true" requiredMessage="Candidate Id: Value is required.">
				</h:inputText>
				
<%-- 				<h:outputLabel for="editReceivedDate" styleClass="small" value="Received Date : *"/> --%>
<%-- 				<rich:calendar id="editReceivedDate" --%>
<%-- 				value="#{testScoreController.selectedPraxisITestScore.testDate}" cellWidth="24px" --%>
<%-- 				datePattern="MM/dd/yyyy" enableManualInput="true" cellHeight="22px" --%>
<%-- 				style="width:200px" required="true" --%>
<%-- 				requiredMessage="Received Date: Value is required."> --%>
<%-- 				</rich:calendar> --%>
				
				<h:outputLabel for="editTestDate" value="Test Date : " styleClass="small"/>
				<rich:calendar id="editTestDate"
				value="#{testScoreController.selectedPraxisITestScore.testDate}"
				cellWidth="24px" datePattern="MM/dd/yyyy" enableManualInput="true"
				cellHeight="22px" style="width:200px" required="true"
				requiredMessage="Test Date: Value is Required">
				</rich:calendar>

				<h:outputLabel for="editPraxisICode" value="Test(Current) : " styleClass="small"/>
				<h:selectOneMenu id="editPraxisICode"
				value="#{testScoreController.selectedPraxisITestScore.praxisITest}"
				converter="#{PraxisITestDataConverter}"
				disabled="#{testScoreController.praxisIEditOldTestCodeFlg}"
				required="true" requiredMessage="Test(Current): Value is Required">
					<f:selectItems value="#{testScoreController.currentPraxisITestList}"
					var="praxisITest" itemValue="#{praxisITest}"
					itemLabel="#{praxisITest.praxisICode}  |  #{praxisITest.title} | Begin: #{praxisITest.beginDateFormatted} |
					End: #{praxisITest.endDateFormatted} | Cutoff: #{praxisITest.cutoff}" />
				</h:selectOneMenu>

				<h:outputLabel for="editIsOldPraxisICode" value="Old Praxis I Test : " styleClass="small"/>					
				<h:selectBooleanCheckbox id="editIsOldPraxisICode"
					value="#{testScoreController.praxisIEditOldTestCodeFlg}">
					<a4j:support event="onclick" ajaxSingle="true"
						reRender="selectedPraxisIScoreEditPanel"
						oncomplete="Richfaces.showModalPanel('selectedPraxisIScoreEditPanel')" />
				</h:selectBooleanCheckbox>

				<h:outputLabel for="editOldPraxisICode" value="Test(Old) : " styleClass="small"/>
				<h:selectOneMenu id="editOldPraxisICode"
				value="#{testScoreController.selectedPraxisITestScore.praxisITest}"
				converter="#{PraxisITestDataConverter}"
				disabled="#{!testScoreController.praxisIEditOldTestCodeFlg}"
				required="true" requiredMessage="Test(Old): Value is Required">
					<f:selectItems value="#{testScoreController.oldPraxisITestList}"
						var="oldPraxisITest" itemValue="#{oldPraxisITest}"
						itemLabel="#{oldPraxisITest.praxisICode}  |  #{oldPraxisITest.title} | Begin: #{oldPraxisITest.beginDateFormatted} |
					 End: #{oldPraxisITest.endDateFormatted} | Cutoff: #{oldPraxisITest.cutoff}" />
				</h:selectOneMenu>

				<h:outputLabel for="editScore" value="Exam Score : " styleClass="small"/>
				<h:inputText id="editScore"
				value="#{testScoreController.selectedPraxisITestScore.score}"
				required="true" requiredMessage="Exam Score: Value is Required">
					<f:convertNumber/>
				</h:inputText>

				<a4j:commandButton id="edit_test" value="Edit"
					styleClass="buttonStyle"
					reRender="praxisI_manager_form:praxisI_list" limitToList="true"
					action="#{testScoreController.editPraxisITestScoreForUser}"
					oncomplete="Richfaces.hideModalPanel('selectedPraxisIScoreEditPanel')" />
			</h:panelGrid>
		</h:form>
	</rich:modalPanel>
</f:subview>