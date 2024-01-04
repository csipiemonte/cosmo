
package it.doqui.acta.acaris.archive;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import it.doqui.acta.acaris.common.CommonPropertiesType;


/**
 * <p>Classe Java per ArchivePropertiesType complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="ArchivePropertiesType">
 *   &lt;complexContent>
 *     &lt;extension base="{common.acaris.acta.doqui.it}CommonPropertiesType">
 *       &lt;sequence>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArchivePropertiesType")
@XmlSeeAlso({
    RelationshipPropertiesType.class,
    DocumentPropertiesType.class,
    PolicyPropertiesType.class,
    FolderPropertiesType.class
})
public abstract class ArchivePropertiesType
    extends CommonPropertiesType
{


}
