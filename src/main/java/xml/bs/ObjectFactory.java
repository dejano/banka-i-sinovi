
package xml.bs;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the xml.bs package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _StatementRequest_QNAME = new QName("http://www.ftn.uns.ac.rs/xws/xsd/bs", "statementRequest");
    private final static QName _BankDetails_QNAME = new QName("http://www.ftn.uns.ac.rs/xws/xsd/common", "bankDetails");
    private final static QName _Statement_QNAME = new QName("http://www.ftn.uns.ac.rs/xws/xsd/bs", "statement");
    private final static QName _BsExceptionEnum_QNAME = new QName("http://www.ftn.uns.ac.rs/xws/xsd/bs", "bsExceptionEnum");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: xml.bs
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Statement }
     * 
     */
    public Statement createStatement() {
        return new Statement();
    }

    /**
     * Create an instance of {@link StatementRequest }
     * 
     */
    public StatementRequest createStatementRequest() {
        return new StatementRequest();
    }

    /**
     * Create an instance of {@link StatementItem }
     * 
     */
    public StatementItem createStatementItem() {
        return new StatementItem();
    }

    /**
     * Create an instance of {@link BankDetails }
     * 
     */
    public BankDetails createBankDetails() {
        return new BankDetails();
    }

    /**
     * Create an instance of {@link AccountDetails }
     * 
     */
    public AccountDetails createAccountDetails() {
        return new AccountDetails();
    }

    /**
     * Create an instance of {@link Payment }
     * 
     */
    public Payment createPayment() {
        return new Payment();
    }

    /**
     * Create an instance of {@link Statement.Items }
     * 
     */
    public Statement.Items createStatementItems() {
        return new Statement.Items();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link StatementRequest }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.ftn.uns.ac.rs/xws/xsd/bs", name = "statementRequest")
    public JAXBElement<StatementRequest> createStatementRequest(StatementRequest value) {
        return new JAXBElement<StatementRequest>(_StatementRequest_QNAME, StatementRequest.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BankDetails }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.ftn.uns.ac.rs/xws/xsd/common", name = "bankDetails")
    public JAXBElement<BankDetails> createBankDetails(BankDetails value) {
        return new JAXBElement<BankDetails>(_BankDetails_QNAME, BankDetails.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Statement }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.ftn.uns.ac.rs/xws/xsd/bs", name = "statement")
    public JAXBElement<Statement> createStatement(Statement value) {
        return new JAXBElement<Statement>(_Statement_QNAME, Statement.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BsExceptionEnum }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.ftn.uns.ac.rs/xws/xsd/bs", name = "bsExceptionEnum")
    public JAXBElement<BsExceptionEnum> createBsExceptionEnum(BsExceptionEnum value) {
        return new JAXBElement<BsExceptionEnum>(_BsExceptionEnum_QNAME, BsExceptionEnum.class, null, value);
    }

}
