package com.lifeosai.app.ai.dna.usecase

import com.lifeosai.app.ai.dna.engine.DNAEngine
import com.lifeosai.app.domain.model.LifeOSDNA
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

/**
 * Domain-level use case to observe the user's intelligence profile.
 */
class GetLifeOSDNAUseCase @Inject constructor(
    private val dnaEngine: DNAEngine
) {
    operator fun invoke(): StateFlow<LifeOSDNA> {
        return dnaEngine.currentDna
    }
}
