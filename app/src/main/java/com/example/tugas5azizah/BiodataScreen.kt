package com.example.tugas5azizah

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BiodataScreen() {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }
    var experience by remember { mutableStateOf("") }
    var isAndroidChecked by remember { mutableStateOf(false) }
    var isUiUxChecked by remember { mutableStateOf(false) }
    var isWebChecked by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()
    var expandedDropdown by remember { mutableStateOf(false) }
    val experienceOptions = stringArrayResource(id = R.array.experience_options)
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // Variabel Job untuk mengelola waktu tunggu pop-up agar akurat 5 detik
    var dialogJob by remember { mutableStateOf<Job?>(null) }
    // State untuk mengontrol munculnya pop-up
    var showSuccessDialog by remember { mutableStateOf(false) }

    // Logika untuk mengecek apakah form sudah diisi semua
    val isFormValid = firstName.isNotBlank() && lastName.isNotBlank() && email.isNotBlank() &&
            birthDate.isNotBlank() && experience.isNotBlank() &&
            (isAndroidChecked || isUiUxChecked || isWebChecked)
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Image(
                painter = painterResource(id = R.drawable.bg_green_totoro),
                contentDescription = stringResource(id = R.string.desc_bg),
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.35f),
                contentScale = ContentScale.Crop
            )
            // 2. Box / Card yang Mengapung Di atas Scaffold
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.78f)
                    .align(Alignment.BottomCenter)
                    .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                    .background(colorResource(id = R.color.card_background))
            ) {
                // ==========================================
                // KONTEN FORMULIR
                // ==========================================
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(start = 24.dp, end = 24.dp, top = 24.dp, bottom = 60.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(id = R.string.form_title),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(id = R.color.primary_green)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = stringResource(id = R.string.form_subtitle),
                        fontSize = 12.sp,
                        color = colorResource(id = R.color.text_gray)
                    )
                    Spacer(modifier = Modifier.height(24.dp))

                    // NAMA DEPAN & NAMA BELAKANG
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedTextField(
                            value = firstName,
                            onValueChange = { firstName = it },
                            placeholder = { Text(stringResource(id = R.string.placeholder_first_name)) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = colorResource(id = R.color.border_green),
                                unfocusedBorderColor = colorResource(id = R.color.border_green).copy(alpha = 0.5f)
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = lastName,
                            onValueChange = { lastName = it },
                            placeholder = { Text(stringResource(id = R.string.placeholder_last_name)) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = colorResource(id = R.color.border_green),
                                unfocusedBorderColor = colorResource(id = R.color.border_green).copy(alpha = 0.5f)
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        // EMAIL
                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            placeholder = { Text(stringResource(id = R.string.placeholder_email)) },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = colorResource(id = R.color.border_green),
                                unfocusedBorderColor = colorResource(id = R.color.border_green).copy(alpha = 0.5f)
                            ),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        // TGL LAHIR & EXPERIENCE
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // TGL LAHIR (KALENDER)
                            Box(modifier = Modifier.weight(1f)) {
                                OutlinedTextField(
                                    value = birthDate,
                                    onValueChange = { },
                                    placeholder = { Text(stringResource(id = R.string.placeholder_birth_date)) },
                                    readOnly = true,
                                    trailingIcon = {
                                        Icon(
                                            imageVector = Icons.Default.DateRange,
                                            contentDescription = stringResource(id = R.string.desc_calendar),
                                            tint = colorResource(id = R.color.border_green)
                                        )
                                    },
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = colorResource(id = R.color.border_green),
                                        unfocusedBorderColor = colorResource(id = R.color.border_green).copy(alpha = 0.5f)
                                    ),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.fillMaxWidth()
                                )
                                Box(
                                    modifier = Modifier
                                        .matchParentSize()
                                        .background(Color.Transparent)
                                        .clickable { showDatePicker = true }
                                )
                            }
                            // EXPERIENCE (DROPDOWN)
                            @Suppress("DEPRECATION")
                            ExposedDropdownMenuBox(
                                expanded = expandedDropdown,
                                onExpandedChange = { expandedDropdown = !expandedDropdown },
                                modifier = Modifier.weight(1f)
                            ) {
                                OutlinedTextField(
                                    value = experience,
                                    onValueChange = { },
                                    placeholder = { Text(stringResource(id = R.string.placeholder_experience)) },
                                    readOnly = true,
                                    trailingIcon = {
                                        Icon(
                                            imageVector = Icons.Default.ArrowDropDown,
                                            contentDescription = null,
                                            tint = colorResource(id = R.color.border_green)
                                        )
                                    },
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = colorResource(id = R.color.border_green),
                                        unfocusedBorderColor = colorResource(id = R.color.border_green).copy(alpha = 0.5f)
                                    ),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier
                                        .menuAnchor()
                                        .fillMaxWidth()
                                )
                                ExposedDropdownMenu(
                                    expanded = expandedDropdown,
                                    onDismissRequest = { expandedDropdown = false }
                                ) {
                                    experienceOptions.forEach { option ->
                                        DropdownMenuItem(
                                            text = { Text(text = option) },
                                            onClick = {
                                                experience = option
                                                expandedDropdown = false
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
}
