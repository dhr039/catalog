package com.cannonades.petconnect.common.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cannonades.petconnect.common.presentation.model.UICategory

@Composable
fun CategoriesList(
    modifier: Modifier,
    categories: List<UICategory>,
    openCategoryScreen: (String) -> Unit
) {
    LazyColumn(
        modifier = modifier.padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(categories.size) { index ->
            val category = categories[index]
            CategoryCard(
                category = category,
                onClick = { openCategoryScreen(category.id) }
            )
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryCard(category: UICategory, onClick: () -> Unit) {
    Card(
        shape = MaterialTheme.shapes.small,
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = 12.dp, horizontal = 16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = category.name, style = MaterialTheme.typography.bodyLarge)
            }

            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = null
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCategoryCard() {
    val dummyCategory = UICategory(
        id = "1",
        name = "Dummy Category"
        // add other fields if needed
    )

    MaterialTheme {
        CategoryCard(category = dummyCategory, onClick = {})
    }
}