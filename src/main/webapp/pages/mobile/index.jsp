<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<ui:composition xmlns="http://www.w3.org/1999/xhtml"
    xmlns:h="http://java.sun.com/jsf/html"
    xmlns:f="http://java.sun.com/jsf/core"
    xmlns:ui="http://java.sun.com/jsf/facelets"
    xmlns:a4j="http://richfaces.org/a4j"
    xmlns:rich="http://richfaces.org/rich">
    <h:form>
        <h:panelGrid columns="3">
            <h:outputText value="Name:" />
            <h:inputText  />
            <a4j:commandButton value="Say Hello"/>
        </h:panelGrid>
    </h:form>
    <br />
    <a4j:outputPanel id="out">
        <h:outputText value="Hello  !" styleClass="outhello" />
    </a4j:outputPanel>
</ui:composition>