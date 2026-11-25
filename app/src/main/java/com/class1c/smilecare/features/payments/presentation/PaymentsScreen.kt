package com.class1c.smilecare.features.payments.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.class1c.smilecare.domain.model.user.UserRole
import com.class1c.smilecare.navigation.Route
import com.class1c.smilecare.ui.components.BalanceCard
import com.class1c.smilecare.ui.components.NoticeCard
import com.class1c.smilecare.ui.components.SectionTitle
import com.class1c.smilecare.ui.components.SmileCard
import com.class1c.smilecare.ui.components.SmileScreen
import com.class1c.smilecare.ui.components.SmileTabs
import com.class1c.smilecare.ui.components.StatusPill
import com.class1c.smilecare.ui.components.StatusTone
import com.class1c.smilecare.ui.components.TextAction

/**
 * Patient payments and reception billing share one screen. The role decides
 * the summary copy and whether a payment action is offered.
 */
@Composable
fun PaymentsScreen(
    role: UserRole,
    selectedTab: Int = 0,
    onTabChange: (Int) -> Unit = {},
    onPayClick: () -> Unit = {},
    onInvoiceClick: (String) -> Unit = {},
    onFilterClick: () -> Unit = {},
    onNavigate: (Route) -> Unit = {},
) {
    val nurses = role == UserRole.NURSE
    SmileScreen(
        role = role,
        currentRoute = Route.Payments,
        title = if (nurses) "Billing overview" else "My payments",
        onNavigate = onNavigate,
    ) {
        Text(
            text = if (nurses) {
                "Every payment, accounted for with care."
            } else {
                "Your care, clearly accounted for."
            },
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        BalanceCard(
            label = if (nurses) "Collected this month" else "Outstanding balance",
            amount = if (nurses) "RM 0,000.00" else "RM 00.00",
            hint = if (nurses) {
                "Error text compared to last month"
            } else {
                "0 invoices awaiting payment"
            },
            actionLabel = if (nurses) null else "Pay now",
            onActionClick = onPayClick,
        )

        SmileTabs(
            options = listOf("All invoices", "Pending", "Paid"),
            selectedIndex = selectedTab,
            onSelect = onTabChange,
        )

        SectionTitle(
            title = if (nurses) "Recent transactions" else "Payment history",
            actionLabel = "Filter",
            onActionClick = onFilterClick,
        )

        invoicePreviews.forEach { invoice ->
            InvoiceCard(invoice = invoice, onClick = { onInvoiceClick(invoice.id) })
        }

        NoticeCard(
            text = if (nurses) {
                "Issued invoices and payment records are kept for clinic accounting."
            } else {
                "Payments are processed securely. Receipts stay in your history."
            },
        )
    }
}

@Composable
private fun InvoiceCard(invoice: InvoicePreview, onClick: () -> Unit) {
    SmileCard(onClick = onClick) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = invoice.id,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            StatusPill(text = invoice.status, tone = invoice.tone)
        }
        Text(
            text = invoice.service,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = invoice.date,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                text = invoice.amount,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary,
            )
        }
        if (invoice.status == "Pending") {
            TextAction(text = "Pay invoice", onClick = onClick)
        }
    }
}

private data class InvoicePreview(
    val id: String,
    val service: String,
    val date: String,
    val amount: String,
    val status: String,
    val tone: StatusTone,
)

private val invoicePreviews = listOf(
    InvoicePreview(
        id = "INV-2026-0048",
        service = "Service name",
        date = "DD/MM/YY",
        amount = "RM 00.00",
        status = "Pending",
        tone = StatusTone.WARNING,
    ),
    InvoicePreview(
        id = "INV-2026-0047",
        service = "Service name",
        date = "DD/MM/YY",
        amount = "RM 00.00",
        status = "Paid",
        tone = StatusTone.SUCCESS,
    ),
    InvoicePreview(
        id = "INV-2026-0046",
        service = "Service name",
        date = "DD/MM/YY",
        amount = "RM 00.00",
        status = "Paid",
        tone = StatusTone.SUCCESS,
    ),
    InvoicePreview(
        id = "INV-2026-0045",
        service = "Service name",
        date = "DD/MM/YY",
        amount = "RM 00.00",
        status = "Paid",
        tone = StatusTone.SUCCESS,
    ),
)
