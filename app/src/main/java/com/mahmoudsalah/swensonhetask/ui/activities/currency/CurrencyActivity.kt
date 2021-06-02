package com.mahmoudsalah.swansontask.ui.activities.currency

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.base.medicalgate.ui.activties.CurrencyViewModel
import com.base.medicalgate.ui.activties.SingleLiveEvent
import com.mahmoudsalah.swansontask.R
import com.mahmoudsalah.swansontask.contracts.CurrencyContract
import com.mahmoudsalah.swansontask.entities.request.currencies.ParaSendModel
import com.mahmoudsalah.swansontask.entities.response.ParaResponseModel
import com.mahmoudsalah.swansontask.entities.response.Rate
import com.mahmoudsalah.swansontask.entities.response.Rates
import com.mahmoudsalah.swansontask.listeners.CurrencyAdapterListener
import com.mahmoudsalah.swansontask.ui.adapters.CurrenciesAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_currencies.*
import kotlinx.android.synthetic.main.layout_calculator.*
import retrofit2.Response
import java.lang.reflect.Field
import java.util.*
import kotlin.collections.ArrayList


@AndroidEntryPoint
class CurrencyActivity : AppCompatActivity(), CurrencyContract, CurrencyAdapterListener {


    private val viewModel: CurrencyViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setContentView(R.layout.activity_currencies)
        initUi()
        setupRecyclerView()
        loadFromFixer()
    }

    fun showProgress() {
        this.showProgress(true)
    }

    fun hideProgress() {
        this.showProgress(false)
    }

    private fun hideSoftInput() {
        val inputMethodManager = this.getSystemService(
            Activity.INPUT_METHOD_SERVICE
        ) as InputMethodManager
        var view = this.currentFocus
        if (view == null) {
            view = View(this)
        }
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private fun showProgress(show: Boolean) {
        runOnUiThread {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
                showProgressApi13(show)
            } else {
                currency_progress!!.visibility = if (show) View.VISIBLE else View.GONE
            }
        }
    }

    private fun showProgressApi13(show: Boolean) {
        val shortAnimTime =
            this.resources.getInteger(android.R.integer.config_shortAnimTime)
        currency_progress!!.visibility = if (show) View.VISIBLE else View.GONE
        currency_progress!!.animate()
            .setDuration(shortAnimTime.toLong())
            .alpha(if (show) 1F else 0.toFloat())
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    currency_progress!!
                        .setVisibility(if (show) View.VISIBLE else View.GONE)
                }
            })
    }

    fun initUi()
    {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        ed_value_base.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                if(!ed_value_base.text.toString().equals(""))
                    tv_result.setText(String.format("%.3f", (ed_value_base.text.toString().toDouble() * currentRate.value.toString().toDouble()).toString().toDouble()))
                else
                    tv_result.setText((1 * currentRate.value.toString().toDouble()).toString())

            }

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
            }
        })
    }

    public fun showKeyboard(activity:Activity) {
        if (this != null) {
            getWindow()
                .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    public fun hideKeyboard(activity:Activity) {
        if (activity != null) {
            activity.getWindow()
                .setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }
    }

    private fun navigateToActivity(aClass: Class<*>, paraResponseModel: ParaResponseModel) {
    }

    @Override
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        try {
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }catch (e:Exception) {}

        return true
    }

    @Override
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val view = currentFocus
        if (view != null && (ev.action == MotionEvent.ACTION_UP || ev.action == MotionEvent.ACTION_MOVE) && view is EditText && !view.javaClass.name
                .startsWith("android.webkit.")
        ) {
            val scrcoords = IntArray(2)
            view.getLocationOnScreen(scrcoords)
            val x = ev.rawX + view.getLeft() - scrcoords[0]
            val y = ev.rawY + view.getTop() - scrcoords[1]
            if (x < view.getLeft() || x > view.getRight() || y < view.getTop() || y > view.getBottom()) (this.getSystemService(
                Context.INPUT_METHOD_SERVICE
            ) as InputMethodManager).hideSoftInputFromWindow(
                this.window.decorView.applicationWindowToken, 0
            )
        }
        return super.dispatchTouchEvent(ev)
    }


    lateinit var paraSendModel: ParaSendModel
    var headers: HashMap<String, Any>? = null
    override fun loadFromFixer() {
        hideSoftInput()

        paraSendModel = ParaSendModel()

        val updateEvent = SingleLiveEvent<Response<ParaResponseModel>>()

        updateEvent.observe(this, androidx.lifecycle.Observer {
            hideProgress()

            if(updateEvent.value!!.code() == 200)
            {
                val responseModel:ParaResponseModel? = updateEvent.value!!.body()
                renderCurrencies(responseModel!!.getRates());

                //navigateToActivity(HomeActivity::class.java, sentLogin!!)
            }
            else
            {
                errorAuthenticationFailure(updateEvent.value!!.errorBody()!!.string())
            }
        } )

        this.showProgress(true)
        viewModel.loacCurrencies(paraSendModel, headers, updateEvent)

    }


    var currencyAdapter:CurrenciesAdapter? = null
    var mLayoutManager: LinearLayoutManager? = null
    override fun setupRecyclerView() {
        currencyAdapter = CurrenciesAdapter(this)
        currencyAdapter!!.setOnItemClickListener(this)
        mLayoutManager = LinearLayoutManager(this)
        rv_currencies.layoutManager = mLayoutManager
        //rv_vacations.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL ,false)
        rv_currencies.adapter = currencyAdapter
    }

    lateinit var ratesMap:Map<String, Any>
    lateinit var _rates:ArrayList<Rate>
    lateinit var _rate:Rate
    override fun renderCurrencies(rates: Rates) {

        _rates = ArrayList()

        ratesMap = this!!.getMap(rates)!!

        for ((key, value) in ratesMap) {

            _rate = Rate()
            _rate.country = key
            _rate.value = value as Double

            _rates.add(_rate)
        }

        println("ret length : " + _rates.size)

        //for(i in 0 until getMap(rates)!!.size)
        //{
        //    _rates.add(Rate().country)
        //}
        currencyAdapter!!.setTabCollection(_rates)
    }

    override fun warnInternetConnection() {
        TODO("Not yet implemented")
    }

    override fun errorAuthenticationFailure(error: String) {
        TODO("Not yet implemented")
    }


    fun getMap(o: Any): Map<String, Any>? {
        val result: MutableMap<String, Any> =
            HashMap()
        val declaredFields: Array<Field> = o.javaClass.declaredFields
        for (field in declaredFields) {
            field.isAccessible = true
            result[field.getName()] = field.get(o)
        }
        return result
    }

    lateinit var currentRate:Rate
    public fun hideCalculator(view: View)
    {
        ll_calculator.visibility = View.GONE
        rv_currencies.visibility = View.VISIBLE

        tv_result.setText("0")

        ed_value_base.setText("")
    }

    override fun onCurrencyClicked(rate: Rate?) {

        currentRate = rate!!
        println("ret onCurrencyClicked : " + currentRate!!.value)

        tv_result.setText(currentRate.value.toString())

        ll_calculator.visibility = View.VISIBLE
        rv_currencies.visibility = View.GONE

        ed_value_base.setText("1")
        tv_name_base.setText("USD")

        tv_value_current.setText(rate!!.value.toString())
        tv_name_current.setText(rate.country)
    }
}
