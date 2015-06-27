
package xml.bs;

import javax.annotation.Generated;
import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for BsExceptionEnum.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="BsExceptionEnum">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="invalidAccountNumber"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "BsExceptionEnum", namespace = "http://www.ftn.uns.ac.rs/xws/xsd/bs")
@XmlEnum
@Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2015-06-26T10:32:50+02:00", comments = "JAXB RI v2.2.4-2")
public enum BsExceptionEnum {

    @XmlEnumValue("invalidAccountNumber")
    INVALID_ACCOUNT_NUMBER("invalidAccountNumber");
    private final String value;

    BsExceptionEnum(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static BsExceptionEnum fromValue(String v) {
        for (BsExceptionEnum c: BsExceptionEnum.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
