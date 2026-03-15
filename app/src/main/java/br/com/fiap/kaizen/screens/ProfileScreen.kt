package br.com.fiap.kaizen.screens

import android.content.res.Configuration
import android.content.res.Resources
import android.database.sqlite.SQLiteConstraintException
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Patterns
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.RemoveRedEye
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import br.com.fiap.kaizen.R
import br.com.fiap.kaizen.model.User
import br.com.fiap.kaizen.navigation.Destination
import br.com.fiap.kaizen.repository.RoomUserRepository
import br.com.fiap.kaizen.ui.theme.KaizenTheme
import br.com.fiap.kaizen.utils.convertBitmapToByteArray
import br.com.fiap.kaizen.utils.convertByteArrayToBitmap

@Composable
fun ProfileScreen(navController: NavHostController?, email: String) {
    val context = LocalContext.current
    val userRepository = remember { RoomUserRepository(context) }

    val placeholderImage = BitmapFactory.decodeResource(
        Resources.getSystem(),
        android.R.drawable.ic_menu_gallery
    )

    var profileImage by remember {
        mutableStateOf(placeholderImage)
    }

    LaunchedEffect(email) {
        val savedUser = userRepository.getUserByEmail(email)
        if (savedUser?.userImage != null) {
            profileImage = convertByteArrayToBitmap(savedUser.userImage)
        }
    }

    val launchImage = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        if (uri != null) {
            profileImage =
                if (Build.VERSION.SDK_INT < 28) {
                    MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
                } else {
                    val source = ImageDecoder.createSource(context.contentResolver, uri)
                    ImageDecoder.decodeBitmap(source)
                }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProfileTitleComponent()

            Spacer(modifier = Modifier.height(48.dp))

            ProfileUserImage(
                profileImage = profileImage,
                launchImage = launchImage
            )

            ProfileUserForm(
                navController = navController,
                profileImage = profileImage,
                email = email
            )
        }
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    locale = "en"
)
@Composable
private fun ProfileScreenPreview() {
    KaizenTheme {
        ProfileScreen(null, "")
    }
}

@Composable
fun ProfileTitleComponent(modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.profile),
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            text = stringResource(R.string.user_profile_details),
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleSmall
        )
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    locale = "en"
)
@Composable
private fun ProfileTitleComponentPreview() {
    KaizenTheme {
        ProfileTitleComponent()
    }
}

@Composable
fun ProfileUserImage(
    profileImage: Bitmap?,
    launchImage: ManagedActivityResultLauncher<String, Uri?>
) {
    if (profileImage == null) return

    Box(
        modifier = Modifier.size(120.dp)
    ) {
        Image(
            bitmap = profileImage.asImageBitmap(),
            contentDescription = stringResource(R.string.user_profile_image),
            modifier = Modifier
                .clip(CircleShape)
                .size(110.dp)
                .align(Alignment.Center),
            contentScale = ContentScale.Crop
        )
        Icon(
            imageVector = Icons.Default.AddAPhoto,
            tint = MaterialTheme.colorScheme.primary,
            contentDescription = stringResource(R.string.choose_company_image),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .clickable {
                    launchImage.launch("image/*")
                }
        )
    }
}

