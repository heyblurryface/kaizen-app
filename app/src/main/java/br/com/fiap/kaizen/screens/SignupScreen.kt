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
import androidx.compose.foundation.layout.width
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.res.painterResource
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

// *** Tela SignupScreen ***
@Composable
fun SignupScreen(navController: NavHostController?) {

    val context = LocalContext.current

    // Criar uma variável que armazena uma
    // imagem default para o perfil
    val placeholderImage = BitmapFactory
        .decodeResource(
            Resources.getSystem(),
            android.R.drawable.ic_menu_gallery
        )

    // Armazenar a imagem de profile
    // em uma variável de estado do tipo Bitmap
    var profileImage by remember {
        mutableStateOf<Bitmap>(placeholderImage)
    }

    // Criar um lançador de atividade para
    // abrir a galeria de imagens
    val launchImage = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) {uri ->
        if (Build.VERSION.SDK_INT < 28){
            profileImage = MediaStore
                .Images
                .Media
                .getBitmap(
                    context.contentResolver,
                    uri
                )
        } else {
            if (uri != null){
                val source = ImageDecoder.createSource(context.contentResolver, uri)
                profileImage = ImageDecoder.decodeBitmap(source)
            } else{
                profileImage = placeholderImage
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
            Image(
                painter = painterResource(id = R.drawable.logo_kaizen),
                contentDescription = stringResource(R.string.header_logo),
                modifier = Modifier.size(100.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))
            TitleComponent()
            Spacer(modifier = Modifier.height(48.dp))

            UserImage(
                profileImage = profileImage,
                launchImage = launchImage
            )
            SignupUserForm(navController, profileImage)
        }
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    locale = "en"
)
@Composable
private fun SignupScreenPreview() {
    KaizenTheme {
        SignupScreen(null)
    }
}

// *** Componente 1 - Título da tela ***
@Composable
fun TitleComponent(modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.sign_up),
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            text = stringResource(R.string.signup_subtitle),
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
private fun TitleComponentPreview() {
    KaizenTheme {
        TitleComponent()
    }

}

// TRECHO DE CÓDIGO FONTE OMITIDO...
// *** Componente 2 - Imagem do usuário
@Composable
fun UserImage(
    profileImage: Bitmap?,
    launchImage: ManagedActivityResultLauncher<String, Uri?>
) {
    Box(
        modifier = Modifier
            .size(120.dp)
    ) {
        Image(
            bitmap = profileImage?.asImageBitmap()!!,
            contentDescription = "",
            modifier = Modifier
                .clip(CircleShape)
                .size(110.dp)
                .align(Alignment.Center),
            contentScale = ContentScale.Crop
        )
        Icon(
            imageVector = Icons.Default.AddAPhoto,
            tint = MaterialTheme.colorScheme.primary,
            contentDescription = "",
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .clickable(onClick = {
                    launchImage.launch("image/*")
                })
        )
    }
}

@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    locale = "en"
)
@Composable
private fun UserImagePreview() {
    KaizenTheme {
       // UserImage(null, onProfileImageChange = {})
    }
}

// TRECHO DE CÓDIGO FONTE OMITIDO...
// *** Componente 3 - Formulário do Usuário
@Composable
fun SignupUserForm(
    navController: NavHostController?,
    profileImage: Bitmap?
) {

    // Variáveis de estado para controlar
    // os valores exibidos nos OutlinedTextFields
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Variáveis de estado para controlar
    // se os dados estão corretos
    var isNameError by remember { mutableStateOf(false) }
    var isEmailError by remember { mutableStateOf(false) }
    var isPasswordError by remember { mutableStateOf(false) }

    // Variável de estado, que controla a exibição
    // da caixa de diálogo de erro de validação
    var showDialogError by remember { mutableStateOf<String?>(null) }

    // Variável de estado que controla a exibição
    // da caixa de diálogo de confirmação de cadastro
    var showDialogSuccess by remember { mutableStateOf(false) }

    // Função de validação dos dados digitados
    fun validate(): Boolean {
        isNameError = name.length < 3
        isEmailError = email.length < 3 || !Patterns.EMAIL_ADDRESS.matcher(email).matches()
        isPasswordError = password.length < 3
        return !isNameError && !isEmailError && !isPasswordError
    }

    // Criar uma instância da classe SharedPreferencesUserRepository
    //val userRepository: UserRepository = SharedPreferencesUserRepository(LocalContext.current)
    val userRepository = RoomUserRepository(LocalContext.current)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp)
    )

        {
        // Caixa de texto your name
        OutlinedTextField(
            value = name,
            onValueChange = {
                name = it
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 4.dp),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults
                .colors(
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
                    contentDescription = "",
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
                if (isNameError){
                    Icon(imageVector = Icons.Default.Error, contentDescription = "")
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
        // Caixa de texto your e-mail
        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 4.dp),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults
                .colors(
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
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.tertiary
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            isError = isEmailError,
            trailingIcon = {
                if (isEmailError){
                    Icon(imageVector = Icons.Default.Error, contentDescription = "")
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
        // Caixa de texto your password
        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
            },
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults
                .colors(
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
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.tertiary
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.NumberPassword,
                imeAction = ImeAction.Done
            ),
            isError = isPasswordError,
            trailingIcon = {
                if(isPasswordError){
                    Icon(imageVector = Icons.Default.Error, contentDescription = "")
                } else {
                    Icon(
                        imageVector = Icons.Default.RemoveRedEye,
                        contentDescription = "",
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
        // Botão Create account
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = {
                if (validate()) {
                    // Criação de um objeto User
                    val user = User(
                        name = name,
                        email = email,
                        password = password,
                        userImage = convertBitmapToByteArray(profileImage!!)
                    )
                    try {
                        userRepository.saveUser(user)
                        showDialogSuccess = true
                    } catch (e: SQLiteConstraintException){
                        isEmailError = true
                        showDialogError = "Error"
                    }

                    //userRepository
                    //    .saveUser(User(name = name, email = email, password = password))
                    // Abrir o dialog informando que o
                    // cadastro ocorreu com sucesso
//                    showDialogSuccess = true
                } else {
                    showDialogError = "Error"
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(8.dp),
        ) {
            Text(
                text = stringResource(R.string.create_account),
                style = MaterialTheme.typography.labelMedium
            )
        }
    }

    // Mostra a mensagem de cadastro efetuado com sucesso
    if (showDialogSuccess){
        AlertDialog(
            onDismissRequest = { showDialogError = null },
            title = { Text(stringResource(R.string.sucesso))},
            text = { Text(stringResource(R.string.registration_completed_successfully))},
            confirmButton = {
                Button(onClick = {navController!!.navigate(Destination.LoginScreen.route)}) {
                    Text(text = stringResource(R.string.log_in))
                }
            }
        )
    }

    // Mostra a mensagem de erro de validação
    if (showDialogError != null) {
        AlertDialog(
            onDismissRequest = { showDialogError = null },
            title = {
                Text(
                    text = stringResource(R.string.validation_error)
                )
            },
            text = {
                Text(
                    text = stringResource(R.string.please_correct_the_highlighted_fields)
                )
            },
            confirmButton = {
                TextButton(onClick = { showDialogError = null }) {
                    Text("OK")
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
private fun SignupUserFormPreview() {
    KaizenTheme {
        SignupUserForm(null, null)
    }
}