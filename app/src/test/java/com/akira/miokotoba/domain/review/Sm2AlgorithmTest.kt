package com.akira.miokotoba.domain.review

import com.akira.miokotoba.model.ReviewState
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class Sm2AlgorithmTest {
    /**
     * 验证首次成功复习时，连续成功次数变为 1，
     * 下次复习间隔设置为 1 天。
     */
    @Test
    fun `first successful review uses one day interval`() {
        val result = Sm2Algorithm.calculate(
            reviewState = ReviewState.Know,
            repetitions = 0,
            easeFactor = 2.5,
            intervalDays = 0
        )

        assertEquals(1, result.repetitions)
        assertEquals(1, result.intervalDays)
        assertEquals(2.5, result.easeFactor, 0.001)
    }

    /**
     * 验证第二次连续成功复习时，
     * SM-2 使用固定的 6 天复习间隔。
     */
    @Test
    fun `second successful review uses six day interval`() {
        val result = Sm2Algorithm.calculate(
            reviewState = ReviewState.Know,
            repetitions = 1,
            easeFactor = 2.5,
            intervalDays = 1
        )

        assertEquals(2, result.repetitions)
        assertEquals(6, result.intervalDays)
        assertEquals(2.5, result.easeFactor, 0.001)
    }

    /**
     * 验证从第三次连续成功开始，
     * 新间隔按照“旧间隔乘以当前易度因子”计算。
     */
    @Test
    fun `later successful review multiplies interval by ease factor`() {
        val result = Sm2Algorithm.calculate(
            reviewState = ReviewState.Know,
            repetitions = 2,
            easeFactor = 2.5,
            intervalDays = 6
        )

        assertEquals(3, result.repetitions)
        assertEquals(15, result.intervalDays)
        assertEquals(2.5, result.easeFactor, 0.001)
    }

    /**
     * 验证计算结果包含小数时，
     * 复习间隔会四舍五入为整数天。
     */
    @Test
    fun `calculated interval is rounded to nearest whole day`() {
        val result = Sm2Algorithm.calculate(
            reviewState = ReviewState.Know,
            repetitions = 2,
            easeFactor = 2.3,
            intervalDays = 7
        )

        // 7 × 2.3 = 16.1，四舍五入后为 16 天
        assertEquals(16, result.intervalDays)
    }

    /**
     * 验证选择“不认识”时，
     * 连续成功次数归零，复习间隔重置为 1 天。
     */
    @Test
    fun `again resets repetitions and interval`() {
        val result = Sm2Algorithm.calculate(
            reviewState = ReviewState.Again,
            repetitions = 4,
            easeFactor = 2.5,
            intervalDays = 30
        )

        assertEquals(0, result.repetitions)
        assertEquals(1, result.intervalDays)
        assertEquals(1.7, result.easeFactor, 0.001)
    }

    /**
     * 验证选择“模糊”仍被视为回忆成功，
     * 但会降低易度因子，使后续复习更加频繁。
     */
    @Test
    fun `vague counts as success but decreases ease factor`() {
        val result = Sm2Algorithm.calculate(
            reviewState = ReviewState.Vague,
            repetitions = 0,
            easeFactor = 2.5,
            intervalDays = 0
        )

        assertEquals(1, result.repetitions)
        assertEquals(1, result.intervalDays)
        assertEquals(2.36, result.easeFactor, 0.001)
    }

    /**
     * 验证选择“简单”时，
     * 连续成功次数增加，同时提高易度因子。
     */
    @Test
    fun `easy increases ease factor`() {
        val result = Sm2Algorithm.calculate(
            reviewState = ReviewState.Easy,
            repetitions = 2,
            easeFactor = 2.5,
            intervalDays = 6
        )

        assertEquals(3, result.repetitions)
        assertEquals(15, result.intervalDays)
        assertEquals(2.6, result.easeFactor, 0.001)
    }

    /**
     * 验证易度因子经过调整后不会低于 SM-2 规定的最小值 1.3。
     */
    @Test
    fun `ease factor never falls below minimum`() {
        val result = Sm2Algorithm.calculate(
            reviewState = ReviewState.Again,
            repetitions = 2,
            easeFactor = 1.3,
            intervalDays = 6
        )

        assertEquals(1.3, result.easeFactor, 0.001)
    }

    /**
     * 验证连续成功次数为负数时，
     * 算法会拒绝非法输入并抛出异常。
     */
    @Test
    fun `negative repetitions are rejected`() {
        assertThrows(IllegalArgumentException::class.java) {
            Sm2Algorithm.calculate(
                reviewState = ReviewState.Know,
                repetitions = -1,
                easeFactor = 2.5,
                intervalDays = 0
            )
        }
    }

    /**
     * 验证易度因子低于最小值 1.3 时，
     * 算法会拒绝非法输入并抛出异常。
     */
    @Test
    fun `ease factor below minimum is rejected`() {
        assertThrows(IllegalArgumentException::class.java) {
            Sm2Algorithm.calculate(
                reviewState = ReviewState.Know,
                repetitions = 0,
                easeFactor = 1.2,
                intervalDays = 0
            )
        }
    }

    /**
     * 验证当前复习间隔为负数时，
     * 算法会拒绝非法输入并抛出异常。
     */
    @Test
    fun `negative interval is rejected`() {
        assertThrows(IllegalArgumentException::class.java) {
            Sm2Algorithm.calculate(
                reviewState = ReviewState.Know,
                repetitions = 0,
                easeFactor = 2.5,
                intervalDays = -1
            )
        }
    }
}
