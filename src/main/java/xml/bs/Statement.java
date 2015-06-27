
package xml.bs;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for Statement complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Statement">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="accountNumber" type="{http://www.ftn.uns.ac.rs/xws/xsd/common}AccountNumber"/>
 *         &lt;element name="orderDate" type="{http://www.w3.org/2001/XMLSchema}date"/>
 *         &lt;element name="statementNumber" type="{http://www.ftn.uns.ac.rs/xws/xsd/bs}StatementNumber"/>
 *         &lt;element name="previousBalance" type="{http://www.ftn.uns.ac.rs/xws/xsd/common}Balance"/>
 *         &lt;element name="payoutCount">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer">
 *               &lt;minInclusive value="0"/>
 *               &lt;maxInclusive value="99"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="payoutAmount" type="{http://www.ftn.uns.ac.rs/xws/xsd/common}Balance"/>
 *         &lt;element name="paymentCount">
 *           &lt;simpleType>
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}integer">
 *               &lt;minInclusive value="0"/>
 *               &lt;maxInclusive value="99"/>
 *             &lt;/restriction>
 *           &lt;/simpleType>
 *         &lt;/element>
 *         &lt;element name="paymentAmount" type="{http://www.ftn.uns.ac.rs/xws/xsd/common}Balance"/>
 *         &lt;element name="newBalance" type="{http://www.ftn.uns.ac.rs/xws/xsd/common}Balance"/>
 *         &lt;element name="items">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;sequence>
 *                   &lt;element name="item" type="{http://www.ftn.uns.ac.rs/xws/xsd/bs}StatementItem" maxOccurs="unbounded" minOccurs="0"/>
 *                 &lt;/sequence>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
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
@XmlType(name = "Statement", namespace = "http://www.ftn.uns.ac.rs/xws/xsd/bs", propOrder = {
    "accountNumber",
    "orderDate",
    "statementNumber",
    "previousBalance",
    "payoutCount",
    "payoutAmount",
    "paymentCount",
    "paymentAmount",
    "newBalance",
    "items"
})
@Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2015-06-26T10:32:50+02:00", comments = "JAXB RI v2.2.4-2")
public class Statement {

    @XmlElement(namespace = "http://www.ftn.uns.ac.rs/xws/xsd/bs", required = true)
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2015-06-26T10:32:50+02:00", comments = "JAXB RI v2.2.4-2")
    protected String accountNumber;
    @XmlElement(namespace = "http://www.ftn.uns.ac.rs/xws/xsd/bs", required = true)
    @XmlSchemaType(name = "date")
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2015-06-26T10:32:50+02:00", comments = "JAXB RI v2.2.4-2")
    protected XMLGregorianCalendar orderDate;
    @XmlElement(namespace = "http://www.ftn.uns.ac.rs/xws/xsd/bs")
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2015-06-26T10:32:50+02:00", comments = "JAXB RI v2.2.4-2")
    protected int statementNumber;
    @XmlElement(namespace = "http://www.ftn.uns.ac.rs/xws/xsd/bs", required = true)
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2015-06-26T10:32:50+02:00", comments = "JAXB RI v2.2.4-2")
    protected BigDecimal previousBalance;
    @XmlElement(namespace = "http://www.ftn.uns.ac.rs/xws/xsd/bs")
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2015-06-26T10:32:50+02:00", comments = "JAXB RI v2.2.4-2")
    protected int payoutCount;
    @XmlElement(namespace = "http://www.ftn.uns.ac.rs/xws/xsd/bs", required = true)
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2015-06-26T10:32:50+02:00", comments = "JAXB RI v2.2.4-2")
    protected BigDecimal payoutAmount;
    @XmlElement(namespace = "http://www.ftn.uns.ac.rs/xws/xsd/bs")
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2015-06-26T10:32:50+02:00", comments = "JAXB RI v2.2.4-2")
    protected int paymentCount;
    @XmlElement(namespace = "http://www.ftn.uns.ac.rs/xws/xsd/bs", required = true)
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2015-06-26T10:32:50+02:00", comments = "JAXB RI v2.2.4-2")
    protected BigDecimal paymentAmount;
    @XmlElement(namespace = "http://www.ftn.uns.ac.rs/xws/xsd/bs", required = true)
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2015-06-26T10:32:50+02:00", comments = "JAXB RI v2.2.4-2")
    protected BigDecimal newBalance;
    @XmlElement(namespace = "http://www.ftn.uns.ac.rs/xws/xsd/bs", required = true)
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2015-06-26T10:32:50+02:00", comments = "JAXB RI v2.2.4-2")
    protected Statement.Items items;

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
     * Gets the value of the statementNumber property.
     * 
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2015-06-26T10:32:50+02:00", comments = "JAXB RI v2.2.4-2")
    public int getStatementNumber() {
        return statementNumber;
    }

