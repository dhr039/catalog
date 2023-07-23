package com.cannonades.petconnect.common.presentation.ui

import android.app.Application
import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.ImageLoader
import coil.request.ImageRequest
import com.cannonades.petconnect.common.domain.model.Animal
import com.cannonades.petconnect.common.domain.usecases.GetAnimalUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.io.OutputStream
import javax.inject.Inject

@HiltViewModel
class AnimalViewModel @Inject constructor(
    private val getAnimalUseCase: GetAnimalUseCase,
    private val app: Application,
    private val imageLoader: ImageLoader
) : ViewModel() {

    private val _animal = MutableStateFlow<Animal?>(null)
    val animal: StateFlow<Animal?> = _animal

    fun loadAnimal(id: String) {
        viewModelScope.launch {
            val animal = getAnimalUseCase(id)
            _animal.emit(animal)
        }
    }

    private val _doneSavingImage = MutableSharedFlow<Uri>() // Emits URIs
    val doneSavingImage: SharedFlow<Uri> = _doneSavingImage

    fun saveImageFromUrl(url: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val request = ImageRequest.Builder(app)
                .data(url)
                .build()

            val result = (imageLoader.execute(request).drawable as? BitmapDrawable)?.bitmap

            // Pass the bitmap to your saving method
            result?.let { bitmap ->
                addImage(bitmap)
            }
        }
    }

    fun addImage(bitmap: Bitmap) {
        viewModelScope.launch {
            performAddImage(bitmap)
        }
    }

    private suspend fun performAddImage(bitmap: Bitmap) {
        withContext(Dispatchers.IO) {

            val name = System.currentTimeMillis().toString()
            val relativeLocation = "${Environment.DIRECTORY_PICTURES}/AppName"
            val contentValues = ContentValues().apply {
                put(MediaStore.Images.ImageColumns.DISPLAY_NAME, name)
                put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                put(MediaStore.Images.ImageColumns.RELATIVE_PATH, relativeLocation)
            }

            val contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            var stream: OutputStream? = null
            var uri: Uri? = null

            try {

                uri = app.contentResolver.insert(contentUri, contentValues)
                if (uri == null) {
                    throw IOException("Failed to create new MediaStore record.")
                }

                stream = app.contentResolver.openOutputStream(uri)
                if (stream == null) {
                    throw IOException("Failed to get output stream.")
                }

                if (!bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)) {
                    throw IOException("Failed to save bitmap.")
                }

                _doneSavingImage.emit(uri)

            } catch (securityException: SecurityException) {
                Log.e("DHR", "securityException $securityException")
                throw securityException
            } catch (ioException: IOException) {
                Log.e("DHR", "IOException $ioException")
                if (uri != null) {
                    app.contentResolver.delete(uri, null, null)
                }
                throw IOException(ioException)

            } finally {
                stream?.close()
            }
        }
    }
}
