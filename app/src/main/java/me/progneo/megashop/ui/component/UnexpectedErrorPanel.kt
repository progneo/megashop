package me.progneo.megashop.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import me.progneo.megashop.R

@Composable
fun UnexpectedErrorPanel(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(5.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(R.drawable.broken_image),
            contentDescription = null,
            modifier = Modifier.size(48.dp),
            tint = MaterialTheme.colorScheme.onBackground
        )
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(R.string.something_went_wrong),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(name = "No internet connection panel", showBackground = true)
@Composable
fun PreviewUnexpectedErrorPanel() {
    Box(modifier = Modifier.fillMaxSize()) {
        UnexpectedErrorPanel()
    }
}
