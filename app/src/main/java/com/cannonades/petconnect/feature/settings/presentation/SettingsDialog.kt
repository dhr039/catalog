package com.cannonades.petconnect.feature.settings.presentation

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.android.billingclient.api.BillingClient
import com.android.billingclient.api.BillingClientStateListener
import com.android.billingclient.api.BillingFlowParams
import com.android.billingclient.api.BillingResult
import com.android.billingclient.api.SkuDetailsParams
import com.cannonades.petconnect.R

private const val SKU_COFFEE_DONATION_5 = "coffee_donation_5"

@Composable
fun SettingsDialog(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    onChangeDarkThemeConfig: (darkThemeConfig: DarkThemeConfig) -> Unit,
    darkThemeConfig: DarkThemeConfig,
    billingClient: BillingClient
) {

    val configuration = LocalConfiguration.current
    val context = LocalContext.current

    AlertDialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        modifier = Modifier.widthIn(max = configuration.screenWidthDp.dp - 80.dp),
        onDismissRequest = { onDismiss() },
        title = {
            Text(
                text = stringResource(R.string.settings),
                style = MaterialTheme.typography.titleLarge,
            )
        },
        text = {
            Column {
                Divider()
                SettingsDialogSectionTitle(text = stringResource(R.string.dark_mode_preference))
                Column(Modifier.selectableGroup()) {
                    SettingsDialogThemeChooserRow(
                        text = stringResource(R.string.dark_mode_config_system_default),
                        selected = darkThemeConfig == DarkThemeConfig.FOLLOW_SYSTEM,
                        onClick = { onChangeDarkThemeConfig(DarkThemeConfig.FOLLOW_SYSTEM) },
                    )
                    SettingsDialogThemeChooserRow(
                        text = stringResource(R.string.dark_mode_config_light),
                        selected = darkThemeConfig == DarkThemeConfig.LIGHT,
                        onClick = { onChangeDarkThemeConfig(DarkThemeConfig.LIGHT) },
                    )
                    SettingsDialogThemeChooserRow(
                        text = stringResource(R.string.dark_mode_config_dark),
                        selected = darkThemeConfig == DarkThemeConfig.DARK,
                        onClick = { onChangeDarkThemeConfig(DarkThemeConfig.DARK) },
                    )
                }
                Divider()
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    onClick = {
                        startBilling(context, billingClient, SKU_COFFEE_DONATION_5)
                    }
                ) {
                    Text(text = "Buy developer a coffee ($5)")
                }
            }

        },
        confirmButton = {
            Text(
                text = stringResource(id = R.string.dismiss_dialog),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .clickable { onDismiss() },
            )
        },
    )
}

fun startBilling(context: Context, billingClient: BillingClient, sku: String) {
    if (!billingClient.isReady) {
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    // The BillingClient is ready. You can query purchases here.
                    queryAndLaunchBillingFlow(context, billingClient, sku)
                } else {
                    Log.e("launchBillingFlow", "BillingClient setup finished with response code: ${billingResult.responseCode}")
                }
            }
            override fun onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }
        })
    } else {
        queryAndLaunchBillingFlow(context, billingClient, sku)
    }
}

fun queryAndLaunchBillingFlow(context: Context, billingClient: BillingClient, sku: String) {
    Log.e("DHR", "queryAndLaunchBillingFlow")
    val skuList = ArrayList<String>()
    skuList.add(sku)
    val params = SkuDetailsParams.newBuilder()
    params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP)
    billingClient.querySkuDetailsAsync(params.build()) { billingResult, skuDetailsList ->
        Log.e("DHR", "querySkuDetailsAsync $skuDetailsList")
        if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && skuDetailsList != null) {
            Log.e("DHR", "billingResult.responseCode == BillingClient.BillingResponseCode.OK")
            for (skuDetails in skuDetailsList) {
                if (skuDetails.sku == sku) {
                    val flowParams = BillingFlowParams.newBuilder()
                        .setSkuDetails(skuDetails)
                        .build()
                    val responseCode = billingClient.launchBillingFlow(context as Activity, flowParams)
                    Log.v("launchBillingFlow","Launching billing flow for sku: $sku. Response code: ${responseCode.responseCode}")
                }
            }
        } else {
            Log.e("launchBillingFlow", "Error querying SKU details: ${billingResult.responseCode}")
        }
    }
}


@Composable
fun SettingsDialogThemeChooserRow(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Row(
        Modifier
            .fillMaxWidth()
            .selectable(
                selected = selected,
                role = Role.RadioButton,
                onClick = onClick,
            )
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(
            selected = selected,
            onClick = null,
        )
        Spacer(Modifier.width(8.dp))
        Text(text)
    }
}

@Composable
private fun SettingsDialogSectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp),
    )
}