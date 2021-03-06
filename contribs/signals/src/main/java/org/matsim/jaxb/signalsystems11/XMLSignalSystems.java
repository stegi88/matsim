//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-558 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2011.09.20 at 07:21:37 PM MESZ 
//


package org.matsim.jaxb.signalsystems11;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


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
 *         &lt;element name="signalSystemDefinition" type="{http://www.matsim.org/files/dtd}signalSystemDefinitionType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="signalGroupDefinition" type="{http://www.matsim.org/files/dtd}signalGroupDefinitionType" maxOccurs="unbounded" minOccurs="0"/>
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
    "signalSystemDefinition",
    "signalGroupDefinition"
})
@XmlRootElement(name = "signalSystems")
public class XMLSignalSystems {

    protected List<XMLSignalSystemDefinitionType> signalSystemDefinition;
    protected List<XMLSignalGroupDefinitionType> signalGroupDefinition;

    /**
     * Gets the value of the signalSystemDefinition property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the signalSystemDefinition property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSignalSystemDefinition().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link XMLSignalSystemDefinitionType }
     * 
     * 
     */
    public List<XMLSignalSystemDefinitionType> getSignalSystemDefinition() {
        if (signalSystemDefinition == null) {
            signalSystemDefinition = new ArrayList<XMLSignalSystemDefinitionType>();
        }
        return this.signalSystemDefinition;
    }

    /**
     * Gets the value of the signalGroupDefinition property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the signalGroupDefinition property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSignalGroupDefinition().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link XMLSignalGroupDefinitionType }
     * 
     * 
     */
    public List<XMLSignalGroupDefinitionType> getSignalGroupDefinition() {
        if (signalGroupDefinition == null) {
            signalGroupDefinition = new ArrayList<XMLSignalGroupDefinitionType>();
        }
        return this.signalGroupDefinition;
    }

}
