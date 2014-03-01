package https.ets_org.edm.webservices;

import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.Service;

/**
 * This class was generated by Apache CXF 2.7.7
 * 2013-10-15T07:01:18.925-04:00
 * Generated source version: 2.7.7
 * 
 */
@WebServiceClient(name = "ScoreReportsServices.", 
                  wsdlLocation = "file:/C:/dev/Java/code/cepis/src/main/resources/wsdl/edmpraxis.wsdl",
                  targetNamespace = "https://ets.org/edm/webservices/") 
public class ScoreReportsServices extends Service {

    public final static URL WSDL_LOCATION;

    public final static QName SERVICE = new QName("https://ets.org/edm/webservices/", "ScoreReportsServices.");
    public final static QName ScoreReportsServicesSoap = new QName("https://ets.org/edm/webservices/", "ScoreReportsServices.Soap");
    public final static QName ScoreReportsServicesSoap12 = new QName("https://ets.org/edm/webservices/", "ScoreReportsServices.Soap12");
    static {
        URL url = null;
        try {
            url = new URL("file:/C:/dev/Java/code/cepis/src/main/resources/wsdl/edmpraxis.wsdl");
        } catch (MalformedURLException e) {
            java.util.logging.Logger.getLogger(ScoreReportsServices.class.getName())
                .log(java.util.logging.Level.INFO, 
                     "Can not initialize the default wsdl from {0}", "file:/C:/dev/Java/code/cepis/src/main/resources/wsdl/edmpraxis.wsdl");
        }
        WSDL_LOCATION = url;
    }

    public ScoreReportsServices(URL wsdlLocation) {
        super(wsdlLocation, SERVICE);
    }

    public ScoreReportsServices(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public ScoreReportsServices() {
        super(WSDL_LOCATION, SERVICE);
    }
    
    //This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
    //API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
    //compliant code instead.
    public ScoreReportsServices(WebServiceFeature ... features) {
        super(WSDL_LOCATION, SERVICE, features);
    }

    //This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
    //API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
    //compliant code instead.
    public ScoreReportsServices(URL wsdlLocation, WebServiceFeature ... features) {
        super(wsdlLocation, SERVICE, features);
    }

    //This constructor requires JAX-WS API 2.2. You will need to endorse the 2.2
    //API jar or re-run wsdl2java with "-frontend jaxws21" to generate JAX-WS 2.1
    //compliant code instead.
    public ScoreReportsServices(URL wsdlLocation, QName serviceName, WebServiceFeature ... features) {
        super(wsdlLocation, serviceName, features);
    }

    /**
     *
     * @return
     *     returns ScoreReportsServicesSoap
     */
    @WebEndpoint(name = "ScoreReportsServices.Soap")
    public ScoreReportsServicesSoap getScoreReportsServicesSoap() {
        return super.getPort(ScoreReportsServicesSoap, ScoreReportsServicesSoap.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns ScoreReportsServicesSoap
     */
    @WebEndpoint(name = "ScoreReportsServices.Soap")
    public ScoreReportsServicesSoap getScoreReportsServicesSoap(WebServiceFeature... features) {
        return super.getPort(ScoreReportsServicesSoap, ScoreReportsServicesSoap.class, features);
    }
    /**
     *
     * @return
     *     returns ScoreReportsServicesSoap
     */
    @WebEndpoint(name = "ScoreReportsServices.Soap12")
    public ScoreReportsServicesSoap getScoreReportsServicesSoap12() {
        return super.getPort(ScoreReportsServicesSoap12, ScoreReportsServicesSoap.class);
    }

    /**
     * 
     * @param features
     *     A list of {@link javax.xml.ws.WebServiceFeature} to configure on the proxy.  Supported features not in the <code>features</code> parameter will have their default values.
     * @return
     *     returns ScoreReportsServicesSoap
     */
    @WebEndpoint(name = "ScoreReportsServices.Soap12")
    public ScoreReportsServicesSoap getScoreReportsServicesSoap12(WebServiceFeature... features) {
        return super.getPort(ScoreReportsServicesSoap12, ScoreReportsServicesSoap.class, features);
    }

}