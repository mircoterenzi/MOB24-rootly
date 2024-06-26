package com.unibo.rootly.ui.screens

import android.content.Intent
import android.net.Uri
import android.provider.CalendarContract
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.unibo.rootly.data.database.Species
import com.unibo.rootly.ui.composables.ImageDisplay
import com.unibo.rootly.viewmodel.PlantViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Composable
fun PlantDetailsScreen(
    navController: NavHostController,
    plantViewModel: PlantViewModel
) {
    val context = LocalContext.current
    val formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy")

    val plant = plantViewModel.plantSelected!!
    var isFavorite by remember { mutableStateOf(plant.isFavorite) }
    var nextWaterDate by remember { mutableStateOf<LocalDate?>(null) }
    var nextFertilizeDate by remember { mutableStateOf<LocalDate?>(null) }
    var specie by remember { mutableStateOf<Species?>(null) }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            nextWaterDate = plantViewModel.getNextWater(plant)
            nextFertilizeDate = plantViewModel.getNextFertilize(plant)
            specie = plantViewModel.getSpecieDetails(plant.scientificName)
        }
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(Modifier.height(16.dp))
        Column {
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = plant.plantName,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.SemiBold
                    )
                )
                Text(
                    text = "(${plant.scientificName})",
                    style = MaterialTheme.typography.titleLarge
                )
            }
            Text(
                text = "Added on ${plant.birthday.format(formatter)}",
                style = MaterialTheme.typography.bodySmall
            )
        }
        Box(
            contentAlignment = Alignment.TopEnd
        ) {
            ImageDisplay(
                uri = plant.img?.let { Uri.parse(plant.img) },
                contentDescription = "Plant photo",
                modifier = Modifier
                    .clip(RoundedCornerShape(28.dp))
                    .fillMaxWidth()
                    .heightIn(min = 256.dp,  max = 435.dp),
                defaultHigh = 256.dp
            )
            FilledTonalIconButton(
                onClick = {
                    if (isFavorite) {
                        plantViewModel.removeLike(plant.plantId)
                    } else {
                        plantViewModel.addLike(plant.plantId)
                    }
                    isFavorite = !isFavorite
                },
                modifier = Modifier.padding(4.dp)
            ) {
                if(isFavorite) {
                    Icon(Icons.Filled.Favorite, "add to Favorites")
                } else {
                    Icon(Icons.Outlined.FavoriteBorder , "remove from Favorites")
                }
            }
        }
        val lightLevel: String = when (specie?.lightLevel) {
            1 -> "dark"
            2 -> "shade"
            3 -> "part sun"
            4 -> "full sun"
            else -> "error please contact our team :("
        }
        Text(
            text = "Ideal conditions:",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
        )
        Text(
            text = "Light: $lightLevel",
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = "Living temperature: " +
                    "${specie?.minTemperature}° - " +
                    "${specie?.maxTemperature}°",
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = "Next activities:",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
        )
        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Water: ${nextWaterDate?.format(formatter)}",
                style = MaterialTheme.typography.bodyMedium
            )
            AddToCalendarButton(
                title = "Water ${plant.plantName}",
                time = nextWaterDate?.atStartOfDay(ZoneId.systemDefault())?.toInstant()
                    ?.toEpochMilli() ?: System.currentTimeMillis()
            )
        }
        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Fertilizer: ${nextFertilizeDate?.format(formatter)}",
                style = MaterialTheme.typography.bodyMedium
            )
            AddToCalendarButton(
                title = "Fertilizer ${plant.plantName}",
                time = nextFertilizeDate?.atStartOfDay(ZoneId.systemDefault())?.toInstant()
                    ?.toEpochMilli() ?: System.currentTimeMillis()
            )
        }
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = {
                navController.navigateUp()
                plantViewModel.addDead(plant, context = context)
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onErrorContainer
            )
        ) {
            Text("Mark as dead")
        }
        Spacer(Modifier.height(16.dp))
    }
}

@Composable
fun AddToCalendarButton(
    title: String,
    time: Long
) {
    val context = LocalContext.current

    Box(
        Modifier.clickable {
            val intent = Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.Events.TITLE, title)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, time)
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, time)
            context.startActivity(intent)
        }
    ) {
        Icon(
            Icons.Outlined.Event,
            contentDescription = "Add event",
        )
    }
}