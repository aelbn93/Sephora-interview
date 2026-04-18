package com.interview.sephora.core.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.interview.sephora.core.designsystem.icon.SephoraIcons
import com.interview.sephora.core.model.ReviewSortField

@Composable
fun SortFab(
    currentSortField: ReviewSortField,
    onSortSelected: (ReviewSortField) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        FloatingActionButton(onClick = { expanded = true }) {
            Icon(
                imageVector = SephoraIcons.Sort,
                contentDescription = stringResource(R.string.core_ui_sort_reviews),
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            DropdownMenuItem(
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(SephoraIcons.ArrowDownward, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(8.dp))
                        Text(stringResource(R.string.core_ui_best_to_worst))
                    }
                },
                onClick = {
                    onSortSelected(ReviewSortField.BEST_TO_WORST)
                    expanded = false
                },
                trailingIcon = {
                    if (currentSortField == ReviewSortField.BEST_TO_WORST) {
                        Icon(SephoraIcons.Check, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    }
                },
            )
            DropdownMenuItem(
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(SephoraIcons.ArrowUpward, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.width(8.dp))
                        Text(stringResource(R.string.core_ui_worst_to_best))
                    }
                },
                onClick = {
                    onSortSelected(ReviewSortField.WORST_TO_BEST)
                    expanded = false
                },
                trailingIcon = {
                    if (currentSortField == ReviewSortField.WORST_TO_BEST) {
                        Icon(SephoraIcons.Check, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    }
                },
            )
        }
    }
}
