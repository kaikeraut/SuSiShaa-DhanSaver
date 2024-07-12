package co.iin.susiddhi.susishaa_dhansaver.setting

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import co.iin.susiddhi.sushishaa_dhansaver.setting.ui.AddFixedExpenseFragment
import co.iin.susiddhi.sushishaa_dhansaver.setting.ui.ViewFixedExpenseFragment
import co.iin.susiddhi.susishaa_dhansaver.MainActivity
import co.iin.susiddhi.susishaa_dhansaver.R
import co.iin.susiddhi.susishaa_dhansaver.setting.ui.AddCategoryFragment
import co.iin.susiddhi.susishaa_dhansaver.setting.ui.AboutUsFragment
import co.iin.susiddhi.susishaa_dhansaver.setting.ui.AddSubCategoryFragment
import co.iin.susiddhi.susishaa_dhansaver.setting.ui.BackupFragment
import co.iin.susiddhi.susishaa_dhansaver.setting.ui.ExportFragment
import co.iin.susiddhi.susishaa_dhansaver.setting.ui.ImportFragment

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SettingsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SettingsFragment : Fragment() {
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
        var view:View =  inflater.inflate(R.layout.fragment_settings, container, false)
        setHasOptionsMenu(true)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(true)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Setting"
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var textViewSummaryReport: TextView = requireView()?.findViewById(R.id.textViewSummaryReport)
        var textViewCategory: TextView = requireView()?.findViewById(R.id.textViewNewCategory)
        var textViewSubCategory: TextView = requireView()?.findViewById(R.id.textViewSubCategory)
        //var textViewExport: TextView = requireView()?.findViewById(R.id.textViewExport)
        var textViewImport: TextView = requireView()?.findViewById(R.id.textViewImport)
        var textViewBackup: TextView = requireView()?.findViewById(R.id.textViewBackupMail)
        var textViewAddFixedExpense: TextView = requireView()?.findViewById(R.id.textViewAddFixedExpense)
        var textViewViewFixedExpense: TextView = requireView()?.findViewById(R.id.textViewViewFixedExpense)
        var textViewAboutUs: TextView = requireView()?.findViewById(R.id.textViewAboutUs)


        textViewSummaryReport.setOnClickListener { replaceFragment(SummaryReportFragment()) }
        textViewCategory.setOnClickListener { replaceFragment(AddCategoryFragment()) }
        textViewSubCategory.setOnClickListener { replaceFragment(AddSubCategoryFragment()) }
        //textViewExport.setOnClickListener { replaceFragment(ExportFragment()) }
        textViewImport.setOnClickListener { replaceFragment(ImportFragment()) }
        textViewBackup.setOnClickListener { replaceFragment(BackupFragment()) }
        textViewAboutUs.setOnClickListener { replaceFragment(AboutUsFragment()) }
        textViewAddFixedExpense.setOnClickListener{ replaceFragment(AddFixedExpenseFragment()) }
        textViewViewFixedExpense.setOnClickListener{ replaceFragment(ViewFixedExpenseFragment()) }
    }

    fun replaceFragment(fragment: Fragment)
    {
        activity?.run {
            supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerViewSetting, fragment)
                .addToBackStack(fragment.toString())
                .commit()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // handle arrow click here
        if (item.getItemId() === android.R.id.home) {
            startActivity(Intent(activity, MainActivity::class.java))
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
         * @return A new instance of fragment SettingsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SettingsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}