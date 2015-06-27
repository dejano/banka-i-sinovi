
package xml.bs;

import javax.annotation.Generated;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for Message complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="Message">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="messageId" type="{http://www.ftn.uns.ac.rs/xws/xsd/common}MessageId" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Message", namespace = "http://www.ftn.uns.ac.rs/xws/xsd/common")
@Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2015-06-26T10:32:50+02:00", comments = "JAXB RI v2.2.4-2")
public abstract class Message {

    @XmlAttribute(name = "messageId")
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2015-06-26T10:32:50+02:00", comments = "JAXB RI v2.2.4-2")
    protected String messageId;

    /**
     * Gets the value of the messageId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2015-06-26T10:32:50+02:00", comments = "JAXB RI v2.2.4-2")
    public String getMessageId() {
        return messageId;
    }

    /**
     * Sets the value of the messageId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2015-06-26T10:32:50+02:00", comments = "JAXB RI v2.2.4-2")
    public void setMessageId(String value) {
        this.messageId = value;
    }

}
