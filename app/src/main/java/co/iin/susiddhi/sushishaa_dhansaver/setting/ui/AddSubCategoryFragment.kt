package co.iin.susiddhi.susishaa_dhansaver.setting.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import co.iin.susiddhi.susishaa_dhansaver.R
import co.iin.susiddhi.susishaa_dhansaver.database.DataBaseHandler
import co.iin.susiddhi.susishaa_dhansaver.setting.SettingsFragment
import java.util.*
import kotlin.collections.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddSubCategoryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddSubCategoryFragment : Fragment() {
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
        var view:View =  inflater.inflate(R.layout.fragment_add_sub_category, container, false)
        setHasOptionsMenu(true)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(true)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Add Sub Category"
        return view
    }

    lateinit var textViewExistingSubCategory: TextView
    var subcategoryfullstring = ""
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var addButton: Button = requireView()?.findViewById(R.id.buttonAddCategory)
        var editText: EditText = requireView()?.findViewById(R.id.editTextAddSubCategory)
        var spinner: Spinner = requireView()?.findViewById(R.id.spinnerCategoryList)
        textViewExistingSubCategory = requireView()?.findViewById(R.id.textViewExistingSubCategory)

        var db = context?.let { it1 -> DataBaseHandler(it1) }

        val categoryClassList = db?.readCategoryTable() as ArrayList
        val list:ArrayList<String> = ArrayList()

        for (i in 0 until categoryClassList.size) {
            list.add(categoryClassList[i].category)
        }

        val adapter =
            context?.let { ArrayAdapter(it, android.R.layout.simple_spinner_dropdown_item, list) }
        spinner.adapter = adapter

        var categorySelected = ""
        var categorySeltectedPostion = 0
        spinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,
                                        view: View, position: Int, id: Long) {
                categorySelected = (list?.get(position) ?: "") as String
                Log.i("SPINNER", "SELECTED:$categorySelected")
                subcategoryfullstring = categoryClassList[position].sub_category
                textViewExistingSubCategory.setText(subcategoryfullstring)
                categorySeltectedPostion = position
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }

        addButton.setOnClickListener{
            Log.i("TAG", "Enter SUB Category: "+editText.text)
            if(editText.text.isEmpty())
            {
                editText.error = "Enter Sub Category !!"
            }
            else {
                var finalString = "";
                if(subcategoryfullstring.isEmpty())
                {
                    finalString = editText.text.toString().uppercase(Locale.getDefault())
                }
                else
                {
                    finalString = "$subcategoryfullstring," +editText.text.toString().uppercase(
                        Locale.getDefault())
                }
                var ret = db?.updateNewSubCategory(finalString, categorySelected)
                if (ret != null) {
                    if(ret != 0) {
                        editText.setText("")
                        updateSubCategoryTextView(categorySeltectedPostion)
                    }
                }
            }
        }
    }

    private fun updateSubCategoryTextView(pos:Int)
    {
        var db = context?.let { it1 -> DataBaseHandler(it1) }
        val categoryClassList = db?.readCategoryTable() as ArrayList
        subcategoryfullstring = categoryClassList[pos].sub_category
        textViewExistingSubCategory.text = categoryClassList[pos].sub_category
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // handle arrow click here
        if (item.getItemId() === android.R.id.home) {
            activity?.run {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerViewSetting, SettingsFragment())
                    //.addToBackStack(SettingsFragment.toString())
                    .commit()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AddSubCategoryFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddSubCategoryFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}