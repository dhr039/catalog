package com.cannonades.petconnect.common.presentation.ui

import android.content.Intent
import android.net.Uri
import android.util.Patterns
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.cannonades.petconnect.common.presentation.model.UIBreed
import kotlinx.coroutines.launch


@Composable
fun AnimalScreen(animalId: String, viewModel: AnimalViewModel = hiltViewModel()) {
    LaunchedEffect(animalId) {
        viewModel.loadAnimal(animalId)
    }

    val viewState by viewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
            .verticalScroll(rememberScrollState())
    ) {
        viewState.animal?.photo?.let { url ->

            AsyncImage(
                modifier = Modifier.fillMaxSize(),
                model = url,
                contentDescription = null,
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 60.dp),
                onClick = { viewModel.saveImageFromUrl(url) },
                enabled = !(viewState.fileSaved),
            ) {
                when {
                    viewState.fileSaved -> {
                        Text("Saved")
                    }

                    else -> {
                        Text("Save Image")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        viewState.animal?.breeds?.firstOrNull()?.let { breed ->
            BreedInfoTable(breed)
        }
    }
}


@Composable
fun BreedInfoTable(breed: UIBreed) {
    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.padding(16.dp)
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            breed.name.let {
                if (it.isNotBlank()) {
                    DisplayTextRow("Breed:", it)
                }
            }

            breed.description?.let {
                if (it.isNotBlank()) {
                    DisplayTextRow("Description:", it)
                }
            }
            breed.altNames?.let {
                if (it.isNotBlank()) {
                    DisplayTextRow("Alt Names:", it)
                }
            }
            breed.wikipediaUrl?.let {
                if (it.isNotBlank()) {
                    DisplayTextRow("Wikipedia URL:", it)
                }
            }
            breed.cfaUrl?.let {
                if (it.isNotBlank()) {
                    DisplayTextRow("CFA URL:", it)
                }
            }
            breed.vetstreetUrl?.let {
                if (it.isNotBlank()) {
                    DisplayTextRow("VetStreet URL:", it)
                }
            }
            breed.temperament?.let {
                if (it.isNotBlank()) {
                    DisplayTextRow("Temperament:", it)
                }
            }
            breed.origin?.let {
                if (it.isNotBlank()) {
                    DisplayTextRow("Origin:", it)
                }
            }
            breed.lifeSpan?.let {
                if (it.isNotBlank()) {
                    DisplayTextRow("Life Span:", it)
                }
            }
            breed.indoor?.let {
                DisplayTextRow("Indoor:", it.toString())
            }
            breed.adaptability?.let {
                DisplayTextRow("Adaptability:", it.toString())
            }
            breed.affectionLevel?.let {
                DisplayTextRow("Affection Level:", it.toString())
            }
            breed.childFriendly?.let {
                DisplayTextRow("Child Friendly:", it.toString())
            }
            breed.dogFriendly?.let {
                DisplayTextRow("Dog Friendly:", it.toString())
            }
            breed.energyLevel?.let {
                DisplayTextRow("Energy Level:", it.toString())
            }
            breed.grooming?.let {
                DisplayTextRow("Grooming:", it.toString())
            }
            breed.healthIssues?.let {
                DisplayTextRow("Health Issues:", it.toString())
            }
            breed.intelligence?.let {
                DisplayTextRow("Intelligence:", it.toString())
            }
            breed.sheddingLevel?.let {
                DisplayTextRow("Shedding Level:", it.toString())
            }
            breed.socialNeeds?.let {
                DisplayTextRow("Social Needs:", it.toString())
            }
            breed.strangerFriendly?.let {
                DisplayTextRow("Stranger Friendly:", it.toString())
            }
            breed.vocalisation?.let {
                DisplayTextRow("Vocalisation:", it.toString())
            }
            breed.hypoallergenic?.let {
                DisplayTextRow("Hypoallergenic:", it.toString())
            }
        }
    }
}

@Composable
fun DisplayTextRow(label: String, text: String) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val isUrl = Patterns.WEB_URL.matcher(text).matches()
    val url = if (isUrl && !text.startsWith("http://") && !text.startsWith("https://")) {
        "http://$text"
    } else {
        text
    }

    val annotatedText = if (isUrl) {
        buildAnnotatedString {
            withStyle(style = SpanStyle(textDecoration = TextDecoration.Underline)) {
                append(text)
            }
            addStringAnnotation(
                tag = "URL",
                annotation = url,
                start = 0,
                end = text.length
            )
        }
    } else {
        AnnotatedString(text)
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 5.dp)
        )
        Text(
            text = annotatedText,
            modifier = Modifier
                .wrapContentWidth(Alignment.Start)
                .padding(start = 10.dp)
                .clickable {
                    annotatedText
                        .getStringAnnotations(
                            "URL",
                            annotatedText.text.indexOf(text),
                            annotatedText.text.indexOf(text) + text.length
                        )
                        .firstOrNull()
                        ?.let { urlAnnotation ->
                            coroutineScope.launch {
                                val intent =
                                    Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse(urlAnnotation.item)
                                    ).apply {
                                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                    }
                                context.startActivity(intent)
                            }
                        }
                },
            textDecoration = if (isUrl) TextDecoration.Underline else null
        )
    }
}