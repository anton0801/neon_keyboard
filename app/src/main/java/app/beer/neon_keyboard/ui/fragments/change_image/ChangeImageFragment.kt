package app.beer.neon_keyboard.ui.fragments.change_image

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.os.AsyncTask
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import app.beer.neon_keyboard.R
import app.beer.neon_keyboard.utils.APP_ACTIVITY
import app.beer.neon_keyboard.utils.showToast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions.bitmapTransform
import jp.wasabeef.glide.transformations.BlurTransformation
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception

const val GET_IMAGE_REQUEST_CODE = 123

class ChangeImageFragment : Fragment() {

    private lateinit var changeImageToolbar: Toolbar

    private lateinit var chooseVariantGroup: RadioGroup
    private lateinit var imageCustomisationLabel: TextView
    private lateinit var imagePreview: ImageView

    private lateinit var blurChooseContainer: RadioGroup
    private lateinit var defaultBlurBtn: RadioButton
    private lateinit var acceptBtn: TextView

    private var imageBitmap: Bitmap? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_change_image, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(view) {
            changeImageToolbar = findViewById(R.id.change_image_toolbar)

            chooseVariantGroup = findViewById(R.id.choose_variant_group)

            imageCustomisationLabel = findViewById(R.id.image_customisation_label)
            imagePreview = findViewById(R.id.image_preview)

            blurChooseContainer = findViewById(R.id.blur_choose_container)
            defaultBlurBtn = findViewById(R.id.default_blur_btn)
            acceptBtn = findViewById(R.id.accept_btn)
        }

        chooseVariantGroup.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.change_image) {
                val intent =
                    Intent(Intent.ACTION_GET_CONTENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(intent, GET_IMAGE_REQUEST_CODE)
            }
        }

        blurChooseContainer.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.default_blur_btn -> blur(1)
                R.id.medium_blur_btn -> blur(20)
                R.id.average_blur_btn -> blur(40)
            }
        }

        acceptBtn.setOnClickListener {
            if (imageBitmap != null) {
                SaveImage(imageBitmap!!).execute()
            } else {
                showToast("You have choose an image!")
            }
        }
    }

    override fun onStart() {
        super.onStart()
        APP_ACTIVITY.setSupportActionBar(changeImageToolbar)
        APP_ACTIVITY.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        changeImageToolbar.setNavigationOnClickListener { APP_ACTIVITY.supportFragmentManager.popBackStack() }
    }

    override fun onPause() {
        super.onPause()
        APP_ACTIVITY.setSupportActionBar(null)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GET_IMAGE_REQUEST_CODE) {
            val imageUri = data?.data
            imageBitmap = MediaStore.Images.Media.getBitmap(APP_ACTIVITY.contentResolver, imageUri)
            if (imageBitmap != null) {
                imageCustomisationLabel.visibility = View.GONE
                defaultBlurBtn.isChecked = true
                imagePreview.setImageBitmap(imageBitmap)
            } else {
                imageCustomisationLabel.visibility = View.VISIBLE
                imagePreview.setImageBitmap(null)
                defaultBlurBtn.isChecked = false
            }
        }
    }

    private fun blur(blur: Int) {
        if (imageBitmap != null) {
            Glide.with(APP_ACTIVITY).load(imageBitmap)
                .apply(bitmapTransform(BlurTransformation(blur)))
                .into(imagePreview)
        }
    }

    class SaveImage(val bitmap: Bitmap) : AsyncTask<Void, Void, Boolean>() {

        private val pd: ProgressDialog = ProgressDialog(APP_ACTIVITY).apply {
            setMessage("Saving...")
            setCancelable(false)
        }

        override fun onPreExecute() {
            super.onPreExecute()
            pd.show()
        }

        override fun doInBackground(vararg params: Void?): Boolean {
            return try {
                val file = File(APP_ACTIVITY.filesDir, "keyboard_img.png")
                if (file.exists())
                    file.createNewFile()
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, FileOutputStream(file))
                true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }

        override fun onPostExecute(result: Boolean?) {
            super.onPostExecute(result)
            if (result != null) {
                showToast(if (result) "Image saved success" else "Some went wrong")
            }
            pd.dismiss()
        }

    }

}