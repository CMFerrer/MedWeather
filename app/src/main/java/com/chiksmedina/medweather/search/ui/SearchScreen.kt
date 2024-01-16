package com.chiksmedina.medweather.search.ui

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.chiksmedina.solar.OutlineSolar
import com.chiksmedina.solar.outline.Arrows
import com.chiksmedina.solar.outline.Search
import com.chiksmedina.solar.outline.arrows.AltArrowLeft
import com.chiksmedina.solar.outline.search.MinimalisticMagnifer
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Composable
fun SearchScreen(
    uiState: SearchUiState.Success,
    scope: CoroutineScope,
    search: (String) -> Unit,
    saveCityAndLocation: (String, Double, Double) -> Unit,
    onBackPress: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment =  Alignment.CenterHorizontally
    ) {
        SearchTopBar(onBackPress, search)

        ButtonRequestLocationPermission(scope = scope, saveCityAndLocation = saveCityAndLocation)
        uiState.cities?.results?.let {
            for (city in it) {
                ListItem(
                    headlineContent = { Text(text = city.name) },
                    modifier = Modifier.clickable { saveCityAndLocation(city.name, city.latitude, city.longitude) },
                    supportingContent = { Text(text = "${city.admin1} ${city.country}") },
                )
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@SuppressLint("MissingPermission")
@Composable
fun ButtonRequestLocationPermission(
    scope: CoroutineScope,
    saveCityAndLocation: (String, Double, Double) -> Unit
) {
    val context = LocalContext.current
    val locationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }
    var locationInfo by remember {
        mutableStateOf("")
    }
    val locationPermissionsState = rememberMultiplePermissionsState(
        listOf(
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
        )
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = {
                if (locationPermissionsState.allPermissionsGranted) {
                    scope.launch(Dispatchers.IO) {
                        val result = locationClient.lastLocation.await()
                        locationInfo = if (result == null) {
                            "No last known location. Try fetching the current location first"
                        } else {
                            "Current location is \n" + "lat : ${result.latitude}\n" +
                                    "long : ${result.longitude}\n" + "fetched at ${System.currentTimeMillis()}"
                        }
                        Log.d("Location", locationInfo)
                        if (result!= null) saveCityAndLocation("Ubicación Actual", result.latitude, result.longitude)
                    }
                } else {
                    locationPermissionsState.launchMultiplePermissionRequest()
                }
            },
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium
        ) {
            Text(text = "Usar ubicación actual")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTopBar(
    onBackPress: () -> Unit,
    search: (String) -> Unit
) {
    var text by remember { mutableStateOf("") }

    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        navigationIcon = {
            IconButton(onClick = { onBackPress() }) {
                Icon(imageVector = OutlineSolar.Arrows.AltArrowLeft, contentDescription = null)
            }
        },
        title = {
            TextField(
                value = text,
                onValueChange = { text = it },
                placeholder = { Text(text = "Buscar...") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = { search(text) }
                ),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    focusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                    unfocusedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                )
            )
        },
        actions = {
            IconButton(onClick = { search(text) }) {
                Icon(
                    imageVector = OutlineSolar.Search.MinimalisticMagnifer,
                    contentDescription = "Search icon"
                )
            }
        }
    )
}