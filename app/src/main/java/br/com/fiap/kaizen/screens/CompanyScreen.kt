package br.com.fiap.kaizen.screens

import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import br.com.fiap.kaizen.model.Company
import br.com.fiap.kaizen.navigation.Destination
import br.com.fiap.kaizen.repository.RoomCompanyRepository
import br.com.fiap.kaizen.ui.theme.KaizenTheme
import br.com.fiap.kaizen.utils.convertBitmapToByteArray
import br.com.fiap.kaizen.utils.convertByteArrayToBitmap

@Composable
fun CompanyScreen(navController: NavController) {
    Scaffold(
        topBar = {
            MyTopAppBarCompany(navController = navController)
        }
    ) { paddingValues ->
        ContentScreenCompany(
            modifier = Modifier.padding(paddingValues),
            navController = navController
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBarCompany(navController: NavController) {
    TopAppBar(
        title = {
            Text(
                text = "Company Form",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary
            )
        },
        navigationIcon = {
            IconButton(
                onClick = {
                    navController.navigate(Destination.HomeScreen.route) {
                        popUpTo(Destination.HomeScreen.route) { inclusive = false }
                        launchSingleTop = true
                    }
                }
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    )
}

@Composable
fun CompanyImagePicker(
    companyImage: Bitmap,
    onImageChange: (Bitmap) -> Unit
) {
    val context = LocalContext.current

    val launchImage = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            val bitmap =
                if (Build.VERSION.SDK_INT < 28) {
                    MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
                } else {
                    val source = ImageDecoder.createSource(context.contentResolver, uri)
                    ImageDecoder.decodeBitmap(source)
                }
            onImageChange(bitmap)
        }
    }

    Box(
        modifier = Modifier.size(120.dp)
    ) {
        Image(
            bitmap = companyImage.asImageBitmap(),
            contentDescription = "Company image",
            modifier = Modifier
                .clip(CircleShape)
                .size(110.dp)
                .align(Alignment.Center),
            contentScale = ContentScale.Crop
        )

        IconButton(
            onClick = { launchImage.launch("image/*") },
            modifier = Modifier.align(Alignment.BottomEnd)
        ) {
            Icon(
                imageVector = Icons.Default.AddAPhoto,
                contentDescription = "Choose company image",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun CompanyFormContent(
    companyName: String,
    onCompanyNameChange: (String) -> Unit,
    respondentName: String,
    onRespondentNameChange: (String) -> Unit,
    role: String,
    onRoleChange: (String) -> Unit,
    employees: String,
    onEmployeesChange: (String) -> Unit,
    sector: String,
    onSectorChange: (String) -> Unit,
    size: String,
    onSizeChange: (String) -> Unit,
    businessAreas: String,
    onBusinessAreasChange: (String) -> Unit,
    hasThirdParties: String,
    onHasThirdPartiesChange: (String) -> Unit,
    onSaveClick: () -> Unit
) {
    Spacer(modifier = Modifier.height(10.dp))

    OutlinedTextField(
        value = companyName,
        onValueChange = onCompanyNameChange,
        label = { Text("Company name") },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        leadingIcon = {
            Icon(
                Icons.Default.Business,
                contentDescription = "Company Name",
                tint = MaterialTheme.colorScheme.primary
            )
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.primary,
            focusedLabelColor = MaterialTheme.colorScheme.primary,
            cursorColor = MaterialTheme.colorScheme.primary
        )
    )

    Spacer(modifier = Modifier.height(12.dp))

    OutlinedTextField(
        value = respondentName,
        onValueChange = onRespondentNameChange,
        label = { Text("Respondent name") },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        leadingIcon = {
            Icon(
                Icons.Default.Person,
                contentDescription = "Respondent name",
                tint = MaterialTheme.colorScheme.primary
            )
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.primary,
            focusedLabelColor = MaterialTheme.colorScheme.primary,
            cursorColor = MaterialTheme.colorScheme.primary
        )
    )

    Spacer(modifier = Modifier.height(12.dp))

    OutlinedTextField(
        value = role,
        onValueChange = onRoleChange,
        label = { Text("Role / Position") },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        leadingIcon = {
            Icon(
                Icons.Default.Work,
                contentDescription = "Role / Position",
                tint = MaterialTheme.colorScheme.primary
            )
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.primary,
            focusedLabelColor = MaterialTheme.colorScheme.primary,
            cursorColor = MaterialTheme.colorScheme.primary
        )
    )

    Spacer(modifier = Modifier.height(12.dp))

    OutlinedTextField(
        value = employees,
        onValueChange = onEmployeesChange,
        label = { Text("Approx. number of employees") },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        shape = RoundedCornerShape(12.dp),
        leadingIcon = {
            Icon(
                Icons.Default.Groups,
                contentDescription = "Approx. number of employees",
                tint = MaterialTheme.colorScheme.primary
            )
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.primary,
            focusedLabelColor = MaterialTheme.colorScheme.primary,
            cursorColor = MaterialTheme.colorScheme.primary
        )
    )

    Spacer(modifier = Modifier.height(12.dp))

    OutlinedTextField(
        value = sector,
        onValueChange = onSectorChange,
        label = { Text("Industry / Sector") },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.primary,
            focusedLabelColor = MaterialTheme.colorScheme.primary,
            cursorColor = MaterialTheme.colorScheme.primary
        )
    )

    Spacer(modifier = Modifier.height(12.dp))

    OutlinedTextField(
        value = size,
        onValueChange = onSizeChange,
        label = { Text("Company size") },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.primary,
            focusedLabelColor = MaterialTheme.colorScheme.primary,
            cursorColor = MaterialTheme.colorScheme.primary
        )
    )

    Spacer(modifier = Modifier.height(12.dp))

    OutlinedTextField(
        value = businessAreas,
        onValueChange = onBusinessAreasChange,
        label = { Text("Business areas") },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.primary,
            focusedLabelColor = MaterialTheme.colorScheme.primary,
            cursorColor = MaterialTheme.colorScheme.primary
        )
    )

    Spacer(modifier = Modifier.height(12.dp))

    OutlinedTextField(
        value = hasThirdParties,
        onValueChange = onHasThirdPartiesChange,
        label = { Text("Critical third parties?") },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.primary,
            focusedLabelColor = MaterialTheme.colorScheme.primary,
            cursorColor = MaterialTheme.colorScheme.primary
        )
    )

    Spacer(modifier = Modifier.height(24.dp))

    Button(
        onClick = onSaveClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Text("Save and Continue")
    }

    Spacer(modifier = Modifier.height(40.dp))
}

@Composable
fun ContentScreenCompany(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    val context = LocalContext.current
    val companyRepository = RoomCompanyRepository(context)

    val placeholderImage = BitmapFactory.decodeResource(
        Resources.getSystem(),
        android.R.drawable.ic_menu_gallery
    )

    var companyId by remember { mutableLongStateOf(0L) }

    var companyImage by remember { mutableStateOf(placeholderImage) }

    var companyName by remember { mutableStateOf("") }
    var respondentName by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("") }
    var employees by remember { mutableStateOf("") }
    var sector by remember { mutableStateOf("") }
    var size by remember { mutableStateOf("") }
    var businessAreas by remember { mutableStateOf("") }
    var hasThirdParties by remember { mutableStateOf("") }

    var showSuccessDialog by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        val savedCompany = companyRepository.getLastCompany()

        if (savedCompany != null) {
            companyId = savedCompany.id
            companyName = savedCompany.companyName
            respondentName = savedCompany.respondentName
            role = savedCompany.role
            employees = savedCompany.employees.toString()
            sector = savedCompany.sector
            size = savedCompany.size
            businessAreas = savedCompany.businessAreas
            hasThirdParties = savedCompany.hasThirdParties

            if (savedCompany.companyImage != null) {
                companyImage = convertByteArrayToBitmap(savedCompany.companyImage)
            }
        }
    }

    fun validate(): Boolean {
        return companyName.isNotBlank() &&
                respondentName.isNotBlank() &&
                role.isNotBlank() &&
                employees.isNotBlank() &&
                sector.isNotBlank() &&
                size.isNotBlank() &&
                businessAreas.isNotBlank() &&
                hasThirdParties.isNotBlank()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            CompanyImagePicker(
                companyImage = companyImage,
                onImageChange = { bitmap ->
                    companyImage = bitmap
                }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        CompanyFormContent(
            companyName = companyName,
            onCompanyNameChange = { companyName = it },
            respondentName = respondentName,
            onRespondentNameChange = { respondentName = it },
            role = role,
            onRoleChange = { role = it },
            employees = employees,
            onEmployeesChange = { employees = it },
            sector = sector,
            onSectorChange = { sector = it },
            size = size,
            onSizeChange = { size = it },
            businessAreas = businessAreas,
            onBusinessAreasChange = { businessAreas = it },
            hasThirdParties = hasThirdParties,
            onHasThirdPartiesChange = { hasThirdParties = it },
            onSaveClick = {
                if (!validate()) {
                    showErrorDialog = "Please fill in all fields."
                } else {
                    try {
                        val company = Company(
                            id = companyId,
                            companyName = companyName,
                            respondentName = respondentName,
                            role = role,
                            employees = employees.toIntOrNull() ?: 0,
                            sector = sector,
                            size = size,
                            businessAreas = businessAreas,
                            hasThirdParties = hasThirdParties,
                            companyImage = convertBitmapToByteArray(companyImage)
                        )

                        if (companyId == 0L) {
                            val newId = companyRepository.saveCompany(company)
                            companyId = newId
                        } else {
                            companyRepository.updateCompany(company)
                        }

                        showSuccessDialog = true
                    } catch (e: Exception) {
                        showErrorDialog = "Error saving company data."
                    }
                }
            }
        )
    }

    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { showSuccessDialog = false },
            title = { Text("Success") },
            text = { Text("Company information saved successfully.") },
            confirmButton = {
                Button(
                    onClick = {
                        showSuccessDialog = false
                        navController.navigate(Destination.HomeScreen.route) {
                            popUpTo(Destination.HomeScreen.route) { inclusive = false }
                            launchSingleTop = true
                        }
                    }
                ) {
                    Text("OK")
                }
            }
        )
    }

    if (showErrorDialog != null) {
        AlertDialog(
            onDismissRequest = { showErrorDialog = null },
            title = { Text("Validation Error") },
            text = { Text(showErrorDialog!!) },
            confirmButton = {
                TextButton(onClick = { showErrorDialog = null }) {
                    Text("OK")
                }
            }
        )
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
fun CompanyPreview() {
    KaizenTheme {
        CompanyScreen(navController = rememberNavController())
    }
}