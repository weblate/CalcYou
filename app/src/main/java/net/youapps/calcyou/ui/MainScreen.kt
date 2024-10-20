package net.youapps.calcyou.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import net.youapps.calcyou.AppNavHost
import net.youapps.calcyou.Destination
import net.youapps.calcyou.R
import net.youapps.calcyou.navigateTo
import net.youapps.calcyou.ui.components.NavDrawerContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var currentDestination by remember {
        mutableStateOf<Destination>(Destination.Calculator)
    }
    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = drawerState.isOpen,
        drawerContent = {
            NavDrawerContent(currentDestination = currentDestination, onDestinationSelected = {
                scope.launch {
                    drawerState.close()
                }
                navController.navigateTo(it.route)
                currentDestination = it

            })
        }
    ) {
        Scaffold(topBar = {
            CenterAlignedTopAppBar(title = {
                Text(
                    text = stringResource(
                        id =
                        when (val destination = currentDestination) {
                            is Destination.Converter -> destination.resId
                            else -> R.string.app_name
                        }
                    )
                )
            }, navigationIcon = {
                IconButton(onClick = { scope.launch { drawerState.open() } }) {
                    Icon(imageVector = Icons.Rounded.Menu, contentDescription = null)
                }
            })
        }) { paddingValues ->
            AppNavHost(
                modifier = Modifier.padding(paddingValues),
                navHostController = navController
            )
        }
    }
}