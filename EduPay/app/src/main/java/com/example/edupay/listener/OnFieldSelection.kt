package com.example.edupay.listener

import com.example.edupay.model.DashboardOption

interface OnFieldSelection {
    fun onLogoutClick()
    fun onOptionSelection(items: List<DashboardOption>, position: Int)
}