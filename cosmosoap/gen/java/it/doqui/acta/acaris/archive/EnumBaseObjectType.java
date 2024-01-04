
package it.doqui.acta.acaris.archive;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per enumBaseObjectType.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * <p>
 * <pre>
 * &lt;simpleType name="enumBaseObjectType">
 *   &lt;restriction base="{common.acaris.acta.doqui.it}string">
 *     &lt;enumeration value="DocumentPropertiesType"/>
 *     &lt;enumeration value="FolderPropertiesType"/>
 *     &lt;enumeration value="RelationshipPropertiesType"/>
 *     &lt;enumeration value="PolicyPropertiesType"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "enumBaseObjectType")
@XmlEnum
public enum EnumBaseObjectType {

    @XmlEnumValue("DocumentPropertiesType")
    DOCUMENT_PROPERTIES_TYPE("DocumentPropertiesType"),
    @XmlEnumValue("FolderPropertiesType")
    FOLDER_PROPERTIES_TYPE("FolderPropertiesType"),
    @XmlEnumValue("RelationshipPropertiesType")
    RELATIONSHIP_PROPERTIES_TYPE("RelationshipPropertiesType"),
    @XmlEnumValue("PolicyPropertiesType")
    POLICY_PROPERTIES_TYPE("PolicyPropertiesType");
    private final String value;

    EnumBaseObjectType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static EnumBaseObjectType fromValue(String v) {
        for (EnumBaseObjectType c: EnumBaseObjectType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
