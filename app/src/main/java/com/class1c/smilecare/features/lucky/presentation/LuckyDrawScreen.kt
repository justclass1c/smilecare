package com.class1c.smilecare.features.lucky.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.class1c.smilecare.domain.model.user.UserRole
import com.class1c.smilecare.navigation.Route
import com.class1c.smilecare.ui.components.DetailRow
import com.class1c.smilecare.ui.components.MetricCard
import com.class1c.smilecare.ui.components.NoticeCard
import com.class1c.smilecare.ui.components.PersonRow
import com.class1c.smilecare.ui.components.PrimaryButton
import com.class1c.smilecare.ui.components.SectionTitle
import com.class1c.smilecare.ui.components.SmileCard
import com.class1c.smilecare.ui.components.SmileScreen
import com.class1c.smilecare.ui.components.StatusPill
import com.class1c.smilecare.ui.components.StatusTone

/**
 * Monthly Smile Rewards draw. The draw itself is managed elsewhere; this
 * destination presents the current campaign and winners.
 */
@Composable
fun LuckyDrawScreen(
    onManageClick: () -> Unit = {},
    onWinnerClick: (String) -> Unit = {},
    onNavigate: (Route) -> Unit = {},
) {
    SmileScreen(
        role = UserRole.NURSE,
        currentRoute = Route.LuckyDraw,
        title = "Lucky draw",
        onNavigate = onNavigate,
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            color = MaterialTheme.colorScheme.primaryContainer,
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Icon(
                    imageVector = Icons.Filled.CardGiftcard,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                )
                StatusPill(text = "Active draw", tone = StatusTone.SUCCESS)
                Text(
                    text = "A little extra reason to smile.",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    text = "MONTH REWARDS",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontWeight = FontWeight.Medium,
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            MetricCard(
                label = "Entries this month",
                value = "000",
                hint = "Error text",
                modifier = Modifier.weight(1f),
            )
            MetricCard(
                label = "Draw date",
                value = "DD/MM",
                hint = "Error text",
                modifier = Modifier.weight(1f),
            )
        }

        SectionTitle(title = "Draw details")
        SmileCard {
            DetailRow(label = "Grand prize", value = "Prize name")
            DetailRow(label = "Eligibility", value = "Completed & paid appointments")
            DetailRow(label = "Winners", value = "0 lucky patients")
        }

        SectionTitle(title = "Latest winners")
        winnerPreviews.forEach { winner ->
            PersonRow(
                initials = winner.initials,
                name = winner.name,
                subtitle = "MONTH REWARDS · WINNER",
                onClick = { onWinnerClick(winner.id) },
                trailing = {
                    Icon(
                        imageVector = Icons.Filled.CardGiftcard,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                    )
                },
            )
        }

        NoticeCard(
            text = "Winners are drawn from completed and paid appointments.",
        )

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            PrimaryButton(
                text = "Manage draw",
                onClick = onManageClick,
                leadingIcon = Icons.Filled.CardGiftcard,
            )
        }
    }
}

private data class WinnerPreview(
    val id: String,
    val initials: String,
    val name: String,
)

private val winnerPreviews = listOf(
    WinnerPreview("winner-1", "PN", "Patient name"),
    WinnerPreview("winner-2", "PN", "Patient name"),
)