@Composable
fun ProfileUserForm(
    navController: NavHostController?,
    profileImage: Bitmap?,
    email: String = ""
) {
    val userRepository = RoomUserRepository(LocalContext.current)

    var user = userRepository.getUserByEmail(email)

    var name by remember { mutableStateOf(user?.name ?: "") }
    var userEmail by remember { mutableStateOf(user?.email ?: "") }
    var password by remember { mutableStateOf(user?.password ?: "") }

    var isNameError by remember { mutableStateOf(false) }
    var isEmailError by remember { mutableStateOf(false) }
    var isPasswordError by remember { mutableStateOf(false) }

    var showDialogError by remember { mutableStateOf<String?>(null) }
    var showDialogSuccess by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    fun validate(): Boolean {
        isNameError = name.length < 3
        isEmailError = userEmail.length < 3 || !Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()
        isPasswordError = password.length < 3
        return !isNameError && !isEmailError && !isPasswordError
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp)
    ) {
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 4.dp),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.primary
            ),
            label = {
                Text(
                    text = stringResource(R.string.your_name),
                    style = MaterialTheme.typography.labelSmall
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.tertiary
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next
            ),
            isError = isNameError,
            trailingIcon = {
                if (isNameError) {
                    Icon(imageVector = Icons.Default.Error, contentDescription = null)
                }
            },
            supportingText = {
                if (isNameError) {
                    Text(
                        text = stringResource(R.string.username_is_required),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.End,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        )

        OutlinedTextField(
            value = userEmail,
            onValueChange = { userEmail = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 4.dp),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.primary
            ),
            label = {
                Text(
                    text = stringResource(R.string.your_e_mail),
                    style = MaterialTheme.typography.labelSmall
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.tertiary
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            isError = isEmailError,
            trailingIcon = {
                if (isEmailError) {
                    Icon(imageVector = Icons.Default.Error, contentDescription = null)
                }
            },
            supportingText = {
                if (isEmailError) {
                    Text(
                        text = stringResource(R.string.email_is_required),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.End,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.primary
            ),
            label = {
                Text(
                    text = stringResource(R.string.your_password),
                    style = MaterialTheme.typography.labelSmall
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.tertiary
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.NumberPassword,
                imeAction = ImeAction.Done
            ),
            isError = isPasswordError,
            trailingIcon = {
                if (isPasswordError) {
                    Icon(imageVector = Icons.Default.Error, contentDescription = null)
                } else {
                    Icon(
                        imageVector = Icons.Default.RemoveRedEye,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.tertiary
                    )
                }
            },
            supportingText = {
                if (isPasswordError) {
                    Text(
                        text = stringResource(R.string.password_is_required),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.End,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (validate()) {
                    user = User(
                        id = user?.id ?: 0,
                        name = name,
                        email = userEmail,
                        password = password,
                        userImage = profileImage?.let { convertBitmapToByteArray(it) }
                    )
                    try {
                        userRepository.updateUser(user!!)
                        showDialogSuccess = true
                    } catch (_: SQLiteConstraintException) {
                        isEmailError = true
                        showDialogError = "Error"
                    }
                } else {
                    showDialogError = "Error"
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                text = stringResource(R.string.update_profile),
                style = MaterialTheme.typography.labelMedium
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                showDeleteDialog = true
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.tertiary
            )
        ) {
            Text(
                text = stringResource(R.string.delete_profile),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onTertiary
            )
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = {
                Text(text = stringResource(R.string.delete_user))
            },
            text = {
                Text(text = stringResource(R.string.removal_confirmation))
            },
            confirmButton = {
                TextButton(onClick = {
                    showDeleteDialog = false
                    if (user != null) {
                        userRepository.deleteUser(user!!)
                        navController?.navigate(Destination.LoginScreen.route)
                    }
                }) {
                    Text(text = stringResource(R.string.ok))
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDeleteDialog = false
                }) {
                    Text(text = stringResource(R.string.cancel))
                }
            }
        )
    }

    if (showDialogSuccess) {
        AlertDialog(
            onDismissRequest = { showDialogSuccess = false },
            title = { Text(stringResource(R.string.sucesso)) },
            text = { Text(stringResource(R.string.registration_completed_successfully)) },
            confirmButton = {
                Button(onClick = {
                    navController?.navigate(Destination.LoginScreen.route)
                }) {
                    Text(text = stringResource(R.string.log_in))
                }
            }
        )
    }

    if (showDialogError != null) {
        AlertDialog(
            onDismissRequest = { showDialogError = null },
            title = {
                Text(text = stringResource(R.string.validation_error))
            },
            text = {
                Text(text = stringResource(R.string.please_correct_the_highlighted_fields))
            },
            confirmButton = {
                TextButton(onClick = { showDialogError = null }) {
                    Text(stringResource(R.string.ok))
                }
            }
        )
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    locale = "en"
)
@Composable
private fun ProfileUserFormPreview() {
    KaizenTheme {
        ProfileUserForm(null, null, "")
    }
}