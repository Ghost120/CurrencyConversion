package currencyconversion.sbt.ru.currencyconversion.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kasyanenko Konstantin
 * on 04.07.2017.
 */

@Root(name = "ValCurs ")
public class ExchangeRates {

    @Attribute(name = "Date")
    private String mdate;

    @Attribute(name = "name")
    private String name;

    @ElementList(inline = true, name = "Currency")
    private List<Currency> valute;

    public void setDate(String date) {
        mdate = date;
    }

    public String getDate() {
        return mdate;
    }

    public List<Currency> getCurrencies() {
        return valute;
    }

    public void setCurrencies(List<Currency> currencies) {
        valute = currencies;
    }

    public List<String> getListValute() {
        List<String> listVal = new ArrayList<>();
        for (Currency cur : valute
                ) {
            listVal.add(cur.getCharCode());
        }
        return listVal;
    }
}
