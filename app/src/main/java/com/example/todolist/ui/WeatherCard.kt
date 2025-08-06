package com.example.todolist.ui

import com.example.todolist.ViewModel.WeatherUiState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun WeatherCard(location: String, state: WeatherUiState.Success) {
    Column(Modifier.fillMaxWidth()) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(4.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(location, fontSize = 30.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Serif)
                    Spacer(modifier = Modifier.width(16.dp))
                    Text("${state.weatherInfo.current.temp}", fontSize = 25.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Serif)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("🌡", fontSize = 20.sp)
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("🌤️", fontSize = 23.sp, modifier = Modifier.padding(start = 50.dp))
                    Text("🌥️", fontSize = 23.sp, modifier = Modifier.padding(end = 50.dp))
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Sunrise - ${state.astronomyInfo.astronomy.astro.sunrise}", fontSize = 15.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Serif)
                    Spacer(Modifier.height(12.dp))
                    Text("Sunset - ${state.astronomyInfo.astronomy.astro.sunset}", fontSize = 15.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Serif)
                }
            }
        }
    }
}
