package com.mahmoudsalah.swansontask.ui.activities.currency

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.mahmoudsalah.swansontask.ui.activities.currency.CurrencyActivity
import com.mahmoudsalah.swansontask.R
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initUi();
    }


    protected fun initUi()
    {

        tv_run.setOnClickListener(View.OnClickListener {

            iv_right.visibility = View.VISIBLE
            iv_wrong.visibility = View.GONE

            if(! ed_text1.text!!.toString().equals("") && !ed_text2.text!!.toString().equals(""))
            {
                if(detectAnagramUsingArraySort( ed_text1.text!!.toString(), ed_text2.text!!.toString()) == true)
                {
                    iv_right.visibility = View.VISIBLE
                    iv_wrong.visibility = View.GONE
                }
                else
                {
                    iv_right.visibility = View.GONE
                    iv_wrong.visibility = View.VISIBLE
                }
            }
        })

        tv_fibonacci_loop.setOnClickListener(View.OnClickListener {
            displayFibonacciLoop();
        })

        tv_fibonacci_recursive.setOnClickListener(View.OnClickListener {
            displayFibonacciRecursive()
        })

        tv_currency.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, CurrencyActivity::class.java)
            startActivity(intent)
        })
    }



    private fun detectAnagramUsingArraySort(txt1: String, txt2: String): Boolean {
        if (txt1.length != txt2.length) {
            return false
        }

        //Convert Strings to character Array
        val strArray1 = txt1.toCharArray()
        val strArray2 = txt2.toCharArray()

        //Sort the Arrays
        Arrays.sort(strArray1)
        Arrays.sort(strArray2)

        //Convert Arrays to String
        val sortedStr1 = String(strArray1)
        val sortedStr2 = String(strArray2)

        //Check Both String Equals or not After Sorting
        //and Return value True or False
        return sortedStr1 == sortedStr2
    }

    private fun detectAnagramUsingMap(one: String, two: String): Boolean {
        val map = HashMap<Char, Int>()

        for (c in one.toCharArray())
            if (map.containsKey(c))
                map[c] = map[c]!! + 1
            else
                map[c] = 1

        for (c in two.toCharArray())
            if (!map.containsKey(c))
                return false
            else {
                map[c] = map[c]!! - 1

                if (map[c] == 0)
                    map.remove(c)
            }

        return map.isEmpty()
    }


    //======================================= Fibonacci
    private fun displayFibonacciLoop() {
        tv_fibonacci_show.setText("")
        var fibonacci:String = ""
        val index = 9
        var index1 = 1
        var index2 = 1

        for (i in 1..index) {
            if(i==1)
                fibonacci += "Loop: " + index1
            else
                fibonacci += "-" + index1
            val sum = index1 + index2
            index1 = index2
            index2 = sum
        }

        tv_fibonacci_show.setText(fibonacci)
    }

    var i:Int =1
    var index1 = 1
    var index2 = 1
    var fibonacci:String = ""
    val index = 10
    private fun displayFibonacciRecursive() {

        //tv_fibonacci_show.setText("")
        if(i==1)
            fibonacci += "Recursive: " + index1
        else
            fibonacci += "-" + index1
        val sum = index1 + index2

        index1 = index2
        index2 = sum

        tv_fibonacci_show.setText(fibonacci)

        if(i < index-1)
        {
            i += 1
            displayFibonacciRecursive()
        }

    }


}