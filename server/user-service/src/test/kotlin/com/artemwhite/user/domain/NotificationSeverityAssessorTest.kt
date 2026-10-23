package com.artemwhite.user.domain

import com.artemwhite.shared.model.enums.ObjectStatus
import com.artemwhite.user.domain.events.DecayedEvent
import com.artemwhite.user.domain.events.NotificationSeverity
import com.artemwhite.user.domain.events.NotificationSeverityAssessor
import com.artemwhite.user.domain.events.PassOverEvent
import com.artemwhite.user.domain.events.StatusChangedEvent
import com.artemwhite.user.testutil.UserFixtures.NOW
import com.artemwhite.user.testutil.UserFixtures.OBJECT_ID
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class NotificationSeverityAssessorTest {

    private val assessor = NotificationSeverityAssessor()

    @Test
    fun `a status change is informational`() {
        assertThat(StatusChangedEvent(OBJECT_ID, ObjectStatus.DECAYED, NOW).accept(assessor))
            .isEqualTo(NotificationSeverity.INFO)
    }

    @Test
    fun `a decay is critical`() {
        assertThat(DecayedEvent(OBJECT_ID, NOW).accept(assessor))
            .isEqualTo(NotificationSeverity.CRITICAL)
    }

    @Test
    fun `a pass over is a warning`() {
        assertThat(PassOverEvent(OBJECT_ID, NOW).accept(assessor))
            .isEqualTo(NotificationSeverity.WARNING)
    }
}
