package co.iin.susiddhi.sushishaa_dhansaver.setting.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import co.iin.susiddhi.sushishaa_dhansaver.R
import co.iin.susiddhi.sushishaa_dhansaver.database.DataBaseHandler
import co.iin.susiddhi.sushishaa_dhansaver.database.ExpenseClassModel
import co.iin.susiddhi.sushishaa_dhansaver.setting.SettingsFragment
import java.io.InputStream
import kotlin.math.log

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ImportFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ImportFragment : Fragment() {
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
    lateinit var textViewImportData:TextView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view:View =  inflater.inflate(R.layout.fragment_import, container, false)
        setHasOptionsMenu(true)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(true)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Import"
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        textViewImportData = requireActivity().findViewById(R.id.textViewImportData)
        val openBackupFileIntent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "*/*"
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        resultLauncher.launch(openBackupFileIntent)
    }

    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        Log.i("DownloadFolder", "Ret: ${result.resultCode} --- ${result.data}")
        if(result.data != null)
        {
            try {
                Log.i("result?.data?.data", "${result?.data?.data} -- ${result?.data?.flags}")
                var input: InputStream? = result?.data?.data?.let {
                    context?.contentResolver?.openInputStream(
                        it
                    )
                }
                //Log.i("asdfasdf",  "${input?.readBytes()?.toString(Charsets.UTF_8)}")
                var tableRowListString = input?.readBytes()?.toString(Charsets.UTF_8)
                var tableRowList = tableRowListString?.split("\n")
                Log.i("ROW", "tableRowListString: ${tableRowListString}")
                Log.i("ROW", "tableRowList->>: ${tableRowList?.get(0)}")
                Log.i("ROW", "tableRowList Size: ${tableRowList?.size}")
                Log.i("COL", "row->${tableRowList?.get(0)}")
                var db = context?.let { it1 -> DataBaseHandler(it1) }
                if (tableRowList != null) {
                    for ((count, row) in tableRowList.withIndex()) {
                        Log.i("COL", "row->${row}")
                        var col = row.split("~")
                        //for(index in 0..col.size-1)
                        //    Log.i("COL","col Size:$index-> ${col[index]}")
                        if (col.size > 10) {
                            Log.i(
                                "COL", "${col[0]}, ${col[1]}, ${col[2]}, ${col[3]}, ${col[4]}\n" +
                                        "${col[5]}, ${col[6]}, ${col[7]}, ${col[8]}, ${col[9]}, ${col[10]}"
                            )
                            var ret = db?.insertImportedExpenseData(
                                ExpenseClassModel(
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
                            )
                            Log.i("UPDATE DB:", "ret:${ret}: Count: ${count}")
                        }
                    }
                }
                textViewImportData.setText("Total: ${tableRowList?.size} rows populated in DB")
            }catch (e:Exception)
            {
                activity?.run {
                    supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerViewSetting, SettingsFragment())
                        .addToBackStack(SettingsFragment.toString())
                        .commit()
                }
            }
        }
        else
        {
            activity?.run {
                supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerViewSetting, SettingsFragment())
                    .addToBackStack(SettingsFragment.toString())
                    .commit()
            }
        }
    }//activityResult

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
         * @return A new instance of fragment ImportFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ImportFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}