package kg.kstu.smartapiary.presentation.screens.apiary

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kg.kstu.smartapiary.R
import kg.kstu.smartapiary.domain.data.ApiaryData
import kg.kstu.smartapiary.presentation.screens.InfoItem

@Composable
fun ApiaryCard(hive: ApiaryData, onHiveClick: (String) -> Unit) {

    val weightDevice = hive.devices.find { it.weight != "N/A" }
    val sensorDevice = hive.devices.find { it.temp != "N/A" || it.humid != "N/A" || it.pressure != "N/A" || it.altitude != "N/A" }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .clickable { onHiveClick(hive.hiveId) },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 16.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .background(Color.White, shape = CircleShape)
                            .clip(CircleShape)
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_apiary),
                            contentDescription = "Apiary",
                            tint = Color.Black,
                            modifier = Modifier.size(56.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Пасека №${hive.hiveId}",
                                style = MaterialTheme.typography.titleMedium,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(
                                painter = painterResource(id = R.drawable.ic_wifi),
                                contentDescription = "WiFi",
                                tint = Color(0xFFFBC803),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Text(
                            text = "г. Бишкек",
                            style = MaterialTheme.typography.bodySmall,
                            fontSize = 14.sp
                        )
                    }
                }

                Divider(color = Color.LightGray, thickness = 1.dp)

                if (sensorDevice != null) {
                    Text(
                        text = "Погода на пасеке",
                        style = MaterialTheme.typography.titleSmall,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        InfoItem(R.drawable.ic_temperature, "${sensorDevice.temp}° C")
                        InfoItem(R.drawable.ic_humidity, "${sensorDevice.humid}%")
                        InfoItem(R.drawable.ic_pressure, "${sensorDevice.pressure} кПа")
                    }
                }

                if (weightDevice != null || sensorDevice?.altitude != "N/A") {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        weightDevice?.let {
                            InfoItem(R.drawable.ic_weight, "${it.weight} кг")
                        }
                        Spacer(modifier = Modifier.width(32.dp))
                        InfoItem(R.drawable.ic_altitude, "${sensorDevice?.altitude ?: "N/A"} м")
                    }
                }
            }

            Icon(
                painter = painterResource(id = R.drawable.ic_notification),
                contentDescription = "Notifications",
                tint = Color(0xFFFBC803),
                modifier = Modifier
                    .size(28.dp)
                    .align(Alignment.TopEnd)
                    .offset(y = (6).dp)
            )
        }
    }
}