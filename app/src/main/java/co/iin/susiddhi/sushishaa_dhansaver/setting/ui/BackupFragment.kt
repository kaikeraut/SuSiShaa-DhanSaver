package co.iin.susiddhi.susishaa_dhansaver.setting.ui

import android.app.Activity
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.View.*
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import co.iin.susiddhi.susishaa_dhansaver.R
import co.iin.susiddhi.susishaa_dhansaver.database.DataBaseHandler
import co.iin.susiddhi.susishaa_dhansaver.setting.SettingsFragment
import java.io.File
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BackupFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BackupFragment : Fragment() {
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
    lateinit var imageViewMailDone:ImageView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view:View =  inflater.inflate(R.layout.fragment_backup, container, false)
        setHasOptionsMenu(true)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(true)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Backup"
        val sdf = SimpleDateFormat("dd/MM/yyyy-hh:mm")
        var currentDateTime = sdf.format(Date())
        var filename = "Backup#"+getText(R.string.app_name)+"#"+currentDateTime
        var db = context?.let { it1 -> DataBaseHandler(it1) }
        if (db != null) {
            var fileUri = db.readAllExpenseDataAndSaveToFile(filename)
            Log.e("FileUri", "fileUri: ${fileUri} -- ${context?.packageName} -- ${context?.filesDir}")
            var fileFd = File(context?.filesDir, fileUri)
            Log.e("fileFd","fileFd:${fileFd} : ${fileFd.length()}")
                val uri = context?.let {
                FileProvider.getUriForFile(
                    it,
                    context?.packageName + ".provider",
                    fileFd
                )
            }
            Log.e("FileUri", "NEW fileUri: ${uri}")
            composeEmail(currentDateTime, filename, uri)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imageViewMailDone = requireView()?.findViewById(R.id.imageViewMailDone)
        imageViewMailDone.visibility = INVISIBLE

    }
    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        Log.e("MailResult", "Ret: ${result.resultCode}")
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data: Intent? = result.data
            //Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
        }
        else {
            //Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
        }
        imageViewMailDone.visibility = VISIBLE
    }

    fun composeEmail(datetime: String, subject: String, attachment: Uri?) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            //data = Uri.parse("mailto:") // Only email apps handle this.
            //putExtra(Intent.EXTRA_EMAIL, addresses)
            setDataAndType(attachment, attachment?.let { context?.contentResolver?.getType(it) })
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            putExtra(Intent.EXTRA_STREAM, attachment);
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, "Database Backup file Created on Date:${datetime} \n\nRegards,\nSuSiShaa Team")
        }
        //startActivity(intent)
        resultLauncher.launch(intent)

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
         * @return A new instance of fragment BackupFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BackupFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}