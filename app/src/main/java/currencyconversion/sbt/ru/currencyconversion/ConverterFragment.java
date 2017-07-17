package currencyconversion.sbt.ru.currencyconversion;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.simpleframework.xml.core.Persister;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Locale;

import currencyconversion.sbt.ru.currencyconversion.model.Currency;
import currencyconversion.sbt.ru.currencyconversion.model.ExchangeRates;
import currencyconversion.sbt.ru.currencyconversion.util.Constants;

import static currencyconversion.sbt.ru.currencyconversion.model.StateFragment.getStateFragment;


public class ConverterFragment extends Fragment {
    private static final String TAG = ConverterFragment.class.getSimpleName();

    private Spinner spinerFrom;
    private Spinner spinerTo;
    private EditText valFrom;
    private TextView valTo;
    private Button buttonCalculate;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_currency_conversion, container, false);
        findView(v);
        if (getStateFragment().getExchangeRates() == null) {
            new CurrencyDownloadTask().execute(Constants.URL);
        } else {
            initSpinners();
            if (getStateFragment().getResult() != null) {
                if (!getStateFragment().getResult().equals("")) {
                    valTo.setText(getStateFragment().getResult());
                }
            }
        }

        initListeners();
        return v;
    }

    private void initListeners() {
        buttonCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (valFrom.getText().toString().equals("")) {
                    Toast.makeText(getContext(), getString(R.string.empty_messsage), Toast.LENGTH_SHORT).show();
                } else if (Double.parseDouble(valFrom.getText().toString()) <= 0) {
                    Toast.makeText(getContext(), getString(R.string.negative_values), Toast.LENGTH_SHORT).show();
                } else {
                    String result = "Результат: " + String.format(Locale.getDefault(), "%.2f", calculate()) + " "
                            + getStateFragment().getExchangeRates().getListValute().get(spinerTo.getSelectedItemPosition());
                    valTo.setText(result);
                    getStateFragment().setResult(result);
                }
            }
        });
        spinerFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getStateFragment().setValSpinerFrom(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinerTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getStateFragment().setValSpinerTo(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void findView(View v) {
        spinerFrom = (Spinner) v.findViewById(R.id.spinner_from);
        spinerTo = (Spinner) v.findViewById(R.id.spinner_to);
        valFrom = (EditText) v.findViewById(R.id.currency_input_from);
        valTo = (TextView) v.findViewById(R.id.calculated_value);
        buttonCalculate = (Button) v.findViewById(R.id.button_calculate);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private double calculate() {
        Double valueFrom = Double.valueOf(valFrom.getText().toString());
        List<Currency> currencies = getStateFragment().getExchangeRates().getCurrencies();
        Currency currencyFrom = currencies.get(spinerFrom.getSelectedItemPosition());
        Currency currencyTo = currencies.get(spinerTo.getSelectedItemPosition());
        return valueFrom * getDoubleValue(currencyFrom.getValue()) / getDoubleValue(currencyFrom.getNominal()) * getDoubleValue(currencyTo.getNominal()) / getDoubleValue(currencyTo.getValue());
    }

    private Double getDoubleValue(String val) {
        return Double.parseDouble(val.replace(",", "."));
    }

    private void initSpinners() {
        ArrayAdapter<String> fromCurrencyAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_1, getStateFragment().getExchangeRates().getListValute());
        spinerFrom.setAdapter(fromCurrencyAdapter);
        spinerFrom.setSelection(getStateFragment().getValSpinerFrom());

        ArrayAdapter<String> toCurrencyAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_1, getStateFragment().getExchangeRates().getListValute());
        spinerTo.setAdapter(toCurrencyAdapter);
        spinerTo.setSelection(getStateFragment().getValSpinerTo());
    }

    private class CurrencyDownloadTask extends AsyncTask<String, Void, Boolean> {
        private static final String TAG = "CurrencyDownloadTask";
        private static final String ENCODING = "windows-1251";
        private static final String WRENC = "Wrong Encoding";
        private static final int TIMEOUT = 10000;

        private CurrencyDownloadTask() {
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d(TAG, "CurrencyDownloadTask start onPreExecute");
        }

        @Override
        protected Boolean doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL(params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setConnectTimeout(TIMEOUT);
                InputStream in = urlConnection.getInputStream();
                String xml = readStream(in);

                Reader reader = new StringReader(xml);
                Persister serializer = new Persister();
                try {
                    getStateFragment().setExchangeRates(serializer.read(ExchangeRates.class, reader, false));
                } catch (Exception e) {
                    Log.e(ConverterFragment.TAG, "Serializer not read", e);
                }
            } catch (IOException e) {
                Log.e(TAG, "IOExp", e);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return true;
        }

        private String readStream(InputStream in) {
            BufferedReader reader = null;
            StringBuilder response = new StringBuilder();
            try {
                reader = new BufferedReader(new InputStreamReader(in, ENCODING));
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            } catch (UnsupportedEncodingException e) {
                Log.e(TAG, WRENC, e);
            } catch (IOException e) {
                Log.e(TAG, "IOException", e);
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        Log.e(ConverterFragment.TAG, "IOException", e);
                    }
                }
            }
            return response.toString();
        }

        @Override
        protected void onPostExecute(Boolean isDownloaded) {
            super.onPostExecute(isDownloaded);
            initSpinners();
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            Log.d(TAG, "CurrencyDownloadTask cancel");
        }
    }
}