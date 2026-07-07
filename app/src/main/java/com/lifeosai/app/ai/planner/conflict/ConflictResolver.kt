package com.lifeosai.app.ai.planner.conflict

import com.lifeosai.app.ai.planner.model.ScheduledBlock
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Ensures the sanity of the schedule.
 * Detects and proposes resolutions for overlapping time blocks.
 */
@Singleton
class ConflictResolver @Inject constructor() {

    /**
     * Identifies overlaps in a list of scheduled blocks.
     */
    fun detectConflicts(blocks: List<ScheduledBlock>): List<ScheduleConflict> {
        val conflicts = mutableListOf<ScheduleConflict>()
        val sortedBlocks = blocks.sortedBy { it.startTime }

        for (i in 0 until sortedBlocks.size - 1) {
            val current = sortedBlocks[i]
            val next = sortedBlocks[i + 1]

            if (current.endTime.isAfter(next.startTime)) {
                conflicts.add(ScheduleConflict(current, next, ConflictType.OVERLAP))
            }
        }

        return conflicts
    }

    /**
     * Suggests a resolution (e.g., shifting the second block).
     */
    fun resolve(conflict: ScheduleConflict): ScheduledBlock {
        // Simple resolution: move next block to start exactly when current ends
        return conflict.blockB.copy(
            startTime = conflict.blockA.endTime,
            endTime = conflict.blockA.endTime.plusMinutes(
                java.time.Duration.between(conflict.blockB.startTime, conflict.blockB.endTime).toMinutes()
            )
        )
    }
}

data class ScheduleConflict(
    val blockA: ScheduledBlock,
    val blockB: ScheduledBlock,
    val type: ConflictType
)

enum class ConflictType {
    OVERLAP, TRAVEL_IMPOSSIBLE, EXCEEDS_WORK_HOURS
}
