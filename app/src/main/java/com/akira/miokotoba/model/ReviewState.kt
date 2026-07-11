package com.akira.miokotoba.model

/**
 * 用户复习单词后给出的熟悉度反馈。
 *
 * 这四个状态会同时用于 UI 按钮、SM-2 调度算法和数据库存储。
 */
enum class ReviewState {
    Again,
    Vague,
    Know,
    Easy
}
