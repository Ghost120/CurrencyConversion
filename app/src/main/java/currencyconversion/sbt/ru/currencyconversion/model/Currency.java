package currencyconversion.sbt.ru.currencyconversion.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by Kasyanenko Konstantin
 * on 03.07.2017.
 */

@Root(name = "Valute")
public class Currency {

    @Attribute(name = "ID")
    private String id;

    @Element(name = "NumCode")
    private String numCode;

    @Element(name = "CharCode")
    private String charCode;

    @Element(name = "Nominal")
    private String nominal;

    @Element(name = "Name")
    private String name;

    @Element(name = "Value")
    private String value;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumCode() {
        return numCode;
    }

    public void setNumCode(String numCode) {
        this.numCode = numCode;
    }

    public String getCharCode() {
        return charCode;
    }

    public void setCharCode(String charCode) {
        this.charCode = charCode;
    }

    public String getNominal() {
        return nominal;
    }

    public void setNominal(String nominal) {
        this.nominal = nominal;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