    /**
     * Sets the value of the statementNumber property.
     * 
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2015-06-26T10:32:50+02:00", comments = "JAXB RI v2.2.4-2")
    public void setStatementNumber(int value) {
        this.statementNumber = value;
    }

    /**
     * Gets the value of the previousBalance property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2015-06-26T10:32:50+02:00", comments = "JAXB RI v2.2.4-2")
    public BigDecimal getPreviousBalance() {
        return previousBalance;
    }

    /**
     * Sets the value of the previousBalance property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2015-06-26T10:32:50+02:00", comments = "JAXB RI v2.2.4-2")
    public void setPreviousBalance(BigDecimal value) {
        this.previousBalance = value;
    }

    /**
     * Gets the value of the payoutCount property.
     * 
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2015-06-26T10:32:50+02:00", comments = "JAXB RI v2.2.4-2")
    public int getPayoutCount() {
        return payoutCount;
    }

    /**
     * Sets the value of the payoutCount property.
     * 
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2015-06-26T10:32:50+02:00", comments = "JAXB RI v2.2.4-2")
    public void setPayoutCount(int value) {
        this.payoutCount = value;
    }

    /**
     * Gets the value of the payoutAmount property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2015-06-26T10:32:50+02:00", comments = "JAXB RI v2.2.4-2")
    public BigDecimal getPayoutAmount() {
        return payoutAmount;
    }

    /**
     * Sets the value of the payoutAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2015-06-26T10:32:50+02:00", comments = "JAXB RI v2.2.4-2")
    public void setPayoutAmount(BigDecimal value) {
        this.payoutAmount = value;
    }

    /**
     * Gets the value of the paymentCount property.
     * 
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2015-06-26T10:32:50+02:00", comments = "JAXB RI v2.2.4-2")
    public int getPaymentCount() {
        return paymentCount;
    }

    /**
     * Sets the value of the paymentCount property.
     * 
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2015-06-26T10:32:50+02:00", comments = "JAXB RI v2.2.4-2")
    public void setPaymentCount(int value) {
        this.paymentCount = value;
    }

    /**
     * Gets the value of the paymentAmount property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2015-06-26T10:32:50+02:00", comments = "JAXB RI v2.2.4-2")
    public BigDecimal getPaymentAmount() {
        return paymentAmount;
    }

    /**
     * Sets the value of the paymentAmount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2015-06-26T10:32:50+02:00", comments = "JAXB RI v2.2.4-2")
    public void setPaymentAmount(BigDecimal value) {
        this.paymentAmount = value;
    }

    /**
     * Gets the value of the newBalance property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2015-06-26T10:32:50+02:00", comments = "JAXB RI v2.2.4-2")
    public BigDecimal getNewBalance() {
        return newBalance;
    }

    /**
     * Sets the value of the newBalance property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2015-06-26T10:32:50+02:00", comments = "JAXB RI v2.2.4-2")
    public void setNewBalance(BigDecimal value) {
        this.newBalance = value;
    }

    /**
     * Gets the value of the items property.
     * 
     * @return
     *     possible object is
     *     {@link Statement.Items }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2015-06-26T10:32:50+02:00", comments = "JAXB RI v2.2.4-2")
    public Statement.Items getItems() {
        return items;
    }

    /**
     * Sets the value of the items property.
     * 
     * @param value
     *     allowed object is
     *     {@link Statement.Items }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2015-06-26T10:32:50+02:00", comments = "JAXB RI v2.2.4-2")
    public void setItems(Statement.Items value) {
        this.items = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;sequence>
     *         &lt;element name="item" type="{http://www.ftn.uns.ac.rs/xws/xsd/bs}StatementItem" maxOccurs="unbounded" minOccurs="0"/>
     *       &lt;/sequence>
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "", propOrder = {
        "item"
    })
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2015-06-26T10:32:50+02:00", comments = "JAXB RI v2.2.4-2")
    public static class Items {

        @XmlElement(namespace = "http://www.ftn.uns.ac.rs/xws/xsd/bs")
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2015-06-26T10:32:50+02:00", comments = "JAXB RI v2.2.4-2")
        protected List<StatementItem> item;

        /**
         * Gets the value of the item property.
         * 
         * <p>
         * This accessor method returns a reference to the live list,
         * not a snapshot. Therefore any modification you make to the
         * returned list will be present inside the JAXB object.
         * This is why there is not a <CODE>set</CODE> method for the item property.
         * 
         * <p>
         * For example, to add a new item, do as follows:
         * <pre>
         *    getItem().add(newItem);
         * </pre>
         * 
         * 
         * <p>
         * Objects of the following type(s) are allowed in the list
         * {@link StatementItem }
         * 
         * 
         */
        @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2015-06-26T10:32:50+02:00", comments = "JAXB RI v2.2.4-2")
        public List<StatementItem> getItem() {
            if (item == null) {
                item = new ArrayList<StatementItem>();
            }
            return this.item;
        }

        public void setItem(List<StatementItem> item) {
            this.item = item;
        }


    }

}
