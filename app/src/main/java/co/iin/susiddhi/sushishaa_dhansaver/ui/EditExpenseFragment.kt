package co.iin.susiddhi.susishaa_dhansaver.ui

import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import co.iin.susiddhi.susishaa_dhansaver.R
import co.iin.susiddhi.susishaa_dhansaver.database.DataBaseHandler
import co.iin.susiddhi.susishaa_dhansaver.database.EXPENSE_ESSENTIALS
import co.iin.susiddhi.susishaa_dhansaver.database.EXPENSE_NON_ESSENTIALS
import co.iin.susiddhi.susishaa_dhansaver.database.ExpenseClassModel
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern
import kotlin.collections.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [EditExpenseFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditExpenseFragment : Fragment() {
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
        var view:View =  inflater.inflate(R.layout.fragment_edit_expense, container, false)
        setHasOptionsMenu(true)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(true)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Edit/Delete Expense"
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var editTextExpenseDay: EditText = requireView()?.findViewById(R.id.editTextExpenseDay)
        var editTextExpenseTime: EditText = requireView()?.findViewById(R.id.editTextExpenseTime)
        var editTextExpenseRupee: EditText = requireView()?.findViewById(R.id.editTextExpenseRupee)
        var editTextExpenseReason: EditText =
            requireView()?.findViewById(R.id.editTextExpenseReason)
        var updateButton: Button = requireView()?.findViewById(R.id.buttonExpenseEdit)
        var deleteButton: Button = requireView()?.findViewById(R.id.buttonExpenseDelete)

        var spinnerCategory: Spinner = requireView()?.findViewById(R.id.spinnerCategoryList)
        var spinnerSubCategory: Spinner = requireView()?.findViewById(R.id.spinnerSubCategory)
        var spinnerMode: Spinner = requireView()?.findViewById(R.id.spinnerMode)

        var essentialChkButton: CheckBox = requireView()?.findViewById(R.id.checkBoxEssentials)
        var nonEssentialChkButton: CheckBox =
            requireView()?.findViewById(R.id.checkBoxNonEssentials)

        val bundle = this.arguments
        var recvdData = bundle?.getString("AddExpenseEditDeleteRequest").toString()
        Log.i("RECVDDATA", "recvdData: ${recvdData}")
        var col = recvdData.split("~")
        var model = ExpenseClassModel(
            col[0].toInt(),
            col[1],
            col[2].toInt(),
            col[3],
            col[4],
            col[5],
            col[6],
            col[7],
            col[8].toInt(),
            col[9].toInt(),
            col[10].toInt()
        )
        Log.i("Date:", "Date: ${col[1]} --- ${model.date}")
        editTextExpenseDay.setText(model.date.split(" ")[0]);
        editTextExpenseTime.setText(model.date.split(" ")[1])
        editTextExpenseRupee.setText(model.rupee.toString())
        editTextExpenseReason.setText(model.purpose)
        if (model.essential == EXPENSE_ESSENTIALS) {
            essentialChkButton.isChecked = true
            essentialChkButton.isEnabled = true
            nonEssentialChkButton.isEnabled = false
        } else {
            nonEssentialChkButton.isChecked = true
            nonEssentialChkButton.isEnabled = true
            essentialChkButton.isEnabled = false
        }
        essentialChkButton.setOnClickListener {
            if (essentialChkButton.isChecked) {
                nonEssentialChkButton.isEnabled = false
            } else {
                nonEssentialChkButton.isEnabled = true
                essentialChkButton.isEnabled = true
            }
        }
        nonEssentialChkButton.setOnClickListener {
            if (nonEssentialChkButton.isChecked) {
                essentialChkButton.isEnabled = false
            } else {
                nonEssentialChkButton.isEnabled = true
                essentialChkButton.isEnabled = true
            }
        }
        var db = context?.let { it1 -> DataBaseHandler(it1) }
        val categoryClassList = db?.readCategoryTable() as ArrayList
        val CategoryList: ArrayList<String> = ArrayList()
        val SubCategoryList: ArrayList<String> = ArrayList()
        val ModeList: List<String> = db?.getModeList()

        for (i in 0 until categoryClassList.size) {
            CategoryList.add(categoryClassList[i].category)
            SubCategoryList.add(categoryClassList[i].sub_category)
        }


        val CategoryAdapter =
            context?.let {
                ArrayAdapter(
                    it,
                    android.R.layout.simple_spinner_dropdown_item,
                    CategoryList
                )
            }
        spinnerCategory.adapter = CategoryAdapter

        val ModeAdapter =
            context?.let {
                ArrayAdapter(
                    it,
                    android.R.layout.simple_spinner_dropdown_item,
                    ModeList
                )
            }
        spinnerMode.adapter = ModeAdapter

        var finalCategorySelected = ""
        var finalSubCategorySelected = ""
        var finalModeSelected = ""
        var finalCategorySelectedPosition = 0

        spinnerCategory.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {
                finalCategorySelected = CategoryList[position]
                finalCategorySelectedPosition = position
                val subCategoryAdapter =
                    context?.let {
                        ArrayAdapter(
                            it,
                            android.R.layout.simple_spinner_dropdown_item,
                            SubCategoryList[position].split(",")
                        )
                    }
                spinnerSubCategory.adapter = subCategoryAdapter
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }

        spinnerSubCategory.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {
                finalSubCategorySelected =
                    SubCategoryList[finalCategorySelectedPosition].split(",")[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }

        spinnerMode.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {
                finalModeSelected = ModeList[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }

        spinnerCategory.setSelection(CategoryList.indexOf(model.category))
        spinnerSubCategory.setSelection(SubCategoryList.indexOf(model.sub_category))
        spinnerMode.setSelection(ModeList.indexOf(model.mode))


        val sdf = SimpleDateFormat("dd/MM/yyyy hh:mm")
        var currentDateTime = sdf.format(Date())
        var todayDate = currentDateTime.split(" ")[0]
        //editTextExpenseDay.setText(todayDate)
        //editTextExpenseTime.setText(currentDateTime.split(" ")[1])

        editTextExpenseDay.setOnKeyListener { v, keyCode, event -> //You can identify which key pressed by checking keyCode value with KeyEvent.KEYCODE_
            Log.e(
                "keyCode",
                "keyCode: ${keyCode} selc:${editTextExpenseDay.selectionStart} -- ${editTextExpenseDay.selectionEnd}"
            )
            if (keyCode == KeyEvent.KEYCODE_DEL || keyCode == KeyEvent.KEYCODE_DPAD_LEFT ||
                keyCode == KeyEvent.KEYCODE_DPAD_RIGHT
            ) {
                //this is for backspace
                editTextExpenseDay.setSelection(editTextExpenseDay.selectionStart)
            } else {
                if (editTextExpenseDay.text.length == 2) {
                    editTextExpenseDay.setText("${editTextExpenseDay.text}/")
                    editTextExpenseDay.setSelection(editTextExpenseDay.text.length)
                } else if (editTextExpenseDay.text.length == 5) {
                    editTextExpenseDay.setText("${editTextExpenseDay.text}/")
                    editTextExpenseDay.setSelection(editTextExpenseDay.text.length)
                }
            }
            false
        }
        editTextExpenseTime.setOnKeyListener { v, keyCode, event -> //You can identify which key pressed by checking keyCode value with KeyEvent.KEYCODE_
            if (keyCode == KeyEvent.KEYCODE_DEL) {
                //this is for backspace
            } else {
                if (editTextExpenseTime.text.length == 2) {
                    editTextExpenseTime.setText("${editTextExpenseTime.text}:")
                    editTextExpenseDay.setSelection(editTextExpenseDay.text.length)
                }
            }
            false
        }
        deleteButton.setOnClickListener {
            var success = db.deleteExpenseData(
                ExpenseClassModel(
                    model.id,
                    model.date,
                    editTextExpenseRupee.text.toString().toInt(),
                    finalModeSelected.toString(),
                    finalCategorySelected,
                    finalSubCategorySelected,
                    editTextExpenseReason.text.toString(),
                    "Vineet",
                    model.month,
                    model.year,
                    model.essential
                )
            )
            if (success != 0) {
                Toast.makeText(context, "Expense Deleted: SUCCESS", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Expense Deleted: FAILED", Toast.LENGTH_SHORT).show()
            }
            activity?.run {
                supportFragmentManager.popBackStack()
            }
        }
        updateButton.setOnClickListener {
            var allGood = true
            try {
                val pattern: Pattern = Pattern.compile("[0-9][0-9]/[0-9][0-9]/[0-9][0-9][0-9][0-9]")
                val matcher: Matcher = pattern.matcher(editTextExpenseDay.text.toString())
                val matchFound: Boolean = matcher.matches()
                Log.e(
                    "EXC",
                    "matchFound:" + matchFound + " Error:" + editTextExpenseDay.text.toString()
                )
                if (!matchFound) {
                    editTextExpenseDay.setError("DD/MM/YYYY")
                    allGood = false
                } else {
                    editTextExpenseDay.error = null
                }
            } catch (ex: Exception) {
                editTextExpenseDay.setError("DD/MM/YYYY")
                allGood = false
                Log.e("EXC", "Error:" + editTextExpenseDay.text.toString())
            }
            try {
                val pattern: Pattern = Pattern.compile("[0-9][0-9]:[0-9][0-9]")
                val matcher: Matcher = pattern.matcher(editTextExpenseTime.text.toString())
                val matchFound: Boolean = matcher.matches()
                if (!matchFound) {
                    editTextExpenseTime.setError("MM:HH")
                    allGood = false
                } else {
                    editTextExpenseTime.error = null
                }
            } catch (ex: Exception) {
                editTextExpenseTime.setError("DD/MM/YYYY")
                allGood = false
            }
            if (editTextExpenseRupee.length() == 0) {
                allGood = true
                editTextExpenseRupee.setError("Enter Money")
            }
            if (editTextExpenseReason.length() == 0) {
                allGood = false
                editTextExpenseReason.setError("Enter Reason")
            }
            var checkedBox = 0
            if (essentialChkButton.isChecked) {
                checkedBox = EXPENSE_ESSENTIALS
                essentialChkButton.setError(null)
                nonEssentialChkButton.setError(null)
            } else if (nonEssentialChkButton.isChecked) {
                checkedBox = EXPENSE_NON_ESSENTIALS
                essentialChkButton.setError(null)
                nonEssentialChkButton.setError(null)
            }
            if (checkedBox == 0) {
                allGood = false
                essentialChkButton.setError("")
                nonEssentialChkButton.setError("")
            }

            if (allGood) {
                val dateTime =
                    "${editTextExpenseDay.text.toString()} ${editTextExpenseTime.text.toString()}"

                var dateList = editTextExpenseDay.text.toString().split("/")
                var month = dateList[1].toString().toInt()
                var year = dateList[2].toString().toInt()

                Log.e(
                    "EXPENSE ENTERED", "Date:" + dateTime + " Rupee:" +
                            editTextExpenseRupee.text.toString()
                                .toInt() + " Mode:" + finalModeSelected + " Category:" +
                            finalCategorySelected + " SubCategory:" + finalSubCategorySelected + " Reason" + editTextExpenseReason.text.toString() +
                            " Month:" + month + " Year:" + year + "Checked:$checkedBox"
                )
                var ret = db?.updateExpenseData(
                    ExpenseClassModel(
                        model.id,
                        dateTime,
                        editTextExpenseRupee.text.toString().toInt(),
                        finalModeSelected.toString(),
                        finalCategorySelected,
                        finalSubCategorySelected,
                        editTextExpenseReason.text.toString(),
                        "Vineet",
                        month,
                        year,
                        checkedBox
                    )
                )

                if (!ret.equals(0)) {
                    val currentDateTime = sdf.format(Date())
                    var todayDate = currentDateTime.split(" ")[0]
                    editTextExpenseDay.setText(todayDate)
                    editTextExpenseTime.setText(currentDateTime.split(" ")[1])
                    editTextExpenseRupee.text.clear()
                    editTextExpenseReason.text.clear()
                    essentialChkButton.isChecked = false
                    nonEssentialChkButton.isChecked = false
                    essentialChkButton.isEnabled = true
                    nonEssentialChkButton.isEnabled = true
                    Toast.makeText(context, "Expense Updated", Toast.LENGTH_SHORT).show()
                    activity?.run {
                        supportFragmentManager.popBackStack()
                    }
                }
            }
        }
    }//onViewCreated
/*
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // handle arrow click here
        if (item.itemId == android.R.id.home) {
            activity?.run {
                supportFragmentManager.beginTransaction().replace(R.id.main_fragment_section_mainactivity, ExpenseChartFragment())
                    //.addToBackStack(ExpenseChartFragment().toString())
                    .commit()
            }
        }
        return super.onOptionsItemSelected(item)
    }
*/

}