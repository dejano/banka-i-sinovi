
package xml.bs;

import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for Payment complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Payment">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="debtor" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="paymentPurpose" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="creditor" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="orderDate" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         &lt;element name="debtorAccountDetails" type="{http://www.ftn.uns.ac.rs/xws/xsd/common}AccountDetails"/>
 *         &lt;element name="creditorAccountDetails" type="{http://www.ftn.uns.ac.rs/xws/xsd/common}AccountDetails"/>
 *         &lt;element name="amount" type="{http://www.ftn.uns.ac.rs/xws/xsd/common}Balance"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Payment", namespace = "http://www.ftn.uns.ac.rs/xws/xsd/common", propOrder = {
    "debtor",
    "paymentPurpose",
    "creditor",
    "orderDate",
    "debtorAccountDetails",
    "creditorAccountDetails",
    "amount"
})
@XmlSeeAlso({
    StatementItem.class
})
@Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2015-06-26T10:32:50+02:00", comments = "JAXB RI v2.2.4-2")
public class Payment {

    @XmlElement(namespace = "http://www.ftn.uns.ac.rs/xws/xsd/common", required = true)
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2015-06-26T10:32:50+02:00", comments = "JAXB RI v2.2.4-2")
    protected String debtor;
    @XmlElement(namespace = "http://www.ftn.uns.ac.rs/xws/xsd/common", required = true)
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2015-06-26T10:32:50+02:00", comments = "JAXB RI v2.2.4-2")
    protected String paymentPurpose;
    @XmlElement(namespace = "http://www.ftn.uns.ac.rs/xws/xsd/common", required = true)
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2015-06-26T10:32:50+02:00", comments = "JAXB RI v2.2.4-2")
    protected String creditor;
    @XmlElement(namespace = "http://www.ftn.uns.ac.rs/xws/xsd/common", required = true)
    @XmlSchemaType(name = "date")
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2015-06-26T10:32:50+02:00", comments = "JAXB RI v2.2.4-2")
    protected XMLGregorianCalendar orderDate;
    @XmlElement(namespace = "http://www.ftn.uns.ac.rs/xws/xsd/common", required = true)
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2015-06-26T10:32:50+02:00", comments = "JAXB RI v2.2.4-2")
    protected AccountDetails debtorAccountDetails;
    @XmlElement(namespace = "http://www.ftn.uns.ac.rs/xws/xsd/common", required = true)
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2015-06-26T10:32:50+02:00", comments = "JAXB RI v2.2.4-2")
    protected AccountDetails creditorAccountDetails;
    @XmlElement(namespace = "http://www.ftn.uns.ac.rs/xws/xsd/common", required = true)
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2015-06-26T10:32:50+02:00", comments = "JAXB RI v2.2.4-2")
    protected BigDecimal amount;

    /**
     * Gets the value of the debtor property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2015-06-26T10:32:50+02:00", comments = "JAXB RI v2.2.4-2")
    public String getDebtor() {
        return debtor;
    }

    /**
     * Sets the value of the debtor property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2015-06-26T10:32:50+02:00", comments = "JAXB RI v2.2.4-2")
    public void setDebtor(String value) {
        this.debtor = value;
    }

    /**
     * Gets the value of the paymentPurpose property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2015-06-26T10:32:50+02:00", comments = "JAXB RI v2.2.4-2")
    public String getPaymentPurpose() {
        return paymentPurpose;
    }

    /**
     * Sets the value of the paymentPurpose property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2015-06-26T10:32:50+02:00", comments = "JAXB RI v2.2.4-2")
    public void setPaymentPurpose(String value) {
        this.paymentPurpose = value;
    }

    /**
     * Gets the value of the creditor property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2015-06-26T10:32:50+02:00", comments = "JAXB RI v2.2.4-2")
    public String getCreditor() {
        return creditor;
    }

    /**
     * Sets the value of the creditor property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2015-06-26T10:32:50+02:00", comments = "JAXB RI v2.2.4-2")
    public void setCreditor(String value) {
        this.creditor = value;
    }

    /**
     * Gets the value of the orderDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2015-06-26T10:32:50+02:00", comments = "JAXB RI v2.2.4-2")
    public XMLGregorianCalendar getOrderDate() {
        return orderDate;
    }

    /**
     * Sets the value of the orderDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2015-06-26T10:32:50+02:00", comments = "JAXB RI v2.2.4-2")
    public void setOrderDate(XMLGregorianCalendar value) {
        this.orderDate = value;
    }

    /**
     * Gets the value of the debtorAccountDetails property.
     * 
     * @return
     *     possible object is
     *     {@link AccountDetails }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2015-06-26T10:32:50+02:00", comments = "JAXB RI v2.2.4-2")
    public AccountDetails getDebtorAccountDetails() {
        return debtorAccountDetails;
    }

    /**
     * Sets the value of the debtorAccountDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link AccountDetails }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2015-06-26T10:32:50+02:00", comments = "JAXB RI v2.2.4-2")
    public void setDebtorAccountDetails(AccountDetails value) {
        this.debtorAccountDetails = value;
    }

    /**
     * Gets the value of the creditorAccountDetails property.
     * 
     * @return
     *     possible object is
     *     {@link AccountDetails }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2015-06-26T10:32:50+02:00", comments = "JAXB RI v2.2.4-2")
    public AccountDetails getCreditorAccountDetails() {
        return creditorAccountDetails;
    }

    /**
     * Sets the value of the creditorAccountDetails property.
     * 
     * @param value
     *     allowed object is
     *     {@link AccountDetails }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2015-06-26T10:32:50+02:00", comments = "JAXB RI v2.2.4-2")
    public void setCreditorAccountDetails(AccountDetails value) {
        this.creditorAccountDetails = value;
    }

    /**
     * Gets the value of the amount property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2015-06-26T10:32:50+02:00", comments = "JAXB RI v2.2.4-2")
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * Sets the value of the amount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2015-06-26T10:32:50+02:00", comments = "JAXB RI v2.2.4-2")
    public void setAmount(BigDecimal value) {
        this.amount = value;
    }

}
