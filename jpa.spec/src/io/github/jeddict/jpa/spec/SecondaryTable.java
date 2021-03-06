//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source schema.
// Generated on: 2014.01.21 at 01:52:19 PM IST
//
package io.github.jeddict.jpa.spec;

import java.util.ArrayList;
import java.util.List;
import static java.util.stream.Collectors.toList;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import org.eclipse.persistence.internal.jpa.metadata.columns.PrimaryKeyForeignKeyMetadata;
import org.eclipse.persistence.internal.jpa.metadata.tables.SecondaryTableMetadata;
import static io.github.jeddict.jcode.jpa.JPAConstants.SECONDARY_TABLES_FQN;
import static io.github.jeddict.jcode.jpa.JPAConstants.SECONDARY_TABLE_FQN;
import io.github.jeddict.jpa.spec.validator.column.ForeignKeyValidator;
import io.github.jeddict.source.JavaSourceParserUtil;

/**
 *
 *
 * @Target({TYPE}) @Retention(RUNTIME) public @interface SecondaryTable { String
 * name(); String catalog() default ""; String schema() default "";
 * PrimaryKeyJoinColumn[] pkJoinColumns() default {}; UniqueConstraint[]
 * uniqueConstraints() default {}; Index[] indexes() default {}; }
 *
 *
 *
 * <p>
 * Java class for secondary-table complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 *
 * <pre>
 * &lt;complexType name="secondary-table">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;sequence>
 *           &lt;element name="primary-key-join-column" type="{http://xmlns.jcp.org/xml/ns/persistence/orm}primary-key-join-column" maxOccurs="unbounded" minOccurs="0"/>
 *           &lt;element name="primary-key-foreign-key" type="{http://xmlns.jcp.org/xml/ns/persistence/orm}foreign-key" minOccurs="0"/>
 *         &lt;/sequence>
 *         &lt;element name="unique-constraint" type="{http://xmlns.jcp.org/xml/ns/persistence/orm}unique-constraint" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="index" type="{http://xmlns.jcp.org/xml/ns/persistence/orm}index" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="catalog" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="schema" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "secondary-table", propOrder = {
    "primaryKeyJoinColumn",
    "primaryKeyForeignKey",
//    "uniqueConstraint",
//    "index",
    "foreignKey"
})
public class SecondaryTable extends Table {

    @XmlElement(name = "pk-jc")
    protected List<PrimaryKeyJoinColumn> primaryKeyJoinColumn;
    @XmlElement(name = "pk-fk")
    protected ForeignKey primaryKeyForeignKey;//REVENG PENDING
    @XmlElement(name = "fk")
    protected ForeignKey foreignKey;//REVENG PENDING

    private static SecondaryTable loadSecondaryTable(Element element, AnnotationMirror annotationMirror) {
        SecondaryTable secondaryTable = null;
        if (annotationMirror != null) {
            secondaryTable = new SecondaryTable();
            List uniqueConstraintsAnnot = (List) JavaSourceParserUtil.findAnnotationValue(annotationMirror, "uniqueConstraints");
            if (uniqueConstraintsAnnot != null) {
                for (Object uniqueConstraintsObj : uniqueConstraintsAnnot) {
                    secondaryTable.getUniqueConstraint().add(UniqueConstraint.load(element, (AnnotationMirror) uniqueConstraintsObj));
                }
            }
            
            List indexesAnnot = (List) JavaSourceParserUtil.findAnnotationValue(annotationMirror, "indexes");
            if (indexesAnnot != null) {
                for (Object indexObj : indexesAnnot) {
                    secondaryTable.getIndex().add(Index.load(element, (AnnotationMirror) indexObj));
                }
            }

            secondaryTable.name = (String) JavaSourceParserUtil.findAnnotationValue(annotationMirror, "name");
            secondaryTable.catalog = (String) JavaSourceParserUtil.findAnnotationValue(annotationMirror, "catalog");
            secondaryTable.schema = (String) JavaSourceParserUtil.findAnnotationValue(annotationMirror, "schema");
        }
        return secondaryTable;
    }

    public static List<SecondaryTable> loadTables(Element element) {
        List<SecondaryTable> secondaryTables = new ArrayList<>();

        AnnotationMirror secondaryTablesMirror = JavaSourceParserUtil.findAnnotation(element, SECONDARY_TABLES_FQN);
        if (secondaryTablesMirror != null) {
            List secondaryTablesMirrorList = (List) JavaSourceParserUtil.findAnnotationValue(secondaryTablesMirror, "value");
            if (secondaryTablesMirrorList != null) {
                for (Object secondaryTableObj : secondaryTablesMirrorList) {
                    secondaryTables.add(SecondaryTable.loadSecondaryTable(element, (AnnotationMirror) secondaryTableObj));
                }
            }
        } else {
            secondaryTablesMirror = JavaSourceParserUtil.findAnnotation(element, SECONDARY_TABLE_FQN);
            if (secondaryTablesMirror != null) {
                secondaryTables.add(SecondaryTable.loadSecondaryTable(element, secondaryTablesMirror));
            }
        }
        return secondaryTables;
    }
    

    /**
     * Gets the value of the primaryKeyJoinColumn property.
     *
     * <p>
     * This accessor method returns a reference to the live list, not a
     * snapshot. Therefore any modification you make to the returned list will
     * be present inside the JAXB object. This is why there is not a
     * <CODE>set</CODE> method for the primaryKeyJoinColumn property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPrimaryKeyJoinColumn().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PrimaryKeyJoinColumn }
     *
     *
     */
    public List<PrimaryKeyJoinColumn> getPrimaryKeyJoinColumn() {
        if (primaryKeyJoinColumn == null) {
            primaryKeyJoinColumn = new ArrayList<>();
        }
        return this.primaryKeyJoinColumn;
    }

    /**
     * Gets the value of the primaryKeyForeignKey property.
     *
     * @return possible object is {@link ForeignKey }
     *
     */
    public ForeignKey getPrimaryKeyForeignKey() {
        return primaryKeyForeignKey;
    }

    /**
     * Sets the value of the primaryKeyForeignKey property.
     *
     * @param value allowed object is {@link ForeignKey }
     *
     */
    public void setPrimaryKeyForeignKey(ForeignKey value) {
        this.primaryKeyForeignKey = value;
    }
    
    /**
     * Gets the value of the foreignKey property.
     *
     * @return possible object is {@link ForeignKey }
     *
     */
    public ForeignKey getForeignKey() {
        if(foreignKey==null){
            foreignKey = new ForeignKey();
        }
        return foreignKey;
    }

    /**
     * Sets the value of the foreignKey property.
     *
     * @param value allowed object is {@link ForeignKey }
     *
     */
    public void setForeignKey(ForeignKey value) {
        this.foreignKey = value;
    }

    @Override
    public SecondaryTableMetadata getAccessor() {
        SecondaryTableMetadata accessor = new SecondaryTableMetadata();
        super.getAccessor(accessor);
        accessor.setPrimaryKeyJoinColumns(this.getPrimaryKeyJoinColumn()
                .stream()
                .map(PrimaryKeyJoinColumn::getAccessor)
                .collect(toList()));
        if (ForeignKeyValidator.isNotEmpty(primaryKeyForeignKey)) {
            accessor.setPrimaryKeyForeignKey((PrimaryKeyForeignKeyMetadata)primaryKeyForeignKey.getAccessor(new PrimaryKeyForeignKeyMetadata()));
        }
        return accessor;
    }

}
