package co.iin.susiddhi.sushishaa_dhansaver.setting.ui

import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.iin.susiddhi.susishaa_dhansaver.R
import co.iin.susiddhi.susishaa_dhansaver.adapter.FixedExpenseCardViewListAdapter
import co.iin.susiddhi.susishaa_dhansaver.database.DataBaseHandler
import co.iin.susiddhi.susishaa_dhansaver.database.ExpenseClassModel
import co.iin.susiddhi.susishaa_dhansaver.database.FixedExpenseClassModel
import co.iin.susiddhi.susishaa_dhansaver.database.SUSISHAA_CATEGORY_INVESTMENT
import co.iin.susiddhi.susishaa_dhansaver.setting.SettingsFragment
import java.util.Date
import java.util.regex.Matcher
import java.util.regex.Pattern

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [UpdateFixedExpenseFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class UpdateFixedExpenseFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

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
        var view:View =  inflater.inflate(R.layout.fragment_update_fixed_expense, container, false)
        setHasOptionsMenu(true)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(true)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Update Fixed Expense"
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var buttonUpdateFixedExpense: Button = requireView()?.findViewById(R.id.buttonUpdateFixedExpense)
        var buttonDeleteFixedExpense: Button = requireView()?.findViewById(R.id.buttonDeleteFixedExpense)

        var editTextRupeeFixedNo: EditText = requireView()?.findViewById(R.id.editTextRupeeFixedNo)
        var editTextFixedDate: EditText = requireView()?.findViewById(R.id.editTextFixedDate)
        var editTextFixedComments: EditText = requireView()?.findViewById(R.id.editTextFixedComments)
        var spinnerFixedOccurance: Spinner = requireView()?.findViewById(R.id.spinnerFixedOccurance)
        var spinnerFixedDay: Spinner = requireView()?.findViewById(R.id.spinnerFixedDay)
        var spinnerFixedMonth: Spinner = requireView()?.findViewById(R.id.spinnerFixedMonth)
        var spinnerFixedCategory: Spinner = requireView()?.findViewById(R.id.spinnerFixedCategory)
        var spinnerFixedSubCategory: Spinner = requireView()?.findViewById(R.id.spinnerFixedSubCategory)

        var occurrenceList:ArrayList<String> = ArrayList()

        var fixedExpenseId = 0


        val bundle = this.arguments
        var recvdData = bundle?.getString("FixedExpenseEditDeleteRequest").toString()
        Log.i("RECVDDATA", "recvdData: ${recvdData}")
        var col = recvdData.split("~")
        var model = FixedExpenseClassModel(
            col[0].toInt(),
            col[1].toInt(),
            col[2],
            col[3],
            col[4],
            col[5],
            col[6],
            col[7]
        )

        fixedExpenseId = model.id
        editTextRupeeFixedNo.setText(model.expense.toString())
        editTextFixedDate.setText(model.enddate)
        editTextFixedComments.setText(model.comments)

        var CONST_OCCURRENCE_YEARLY = "YEARLY"
        var CONST_OCCURRENCE_MONTHLY = "MONTHLY"
        var CONST_OCCURRENCE_WEEKLY = "WEEKLY"
        var CONST_OCCURRENCE_DAILY = "DAILY"

        occurrenceList.add(CONST_OCCURRENCE_YEARLY)
        occurrenceList.add(CONST_OCCURRENCE_MONTHLY)
        occurrenceList.add(CONST_OCCURRENCE_WEEKLY)
        occurrenceList.add(CONST_OCCURRENCE_DAILY)

        val adapterOccurrence = context?.let { ArrayAdapter(it, android.R.layout.simple_spinner_dropdown_item, occurrenceList) }
        spinnerFixedOccurance.adapter = adapterOccurrence

        var dayList:ArrayList<String> = ArrayList()
        var monList:ArrayList<String> = ArrayList()
        var weekDayList:ArrayList<String> = ArrayList()
        weekDayList.add("MON")
        weekDayList.add("TUE")
        weekDayList.add("WED")
        weekDayList.add("THU")
        weekDayList.add("FRI")
        weekDayList.add("SAT")
        weekDayList.add("SUN")
        dayList.add("DAY")
        for (index in 1..31){
            dayList.add(index.toString())
        }
        monList.add("MONTH")
        for (index in 1..12){
            monList.add(index.toString())
        }
        val adapterDayListAdapter = context?.let { ArrayAdapter(it, android.R.layout.simple_spinner_dropdown_item, dayList) }
        val adapterMonListAdapter = context?.let { ArrayAdapter(it, android.R.layout.simple_spinner_dropdown_item, monList) }
        val adapterWeekListAdapter = context?.let { ArrayAdapter(it, android.R.layout.simple_spinner_dropdown_item, weekDayList) }
        spinnerFixedDay.adapter = adapterDayListAdapter
        spinnerFixedMonth.adapter = adapterMonListAdapter
        var weeklyActive = false
        var occurrenceSelected = ""
        spinnerFixedOccurance.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,
                                        view: View, position: Int, id: Long) {
                occurrenceSelected = (occurrenceList?.get(position) ?: "") as String
                Log.i("SPINNER", "SELECTED:$occurrenceSelected")
                weeklyActive = false
                if(occurrenceSelected == CONST_OCCURRENCE_YEARLY)
                {
                    spinnerFixedDay.visibility = View.VISIBLE
                    spinnerFixedMonth.visibility = View.VISIBLE
                    spinnerFixedDay.adapter = adapterDayListAdapter
                }
                else if(occurrenceSelected == CONST_OCCURRENCE_MONTHLY)
                {
                    spinnerFixedDay.visibility = View.VISIBLE
                    spinnerFixedMonth.visibility = View.GONE
                    spinnerFixedDay.adapter = adapterDayListAdapter
                }
                else if(occurrenceSelected == CONST_OCCURRENCE_WEEKLY)
                {
                    spinnerFixedDay.visibility = View.VISIBLE
                    spinnerFixedMonth.visibility = View.GONE
                    spinnerFixedDay.adapter = adapterWeekListAdapter
                    weeklyActive = true
                }
                else
                {
                    spinnerFixedDay.visibility = View.GONE
                    spinnerFixedMonth.visibility = View.GONE
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }
        spinnerFixedDay.visibility = View.GONE
        spinnerFixedMonth.visibility = View.GONE
        //buttonAddFixedExpense.
        spinnerFixedDay.onItemSelectedListener

        var finalDayListSelected = ""
        var finalMonListSelected = ""
        spinnerFixedDay.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,
                                        view: View, position: Int, id: Long) {
                if(weeklyActive)
                {
                    finalDayListSelected = weekDayList[position]
                }
                else {
                    finalDayListSelected = dayList[position]
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }

        spinnerFixedMonth.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,
                                        view: View, position: Int, id: Long) {
                finalMonListSelected = monList[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }

        var db = context?.let { it1 -> DataBaseHandler(it1) }
        val categoryClassList = db?.readCategoryTable() as ArrayList
        val categoryList: ArrayList<String> = ArrayList()
        val subCategoryList: ArrayList<String> = ArrayList()

        for (i in 0 until categoryClassList.size) {
            categoryList.add(categoryClassList[i].category)
            subCategoryList.add(categoryClassList[i].sub_category)
        }


        val CategoryAdapter =
            context?.let { ArrayAdapter(it, android.R.layout.simple_spinner_dropdown_item, categoryList) }
        spinnerFixedCategory.adapter = CategoryAdapter

        var finalCategorySelected = ""
        var finalSubCategorySelected = ""
        var finalCategorySelectedPosition = 0

        spinnerFixedCategory.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,
                                        view: View, position: Int, id: Long) {
                finalCategorySelected = categoryList[position]
                finalCategorySelectedPosition = position
                val subCategoryAdapter =
                    context?.let { ArrayAdapter(it, android.R.layout.simple_spinner_dropdown_item, subCategoryList[position].split(",")) }
                spinnerFixedSubCategory.adapter = subCategoryAdapter
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }

        spinnerFixedSubCategory.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,
                                        view: View, position: Int, id: Long) {
                finalSubCategorySelected = subCategoryList[finalCategorySelectedPosition].split(",")[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }

        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm")
        var currentDateTime = sdf.format(Date())
        var todayDate = currentDateTime.split(" ")[0]
        editTextFixedDate.setText(todayDate)

        editTextFixedDate.setOnKeyListener { v, keyCode, event -> //You can identify which key pressed by checking keyCode value with KeyEvent.KEYCODE_
            Log.e("keyCode", "keyCode: ${keyCode} selc:${editTextFixedDate.selectionStart} -- ${editTextFixedDate.selectionEnd}")
            if (keyCode == KeyEvent.KEYCODE_DEL || keyCode == KeyEvent.KEYCODE_DPAD_LEFT ||
                keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                //this is for backspace
                editTextFixedDate.setSelection(editTextFixedDate.selectionStart)
            }else
            {
                if(editTextFixedDate.text.length == 2)
                {
                    editTextFixedDate.setText("${editTextFixedDate.text}/")
                    editTextFixedDate.setSelection(editTextFixedDate.text.length)
                }
                else if(editTextFixedDate.text.length == 5)
                {
                    editTextFixedDate.setText("${editTextFixedDate.text}/")
                    editTextFixedDate.setSelection(editTextFixedDate.text.length)
                }
            }
            false
        }
        spinnerFixedCategory.setSelection(categoryList.indexOf(SUSISHAA_CATEGORY_INVESTMENT))
        buttonDeleteFixedExpense.setOnClickListener {
            var ret = db.deleteFixedExpenseData(
                FixedExpenseClassModel(
                    fixedExpenseId,
                    editTextRupeeFixedNo.text.toString().toInt(),
                    occurrenceSelected,
                    "$finalDayListSelected:$finalMonListSelected",
                    finalCategorySelected,
                    finalSubCategorySelected,
                    editTextFixedDate.text.toString(),
                    editTextFixedComments.text.toString()
                )
            )
            if (ret != 0) {
                var toastMsg = "Fixed Expense Deleted"
                editTextRupeeFixedNo.text.clear()
                editTextFixedComments.text.clear()
                Toast.makeText(context, toastMsg, Toast.LENGTH_LONG).show()
                activity?.run {
                    supportFragmentManager.popBackStack()
                }
            }
        }
        buttonUpdateFixedExpense.setOnClickListener {
            var allGood = true
            try {
                val pattern: Pattern = Pattern.compile("[0-9][0-9]/[0-9][0-9]/[0-9][0-9][0-9][0-9]")
                val matcher: Matcher = pattern.matcher(editTextFixedDate.text.toString())
                val matchFound: Boolean = matcher.matches()
                Log.e("EXC", "matchFound:"+matchFound+" Error:" + editTextFixedDate.text.toString())
                if(!matchFound)
                {
                    editTextFixedDate.setError("DD/MM/YYYY")
                    allGood = false
                }
                else
                {
                    editTextFixedDate.error = null
                }
            }catch (ex: Exception) {
                editTextFixedDate.setError("DD/MM/YYYY")
                allGood = false
                Log.e("EXC", "Error:" + editTextFixedDate.text.toString())
            }
            if(editTextRupeeFixedNo.text.isEmpty()) {
                allGood = false
                editTextRupeeFixedNo.error = "Enter Money"
            }
            if(occurrenceSelected == CONST_OCCURRENCE_YEARLY)
            {
                if(finalDayListSelected == "DAY" || finalMonListSelected == "MONTH")
                {
                    allGood = false
                }
            }
            else if(occurrenceSelected == CONST_OCCURRENCE_MONTHLY)
            {
                if(finalDayListSelected == "DAY")
                {
                    allGood = false
                }
                finalMonListSelected = ""
            }
            else if(occurrenceSelected == CONST_OCCURRENCE_WEEKLY){

            }
            if(allGood) {
                var ret = db.updateFixedExpense(
                    FixedExpenseClassModel(
                        fixedExpenseId,
                        editTextRupeeFixedNo.text.toString().toInt(),
                        occurrenceSelected,
                        "$finalDayListSelected:$finalMonListSelected",
                        finalCategorySelected,
                        finalSubCategorySelected,
                        editTextFixedDate.text.toString(),
                        editTextFixedComments.text.toString()
                    )
                )
                if (!ret.equals(0)) {
                    var toastMsg = "Fixed Expense Updated for Rs.${editTextRupeeFixedNo.text}"

                    editTextRupeeFixedNo.text.clear()
                    editTextFixedComments.text.clear()
                    Toast.makeText(context, toastMsg, Toast.LENGTH_LONG).show()
                    activity?.run {
                        supportFragmentManager.popBackStack()
                    }
                }
            }//all good
            else
            {
                Log.e("UpdateFailed", "allGood $allGood, update failed")
            }

        }//addbuttonFixed income

        spinnerFixedOccurance.setSelection(occurrenceList.indexOf(model.occurrence))
        if(model.occurrence == "WEEKLY")
        {
            spinnerFixedDay.setSelection(weekDayList.indexOf(model.sub_occurrence?.split(":")[0]))
        }
        else
        {
            spinnerFixedDay.setSelection(dayList.indexOf(model.sub_occurrence?.split(":")[0]))
        }
        spinnerFixedMonth.setSelection(monList.indexOf(model.sub_occurrence?.split(":")[1]))
        spinnerFixedCategory.setSelection(categoryList.indexOf(model.category))
        spinnerFixedSubCategory.setSelection(categoryList.indexOf(model.sub_category))
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment UpdateFixedExpenseFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            UpdateFixedExpenseFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}