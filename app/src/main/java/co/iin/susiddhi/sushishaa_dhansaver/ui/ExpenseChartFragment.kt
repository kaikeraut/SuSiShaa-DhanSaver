package co.iin.susiddhi.sushishaa_dhansaver.ui

import android.graphics.Color
import android.graphics.Typeface
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import co.iin.susiddhi.sushishaa_dhansaver.R
import co.iin.susiddhi.sushishaa_dhansaver.database.DataBaseHandler
import co.iin.susiddhi.sushishaa_dhansaver.database.ExpenseChartCalulatedData
import co.iin.susiddhi.sushishaa_dhansaver.database.FILTER_MONTHWISE
import co.iin.susiddhi.sushishaa_dhansaver.database.PiChartClassModel
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.LegendEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.MPPointF
import java.time.Year
import java.util.*
import kotlin.math.roundToInt

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ExpenseChartFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ExpenseChartFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var pieChart: PieChart
    val globalCategoryList: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view:View = inflater.inflate(R.layout.fragment_expense_chart, container, false)
        setHasOptionsMenu(true)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(true)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = context?.getString(R.string.app_name)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // on below line we are initializing our
        // variable with their ids.
        pieChart = requireView()?.findViewById(R.id.pieChart)
        //https://devendrac706.medium.com/date-picker-using-kotlin-in-android-studio-datepickerdialog-android-studio-tutorial-kotlin-3bbc606585a
        var buttonYearPrevious: Button = requireView()?.findViewById(R.id.buttonMonthPrevious)
        var buttonMonthPrevious: Button = requireView()?.findViewById(R.id.buttonDayPrevious)
        var buttonExpenseDate: Button = requireView()?.findViewById(R.id.buttonExpenseDate)
        var buttonMonthNext: Button = requireView()?.findViewById(R.id.buttonDayNext)
        var buttonYearNext: Button = requireView()?.findViewById(R.id.buttonMonthNext)

        buttonYearPrevious.visibility = View.GONE
        buttonYearNext.visibility = View.GONE

        //TextView
        var textViewMoneyCredits: TextView = requireView()?.findViewById(R.id.textViewMoneyStart)
        var textViewMoneyDebits: TextView = requireView()?.findViewById(R.id.textViewMoneySpent)
        var textViewMoneyLeft: TextView = requireView()?.findViewById(R.id.textViewMoneyLeft)


        val sdf = SimpleDateFormat("MM-YYYY")
        val currentDate = sdf.format(Date())
        buttonExpenseDate.setText(currentDate)
        var year = Year.now().toString().toInt()
        val calender = Calendar.getInstance()
        var month_number_format = SimpleDateFormat("MM")
        var month_number = month_number_format.format(calender.time).toString().toInt()-1
        buttonMonthPrevious.setOnClickListener{
            month_number -= 1
            if(month_number == -1)
            {
                month_number = 11
                year -= 1
            }
            buttonExpenseDate.text = "${month_number}, $year"
            updatePiChartView(month_number,year, pieChart, textViewMoneyCredits, textViewMoneyDebits, textViewMoneyLeft)
        }
        buttonMonthNext.setOnClickListener{
            month_number += 1
            if(month_number == 12)
            {
                month_number = 0
                year += 1
            }
            buttonExpenseDate.text = "${month_number}, $year"
            updatePiChartView(month_number,year, pieChart, textViewMoneyCredits, textViewMoneyDebits, textViewMoneyLeft)
        }
        buttonExpenseDate.setOnClickListener{
            val currentDate = sdf.format(Date())
            buttonExpenseDate.text = currentDate
            val calendar = Calendar.getInstance()
            calendar.time = Date()
            var currentMonth = calendar.get(Calendar.MONTH) + 1 //Jan starts from 0
            year = Year.now().toString().toInt()
            month_number = currentMonth-1
            updatePiChartView(currentMonth, year, pieChart, textViewMoneyCredits, textViewMoneyDebits, textViewMoneyLeft)
        }

        pieChart.setOnChartValueSelectedListener(object :
            OnChartValueSelectedListener {
            override fun onNothingSelected() {
                Log.e("Entry selected", "Nothing selected.")
            }

            override fun onValueSelected(e: Entry?, h: Highlight?) {

                //val pieEntry = e as PieEntry
                //val label: String = pieEntry.label
                Log.e("Entry selected", e.toString())
                var selectedCategory = ""
                var selectedCategoryDebit = ""
                if (e != null && h != null) {
                    Log.e("CHART CLICK DATA:", "e.y:${e.getY()} h.x:${h.x} h.dataIndex:${h.dataIndex}")
                    selectedCategory = globalCategoryList[h.x.toInt()]?.split("#")[0]
                    selectedCategoryDebit = globalCategoryList[h.x.toInt()]?.split("#")[1]
                    Log.w("CATEGORY:", "GET CLICKED CATEGORY: ${selectedCategory}")
                }
                val bundle = Bundle()
                bundle.putString("PiChartCategory", selectedCategory) // Put anything what you want
                bundle.putString("PiChartCategoryDate", "${month_number},$year") // Put anything what you want
                bundle.putString("PiChartCategoryTotalRupee", selectedCategoryDebit) // Put anything what you want
                var fragment = ExpenseSubCategoryChart()
                fragment.arguments = bundle
                activity?.run {
                    supportFragmentManager.beginTransaction().replace(R.id.main_fragment_section_mainactivity, fragment)
                        .addToBackStack(ExpenseSubCategoryChart.toString())
                        .commit()
                }

            }
        })

        val calendar = Calendar.getInstance()
        calendar.time = Date()
        var currentMonth = calendar.get(Calendar.MONTH) + 1 //Jan starts from 0
        updatePiChartView(currentMonth, year, pieChart, textViewMoneyCredits, textViewMoneyDebits, textViewMoneyLeft)
    }

    private fun updatePiChartView(
        filterMonth: Int, filterYear:Int,
        pieChart: PieChart?,
        textViewMoneyCredits: TextView,
        textViewMoneyDebits: TextView,
        textViewMoneyLeft: TextView
    ) {
        // on below line we are setting user percent value,
        // setting description as enabled and offset for pie chart
        if (pieChart != null) {
            val piData = populatePiChartField(FILTER_MONTHWISE, filterMonth, filterYear, textViewMoneyCredits, textViewMoneyDebits, textViewMoneyLeft)

            pieChart.setUsePercentValues(true)
            pieChart.getDescription().setEnabled(false)
            pieChart.setExtraOffsets(5f, 10f, 5f, 5f)

            // on below line we are setting drag for our pie chart
            pieChart.setDragDecelerationFrictionCoef(0.95f)

            // on below line we are setting hole
            // and hole color for pie chart
            pieChart.setDrawHoleEnabled(true)
            pieChart.setHoleColor(Color.WHITE)

            // on below line we are setting circle color and alpha
            pieChart.setTransparentCircleColor(Color.WHITE)
            pieChart.setTransparentCircleAlpha(110)

            // on below line we are setting hole radius
            pieChart.setHoleRadius(58f)
            pieChart.setTransparentCircleRadius(61f)

            // on below line we are setting center text
            pieChart.setDrawCenterText(true)
            if(piData.entriesData.size == 0)
            {
                pieChart.centerText = "NO EXPENSE DATA"
            }
            else
            {
                pieChart.centerText = "Expenses"
            }
            pieChart.setCenterTextSize(20f)

            // on below line we are setting
            // rotation for our pie chart
            pieChart.setRotationAngle(0f)

            // enable rotation of the pieChart by touch
            pieChart.setRotationEnabled(true)
            pieChart.setHighlightPerTapEnabled(true)

            // on below line we are setting animation for our pie chart
            pieChart.animateY(1400, Easing.EaseInOutQuad)

            // on below line we are disabling our legend for pie chart
            pieChart.legend.isEnabled = true
            pieChart.setEntryLabelColor(Color.WHITE)
            pieChart.setEntryLabelTextSize(12f)
            pieChart.legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER)
            pieChart.legend.setTextSize(14f)
            pieChart.legend.setForm(Legend.LegendForm.CIRCLE)
            pieChart.legend.setWordWrapEnabled(true)
            // on below line we are creating array list and
            // adding data to it to display in pie chart
            // on below line we are setting pie data set
            val dataSet = PieDataSet(piData.entriesData, "Expenses Details")
            pieChart.legend.setCustom(piData.entriesLegend)

            // on below line we are setting icons.
            dataSet.setDrawIcons(false)

            // on below line we are setting slice for pie
            dataSet.sliceSpace = 3f
            dataSet.iconsOffset = MPPointF(0f, 40f)
            dataSet.selectionShift = 5f

            // on below line we are setting colors.
            dataSet.colors = piData.colors

            // on below line we are setting pie data set
            val data = PieData(dataSet)
            data.setValueFormatter(PercentFormatter())
            data.setValueTextSize(15f)
            data.setValueTypeface(Typeface.DEFAULT_BOLD)
            data.setValueTextColor(Color.WHITE)
            pieChart.setData(data)

            // undo all highlights
            pieChart.highlightValues(null)

            // loading chart
            pieChart.invalidate()
        }
    }

    private fun populatePiChartField(filterType: Int, filterValue:Int, filterSubvalue:Int, textViewMoneyCredits: TextView, textViewMoneyDebits: TextView, textViewMoneyLeft: TextView): PiChartClassModel {
        val entriesData: ArrayList<PieEntry> = ArrayList()
        val colors: ArrayList<Int> = ArrayList()
        val entriesLegend: MutableList<LegendEntry> = ArrayList()

        val db = context?.let { DataBaseHandler(it) }
        val calcData: ExpenseChartCalulatedData? =
            db?.calculateDateForPieChart(filterType, filterValue, filterSubvalue)

        if (calcData != null) {
            textViewMoneyCredits.text = "+${calcData.totalCredit.toString()}"
            textViewMoneyDebits.text = "-${calcData.totalDebit.toString()}"
            textViewMoneyLeft.text = "= ${(calcData.totalCredit - calcData.totalDebit).toString()}"
            for (catgory in calcData.categoryExpenseDataMap) {
                globalCategoryList.add(catgory.toString()+"#${catgory.value}")
                Log.i("TAG", "(${catgory.value}/${calcData.totalDebit})*100")
                var categoryFloat = (catgory.value.toFloat() / calcData.totalDebit.toFloat()) * 100
                Log.i("TAG", "${catgory.key}: $categoryFloat")
                entriesData.add(PieEntry(categoryFloat))
                val entry = LegendEntry()
                val rnd = Random()
                var color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
                entry.formColor = color
                entry.label = catgory.key + "(${catgory.value}, ${categoryFloat.roundToInt()}%)"
                entriesLegend.add(entry)
                // add a lot of colors to list
                colors.add(color)
            }
        }
        return PiChartClassModel(entriesData, entriesLegend, colors)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ExpenseChartFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ExpenseChartFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}