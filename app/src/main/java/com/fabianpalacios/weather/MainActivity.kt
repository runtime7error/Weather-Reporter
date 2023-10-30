package com.fabianpalacios.weather

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.fabianpalacios.weather.services.model.Root
import com.fabianpalacios.weather.services.model.WeatherService

class MainActivity : AppCompatActivity() {
    private var txtCountryISOCode: EditText? = null
    private var txtCityName: EditText? = null
    private var lblCurrent: TextView? = null
    private var lblMin: TextView? = null
    private var lblMax: TextView? = null
    private var service: WeatherService? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
        service = WeatherService("a9cf0f7a3cc84a884d84d4df48f057c2")
    }

    private fun initViews() {
        txtCountryISOCode = findViewById(R.id.txtCountryISOCode)
        txtCityName = findViewById(R.id.txtCityName)
        lblCurrent = findViewById(R.id.lblCurrent)
        lblMin = findViewById(R.id.lblMin)
        lblMax = findViewById(R.id.lblMax)
    }

    fun btnGetInfoOnClick(view: View?) {
        val alert = AlertDialog.Builder(this)
        val text = StringBuilder()
        if (txtCountryISOCode!!.text.toString().isEmpty() || txtCityName!!.text.toString()
                .isEmpty()
        ) {
            text.append(getString(R.string.Fields_cannot_be_empty))
            alert.setMessage(text)
            alert.setPositiveButton("close", null)
            alert.show()
        } else {
            getWeatherInfo(txtCityName!!.text.toString(), txtCountryISOCode!!.text.toString())
        }
    }

    private fun getWeatherInfo(cityName: String?, countryISOCode: String?) {
        service!!.requestWeatherData(
            cityName, countryISOCode
        ) { isNetworkError: Boolean, statusCode: Int, root: Root ->
            if (!isNetworkError) {
                if (statusCode == 200) {
                    showWeatherInfo(root)
                } else {
                    Log.d("Clima", "Erro no Serviço")
                }
            } else {
                Log.d("Clima", "Erro na Rede")
            }
        }
    }

    @SuppressLint("SetTextI18n")
    fun showWeatherInfo(root: Root) {
        val temp = root.main.temp.toString()
        val tempMin = root.main.tempMin.toString()
        val tempMax = root.main.tempMax.toString()
        lblCurrent!!.text = getString(R.string.current) + " " + temp
        lblMin!!.text = getString(R.string.minimum) + " " + tempMin
        lblMax!!.text = getString(R.string.maximum) + " " + tempMax
    }
}