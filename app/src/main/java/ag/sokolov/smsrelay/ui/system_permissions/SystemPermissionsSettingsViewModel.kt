package ag.sokolov.smsrelay.ui.system_permissions

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SystemPermissionsSettingsViewModel @Inject constructor(

) : ViewModel() {
    val state = mutableStateOf(SystemPermissionsSettingsScreenState())
}
