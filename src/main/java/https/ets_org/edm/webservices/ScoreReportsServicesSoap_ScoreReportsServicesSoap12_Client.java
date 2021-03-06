
package https.ets_org.edm.webservices;

/**
 * Please modify this class to meet your needs
 * This class is not complete
 */

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.ws.RequestWrapper;
import javax.xml.ws.ResponseWrapper;

/**
 * This class was generated by Apache CXF 2.7.7
 * 2013-10-15T07:01:18.878-04:00
 * Generated source version: 2.7.7
 * 
 */
public final class ScoreReportsServicesSoap_ScoreReportsServicesSoap12_Client {

    private static final QName SERVICE_NAME = new QName("https://ets.org/edm/webservices/", "ScoreReportsServices.");

    private ScoreReportsServicesSoap_ScoreReportsServicesSoap12_Client() {
    }

    public static void main(String args[]) throws java.lang.Exception {
        URL wsdlURL = ScoreReportsServices.WSDL_LOCATION;
        if (args.length > 0 && args[0] != null && !"".equals(args[0])) { 
            File wsdlFile = new File(args[0]);
            try {
                if (wsdlFile.exists()) {
                    wsdlURL = wsdlFile.toURI().toURL();
                } else {
                    wsdlURL = new URL(args[0]);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
      
        ScoreReportsServices ss = new ScoreReportsServices(wsdlURL, SERVICE_NAME);
        ScoreReportsServicesSoap port = ss.getScoreReportsServicesSoap12();  
        
        {
        System.out.println("Invoking getHistoryReport...");
        java.lang.String _getHistoryReport_clientUserId = "";
        java.lang.String _getHistoryReport_clientPassword = "";
        int _getHistoryReport_year = 0;
        byte[] _getHistoryReport__return = port.getHistoryReport(_getHistoryReport_clientUserId, _getHistoryReport_clientPassword, _getHistoryReport_year);
        System.out.println("getHistoryReport.result=" + _getHistoryReport__return);


        }
        {
        System.out.println("Invoking getNewScoreReports...");
        java.lang.String _getNewScoreReports_clientUserId = "";
        java.lang.String _getNewScoreReports_clientPassword = "";
        java.lang.String _getNewScoreReports_strSubProgram = "";
        byte[] _getNewScoreReports__return = port.getNewScoreReports(_getNewScoreReports_clientUserId, _getNewScoreReports_clientPassword, _getNewScoreReports_strSubProgram);
        System.out.println("getNewScoreReports.result=" + _getNewScoreReports__return);


        }
        {
        System.out.println("Invoking getReportingDates...");
        java.lang.String _getReportingDates_clientUserId = "";
        java.lang.String _getReportingDates_clientPassword = "";
        byte[] _getReportingDates__return = port.getReportingDates(_getReportingDates_clientUserId, _getReportingDates_clientPassword);
        System.out.println("getReportingDates.result=" + _getReportingDates__return);


        }
        {
        System.out.println("Invoking getScoreReportsInCDScoreLinkFormat...");
        java.lang.String _getScoreReportsInCDScoreLinkFormat_clientUserId = "";
        java.lang.String _getScoreReportsInCDScoreLinkFormat_clientPassword = "";
        java.lang.String _getScoreReportsInCDScoreLinkFormat_reportDate = "";
        java.lang.String _getScoreReportsInCDScoreLinkFormat_strSubProgram = "";
        byte[] _getScoreReportsInCDScoreLinkFormat__return = port.getScoreReportsInCDScoreLinkFormat(_getScoreReportsInCDScoreLinkFormat_clientUserId, _getScoreReportsInCDScoreLinkFormat_clientPassword, _getScoreReportsInCDScoreLinkFormat_reportDate, _getScoreReportsInCDScoreLinkFormat_strSubProgram);
        System.out.println("getScoreReportsInCDScoreLinkFormat.result=" + _getScoreReportsInCDScoreLinkFormat__return);


        }
        {
        System.out.println("Invoking getScoreReportsGivenReportingDate...");
        java.lang.String _getScoreReportsGivenReportingDate_clientUserId = "";
        java.lang.String _getScoreReportsGivenReportingDate_clientPassword = "";
        java.lang.String _getScoreReportsGivenReportingDate_reportDate = "";
        java.lang.String _getScoreReportsGivenReportingDate_strSubProgram = "";
        byte[] _getScoreReportsGivenReportingDate__return = port.getScoreReportsGivenReportingDate(_getScoreReportsGivenReportingDate_clientUserId, _getScoreReportsGivenReportingDate_clientPassword, _getScoreReportsGivenReportingDate_reportDate, _getScoreReportsGivenReportingDate_strSubProgram);
        System.out.println("getScoreReportsGivenReportingDate.result=" + _getScoreReportsGivenReportingDate__return);


        }
        {
        System.out.println("Invoking getHistoryReportGivenCandidate...");
        java.lang.String _getHistoryReportGivenCandidate_clientUserId = "";
        java.lang.String _getHistoryReportGivenCandidate_clientPassword = "";
        int _getHistoryReportGivenCandidate_year = 0;
        java.lang.String _getHistoryReportGivenCandidate_candidateId = "";
        byte[] _getHistoryReportGivenCandidate__return = port.getHistoryReportGivenCandidate(_getHistoryReportGivenCandidate_clientUserId, _getHistoryReportGivenCandidate_clientPassword, _getHistoryReportGivenCandidate_year, _getHistoryReportGivenCandidate_candidateId);
        System.out.println("getHistoryReportGivenCandidate.result=" + _getHistoryReportGivenCandidate__return);


        }

        System.exit(0);
    }

}
