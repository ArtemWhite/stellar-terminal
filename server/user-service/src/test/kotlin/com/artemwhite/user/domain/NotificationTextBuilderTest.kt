package com.artemwhite.user.domain

import com.artemwhite.shared.model.enums.ObjectStatus
import com.artemwhite.user.domain.events.DecayedEvent
import com.artemwhite.user.domain.events.NotificationTextBuilder
import com.artemwhite.user.domain.events.PassOverEvent
import com.artemwhite.user.domain.events.StatusChangedEvent
import com.artemwhite.user.testutil.UserFixtures.NOW
import com.artemwhite.user.testutil.UserFixtures.OBJECT_ID
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class NotificationTextBuilderTest {

    private val builder = NotificationTextBuilder()

    @Test
    fun `builds a status change message`() {
        assertThat(StatusChangedEvent(OBJECT_ID, ObjectStatus.DECAYED, NOW).accept(builder))
            .isEqualTo("Status of object $OBJECT_ID changed to DECAYED")
    }

    @Test
    fun `builds a decay message`() {
        assertThat(DecayedEvent(OBJECT_ID, NOW).accept(builder))
            .isEqualTo("Object $OBJECT_ID has decayed from orbit")
    }

    @Test
    fun `builds a pass over message`() {
        assertThat(PassOverEvent(OBJECT_ID, NOW).accept(builder))
            .isEqualTo("Object $OBJECT_ID will pass over you at $NOW")
    }
}
