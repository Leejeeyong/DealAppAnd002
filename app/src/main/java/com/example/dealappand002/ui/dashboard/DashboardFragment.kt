package com.example.dealappand002.ui.dashboard

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.dealappand002.MainPage
import com.example.dealappand002.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.fragment_dashboard.view.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel
    private val TAG = "DashBoardFrament"

    lateinit var storageReference: StorageReference
    companion object{
        private val PICK_IMAGE_CODE = 1000
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
            ViewModelProviders.of(this).get(DashboardViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)
        dashboardViewModel.text.observe(viewLifecycleOwner, Observer {

        })

        //gs://dealapp-7e8c9.appspot.com/
        // Access a Cloud Firestore instance from your Activity
        val db = FirebaseFirestore.getInstance()
        // Get a non-default Storage bucket

        var current = LocalDateTime.now()
        var formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
        var formatted = current.format(formatter)

        root.imageButton2.isEnabled = false
        root.imageButton3.isEnabled = false
        root.imageButton4.isEnabled = false
        root.imageButton5.isEnabled = false

        root.imageButton2.setImageResource(R.color.common_google_signin_btn_text_light)
        root.imageButton3.setImageResource(R.color.common_google_signin_btn_text_light)
        root.imageButton4.setImageResource(R.color.common_google_signin_btn_text_light)
        root.imageButton5.setImageResource(R.color.common_google_signin_btn_text_light)

        //root.imageButton2.setBackgroundResource(R.color.common_google_signin_btn_text_light)
        //root.imageButton3.setBackgroundResource(R.color.common_google_signin_btn_text_light)
        //root.imageButton4.setBackgroundResource(R.color.common_google_signin_btn_text_light)
        //root.imageButton5.setBackgroundResource(R.color.common_google_signin_btn_text_light)



        root.imageButton1.setOnClickListener {
            current = LocalDateTime.now()
            formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
            formatted = current.format(formatter)

            storageReference = FirebaseStorage.getInstance().getReference(formatted + "/1")

            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT

            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_CODE+1)
            //root.imageButton2.isEnabled = true
        }
        root.imageButton2.setOnClickListener {

            storageReference = FirebaseStorage.getInstance().getReference(formatted + "/2")

            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT

            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_CODE+2)
            //root.imageButton3.isEnabled = true
        }
        root.imageButton3.setOnClickListener {

            storageReference = FirebaseStorage.getInstance().getReference(formatted + "/3")

            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT

            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_CODE+3)
            //root.imageButton4.isEnabled = true
        }
        root.imageButton4.setOnClickListener {

            storageReference = FirebaseStorage.getInstance().getReference(formatted + "/4")

            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT

            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_CODE+4)
            //root.imageButton5.isEnabled = true
        }
        root.imageButton5.setOnClickListener {

            storageReference = FirebaseStorage.getInstance().getReference(formatted + "/5")

            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT

            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_CODE+5)
        }

        root.registDeal_button.setOnClickListener{

            val tabledata = hashMapOf(
                "Writer" to "Alan",
                "Title" to EditTitleUpload.text.toString(),
                "Price" to EditPriceUpload.text.toString().toInt(),
                "Date" to formatted,
                "State" to 1,
                "ImageInf" to "aaaa"
            )

            // Add a new document with a generated ID
            db.collection("TableData")
                .add(tabledata)
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                    Toast.makeText(this.requireContext(), "게시글 등록 성공", Toast.LENGTH_SHORT).show()
                    var intent = Intent(this.requireContext(),MainPage::class.java)
                    startActivity(intent)
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                    Toast.makeText(this.requireContext(), "게시글 등록 실패", Toast.LENGTH_SHORT).show()
                }
        }



        return root
    }


    fun imageupload(){

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == PICK_IMAGE_CODE+1){
            val uploadTask = storageReference!!.putFile(data!!.data!!)
            val task = uploadTask.continueWithTask { task ->

                if(!task.isSuccessful){
                    Toast.makeText(this.requireContext(), "이미지 업로드 실패", Toast.LENGTH_SHORT).show()
                }
                storageReference!!.downloadUrl
            }.addOnCompleteListener {
                task ->
                if(task.isSuccessful){
                    val downloaUri = task.result
                    val url = downloaUri!!.toString()
                    val bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, data!!.data!!)
                    imageButton1.setImageBitmap(bitmap)
                    imageButton2.isEnabled = true
                    imageButton2.setImageResource(R.drawable.ic_launcher_foreground)
                    Log.d(TAG, "DirectLink" + url)
                }
            }
        }
        else if(requestCode == PICK_IMAGE_CODE+2){
            val uploadTask = storageReference!!.putFile(data!!.data!!)
            val task = uploadTask.continueWithTask { task ->

                if(!task.isSuccessful){
                    Toast.makeText(this.requireContext(), "이미지 업로드 실패", Toast.LENGTH_SHORT).show()
                }
                storageReference!!.downloadUrl
            }.addOnCompleteListener {
                    task ->
                if(task.isSuccessful){
                    val downloaUri = task.result
                    val url = downloaUri!!.toString()
                    val bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, data!!.data!!)
                    imageButton2.setImageBitmap(bitmap)
                    imageButton3.isEnabled = true
                    imageButton3.setImageResource(R.drawable.ic_launcher_foreground)
                    Log.d(TAG, "DirectLink" + url)
                }
            }
        }
        else if(requestCode == PICK_IMAGE_CODE+3){
            val uploadTask = storageReference!!.putFile(data!!.data!!)
            val task = uploadTask.continueWithTask { task ->

                if(!task.isSuccessful){
                    Toast.makeText(this.requireContext(), "이미지 업로드 실패", Toast.LENGTH_SHORT).show()
                }
                storageReference!!.downloadUrl
            }.addOnCompleteListener {
                    task ->
                if(task.isSuccessful){
                    val downloaUri = task.result
                    val url = downloaUri!!.toString()
                    val bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, data!!.data!!)
                    imageButton3.setImageBitmap(bitmap)
                    imageButton4.isEnabled = true
                    imageButton4.setImageResource(R.drawable.ic_launcher_foreground)
                    Log.d(TAG, "DirectLink" + url)
                }
            }
        }
        else if(requestCode == PICK_IMAGE_CODE+4){
            val uploadTask = storageReference!!.putFile(data!!.data!!)
            val task = uploadTask.continueWithTask { task ->

                if(!task.isSuccessful){
                    Toast.makeText(this.requireContext(), "이미지 업로드 실패", Toast.LENGTH_SHORT).show()
                }
                storageReference!!.downloadUrl
            }.addOnCompleteListener {
                    task ->
                if(task.isSuccessful){
                    val downloaUri = task.result
                    val url = downloaUri!!.toString()
                    val bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, data!!.data!!)
                    imageButton4.setImageBitmap(bitmap)
                    imageButton5.isEnabled = true
                    imageButton5.setImageResource(R.drawable.ic_launcher_foreground)
                    Log.d(TAG, "DirectLink" + url)
                }
            }
        }
        else if(requestCode == PICK_IMAGE_CODE+5){
            val uploadTask = storageReference!!.putFile(data!!.data!!)
            val task = uploadTask.continueWithTask { task ->

                if(!task.isSuccessful){
                    Toast.makeText(this.requireContext(), "이미지 업로드 실패", Toast.LENGTH_SHORT).show()
                }
                storageReference!!.downloadUrl
            }.addOnCompleteListener {
                    task ->
                if(task.isSuccessful){
                    val downloaUri = task.result
                    val url = downloaUri!!.toString()
                    val bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, data!!.data!!)
                    imageButton5.setImageBitmap(bitmap)
                    Log.d(TAG, "DirectLink" + url)
                }
            }
        }
    }

    fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(EditPriceUpload.windowToken,  0)
        imm.hideSoftInputFromWindow(EditTitleUpload.windowToken,  0)
    }
}