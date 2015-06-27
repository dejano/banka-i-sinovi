
package xml.bs;

import javax.annotation.Generated;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AccountDetails complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AccountDetails">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="accountNumber" type="{http://www.ftn.uns.ac.rs/xws/xsd/common}AccountNumber"/>
 *         &lt;element name="model">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}int">
 *               &lt;minInclusive value="0"/>
 *               &lt;maxInclusive value="99"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="referenceNumber">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *               &lt;maxLength value="20"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AccountDetails", namespace = "http://www.ftn.uns.ac.rs/xws/xsd/common", propOrder = {
    "accountNumber",
    "model",
    "referenceNumber"
})
@Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2015-06-26T10:32:50+02:00", comments = "JAXB RI v2.2.4-2")
public class AccountDetails {

    @XmlElement(namespace = "http://www.ftn.uns.ac.rs/xws/xsd/common", required = true)
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2015-06-26T10:32:50+02:00", comments = "JAXB RI v2.2.4-2")
    protected String accountNumber;
    @XmlElement(namespace = "http://www.ftn.uns.ac.rs/xws/xsd/common")
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2015-06-26T10:32:50+02:00", comments = "JAXB RI v2.2.4-2")
    protected int model;
    @XmlElement(namespace = "http://www.ftn.uns.ac.rs/xws/xsd/common", required = true)
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2015-06-26T10:32:50+02:00", comments = "JAXB RI v2.2.4-2")
    protected String referenceNumber;

    /**
     * Gets the value of the accountNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2015-06-26T10:32:50+02:00", comments = "JAXB RI v2.2.4-2")
    public String getAccountNumber() {
        return accountNumber;
    }

    /**
     * Sets the value of the accountNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2015-06-26T10:32:50+02:00", comments = "JAXB RI v2.2.4-2")
    public void setAccountNumber(String value) {
        this.accountNumber = value;
    }

    /**
     * Gets the value of the model property.
     * 
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2015-06-26T10:32:50+02:00", comments = "JAXB RI v2.2.4-2")
    public int getModel() {
        return model;
    }

    /**
     * Sets the value of the model property.
     * 
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2015-06-26T10:32:50+02:00", comments = "JAXB RI v2.2.4-2")
    public void setModel(int value) {
        this.model = value;
    }

    /**
     * Gets the value of the referenceNumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2015-06-26T10:32:50+02:00", comments = "JAXB RI v2.2.4-2")
    public String getReferenceNumber() {
        return referenceNumber;
    }

    /**
     * Sets the value of the referenceNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2015-06-26T10:32:50+02:00", comments = "JAXB RI v2.2.4-2")
    public void setReferenceNumber(String value) {
        this.referenceNumber = value;
    }

}
