package currencyconversion.sbt.ru.currencyconversion.model;

import java.util.List;

/**
 * Created by Kasyanenko Konstantin
 * on 16.07.2017.
 */

public class StateFragment {
    private static StateFragment sStateFragment;

    private StateFragment() {
    }

    private List<Currency> mCurrencyList;
    private static int valSpinerFrom;
    private static int valSpinerTo;
    private String valFrom;
    private String valTo;
    private ExchangeRates mExchangeRates;

    private String result;

    public static StateFragment getStateFragment() {
        if (sStateFragment == null) {
            sStateFragment = new StateFragment();
            valSpinerFrom = 1;
            valSpinerTo = 2;
        }
        return sStateFragment;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public ExchangeRates getExchangeRates() {
        return mExchangeRates;
    }

    public void setExchangeRates(ExchangeRates exchangeRates) {
        mExchangeRates = exchangeRates;
    }

    public List<Currency> getCurrencyList() {
        return mCurrencyList;
    }

    public void setCurrencyList(List<Currency> currencyList) {
        mCurrencyList = currencyList;
    }

    public int getValSpinerFrom() {
        return valSpinerFrom;
    }

    public void setValSpinerFrom(int valSpinerFrom) {
        this.valSpinerFrom = valSpinerFrom;
    }

    public int getValSpinerTo() {
        return valSpinerTo;
    }

    public void setValSpinerTo(int valSpinerTo) {
        this.valSpinerTo = valSpinerTo;
    }

    public String getValFrom() {
        return valFrom;
    }

    public void setValFrom(String valFrom) {
        this.valFrom = valFrom;
    }

    public String getValTo() {
        return valTo;
    }

    public void setValTo(String valTo) {
        this.valTo = valTo;
    }
}
