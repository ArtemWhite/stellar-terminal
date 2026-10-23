package com.artemwhite.user.domain

import com.artemwhite.user.domain.models.VisualizationPreset
import com.artemwhite.user.testutil.UserFixtures.preset
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test
import java.util.UUID

class VisualizationPresetTest {

    @Test
    fun `rejects a non-positive scale at creation`() {
        assertThatThrownBy { VisualizationPreset.create(UUID.randomUUID(), UUID.randomUUID(), "#fff", 0.0) }
            .isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun `rejects a non-positive scale on update`() {
        val p = preset(userId = UUID.randomUUID())

        assertThatThrownBy { p.update("#000", -1.0) }
            .isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun `updates colors and scale`() {
        val p = preset(userId = UUID.randomUUID())

        p.update("#00ff00", 3.0)

        assertThat(p.colors).isEqualTo("#00ff00")
        assertThat(p.scale).isEqualTo(3.0)
    }
}
