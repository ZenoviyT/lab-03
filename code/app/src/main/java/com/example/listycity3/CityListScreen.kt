package com.example.listycity3

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.listycity3.ui.theme.ListyCity3Theme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.FloatingActionButton
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.clickable

@Composable
fun CityListScreen(
    cities: List<City>,
    onAddCity: (City) -> Unit,
    onUpdateCity: (City, City) -> Unit,
    modifier: Modifier = Modifier
) {
    var newCityName by remember { mutableStateOf("") }
    var newProvinceName by remember { mutableStateOf("") }
    var showAddCityFields by remember { mutableStateOf(false) }
    var selectedCity by remember { mutableStateOf<City?>(null) } // This stores the currently selected city. This updates based on button inputs.
    var buttonText by remember { mutableStateOf("Add City") } // This is just so I can dynamically edit the button text.

    fun updateSelectedCity(city: City?) {
        // A Helper function that updates visuals (buttons, text fields, input options) when a city is selected.
        // Returns: Nothing
        selectedCity = city
        if (city == null) {
            buttonText = "Add City"
            newCityName = ""
            newProvinceName = ""
        } else {
            buttonText = "Update City"
            newCityName = city.name
            newProvinceName = city.province
        }
        showAddCityFields = true
    }

    Column(modifier = modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            FloatingActionButton(
                modifier = Modifier.padding(16.dp),
                onClick = {
                    showAddCityFields = !showAddCityFields
                }
            ) {
                Text("+")
            }
        }
        if (showAddCityFields) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                OutlinedTextField(
                    value = newCityName,
                    onValueChange = { newCityName = it },
                    label = { Text("City") },
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                OutlinedTextField(
                    value = newProvinceName,
                    onValueChange = { newProvinceName = it },
                    label = { Text("Province") },
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    modifier = Modifier.padding(vertical = 12.dp),
                    onClick = {
                        if (newCityName.isNotBlank() && newProvinceName.isNotBlank()) {
                            if (selectedCity == null) { // This is the "Add City" Button
                                onAddCity(
                                    City(
                                        name = newCityName,
                                        province = newProvinceName
                                    )
                                )
                                newCityName = ""
                                newProvinceName = ""
                                showAddCityFields = false
                            } else { // This is for when a city is selected, and should be overwritten instead of adding a new city
                                onUpdateCity(
                                    selectedCity!!, // So just doing selectedCity can be an issue since it CAN be null, but Kotlin somehow does have an 'Add non-null asserted call' function in the contextual help tab. Very nice.
                                    City(
                                        name = newCityName,
                                        province = newProvinceName
                                    )
                                )
                                newCityName = ""
                                newProvinceName = ""
                                showAddCityFields = false
                                buttonText =
                                    "Add City" // This is so it resets back to 'Add City' correctly. As for why it's formatted like this, blame the IDE Incorrect Formatting Inspection.
                                selectedCity = null
                            }
                        }
                    }
                ) {
                    Text(buttonText)
                }
            }
        }
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            itemsIndexed(cities) { index, city ->
                CityRow(
                    city = city,
                    currentCity = selectedCity,
                    selectCity = { updateSelectedCity(it) })
                if (index < cities.lastIndex) {
                    HorizontalDivider()
                }
            }
        }

    }
}

@Composable
fun CityRow(city: City, currentCity: City?, selectCity: (City?) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp)
            .clickable { // The clickable to select a city
                if (city == currentCity) { // When a city is already selected, and you click the same one...
                    selectCity(null) //...it will effectively de-select it.
                } else { // otherwise, if there is no selected city...
                    selectCity(city) //...it will call the helper function to select it.
                }
            }
    ) {
        Text(
            text = city.name,
            fontSize = 30.sp,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = city.province,
            fontSize = 30.sp,
            modifier = Modifier.weight(1f)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CityListScreenPreview() {
    ListyCity3Theme {
        CityListScreen(
            cities = listOf(
                City("Edmonton", "AB"),
                City("Vancouver", "BC"),
                City("Calgary", "AB")
            ),
            onAddCity = {},
            onUpdateCity = { _, _ -> },
        )
    }
}
